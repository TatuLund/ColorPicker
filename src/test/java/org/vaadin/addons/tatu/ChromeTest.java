package org.vaadin.addons.tatu;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;

/**
 * This class configures Chrome driver to be used in the tests using
 * WebDriverManager, see pom.xml.
 */
public abstract class ChromeTest extends AbstractColorPickerIT {

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        setDriver(TestBench.createDriver(new ChromeDriver(options)));
        getDriver().get(getURL(route));
        browser = "chrome";

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