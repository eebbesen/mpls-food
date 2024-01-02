package com.humegatech.mpls_food.endtoend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

class PlaceTest extends MFSeleniumTest {
    @Test
    void testPlaceList() {
        driver.get(String.format("%s/places", URL_BASE));
        final List<WebElement> placeRows = driver.findElements(By.className("place-row"));
        final WebElement afroDeliRow = placeRows.get(0);
        final WebElement andreaCapellaRow = placeRows.get(1);

        assertEquals("Places | MplsFood", driver.getTitle());

        assertEquals("Afro Deli 705 S Marquette Avenue", afroDeliRow.getText());
        assertNotNull(afroDeliRow.findElement(By.className("order-ahead-ind")));
        assertNotNull(afroDeliRow.findElement(By.className("rewards-ind")));

        assertEquals("Andrea Pizza Capella Tower 225 S 6th St. Capella Tower", andreaCapellaRow.getText());
        assertEquals(0, andreaCapellaRow.findElements(By.className("order-ahead-ind")).size());
        assertEquals(0, andreaCapellaRow.findElements(By.className("rewards-ind")).size());
    }

    @Test
    void testPlaceShowNoDeals() {
        driver.get(String.format("%s/places/show/16", URL_BASE));

        assertEquals("Afro Deli", driver.findElement(By.id("name")).getAttribute("value"));
        assertEquals("705 S Marquette Avenue\nMinneapolis, MN 55402",
                driver.findElement(By.id("address")).getText());
        assertEquals("https://www.afrodeli.com/locations/skyway",
                driver.findElement(By.id("website")).getAttribute("value"));
        assertFalse(Boolean.parseBoolean(driver.findElement(By.id("app1")).getAttribute("selected")));
        assertTrue(Boolean.parseBoolean(driver.findElement(By.id("orderAhead1")).getAttribute("selected")));
        assertEquals("TOAST", driver.findElement(By.id("rewardType")).getAttribute("value"));
        assertEquals("1 point for every $2 you spend and receive a $2.50 discount for every 50 points you redeem. 2.5% back.",
                driver.findElement(By.id("rewardNotes")).getText());
        assertEquals("No Deals found", driver.findElement(By.id("deal-rows")).getText());
    }

    @Test
    void testPlaceShowWithDeals() {
        driver.get(String.format("%s/places/show/10000", URL_BASE));

        assertEquals("Ginelli's Pizza", driver.findElement(By.id("name")).getAttribute("value"));
        assertFalse(Boolean.parseBoolean(driver.findElement(By.id("orderAhead1")).getAttribute("selected")));
        assertEquals("PUNCH_CARD", driver.findElement(By.id("rewardType")).getAttribute("value"));
        assertEquals(5, driver.findElements(By.className("deal-row")).size());
    }

}
