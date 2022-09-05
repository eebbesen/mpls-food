package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PlaceControllerTest extends MFControllerTest {
    private Place place;

    @BeforeEach
    void setUp() {
        final Deal deal = TestObjects.fridayTwofer();
        place = placeRepository.save(deal.getPlace());
        deal.setPlace(place);
        dealRepository.save(deal);
    }

    @Test
    void testList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/places").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testListUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/places").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Create new Place")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testGetEditNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/edit/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testGetEditUserNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/edit/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEditAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/edit/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Edit Place")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testPostEditUserNotAllowed() throws Exception {
        final String originalName = place.getName();
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("name", "updated name")
                        .param("address", "updated address")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is4xxClientError());
        assertEquals(originalName, placeRepository.findById(place.getId()).get().getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostEditAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("name", "updated name")
                        .param("address", "updated address")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
        assertEquals("updated name", placeRepository.findById(place.getId()).get().getName());
        assertEquals("updated address", placeRepository.findById(place.getId()).get().getAddress());
    }

    @Test
    void testPostAddNotAllowed() throws Exception {
        final int originalSize = placeRepository.findAll().size();
        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "new place")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
        assertEquals(originalSize, placeRepository.findAll().size());
    }

    @Test
    @WithMockUser
    void testPostAddUserNotAllowed() throws Exception {
        final int originalSize = placeRepository.findAll().size();
        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "new place")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
        assertEquals(originalSize + 1, placeRepository.findAll().size());
        assertEquals("new place", placeRepository.findAll().get(originalSize).getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostAddAdmin() throws Exception {
        final int originalSize = placeRepository.findAll().size();
        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "new place")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
        assertEquals(originalSize + 1, placeRepository.findAll().size());
        assertEquals("new place", placeRepository.findAll().get(originalSize).getName());
    }

    @Test
    @WithMockUser
    void testPostDeleteUserNotAllowed() throws Exception {
        final int originalSize = placeRepository.findAll().size();
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/delete/%s", place.getId()))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
        assertEquals(originalSize, placeRepository.findAll().size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/delete/%s", place.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(Optional.empty(), placeRepository.findById(place.getId()));
    }

    @Test
    void testShow() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/show/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")));
    }

}
