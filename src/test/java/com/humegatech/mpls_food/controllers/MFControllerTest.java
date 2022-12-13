package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Place;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.DealDTO;
import com.humegatech.mpls_food.models.PlaceDTO;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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


    List<DealDTO> dealsToDealDTOs(final List<Deal> deals) {
        final List<DealDTO> dealDTOs = new ArrayList<>();
        deals.stream().forEach(d -> dealDTOs.add(ReflectionTestUtils
                .invokeMethod(dealService, "mapToDTO", d, new DealDTO())));
        return dealDTOs;
    }

    List<PlaceDTO> placesToPlaceDTOs(final List<Place> places) {
        final List<PlaceDTO> placeDTOs = new ArrayList<>();
        places.stream().forEach(p -> placeDTOs.add(ReflectionTestUtils
                .invokeMethod(placeService, "mapToDTO", p, new PlaceDTO())));
        return placeDTOs;
    }

    List<UploadDTO> uploadsToUploadDTOs(final List<Upload> uploads) {
        final List<UploadDTO> uploadDTOs = new ArrayList<>();
        uploads.stream().forEach(u -> uploadDTOs.add(ReflectionTestUtils
                .invokeMethod(uploadService, "mapToDTO", u, new UploadDTO())));
        return uploadDTOs;
    }

}
