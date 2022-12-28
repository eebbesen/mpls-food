package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.TestObjects;
import com.humegatech.mpls_food.domains.*;
import com.humegatech.mpls_food.models.*;
import com.humegatech.mpls_food.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MFControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    PlaceService placeService;
    @MockBean
    DealService dealService;
    @MockBean
    DayService dayService;
    @MockBean
    DealLogService dealLogService;
    @MockBean
    UploadService uploadService;

    @Mock
    BindingResult bindingResult;

    @Test
    void testSortedPlaces() {
        final MFController controller = new MFController();
        final Place place = TestObjects.ginellis();
        final Place newPlace = TestObjects.tacoJohns();
        when(placeService.findAll()).thenReturn(placesToPlaceDTOs(List.of(place, newPlace)));

        Map<Long, String> placeMap = controller.sortedPlaces(placeService);
        Iterator<Map.Entry<Long, String>> places = placeMap.entrySet().iterator();

        assertEquals("Ginelli's Pizza", places.next().getValue());
        assertEquals("Taco John's", places.next().getValue());
    }

    List<DealDTO> dealsToDealDTOs(final List<Deal> deals) {
        final List<DealDTO> dealDTOs = new ArrayList<>();
        deals.forEach(d -> dealDTOs.add(ReflectionTestUtils
                .invokeMethod(dealService, "mapToDTO", d, new DealDTO())));
        return dealDTOs;
    }

    List<DealLogDTO> dealLogsToDealLogDTOs(final List<DealLog> dealLogs) {
        final List<DealLogDTO> dealLogDTOs = new ArrayList<>();
        dealLogs.forEach(d -> dealLogDTOs.add(ReflectionTestUtils
                .invokeMethod(dealLogService, "mapToDTO", d, new DealLogDTO())));
        return dealLogDTOs;
    }

    List<PlaceDTO> placesToPlaceDTOs(final List<Place> places) {
        final List<PlaceDTO> placeDTOs = new ArrayList<>();
        places.forEach(p -> placeDTOs.add(ReflectionTestUtils
                .invokeMethod(placeService, "mapToDTO", p, new PlaceDTO())));
        return placeDTOs;
    }

    List<UploadDTO> uploadsToUploadDTOs(final List<Upload> uploads) {
        final List<UploadDTO> uploadDTOs = new ArrayList<>();
        uploads.forEach(u -> uploadDTOs.add(ReflectionTestUtils
                .invokeMethod(uploadService, "mapToDTO", u, new UploadDTO())));
        return uploadDTOs;
    }

    List<DayDTO> daysToDayDTOs(final List<Day> days) {
        final List<DayDTO> dayDTOs = new ArrayList<>();
        days.forEach(d -> dayDTOs.add(ReflectionTestUtils
                .invokeMethod(dayService, "mapToDTO", d, new DayDTO())));
        return dayDTOs;
    }

}
