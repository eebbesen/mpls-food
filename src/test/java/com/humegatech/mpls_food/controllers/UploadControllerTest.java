package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.repositories.UploadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;

    @Autowired
    private UploadRepository uploadRepository;

    @BeforeEach
    void setUp() {
        place = placeRepository.save(TestObjects.ginellis());
        Deal d = TestObjects.fridayTwofer();
        d.setPlace(place);
        deal = dealRepository.save(d);
    }

    @Test
    void testUploadNotAuthenticated() throws Exception {
        final long uploadCount = uploadRepository.count();
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, "abcd".getBytes());

        mvc.perform(multipart(String.format("/uploads/%d", deal.getId())).file(multipartFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(uploadCount, uploadRepository.count());
    }

    @Test
    @WithMockUser
    void testUpload() throws Exception {
        final long uploadCount = uploadRepository.count();
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, "abcd".getBytes());

        mvc.perform(multipart(String.format("/uploads/%d", deal.getId())).file(multipartFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(uploadCount + 1, uploadRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRenderImage() throws Exception {
        final Upload upload = TestObjects.upload(deal);
        upload.setId(null);
        uploadRepository.save(upload);

        Object ret = mvc.perform(MockMvcRequestBuilders.get(String.format("/uploads/%d", upload.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful());
        System.out.println(ret);
    }
}
