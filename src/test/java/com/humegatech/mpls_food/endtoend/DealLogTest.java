package com.humegatech.mpls_food.endtoend;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DealLogTest extends MFSeleniumTest {
    @Test
    void testDealLogShow() {
        driver.get(String.format("%s/deal_logs", URL_BASE));

        assertTrue(driver.findElements(By.className("deal-log-row")).size() > 0);
    }
}
