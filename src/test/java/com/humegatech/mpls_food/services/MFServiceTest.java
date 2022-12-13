package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.repositories.DayRepository;
import com.humegatech.mpls_food.repositories.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class MFServiceTest {
    @MockBean
    DayRepository dayRepository;

    @MockBean
    DealRepository dealRepository;

    @Autowired
    DayService service;
}
