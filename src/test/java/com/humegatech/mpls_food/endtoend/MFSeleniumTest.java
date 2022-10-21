package com.humegatech.mpls_food.endtoend;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;

public class MFSeleniumTest {
    WebDriver driver;

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
