package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DealControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;

    @BeforeEach
    void setUp() {
        place = placeRepository.save(TestObjects.ginellis());
        Deal d = TestObjects.fridayTwofer();
        d.setPlace(place);
        deal = dealRepository.save(d);
    }

    @Test
    void testList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testListUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testGetAdd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals/add").accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testGetAddUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals/add").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add Deal")));
    }

    @Test
    @WithMockUser
    void testPostAddUser() throws Exception {
        final int dealCount = dealRepository.findAll().size();
        mvc.perform(MockMvcRequestBuilders.post("/deals/add")
                        .with(csrf())
                        .param("description", "Test deal")
                        .param("friday", "true")
                        .param("place", place.getId().toString()))
                .andExpect(status().is3xxRedirection());
        assertEquals(dealCount + 1, dealRepository.findAll().size());
    }

    // todo this test will fail once the app handles bad payloads better
    // for now the expectation is never used
    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostAddAdminBadPayload() {
        Assertions.assertThrows(NestedServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post("/deals/add")
                        .with(csrf())
                        .param("friday", "true")
                        .param("place", place.getId().toString()))
                .andExpect(status().is4xxClientError()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEditUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/deals/edit/%d", deal.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Edit Deal")));
    }

    @Test
    void testGetEditNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/deals/edit/%d", deal.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testPostEditAdmin() throws Exception {
        final String updatedDescription = LocalDateTime.now(ZoneId.systemDefault()).toString();
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/edit/%d", deal.getId()))
                        .with(csrf())
                        .param("description", updatedDescription)
                        .param("place", place.getId().toString()))
                .andExpect(status().is3xxRedirection());
        assertEquals(updatedDescription, dealRepository.findById(deal.getId()).get().getDescription());
    }

    @Test
    @WithMockUser
    void testPostEditUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/edit/%d", deal.getId()))
                        .with(csrf())
                        .param("description", "Test deal updated")
                        .param("place", place.getId().toString()))
                .andExpect(status().is3xxRedirection());
        assertEquals("Test deal updated", dealRepository.findById(deal.getId()).get().getDescription());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/delete/%d", deal.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(Optional.empty(), dealRepository.findById(deal.getId()));
    }

    @Test
    @WithMockUser
    void testPostDeleteUserNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/delete/%d", deal.getId()))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
        assertNotNull(dealRepository.findById(deal.getId()));
    }
}
