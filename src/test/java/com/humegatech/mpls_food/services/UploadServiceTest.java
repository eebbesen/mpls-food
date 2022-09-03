package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.UploadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UploadServiceTest {
    @MockBean
    private UploadRepository uploadRepository;
    @MockBean
    private DealRepository dealRepository;

    @Autowired
    private UploadService service;

    @Test
    void testMapToDTO() {
        final Deal deal = TestObjects.deal();
        final Upload upload = TestObjects.upload(deal);
        upload.setVerified(true);
        UploadDTO uploadDTO = ReflectionTestUtils.invokeMethod(service, "mapToDTO", upload, new UploadDTO());

        assertEquals(deal.getId(), uploadDTO.getDealId());
        assertTrue(uploadDTO.getImage().length > 100);
        assertTrue(uploadDTO.isVerified());
    }

    @Test
    void testMapToEntity() {
        final Deal deal = TestObjects.deal();
        final UploadDTO uploadDTO = UploadDTO.builder()
                .image(TestObjects.image())
                .dealId(deal.getId())
                .verified(true).build();

        when(dealRepository.findById(deal.getId())).thenReturn(Optional.of(deal));

        Upload upload = ReflectionTestUtils.invokeMethod(service, "mapToEntity", uploadDTO, new Upload());

        assertEquals(deal, upload.getDeal());
        assertTrue(upload.isVerified());
        assertTrue(upload.getImage().length > 100);
    }
}
