package com.humegatech.mpls_food.endtoend;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DealLogTest extends MFSeleniumTest {
    @Test
    void testDealLogShow() {
        driver.get("http://localhost:8080/deal_logs");

        assertTrue(driver.findElements(By.name("deal-log-row")).size() > 0);
    }
}
