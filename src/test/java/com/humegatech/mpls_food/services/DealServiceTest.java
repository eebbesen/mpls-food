package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.*;
import com.humegatech.mpls_food.models.DealDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class DealServiceTest extends MFServiceTest {
    @Autowired
    private DealService service;

    private Deal dealMonTues;
    private DealDTO dealMonTuesDTO;

    @BeforeEach
    void setUp() {
        dealMonTues = TestObjects.dealMonTues();

        dealMonTuesDTO = new DealDTO();
        dealMonTuesDTO.setMonday(true);
        dealMonTuesDTO.setTuesday(true);
        dealMonTuesDTO.setDescription(dealMonTues.getDescription());
        dealMonTuesDTO.setPlace(dealMonTues.getPlace().getId());
        dealMonTuesDTO.setPlaceName(dealMonTues.getPlace().getName());
        dealMonTuesDTO.setDish(dealMonTues.getDish());
        dealMonTuesDTO.setId(dealMonTues.getId());
        dealMonTuesDTO.setMinPrice(dealMonTues.getMinPrice());
        dealMonTuesDTO.setMaxPrice(dealMonTues.getMaxPrice());
        dealMonTuesDTO.setMinDiscount(dealMonTues.getMinDiscount());
        dealMonTuesDTO.setMaxDiscount(dealMonTues.getMaxDiscount());
        dealMonTuesDTO.setMinDiscountPercent(dealMonTues.getMinDiscountPercent());
        dealMonTuesDTO.setMaxDiscountPercent(dealMonTues.getMaxDiscountPercent());
        dealMonTuesDTO.setVerified(dealMonTues.isVerified());
        dealMonTuesDTO.setTaxIncluded(dealMonTues.isTaxIncluded());
        dealMonTuesDTO.setDealType(DealType.DEAL);
    }

    @Test
    void testConfirmOrAddDayDayExists() {
        ReflectionTestUtils.invokeMethod(service.getClass(), "addDay", dealMonTues, DayOfWeek.MONDAY);
        assertEquals(2, dealMonTues.getDays().size());
    }

    @Test
    void testConfirmOrAddDayDayDoesNotExist() {
        ReflectionTestUtils.invokeMethod(service.getClass(), "addDay", dealMonTues, DayOfWeek.WEDNESDAY);
        assertEquals(3, dealMonTues.getDays().size());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateNotFound() {
        final DealDTO dto = new DealDTO();
        final Exception ex = assertThrows(ResponseStatusException.class, () -> service.update(99L, dto));
        assertEquals("404 NOT_FOUND", ex.getMessage());
    }

    @Test
    @WithAnonymousUser
    void testUpdateRemoveUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.update(dealMonTues.getId(), dealMonTuesDTO);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateRemoveDays() {
        final Day deletedDay = dealMonTues.getDays().stream()
                .filter(day -> day.getDayOfWeek().equals(DayOfWeek.MONDAY))
                .findFirst().orElse(null);
        dealMonTuesDTO.setMonday(false);

        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));
        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));

        service.update(dealMonTues.getId(), dealMonTuesDTO);

        verify(dealRepository, times(1)).findById(dealMonTues.getId());
        assertEquals(1, dealMonTues.getDays().size());
        assertFalse(dealMonTues.getDays().contains(deletedDay));
    }

    @Test
    void testMapToEntity() {
        dealMonTuesDTO.setStartTime(LocalTime.of(10, 30));
        dealMonTuesDTO.setEndTime(LocalTime.of(11, 0));
        dealMonTuesDTO.setStartDate(LocalDate.of(2022, 10, 1));
        dealMonTuesDTO.setStartDate(LocalDate.of(2022, 10, 31));

        when(placeRepository.findById(dealMonTues.getPlace().getId())).thenReturn(Optional.of(dealMonTues.getPlace()));

        Deal deal = new Deal();
        ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealMonTuesDTO, deal);

        assertEquals(dealMonTues.getPlace(), deal.getPlace());
        assertEquals(dealMonTues.getDescription(), deal.getDescription());
        assertEquals(dealMonTues.getId(), deal.getId());
        assertEquals(dealMonTues.getDish(), deal.getDish());
        assertEquals(dealMonTues.getMaxPrice(), deal.getMaxPrice());
        assertEquals(dealMonTues.getMinPrice(), deal.getMinPrice());
        assertEquals(dealMonTues.getMaxDiscount(), deal.getMaxDiscount());
        assertEquals(dealMonTues.getMinDiscount(), deal.getMinDiscount());
        assertEquals(dealMonTues.getMaxDiscountPercent(), deal.getMaxDiscountPercent());
        assertEquals(dealMonTues.getMinDiscountPercent(), deal.getMinDiscountPercent());
        assertEquals(dealMonTues.isVerified(), deal.isVerified());
        assertEquals(dealMonTues.isTaxIncluded(), deal.isTaxIncluded());
        assertEquals(dealMonTuesDTO.getStartTime(), deal.getStartTime());
        assertEquals(dealMonTuesDTO.getEndTime(), deal.getEndTime());
        assertEquals(dealMonTuesDTO.getStartDate(), deal.getStartDate());
        assertEquals(dealMonTuesDTO.getEndDate(), deal.getEndDate());
        assertEquals(dealMonTues.getDealType(), deal.getDealType());

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testMapToEntityPlaceNotFound() {
        final DealDTO dealDTO = DealDTO.builder().place(99L).build();
        final Deal deal = new Deal();

        final Exception ex = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealDTO, deal)
        );
        assertEquals("404 NOT_FOUND \"place not found: 99\"", ex.getMessage());
    }

    @Test
    void testMapToEntityNullPlace() {
        final DealDTO dealDTO = DealDTO.builder().build();
        final Deal deal = new Deal();

        final Exception ex = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealDTO, deal)
        );

        assertEquals("404 NOT_FOUND \"place not found: null\"", ex.getMessage());
    }

    @Test
    void testApplyDaysToEntity() {
        Deal deal = new Deal();
        ReflectionTestUtils.invokeMethod(service, "applyDaysToEntity", dealMonTuesDTO, deal);

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testApplyDaysToEntityRemoveDays() {
        Deal deal = new Deal();
        deal.getDays().add(Day.builder().deal(dealMonTues).dayOfWeek(DayOfWeek.SUNDAY).build());
        ReflectionTestUtils.invokeMethod(service, "applyDaysToEntity", dealMonTuesDTO, deal);

        // confirm days
        Set<DayOfWeek> days = deal.getDays().stream().map(Day::getDayOfWeek)
                .collect(Collectors.toSet());
        assertEquals(2, days.size());
        assertTrue(days.contains(DayOfWeek.MONDAY));
        assertTrue(days.contains(DayOfWeek.TUESDAY));
    }

    @Test
    void testApplyDaysToDTO() {
        DealDTO dealDTO = new DealDTO();
        ReflectionTestUtils.invokeMethod(service, "applyDaysToDTO", dealMonTues, dealDTO);

        assertFalse(dealDTO.isSunday());
        assertTrue(dealDTO.isMonday());
        assertTrue(dealDTO.isTuesday());
        assertFalse(dealDTO.isWednesday());
        assertFalse(dealDTO.isThursday());
        assertFalse(dealDTO.isFriday());
        assertFalse(dealDTO.isSaturday());
    }

    @Test
    void testMapToDTO() {
        final Upload upload = TestObjects.upload(dealMonTues);
        dealMonTues.setStartTime(LocalTime.of(10, 30));
        dealMonTues.setEndTime(LocalTime.of(11, 0));
        dealMonTues.setStartDate(LocalDate.of(2022, 10, 1));
        dealMonTues.setStartDate(LocalDate.of(2022, 10, 31));

        final LocalDateTime mon = LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 12);
        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            ldt.when(LocalDateTime::now).thenReturn(mon);
            DealDTO dto = ReflectionTestUtils.invokeMethod(service, "mapToDTO", dealMonTues, new DealDTO());

            assertNotNull(dto);
            assertEquals(dealMonTues.getId(), dto.getId());
            assertTrue(dto.isMonday());
            assertTrue(dto.isTuesday());
            assertFalse(dto.isWednesday());
            assertFalse(dto.isThursday());
            assertFalse(dto.isFriday());
            assertFalse(dto.isSaturday());
            assertFalse(dto.isSunday());
            assertEquals(dealMonTues.getDescription(), dto.getDescription());
            assertEquals(dealMonTues.getPlace().getId(), dto.getPlace());
            assertEquals(dealMonTues.getPlace().getName(), dto.getPlaceName());
            assertEquals(dealMonTues.getMinPrice(), dto.getMinPrice());
            assertEquals(dealMonTues.getMaxPrice(), dto.getMaxPrice());
            assertEquals(dealMonTues.getMinDiscount(), dto.getMinDiscount());
            assertEquals(dealMonTues.getMaxDiscount(), dto.getMaxDiscount());
            assertEquals(dealMonTues.isTaxIncluded(), dto.isTaxIncluded());
            assertEquals(dealMonTues.isVerified(), dto.isVerified());
            assertEquals(dealMonTues.isTaxIncluded(), dto.isTaxIncluded());
            assertEquals(String.format("$%.2f", dealMonTues.getMinPrice()), dto.getPriceRange());
            assertEquals(String.format("$%.2f - $%.2f", dealMonTues.getMinDiscount(),
                    dealMonTues.getMaxDiscount()), dto.getDiscountRange());
            assertEquals(String.format("%.0f%% - %.0f%%", dealMonTues.getMinDiscountPercent(),
                    dealMonTues.getMaxDiscountPercent()), dto.getDiscountPercentRange());
            assertEquals("MT-----", dto.getDaysDisplay());
            assertEquals(dealMonTues.getDish(), dto.getDish());
            assertEquals(dealMonTues.getStartTime(), dto.getStartTime());
            assertEquals(dealMonTues.getEndTime(), dto.getEndTime());
            assertEquals(dealMonTues.getStartDate(), dto.getStartDate());
            assertEquals(dealMonTues.getEndDate(), dto.getEndDate());
            assertEquals(1, dto.getUploads().size());
            assertEquals(upload.getImage(), dto.getUploads().get(0).getImage());
            assertEquals(upload.getDeal().getId(), dto.getUploads().get(0).getDealId());
            assertEquals(upload.isVerified(), dto.getUploads().get(0).isVerified());
            assertEquals(dealMonTues.getDealType(), dto.getDealType());
        }
    }

    @Test
    void testApplyUploadsToDTO() {
        final Upload upload = TestObjects.upload(dealMonTues);
        final Upload upload2 = Upload.builder()
                .deal(dealMonTues)
                .id(2L)
                .verified(true)
                .image(new byte['a']).build();

        List<Upload> uploads = List.of(upload, upload2);
        dealMonTues.getUploads().addAll(uploads);

        final DealDTO dto = new DealDTO();
        ReflectionTestUtils.invokeMethod(service, "applyUploadsToDTO", dealMonTues, dto);

        assertEquals(2, dto.getUploads().size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() {
        service.delete(99L);
        verify(dealRepository, times(1)).deleteById(99L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteUserRole() {
        assertThrows(AccessDeniedException.class, () -> {
            service.delete(99L);
        });
    }

    @Test
    @WithAnonymousUser
    void testDeleteUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.delete(99L);
        });
    }

    @Test
    @WithAnonymousUser
    void testCreateUnauthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.create(dealMonTuesDTO);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreate() {
        dealMonTues.setId(99L);
        when(placeRepository.findById(dealMonTuesDTO.getPlace())).thenReturn(Optional.of(dealMonTues.getPlace()));
        when(dealRepository.save(any(Deal.class))).thenReturn(dealMonTues);

        Long id = service.create(dealMonTuesDTO);

        assertEquals(dealMonTues.getId(), id);
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCopyRoleUser() {
        assertThrows(AccessDeniedException.class, () -> {
            service.copy(dealMonTues.getId(), new ArrayList<>());
        });
    }

    @Test
    @WithAnonymousUser
    void testCopyUnAuthenticated() {
        assertThrows(AccessDeniedException.class, () -> {
            service.copy(dealMonTues.getId(), new ArrayList<>());
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCopy() {
        dealMonTues.setId(99L);
        final List<Place> places = List.of(TestObjects.place("place 1"), TestObjects.place("place 2"),
                TestObjects.place("place 72"));
        final List<Long> placeIds = places.stream().map(Place::getId).toList();

        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));
        when(placeRepository.findByIdIn(placeIds)).thenReturn(places);
        when(placeRepository.findById(placeIds.get(0))).thenReturn(Optional.of(places.get(0)));
        when(placeRepository.findById(placeIds.get(1))).thenReturn(Optional.of(places.get(1)));
        when(placeRepository.findById(placeIds.get(2))).thenReturn(Optional.of(places.get(2)));

        service.copy(dealMonTues.getId(), placeIds);

        verify(dealRepository, times(1)).saveAll(argThat(l -> ((List<?>) l).size() == 3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCopyPlaceNotFound() {
        dealMonTues.setId(99L);
        final Long dealId = dealMonTues.getId();
        final List<Long> ids = List.of(2L, 3L, 72L);

        when(dealRepository.findById(dealMonTues.getId())).thenReturn(Optional.of(dealMonTues));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                service.copy(dealId, ids));

        assertEquals("404 NOT_FOUND \"one or more places not found:\n" +
                " [2, 3, 72]\"", ex.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCopyDealNotFound() {
        final Long dealId = dealMonTues.getId();
        final List<Long> ids = List.of(2L, 3L, 72L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                service.copy(dealId, ids));

        assertEquals("404 NOT_FOUND \"deal not found\"", ex.getMessage());
        assertEquals("deal not found", ex.getReason());
    }
}