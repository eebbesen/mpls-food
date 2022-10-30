package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.controllers.MFControllerTest;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.UploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UploadServiceTest extends MFControllerTest {
    @MockBean
    private DealRepository dealRepository;
    @MockBean
    private UploadRepository uploadRepository;

    @Autowired
    private UploadService service;

    private Deal deal;

    @BeforeEach
    void setUp() {
        deal = TestObjects.deal();
    }

    @Test
    void testMapToDTO() {
        final Upload upload = TestObjects.upload(deal);
        upload.setVerified(true);
        UploadDTO uploadDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", upload, new UploadDTO());

        assertEquals(deal.getId(), uploadDTO.getDealId());
        assertTrue(uploadDTO.getImage().length > 2);
        assertTrue(uploadDTO.isVerified());
        assertEquals(upload.getId(), uploadDTO.getId());
    }

    @Test
    void testMapToEntity() {
        final UploadDTO uploadDTO = UploadDTO.builder()
                .image(TestObjects.image())
                .dealId(deal.getId())
                .verified(true).build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));

        Upload upload = ReflectionTestUtils.invokeMethod(service, "mapToEntity", uploadDTO, new Upload());

        assertEquals(deal, upload.getDeal());
        assertTrue(upload.isVerified());
        assertTrue(upload.getImage().length > 2);
    }

    @Test
    void testMapToEntityNoDealFound() {
        final UploadDTO uploadDTO = UploadDTO.builder()
                .image(TestObjects.image())
                .dealId(deal.getId())
                .verified(true).build();

        Exception ex = assertThrows(ResponseStatusException.class,
                () -> ReflectionTestUtils.invokeMethod(service, "mapToEntity", uploadDTO, new Upload()));

        assertEquals("404 NOT_FOUND \"deal not found\"", ex.getMessage());
    }

    @Test
    void testMapToEntityNoDealId() {
        final UploadDTO uploadDTO = UploadDTO.builder()
                .image(TestObjects.image())
                .verified(true).build();

        Exception ex = assertThrows(ResponseStatusException.class,
                () -> ReflectionTestUtils.invokeMethod(service, "mapToEntity", uploadDTO, new Upload()));

        assertEquals("404 NOT_FOUND \"deal not found\"", ex.getMessage());
    }

    @Test
    void testGetNotFound() {
        Exception exception = assertThrows(ResponseStatusException.class, () -> service.get(99L));
        assertEquals("404 NOT_FOUND", exception.getMessage());
    }

    @Test
    void testFindByDealId() {
        List<Upload> dtos = new ArrayList<>();
        dtos.add(Upload.builder().id(1L).deal(deal).build());
        when(uploadRepository.findByDealId(deal.getId())).thenReturn(dtos);

        assertEquals(1, service.findByDealId(deal.getId()).size());
    }

    @Test
    void testFindByDealIdNoDeal() {
        when(uploadRepository.findByDealId(deal.getId())).thenReturn(new ArrayList<Upload>());
        assertEquals(0, service.findByDealId(deal.getId()).size());
    }
}
