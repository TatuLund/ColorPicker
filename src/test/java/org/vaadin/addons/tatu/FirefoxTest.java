package org.vaadin.addons.tatu;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Objects;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

/**
 * This class configures Firefox driver to be used in the tests using
 * WebDriverManager, see pom.xml.
 */
public abstract class FirefoxTest extends AbstractColorPickerIT {

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.firefoxdriver().setup();
    }

    @Before
    public void setup() throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        setDriver(TestBench.createDriver(new FirefoxDriver(options)));
        getDriver().get(getURL(route));
        browser = "firefox";

        // We do screenshot testing, adjust settings to ensure less flakiness
        Parameters.setScreenshotComparisonTolerance(0.05);
        Parameters.setScreenshotComparisonCursorDetection(true);
        testBench().resizeViewPortTo(800, 600);
        Parameters.setMaxScreenshotRetries(3);
        Parameters.setScreenshotRetryDelay(1000);

        // Wait for frontend compilation complete before testing
        waitForDevServer();
    }
}