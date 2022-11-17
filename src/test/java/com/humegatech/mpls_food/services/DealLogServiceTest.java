package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.domains.DealType;
import com.humegatech.mpls_food.models.DealLogDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DealLogServiceTest {
    @Autowired
    private DealLogService service;

    @MockBean
    private DealRepository dealRepository;

    @MockBean
    private PlaceRepository placeRepository;

    @Test
    void testMapToEntityPlaceNotFound() {
        final DealLogDTO dealLogDTO = DealLogDTO.builder()
                .place(99L)
                .build();

        final Exception exception = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, new DealLog())
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

        when(placeRepository.findById(deal.getPlace().getId())).thenReturn(Optional.of(deal.getPlace()));

        final Exception exception = assertThrows(ResponseStatusException.class, () ->
                ReflectionTestUtils.invokeMethod(service, "mapToEntity", dealLogDTO, new DealLog())
        );

        assertEquals(String.format("%s \"deal not found\"", HttpStatus.NOT_FOUND), exception.getMessage());
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

        assertEquals(dealLog.getDeal().getId(), dealLogDTO.getDeal());
        assertEquals(dealLog.getPlace().getId(), dealLogDTO.getPlace());
        assertEquals(dealLog.getDescription(), dealLogDTO.getDescription());
        assertEquals(dealLog.getRedeemed(), dealLogDTO.getRedeemed());
        assertEquals(dealLog.getRedemptionDate(), dealLogDTO.getRedemptionDate());
        assertEquals(dealLog.getDealType(), dealLogDTO.getDealType());
    }
}
