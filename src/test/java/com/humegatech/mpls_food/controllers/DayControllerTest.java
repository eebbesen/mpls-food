package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.models.DayDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(content().string(not(containsString("slices"))));
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
        mvc.perform(MockMvcRequestBuilders.get("/days?dayOfWeek=BLORTSDAY").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testListWithDish() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dish=Pizza").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")))
                .andExpect(content().string(containsString("Ginelli&#39;s")));
    }

    @Test
    void testListWithDishNoRecords() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?dish=Curry").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(not(containsString("slices"))));
    }

    @Test
    void testListWithCuisine() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?cuisine=Italian").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(containsString("slices")));
    }

    @Test
    void testListWithCuisineNoRecords() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/days?cuisine=Thai").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")))
                .andExpect(content().string(not(containsString("slices"))));
    }

    @Test
    void testListSortByPriceHtml() throws Exception {
        final Deal deal99 = TestObjects.deal(place, "z 99 cent deal",
                LocalDateTime.now(ZoneId.systemDefault()).getDayOfWeek());
        deal99.setMinPrice(0.99d);
        deal99.setId(null);
        deal99.getDays().forEach(d -> d.setId(null));
        place.getDeals().add(deal99);
        dealRepository.save(deal99);
        final Deal deal199 = TestObjects.deal(place, "a 199 cent deal",
                LocalDateTime.now(ZoneId.systemDefault()).getDayOfWeek());
        deal199.setMinPrice(1.99d);
        deal199.setId(null);
        deal199.getDays().forEach(d -> d.setId(null));
        place.getDeals().add(deal199);
        dealRepository.save(deal199);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/days?sortBy=price")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andReturn();
        final String fullHtml = result.getResponse().getContentAsString();
        final String htmlBody = fullHtml.substring(fullHtml.indexOf("<body"), fullHtml.lastIndexOf("</body>") + 7);

        Document doc = Jsoup.parse(htmlBody);
        Elements rows = doc.getElementById("daysTable").getElementsByTag("tr");
        assertTrue(rows.get(1).text().contains("$0.99"));
        assertTrue(rows.get(2).text().contains("$1.99"));
        assertTrue(rows.get(3).text().contains("$5.00"));
    }

    @Test
    void testHandleSortDiscountAsc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minDiscount(12d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minDiscount(9d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "price");

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortDiscountDesc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minDiscount(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minDiscount(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day099);
        days.add(day101);

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "priceDesc");

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortDiscountPercentAsc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minDiscountPercent(12d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minDiscountPercent(9d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "price");

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortDiscountPercentDesc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minDiscountPercent(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minDiscountPercent(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day099);
        days.add(day101);

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "priceDesc");

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortPriceAsc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minPrice(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minPrice(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "price");

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortPriceDesc() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minPrice(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minPrice(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day099);
        days.add(day101);

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "priceDesc");

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortMinPriceNull() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minPrice(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getPlaceName(), days.get(0).getPlaceName());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "price");

        assertEquals(day099.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortBlankSortBy() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minPrice(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minPrice(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getPlaceName(), days.get(0).getPlaceName());

        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, "");

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testHandleSortNullSortBy() {
        final DayDTO day101 = DayDTO.builder()
                .placeName("a")
                .minPrice(1.01d)
                .build();
        final DayDTO day099 = DayDTO.builder()
                .placeName("z")
                .minPrice(.99d)
                .build();
        List<DayDTO> days = new ArrayList<>();
        days.add(day101);
        days.add(day099);

        assertEquals(day101.getPlaceName(), days.get(0).getPlaceName());

        final String nullString = null;
        ReflectionTestUtils.invokeMethod(DayController.class, "handleSort", days, nullString);

        assertEquals(day101.getMinPrice(), days.get(0).getMinPrice());
    }

    @Test
    void testCalculateNextSortDiscountPercentDiscount() {
        assertEquals("discount", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "discountPercent", "discount"));
    }

    @Test
    void testCalculateNextSortPriceAscToDesc() {
        assertEquals("priceDesc", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "price", "price"));
    }

    @Test
    void testCalculateNextSortDiscountAscToDesc() {
        assertEquals("discountDesc", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "discount", "discount"));
    }

    @Test
    void testCalculateNextSortDiscountDescAsc() {
        assertEquals("discount", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "discountDesc", "discount"));
    }

    @Test
    void testCalculateNextSortDescToAsc() {
        assertEquals("price", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "priceDesc", "price"));
    }

    @Test
    void testCalculateNextSortNoMatch() {
        assertEquals("price", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "discount", "price"));
    }

    @Test
    void testCalculateNextSortSortByBlank() {
        assertEquals("price", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", "", "price"));
    }

    @Test
    void testCalculateNextSortSortByNull() {
        final String nullString = null;
        assertEquals("price", ReflectionTestUtils.invokeMethod(DayController.class, "calculateNextSort", nullString, "price"));
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
    void testHandleDayOfWeek() {
        DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", "TUESDAY");
        assertEquals(DayOfWeek.TUESDAY, dow);
    }

    @Test
    void testHandleDayOfWeekNullDow() {
        final String nullString = null;
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", nullString);
        assertNull(dow);
    }

    @Test
    void testHandleDayOfWeekEmptyDow() {
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", "");
        assertNull(dow);
    }

    @Test
    void testHandleDayOfWeekInvalidDow() {
        final String invalid = "BLORTSDAY";
        final DayOfWeek dow = ReflectionTestUtils.invokeMethod(controller, "handleDayOfWeekFilter", invalid);
        assertNull(dow);
    }

    @Test
    void handleFilter() {
        final String dish = ReflectionTestUtils.invokeMethod(controller, "handleFilter", "Pizza");
        assertEquals("Pizza", dish);
    }

    @Test
    void handleFilterNullFilter() {
        final String nullString = null;
        final String dish = ReflectionTestUtils.invokeMethod(controller, "handleFilter", "Samosas");
        assertEquals("Samosas", dish);
    }

    @Test
    void handleFilterEmptyFilter() {
        final String dish = ReflectionTestUtils.invokeMethod(controller, "handleFilter", "");
        assertNull(dish);
    }

}
