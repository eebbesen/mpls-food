package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.repositories.*;
import org.springframework.boot.test.mock.mockito.MockBean;

public class MFServiceTest {
    @MockBean
    DayRepository dayRepository;
    @MockBean
    DealRepository dealRepository;
    @MockBean
    DealLogRepository dealLogRepository;
    @MockBean
    PlaceRepository placeRepository;
    @MockBean
    UploadRepository uploadRepository;

}
