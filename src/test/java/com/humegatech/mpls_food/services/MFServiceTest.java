package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.repositories.*;
import org.springframework.boot.test.mock.mockito.MockBean;

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
