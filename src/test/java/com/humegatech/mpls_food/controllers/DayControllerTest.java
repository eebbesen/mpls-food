package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DayControllerTest extends MFControllerTest {
    private Place place;
    private Deal deal;

    @Autowired
    private DayController controller;

    @BeforeEach
    void setUp() {
        place = placeRepository.save(TestObjects.ginellis());
        Deal d = TestObjects.fridayTwofer();
        d.setPlace(place);
        deal = dealRepository.save(d);
    }

    @Test
    void testList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testListWithDayOfWeek() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dayOfWeek=FRIDAY").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testListWithDayOfWeekNoRecords() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dayOfWeek=MONDAY").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(not(containsString("slices"))))
                .andExpect(content().string(not(containsString("Ginelli&#39;s"))));
    }

    @Test
    void testListWithDayOfWeekNoDay() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dayOfWeek=").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testListWithDayOfInvalidDay() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dayOfWeek=BLAH").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testListUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logout")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Edit")))
                .andExpect(content().string(containsString("Delete")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    @WithMockUser
    void testPostDeleteNotAllowed() throws Exception {
        final Day day = dealRepository.findById(deal.getId()).get().getDays().stream().findFirst().get();
        mvc.perform(MockMvcRequestBuilders.get("/days/add").accept(MediaType.APPLICATION_XML))
                .andExpect(status().is4xxClientError());
        assertEquals(day.getId(), dayRepository.findById(day.getId()).get().getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostDeleteAdmin() throws Exception {
        final Day day = dealRepository.findById(deal.getId()).get().getDays().stream().findFirst().get();
        mvc.perform(MockMvcRequestBuilders.post(String.format("/days/delete/%d", day.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(Optional.empty(), dayRepository.findById(day.getId()));
    }

    @Test
    void testHandleDayOfWeek() throws Exception {
        DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", "TUESDAY");
        assertEquals(DayOfWeek.TUESDAY, dow);
    }

    @Test
    void testHandleDayOfWeekNullDow() throws Exception {
        final String nullString = null;
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", nullString);
        assertEquals(null, dow);
    }

    @Test
    void testHandleDayOfWeekEmptyDow() throws Exception {
        final String emptyString = "";
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", emptyString);
        assertEquals(null, dow);
    }

    @Test
    void testHandleDayOfWeekInvalidDow() throws Exception {
        final String invalid = "BLORTSDAY";
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", invalid);
        assertEquals(null, dow);
    }
}
