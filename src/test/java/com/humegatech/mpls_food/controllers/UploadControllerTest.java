package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.UploadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UploadControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;

    @BeforeEach
    void setUp() {
        place = TestObjects.ginellis();
        deal = TestObjects.fridayTwofer();
        deal.setPlace(place);
    }

    @Test
    void testUploadNotAuthenticated() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, "abcd".getBytes());

        mvc.perform(multipart(String.format("/uploads/%d", deal.getId())).file(multipartFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        verify(uploadService, times(0)).create(any(UploadDTO.class));
    }

    @Test
    @WithMockUser
    void testUpload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, "abcd".getBytes());

        mvc.perform(multipart(String.format("/uploads/%d", deal.getId())).file(multipartFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(uploadService, times(1)).create(any(UploadDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRenderImage() throws Exception {
        final Upload upload = TestObjects.upload(deal);
        upload.setId(99L);
        List<UploadDTO> uploadDTOs = uploadsToUploadDTOs(List.of(upload));

        when(uploadService.get(upload.getId())).thenReturn(uploadDTOs.get(0));

        mvc.perform(MockMvcRequestBuilders.get(String.format("/uploads/%d", upload.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful());

        verify(uploadService, times(1)).get(upload.getId());
    }
}
