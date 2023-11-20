package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.models.DealLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DealLogControllerTest extends MFControllerTest {
    @Autowired
    private DealLogController controller;

    @BeforeEach
    void setUp() {
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Test
    void testGetEdit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deal_logs/edit").accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testGetEditUser() throws Exception {
        final List<DealLogDTO> dtos = dealLogsToDealLogDTOs(List.of(TestObjects.dealLog()));
        when(dealLogService.get(99L)).thenReturn(dtos.get(0));
        mvc.perform(MockMvcRequestBuilders.get(String.format("/deal_logs/edit/%d", 99L)).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Edit Deal Log")));
    }

    @Test
    @WithMockUser
    void testPostEditUser() throws Exception {
        final DealLog dealLog = TestObjects.dealLog();
        dealLog.setId(99L);

        mvc.perform(MockMvcRequestBuilders.post(String.format("/deal_logs/edit/%d", dealLog.getId())).accept(MediaType.APPLICATION_XML)
                        .with(csrf())
                        .param("description", "New description"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testPostEditUserBindingResultError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String ret = controller.edit(1L, new DealLogDTO(), bindingResult, null);
        assertEquals("deal_logs/edit", ret);
    }

    @Test
    void testGetAdd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deal_logs/add").accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testGetAddUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deal_logs/add").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add Deal Log")));
    }

    @Test
    @WithMockUser
    void testPostAddUser() throws Exception {
        final DealLog dealLog = TestObjects.dealLog();
        final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        when(dealLogService.create(any())).thenReturn(99L);

        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/add")
                        .with(csrf())
                        .param("description", dealLog.getDescription())
                        .param("place", dealLog.getPlace().getId().toString())
                        .param("dealType", "DEAL")
                        .param("deal", dealLog.getDeal().getId().toString())
                        .param("redeemed", "1")
                        .param("redemptionDate", df.format(dealLog.getRedemptionDate())))
                .andExpect(status().is3xxRedirection());

        verify(dealLogService, times(1)).create(any(DealLogDTO.class));
    }

    @Test
    @WithMockUser
    void testPostAddUserBindingResultError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String ret = controller.add(new DealLogDTO(), bindingResult, null);
        assertEquals("deal_logs/add", ret);
    }

    @Test
    @WithMockUser
    void testPostDeleteUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/1")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(dealLogService, times(0)).delete(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/99")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(dealLogService, times(1)).delete(99L);
    }

    @Test
    void testList() throws Exception {
        final List<DealLogDTO> dealLogDTOs = dealLogsToDealLogDTOs(List.of(TestObjects.dealLog()));
        when(dealLogService.findAll()).thenReturn(dealLogDTOs);

        mvc.perform(MockMvcRequestBuilders.get("/deal_logs")
                        .with(csrf())).andExpect(status().isOk());

        verify(dealLogService, times(1)).findAll();
    }

    @Test
    void testShow() throws Exception {
        final List<DealLogDTO> dealLogDTOs = dealLogsToDealLogDTOs(List.of(TestObjects.dealLog()));
        when(dealLogService.get(dealLogDTOs.get(0).getId())).thenReturn((dealLogDTOs.get(0)));

        mvc.perform(MockMvcRequestBuilders.get(String.format("/deal_logs/show/%d", (dealLogDTOs.get(0).getId())))
                        .with(csrf()))
                .andExpect((status().is2xxSuccessful()));

        verify(dealLogService, times(1)).get((dealLogDTOs.get(0).getId()));
    }
}
