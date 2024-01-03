package com.humegatech.mpls_food.endtoend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

class DayTest extends MFSeleniumTest {
    @Test
    void testDayShow() {
        driver.get(String.format("%s/days", URL_BASE));

        assertTrue(driver.findElements(By.className("day-row")).size() > 0);
    }

    @Test
    void testFilterByDay() {
        driver.get(String.format("%s/days", URL_BASE));
        assertFalse(driver.findElement(By.id("dish")).isDisplayed());

        driver.findElement(By.linkText("Filters")).click();
        Select dayOfWeek = new Select(driver.findElement(By.id("dayOfWeek")));
        dayOfWeek.selectByVisibleText("Tuesday");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("filter-button")));
        driver.findElement(By.id("filter-button")).click();
        List<WebElement> dayRows = driver.findElements(By.className("day-row"));
        assertTrue(dayRows.size() > 0);
        dayRows.forEach(row -> {
            String dow = row.findElements(By.tagName("td")).get(5).getText();
            assertTrue(dow.contains("Tuesday"));
        });
    }

    @Test
    void testFilterByDish() {
        driver.get(String.format("%s/days", URL_BASE));
        assertFalse(driver.findElement(By.id("dish")).isDisplayed());

        driver.findElement(By.linkText("Filters")).click();
        Select dish = new Select(driver.findElement(By.id("dish")));
        dish.selectByVisibleText("Pizza");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("filter-button")));
        driver.findElement(By.id("filter-button")).click();
        List<WebElement> dayRows = driver.findElements(By.className("day-row"));
        assertTrue(dayRows.size() > 0);
        dayRows.forEach(row -> {
            assertTrue(row.getText().contains("slices "));
        });
    }

    @Test
    void testFilterByPlace() {
        final WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));

        driver.get(String.format("%s/days", URL_BASE));
        assertFalse(driver.findElement(By.id("place")).isDisplayed());

        driver.findElement(By.linkText("Filters")).click();
        Select place = new Select(driver.findElement(By.id("place")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"place\"]/option[8]")));
        place.selectByValue("The Burger Place");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("filter-button")));
        driver.findElement(By.id("filter-button")).click();
        List<WebElement> dayRows = driver.findElements(By.className("day-row"));
        assertTrue(dayRows.size() > 0);
        dayRows.forEach(row -> {
            String dow = row.findElements(By.tagName("td")).get(1).getText();
            assertTrue(dow.contains("The Burger Place"));
        });
    }

    @Test
    void testFilterReset() {
        final WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));

        driver.get(String.format("%s/days", URL_BASE));
        driver.findElement(By.linkText("Filters")).click();
        Select dayOfWeek = new Select(driver.findElement(By.id("dayOfWeek")));
        dayOfWeek.selectByVisibleText("Tuesday");
        Select dish = new Select(driver.findElement(By.id("dish")));
        dish.selectByVisibleText("Burger");
        Select place = new Select(driver.findElement(By.id("place")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"place\"]/option[8]")));
        place.selectByValue("The Burger Place");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("filter-button")));
        driver.findElement(By.id("filter-button")).click();

        final List<WebElement> filteredDayRows = driver.findElements(By.className("day-row"));
        assertTrue(filteredDayRows.size() > 0);
        filteredDayRows.forEach(row -> {
            String dow = row.findElements(By.tagName("td")).get(1).getText();
            assertTrue(dow.contains("The Burger Place"));
        });

        driver.findElement(By.id("reset-button")).click();
        dayOfWeek = new Select(driver.findElement(By.id("dayOfWeek")));
        assertEquals("Day", dayOfWeek.getAllSelectedOptions().get(0).getText());
        dish = new Select(driver.findElement(By.id("dish")));
        assertEquals("Dish", dish.getAllSelectedOptions().get(0).getText());
        place = new Select(driver.findElement(By.id("place")));
        assertEquals("Place", place.getAllSelectedOptions().get(0).getText());

        driver.findElement(By.id("filter-button")).click();
        final List<WebElement> unfilteredDayRows = driver.findElements(By.className("day-row"));
        final List<WebElement> wednesdayDayRows = unfilteredDayRows
            .stream()
            .filter(row -> row.findElements(By.tagName("td")).get(5).getText().equals("Wednesday"))
                                              .collect(Collectors.toList());
        assertTrue(wednesdayDayRows.size() > 0);

        final List<String> unfilteredPlaces = unfilteredDayRows
            .stream()
            .map(row -> row.findElements(By.tagName("td")).get(1).getText())
            .distinct()
            .collect(Collectors.toList());
        assertTrue(unfilteredPlaces.size() > 1);
    }

    @Test
    void testHappyHourFilter() {
        driver.get(String.format("%s/days?dayOfWeek=MONDAY&dish=&place=Barrio&happyHour=on&sortBy=", URL_BASE));
        List<WebElement> dayRows = driver.findElements(By.className("day-row"));
        assertTrue(dayRows.size() > 0);

        driver.findElement(By.id("happyHour")).click();
        driver.findElement(By.id("filter-button")).click();
        assertNotNull(driver.findElement(By.id("no-deals")));
    }
}
