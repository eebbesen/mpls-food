package com.humegatech.mpls_food.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
    @WithMockUser(roles = "ADMIN")
    void testListAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deals").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Create new Deal")))
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
    @WithMockUser(roles = "ADMIN")
    void testGetAddAdmin() throws Exception {
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
                        .param("place", "2")
                        .param("place_id", "true"))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostAddAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deals/add")
                        .with(csrf())
                        .param("description", "Test deal")
                        .param("friday", "true")
                        .param("place", "2")
                        .param("place_id", "true"))
                .andExpect(status().is3xxRedirection());

    }

    // todo this test will fail once the app handles bad payloads better
    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostAddAdminBadPayload() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () -> {
            mvc.perform(MockMvcRequestBuilders.post("/deals/add")
                            .with(csrf())
                            .param("friday", "true")
                            .param("place", "2")
                            .param("place_id", "true"))
                    .andExpect(status().is4xxClientError());
        });
    }
}
