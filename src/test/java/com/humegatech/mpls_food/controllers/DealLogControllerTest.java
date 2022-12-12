package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.repositories.DealLogRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.services.DealLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DealLogControllerTest extends MFControllerTest {
    @Autowired
    private DealLogService dealLogService;

    @MockBean
    private PlaceRepository placeRepository;
    @MockBean
    private DealRepository dealRepository;
    @MockBean
    private DealLogRepository dealLogRepository;

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

        when(placeRepository.findById(dealLog.getPlace().getId())).thenReturn(Optional.of(dealLog.getPlace()));
        when(dealRepository.findById(dealLog.getDeal().getId())).thenReturn(Optional.of(dealLog.getDeal()));
        when(dealLogRepository.save(any())).thenReturn(dealLog);

        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/add")
                        .with(csrf())
                        .param("description", dealLog.getDescription())
                        .param("place", dealLog.getPlace().getId().toString())
                        .param("dealType", "DEAL")
                        .param("deal", dealLog.getDeal().getId().toString())
                        .param("redeemed", "1")
                        .param("redemptionDate", df.format(dealLog.getRedemptionDate())))
                .andExpect(status().is3xxRedirection());

        verify(dealLogRepository, times(1)).save(any(DealLog.class));
    }

    @Test
    @WithMockUser
    void testPostDeleteUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/1")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(dealLogRepository, times(0)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        doNothing().when(dealLogRepository).deleteById(1L);

        mvc.perform(MockMvcRequestBuilders.post("/deal_logs/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(dealLogRepository, times(1)).deleteById(1L);
    }
}
