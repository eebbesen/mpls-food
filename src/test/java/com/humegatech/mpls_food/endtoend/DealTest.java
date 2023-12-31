package com.humegatech.mpls_food.endtoend;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DealTest extends MFSeleniumTest {
    @Test
    void testDealEdit() {
        loginAdmin();
        driver.get(String.format("%s/deals/edit/67", URL_BASE));

        Select placeSelect = new Select(driver.findElement(By.id("place")));
        assertEquals("Taco John's", placeSelect.getFirstSelectedOption().getText());
        assertEquals("Save Deal", driver.findElement(By.id("save-deal")).getAttribute("value"));
        assertEquals("Upload Image", driver.findElement(By.id("fileUploadButton")).getAttribute("value"));
    }

    @Test
    void testList() {
        driver.get(String.format("%s/deals", URL_BASE));

        assertTrue(driver.findElements(By.className("deal-row")).size() > 0);
    }
}
