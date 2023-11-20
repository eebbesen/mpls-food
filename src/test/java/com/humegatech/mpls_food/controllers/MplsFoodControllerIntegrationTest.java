package com.humegatech.mpls_food.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MplsFoodControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    void getRoot() {
        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertTrue(response.getStatusCode().is3xxRedirection());
    }
}
