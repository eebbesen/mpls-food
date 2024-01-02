package com.humegatech.mpls_food.controllers;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.humegatech.mpls_food.domains.Day;
import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.DealLog;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.DayDTO;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.DealLogDTO;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.services.DayService;
import com.humegatech.mpls_food.services.DealLogService;
import com.humegatech.mpls_food.services.DealService;
import com.humegatech.mpls_food.services.PlaceHourService;
import com.humegatech.mpls_food.services.PlaceService;
import com.humegatech.mpls_food.services.UploadService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MFControllerTest {

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
