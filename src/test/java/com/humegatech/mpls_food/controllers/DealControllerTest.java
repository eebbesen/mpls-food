package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class DealControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;
    @Autowired
    private DealController controller;

    @Mock
    private BindingResult bindingResult;

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
    void testPostAddUserBindingResultError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        final String ret = controller.add(new DealDTO(), bindingResult, null);
        assertEquals("deal/add", ret);
    }

//    // todo this test will fail once the app handles bad payloads better
//    // for now the expectation is never used
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testPostAddAdminBadPayload() {
//        Assertions.assertThrows(NestedServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post("/deals/add")
//                        .with(csrf())
//                        .param("friday", "true")
//                        .param("place", place.getId().toString()))
//                .andExpect(status().is4xxClientError()));
//    }

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
    void testPostEditUserBindingResultError() throws Exception {
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
    void testSortedPlaces() {
        controller = new DealController(dealService, placeService);
        final Place newPlace = TestObjects.tacoJohns();
        when(placeService.findAll()).thenReturn(placesToPlaceDTOs(List.of(place, newPlace)));

        Map<Long, String> placeMap = ReflectionTestUtils.invokeMethod(controller, "sortedPlaces");
        Iterator<Map.Entry<Long, String>> places = placeMap.entrySet().iterator();

        assertEquals("Ginelli's Pizza", places.next().getValue());
        assertEquals("Taco John's", places.next().getValue());
    }
}
