package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DealControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;
    @Autowired
    private DealController controller;

    @BeforeEach
    void setUp() {
        place = TestObjects.ginellis();
        deal = TestObjects.fridayTwofer();
        deal.setPlace(place);

        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Test
    void testList() throws Exception {
        when(dealService.findAll()).thenReturn(dealsToDealDTOs(List.of(deal)));

        mvc.perform(MockMvcRequestBuilders.get("/deals").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testListUser() throws Exception {
        when(dealService.findAll()).thenReturn(dealsToDealDTOs(List.of(deal)));

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
        mvc.perform(MockMvcRequestBuilders.post("/deals/add")
                        .with(csrf())
                        .param("description", "Test deal")
                        .param("friday", "true")
                        .param("place", place.getId().toString()))
                .andExpect(status().is3xxRedirection());

        verify(dealService, times(1)).create(any(DealDTO.class));
    }

    @Test
    @WithMockUser
    void testPostAddUserBindingResultError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String ret = controller.add(new DealDTO(), bindingResult, null);
        assertEquals("deal/add", ret);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEditUser() throws Exception {
        when(dealService.get(deal.getId())).thenReturn(dealsToDealDTOs(List.of(deal)).get(0));

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
    }

    @Test
    @WithMockUser
    void testPostEditUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/edit/%d", deal.getId()))
                        .with(csrf())
                        .param("description", "Test deal updated")
                        .param("place", place.getId().toString()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testPostEditUserBindingResultError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String ret = controller.edit(1L, new DealDTO(), bindingResult, null);

        assertEquals("deal/edit", ret);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/delete/%d", deal.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testPostDeleteUserNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/delete/%d", deal.getId()))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser
    void testGetCopyUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals/copy/99")
                        .with(csrf()))
                .andExpect((status().is4xxClientError()))
                .andExpect(content().string(not(containsString("Copy"))));
    }

    @Test
    void testGetCopyNotLoggedIn() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals/copy/99")
                        .with(csrf()))
                .andExpect((status().is3xxRedirection()))
                .andExpect(content().string(not(containsString("Copy"))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCopyAdmin() throws Exception {
        final List<DealDTO> dtos = dealsToDealDTOs(List.of(deal));
        when(dealService.get(deal.getId())).thenReturn(dtos.get(0));

        mvc.perform(MockMvcRequestBuilders.get(String.format("/deals/copy/%d", deal.getId()))
                        .with(csrf()))
                .andExpect((status().is2xxSuccessful()))
                .andExpect(content().string(containsString("Copy")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCopyAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/copy/%d?places=98&places=97", deal.getId()))
                        .with(csrf()))
                .andExpect((status().is3xxRedirection()));

        verify(dealService, times(1)).copy(deal.getId(), List.of(98L, 97L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCopyAdminNoPlaces() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/copy/%d", deal.getId()))
                        .with(csrf()))
                .andExpect((status().is3xxRedirection()));

        verify(dealService, times(0)).copy(eq(deal.getId()),
                ArgumentMatchers.<List>any(List.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCopyAdminEmptyPlaces() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/deals/copy/%d?places=", deal.getId()))
                        .with(csrf()))
                .andExpect((status().is3xxRedirection()));

        verify(dealService, times(0)).copy(eq(deal.getId()),
                ArgumentMatchers.<List>any(List.class));
    }
}
