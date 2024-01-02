package com.humegatech.mpls_food.services;

import org.springframework.boot.test.mock.mockito.MockBean;

import com.humegatech.mpls_food.PlaceHourRepository;
import com.humegatech.mpls_food.repositories.DayRepository;
import com.humegatech.mpls_food.repositories.DealLogRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.PlaceRepository;
import com.humegatech.mpls_food.repositories.UploadRepository;

class MFServiceTest {
    @MockBean
    DayRepository dayRepository;
    @MockBean
    DealRepository dealRepository;
    @MockBean
    DealLogRepository dealLogRepository;
    @MockBean
    PlaceRepository placeRepository;
    @MockBean
    PlaceHourRepository placeHourRepository;
    @MockBean
    UploadRepository uploadRepository;
}
