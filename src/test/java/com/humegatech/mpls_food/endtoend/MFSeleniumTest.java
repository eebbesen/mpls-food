package com.humegatech.mpls_food.endtoend;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class MFSeleniumTest {
    static String URL_BASE = Optional.ofNullable(System.getenv("URL_BASE"))
        .orElse("http://localhost:8080");
    WebDriver driver;

    @BeforeAll
    static void setDriver() {
//        https://bonigarcia.dev/webdrivermanager/#advanced-configuration
//        WebDriverManager.chromedriver().clearDriverCache();
//        WebDriverManager.chromedriver().driverVersion("119.0.6045.105").setup();
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    void loginAdmin() {
        driver.get(String.format("%s/login", URL_BASE));
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("retek01!");
        driver.findElements(By.className("btn")).get(0).click();
    }

    void logout() {
        driver.findElement(By.id("logout")).click();
    }

}
