package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.domains.*;
import com.humegatech.mpls_food.models.*;
import com.humegatech.mpls_food.services.*;
import jakarta.transaction.Transactional;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MFControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    PlaceServiceDTO placeServiceDTO;
    @MockBean
    DealService dealService;
    @MockBean
    DayService dayService;
    @MockBean
    DealLogService dealLogService;
    @MockBean
    UploadService uploadService;
    @MockBean
    PlaceHourService placeHourService;

    @Mock
    BindingResult bindingResult;

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
                .invokeMethod(placeServiceDTO, "mapToDTO", p, new PlaceDTO())));
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

    @Test
    void testSortedPlaces() {
        final MFController controller = new MFController();
        final PlaceDTO place = new PlaceDTO();
        place.setName("Ginelli's Pizza");
        place.setId(1L);
        final PlaceDTO newPlace = new PlaceDTO();
        newPlace.setName("Aurelio's Pizza");
        newPlace.setId(2L);
        PlaceHourService placeHourService = new PlaceHourService(null);
        ReflectionTestUtils.setField(placeServiceDTO, "placeHourService", placeHourService);

        final List<PlaceDTO> placeDTOs = List.of(place, newPlace);
        when(placeServiceDTO.findAll()).thenReturn(placeDTOs);

        Map<Long, String> placeMap = controller.sortedPlaces(placeServiceDTO);
        Iterator<Map.Entry<Long, String>> places = placeMap.entrySet().iterator();

        assertEquals("Aurelio's Pizza", places.next().getValue());
        assertEquals("Ginelli's Pizza", places.next().getValue());
    }
}
