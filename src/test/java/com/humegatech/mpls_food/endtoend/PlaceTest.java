package com.humegatech.mpls_food.endtoend;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceTest extends MFSeleniumTest {
    @Test
    public void testPlaceList() {
        driver.get("http://localhost:8080/places");
        final List<WebElement> placeRows = driver.findElements(By.name("place-row"));
        final WebElement afroDeliRow = placeRows.get(0);
        final WebElement andreaCapellaRow = placeRows.get(1);

        assertEquals("Places | MplsFood", driver.getTitle());

        assertEquals("Afro Deli 705 S Marquette Avenue", afroDeliRow.getText());
        assertNotNull(afroDeliRow.findElement(By.name("order-ahead-ind")));
        assertNotNull(afroDeliRow.findElement(By.name("rewards-ind")));

        assertEquals("Andrea Pizza Capella Tower 225 S 6th St. Capella Tower", andreaCapellaRow.getText());
        assertEquals(0, andreaCapellaRow.findElements(By.name("order-ahead-ind")).size());
        assertEquals(0, andreaCapellaRow.findElements(By.name("rewards-ind")).size());
    }

    @Test
    public void testPlaceShowNoDeals() {
        driver.get("http://localhost:8080/places/show/16");

        assertEquals("Afro Deli", driver.findElement(By.id("name")).getAttribute("value"));
        assertEquals("705 S Marquette Avenue\nMinneapolis, MN 55402",
                driver.findElement(By.id("address")).getText());
        assertEquals("https://www.afrodeli.com/locations/skyway",
                driver.findElement(By.id("website")).getAttribute("value"));
        assertFalse(Boolean.parseBoolean(driver.findElement(By.id("app1")).getAttribute("selected")));
        assertTrue(Boolean.parseBoolean(driver.findElement(By.id("orderAhead1")).getAttribute("selected")));
        assertEquals("TOAST", driver.findElement(By.id("rewardType")).getAttribute("value"));
        assertEquals("1 point for every $2 you spend and receive a $5 discount for every 100 points you redeem",
                driver.findElement(By.id("rewardNotes")).getText());
        assertEquals("No deals found", driver.findElement(By.id("deal-rows")).getText());
    }

    @Test
    public void testPlaceShowWithDeals() {
        driver.get("http://localhost:8080/places/show/10000");

        assertEquals("Ginelli's Pizza", driver.findElement(By.id("name")).getAttribute("value"));
        assertFalse(Boolean.parseBoolean(driver.findElement(By.id("orderAhead1")).getAttribute("selected")));
        assertEquals("PUNCH_CARD", driver.findElement(By.id("rewardType")).getAttribute("value"));
        assertEquals(5, driver.findElements(By.name("deal-row")).size());
    }

}
