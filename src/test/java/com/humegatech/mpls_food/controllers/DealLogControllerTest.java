package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.models.DealLogDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DealLogControllerTest extends MFControllerTest {
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
    void testPostDeleteUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/1")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(dealLogService, times(0)).delete(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        doNothing().when(dealLogService).delete(99L);

        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/99")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(dealLogService, times(1)).delete(99L);
    }
}
