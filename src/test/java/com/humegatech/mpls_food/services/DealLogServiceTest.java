package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.domains.DealType;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealLogDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DealLogServiceTest extends MFServiceTest {
    @Autowired
    private DealLogService service;

    @Test
    void testMapToEntityPlaceNotFound() {
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .place(99L)
                .build();
        final DealLog dealLog = new DealLog();

        final Exception exception = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, dealLog)
        );

        assertEquals(String.format("%s \"place not found\"", HttpStatus.NOT_FOUND), exception.getMessage());
    }

    @Test
    void testMapToEntityPlaceNull() {
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .build();
        final DealLog dealLog = new DealLog();

        final Exception exception = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, dealLog)
        );

        assertEquals(String.format("%s \"place not found\"", HttpStatus.NOT_FOUND), exception.getMessage());
    }

    @Test
    void testMapToEntityDealNotFound() {
        final Deal deal = TestObjects.deal();
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .place(deal.getPlace().getId())
                .deal(99L)
                .build();
        final DealLog dealLog = new DealLog();

        when(placeRepository.findById(deal.getPlace().getId())).thenReturn(Optional.of(deal.getPlace()));

        final Exception exception = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, dealLog)
        );

        assertEquals(String.format("%s \"deal not found\"", HttpStatus.NOT_FOUND), exception.getMessage());
    }

    @Test
    void testMapToEntityDealNull() {
        final Deal deal = TestObjects.deal();
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .place(deal.getPlace().getId())
                .build();

        when(placeRepository.findById(deal.getPlace().getId())).thenReturn(Optional.of(deal.getPlace()));

        final DealLog dealLog = ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, new DealLog());

        assertNotNull(dealLog);
        assertEquals(deal.getPlace().getId(), dealLog.getPlace().getId());
    }

    @Test
    void testMapToEntity() {
        final Deal deal = TestObjects.deal();
        final LocalDate redemptionDate = LocalDate.now();
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .deal(deal.getId())
                .place(deal.getPlace().getId())
                .description("Got deal")
                .dealType(DealType.DEAL)
                .redemptionDate(redemptionDate)
                .build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));
        when(placeRepository.findById(deal.getPlace().getId())).thenReturn(Optional.of(deal.getPlace()));

        DealLog dealLog = ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, new DealLog());

        assertNotNull(dealLog);
        assertEquals(dealLogDTO.getDeal(), dealLog.getDeal().getId());
        assertEquals(dealLogDTO.getPlace(), dealLog.getPlace().getId());
        assertEquals(dealLogDTO.getDescription(), dealLog.getDescription());
        assertEquals(dealLogDTO.getDealType(), dealLog.getDealType());
        assertEquals(dealLogDTO.getRedeemed(), dealLog.getRedeemed());
        assertEquals(redemptionDate, dealLog.getRedemptionDate());
    }

    @Test
    void testMapToDTO() {
        final DealLog dealLog = TestObjects.dealLog();

        DealLogDTO dealLogDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", dealLog, new DealLogDTO());

        assertNotNull(dealLogDTO);
        assertEquals(dealLog.getDeal().getId(), dealLogDTO.getDeal());
        assertEquals(dealLog.getDeal().getDescription(), dealLogDTO.getDealDescription());
        assertEquals(dealLog.getPlace().getId(), dealLogDTO.getPlace());
        assertEquals(dealLog.getPlace().getName(), dealLogDTO.getPlaceName());
        assertEquals(dealLog.getDescription(), dealLogDTO.getDescription());
        assertEquals(dealLog.getRedeemed(), dealLogDTO.getRedeemed());
        assertEquals(dealLog.getRedemptionDate(), dealLogDTO.getRedemptionDate());
        assertEquals(dealLog.getDealType(), dealLogDTO.getDealType());
    }

    @Test
    void testFindAll() {
        final DealLog dealLog1 = TestObjects.dealLog();
        final DealLog dealLog2 = DealLog.builder()
                .id(99L)
                .dealType(DealType.EMAIL)
                .place(dealLog1.getPlace())
                .build();

        when(dealLogRepository.findAll()).thenReturn(List.of(dealLog1, dealLog2));

        List<DealLogDTO> dealLogDTOs = service.findAll();

        assertTrue(dealLogDTOs.size() > 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreate() {
        final Deal deal = TestObjects.deal();
        deal.setId(77L);
        deal.getPlace().setId(88L);

        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .id(99L)
                .deal(deal.getId())
                .place(deal.getPlace().getId())
                .build();

        final DealLog dealLog = TestObjects.dealLog();
        dealLog.setId(dealLogDTO.getId());

        when(placeRepository.findById(deal.getPlace().getId())).thenReturn(Optional.of(deal.getPlace()));
        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));
        when(dealLogRepository.save(any(DealLog.class))).thenReturn(dealLog);

        service.create(dealLogDTO);

        verify(dealLogRepository, times(1)).save(any(DealLog.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdate() {
        final Place place = TestObjects.place("Taco Bell");
        place.setId(88L);
        final Deal deal = TestObjects.deal();
        deal.setId(77L);
        deal.setPlace(place);

        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .id(99L)
                .deal(deal.getId())
                .place(place.getId())
                .build();

        final DealLog dealLog = TestObjects.dealLog();
        dealLog.setId(99L);

        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));


        when(dealLogRepository.findById(dealLog.getId())).thenReturn(Optional.of(dealLog));

        service.update(dealLog.getId(), dealLogDTO);

        verify(dealLogRepository, times(1)).save(dealLog);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateNoDealLog() {
        when(dealLogRepository.findById(99L)).thenReturn(Optional.empty());
        final DealLogDTO dto = new DealLogDTO();

        assertThrows(ResponseStatusException.class, () -> service.update(99L, dto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() {
        service.delete(99L);

        verify(dealLogRepository, times(1)).deleteById(99L);
    }

    @Test
    void testGet() {
        final DealLog dealLog = TestObjects.dealLog();
        dealLog.setId(99L);

        when(dealLogRepository.findById(dealLog.getId())).thenReturn(Optional.of(dealLog));

        final DealLogDTO dealLogDTO = service.get(dealLog.getId());

        assertEquals(dealLog.getId(), dealLogDTO.getId());
    }

    @Test
    void testGetNoDealLog() {
        assertThrows(ResponseStatusException.class, () -> service.get(99L));
    }
}
