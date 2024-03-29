package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.PlaceHourDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlaceControllerDTOTest extends MFControllerTest {
    private Place place;
    private PlaceDTO placeDTO;
    private Deal deal;

    @BeforeEach
    void setUp() {
        deal = TestObjects.fridayTwofer();
        place = deal.getPlace();
        deal.setPlace(place);

        final PlaceHourDTO placeHourDTO1 = PlaceHourDTO.builder()
                .place(place.getId())
                .dayOfWeek(DayOfWeek.FRIDAY)
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(17, 0))
                .build();
        final PlaceHourDTO placeHourDTO2 = PlaceHourDTO.builder()
                .place(place.getId())
                .dayOfWeek(DayOfWeek.THURSDAY)
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(17, 0))
                .build();
        final Set<PlaceHourDTO> placeHours = new HashSet<>();
        placeHours.add(placeHourDTO1);
        placeHours.add(placeHourDTO2);

        placeDTO = PlaceDTO.builder()
                .rewardType(place.getReward().getRewardType())
                .rewardNotes(place.getReward().getNotes())
                .address(place.getAddress())
                .website(place.getWebsite())
                .name(place.getName())
                .placeHours(placeHours)
                .build();
    }

    @Test
    void testList() throws Exception {
        when(placeServiceDTO.findAll()).thenReturn(List.of(placeDTO));

        mvc.perform(MockMvcRequestBuilders.get("/places").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testListUser() throws Exception {
        when(placeServiceDTO.findAll()).thenReturn(List.of(placeDTO));

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
        when(placeServiceDTO.get(place.getId())).thenReturn(placeDTO);

        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/edit/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("Edit Place")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testPostEditUserNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("name", "updated name")
                        .param("address", "updated address")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostEditAdmin() throws Exception {
        when(placeServiceDTO.get(place.getId())).thenReturn(placeDTO);

        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("name", "updated name")
                        .param("address", "updated address")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostEditNameTheSame() throws Exception {
        when(placeServiceDTO.get(place.getId())).thenReturn(placeDTO);

        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("name", place.getName())
                        .param("address", "updated address")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostEditDifferentNameNameExists() throws Exception {
        Place otherPlace = TestObjects.place("New Place");
        otherPlace.setId(9999L);
        when(placeServiceDTO.get(otherPlace.getId())).thenReturn(placeDTO);
        when(placeServiceDTO.nameExists(otherPlace.getName())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", otherPlace.getId()))
                        .with(csrf())
                        .param("id", otherPlace.getId().toString())
                        .param("name", otherPlace.getName())
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("This Name is already taken")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostEditNameBlank() throws Exception {
        when(placeServiceDTO.get(place.getId())).thenReturn(placeDTO);

        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/edit/%s", place.getId()))
                        .with(csrf())
                        .param("id", place.getId().toString())
                        .param("address", "updated address")
                        .param("name", "")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testPostAddNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "new place")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());

        verify(placeServiceDTO, times(0)).create(any(PlaceDTO.class));
    }

    @Test
    @WithMockUser
    void testPostAddUser() throws Exception {
        when(placeServiceDTO.create(placeDTO)).thenReturn(1L);

        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "new place")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is3xxRedirection());

        verify(placeServiceDTO, times(1)).create(any(PlaceDTO.class));
    }

    @Test
    @WithMockUser
    void testPostAddUserBlankName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", "")
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful());

        verify(placeServiceDTO, times(0)).create(any(PlaceDTO.class));
    }

    @Test
    @WithMockUser
    void testPostAddUserNameExists() throws Exception {
        when(placeServiceDTO.nameExists(place.getName())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.post("/places/add")
                        .with(csrf())
                        .param("name", place.getName())
                        .param("address", "123 main street")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void testPostDeleteUserNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/delete/%s", place.getId()))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(String.format("/places/delete/%s", place.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(placeServiceDTO, times(1)).delete(place.getId());
    }

    @Test
    void testShow() throws Exception {
        place.setId(99L);
        final DealDTO dealDTO = DealDTO.builder()
                .place(deal.getPlace().getId())
                .dish(deal.getDish())
                .placeName(deal.getPlace().getName())
                .description(deal.getDescription())
                .build();
        when(placeServiceDTO.get(place.getId())).thenReturn(placeDTO);
        when(dealService.findByPlaceId(place.getId())).thenReturn(List.of(dealDTO));

        mvc.perform(MockMvcRequestBuilders.get(String.format("/places/show/%s", place.getId())).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")));
    }

    @Test
    @WithMockUser
    void testGetAdd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/places/add"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("Add Place")));
    }
}
