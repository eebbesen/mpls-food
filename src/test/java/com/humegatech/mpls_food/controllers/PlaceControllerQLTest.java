package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.services.PlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.Mockito.when;

@GraphQlTest(PlaceControllerQL.class)
public class PlaceControllerQLTest {
    @Autowired
    private GraphQlTester graphQlTester;
    @MockBean
    PlaceService placeService;

    private final static List<Place> places = List.of(TestObjects.ginellis(), TestObjects.tacoJohns());

    @Test
    void testFindByIdWithRewardAttributes() {
        final Long id = places.get(0).getId();
        when(placeService.get(id)).thenReturn(places.get(0));
        final String query = String.format("query findById { findPlaceById(id: %d) { name reward { notes rewardType }  } }", id);
        graphQlTester.document(query)
                .execute()
                .path("findPlaceById.name")
                .entity(String.class)
                .isEqualTo("Ginelli's Pizza")
                .path("findPlaceById.reward")
                .matchesJson("""
                        {
                            "notes":"Free slice after purchase of 9 regularly priced slices",
                            "rewardType":"PUNCH_CARD"
                        }
                        """);
    }

    @Test
    void testFindByIdWithPlaceHourAttributes() {
        final Long id = places.get(0).getId();
        when(placeService.get(id)).thenReturn(places.get(0));
        final String query = String.format("query findById { findPlaceById(id: %d) { name placeHours { dayOfWeek openTime closeTime }  } }", id);
        graphQlTester.document(query)
                .execute()
                .path("findPlaceById.name")
                .entity(String.class)
                .isEqualTo("Ginelli's Pizza")
                .path("findPlaceById.placeHours[*].dayOfWeek")
                .entityList(String.class)
                .contains("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
                .path("findPlaceById.placeHours[*].openTime")
                .entityList(String.class)
                .contains(places.get(0).getPlaceHours().stream().map(ph -> ph.getOpenTime().toString()).toArray(String[]::new))
                .path("findPlaceById.placeHours[*].closeTime")
                .entityList(String.class)
                .contains(places.get(0).getPlaceHours().stream().map(ph -> ph.getCloseTime().toString()).toArray(String[]::new));
    }

    @Test
    void testFindById() {
        final Long id = places.get(0).getId();
        when(placeService.get(id)).thenReturn(places.get(0));
        final String query = String.format("query findById { findPlaceById(id: %d) { id name address website app orderAhead  } }", id);
        graphQlTester.document(query)
                .execute()
                .path("findPlaceById")
                .matchesJson(String.format("""
                               {
                                   "id":"%s",
                                   "name":"Ginelli's Pizza",
                                   "address":"121 S 8th Street #235\\nMinneapolis, MN 55402",
                                   "website":"https://www.ginellispizza.com/",
                                   "app":false,
                                   "orderAhead":false
                               }
                        """, id));
    }

    @Test
    void testAllPlaces() {
        when(placeService.findAll()).thenReturn(places);

        final Long id1 = places.get(0).getId();
        final Long id2 = places.get(1).getId();
        final String query = "query findAllPlaces { allPlaces { id name address website app orderAhead } }";
        graphQlTester.document(query)
                .execute()
                .path("allPlaces")
                .matchesJson(String.format("""
                               [{
                                   "id":"%d",
                                   "name":"Ginelli's Pizza",
                                   "address":"121 S 8th Street #235\\nMinneapolis, MN 55402",
                                   "website":"https://www.ginellispizza.com/",
                                   "app":false,
                                   "orderAhead":false
                               },
                               {
                                   "id":"%d",
                                   "name":"Taco John's",
                                   "address":"'607 Marquette Ave.\\n        Minneapolis, MN 55402",
                                   "website":"https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/",
                                   "app":true,
                                   "orderAhead":false
                               }]
                        """, id1, id2));

    }
}
