package org.vaadin.addons.tatu;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vaadin.flow.testutil.TestPath;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class AbstractComponentIT
        extends AbstractViewTest {
    private static final int SERVER_PORT = 8080;
    private static final String USE_HUB_PROPERTY = "test.use.hub";

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

        // We do screenshot testing, adjust settings to ensure less flakiness
        Parameters.setScreenshotComparisonTolerance(0.05);
        Parameters.setScreenshotComparisonCursorDetection(true);
        testBench().resizeViewPortTo(800, 600);
        Parameters.setMaxScreenshotRetries(3);
        Parameters.setScreenshotRetryDelay(1000);

        // Wait for frontend compilation complete before testing
        waitForDevServer();
    }

    protected void open() {
        open((String[]) null);
    }

    protected void open(String... parameters) {
        String url = getTestURL(parameters);
        getDriver().get(url);
        waitForDevServer();
    }    

    protected String getTestURL(String... parameters) {
        return getTestURL(getRootURL(), getTestPath(), parameters);
    }

    public static String getTestURL(String rootUrl, String testPath,
            String... parameters) {
        while (rootUrl.endsWith("/")) {
            rootUrl = rootUrl.substring(0, rootUrl.length() - 1);
        }
        rootUrl = rootUrl + testPath;

        if (parameters != null && parameters.length != 0) {
            if (!rootUrl.contains("?")) {
                rootUrl += "?";
            } else {
                rootUrl += "&";
            }

            rootUrl += Arrays.stream(parameters)
                    .collect(Collectors.joining("&"));
        }

        return rootUrl;
    }

    protected String getTestPath() {
        TestPath annotation = getClass().getAnnotation(TestPath.class);
        if (annotation == null) {
            throw new IllegalStateException(
                    "The test class should be annotated with @TestPath");
        }

        String path = annotation.value();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    /**
     * Returns the URL to the root of the server, e.g. "http://localhost:8888"
     *
     * @return the URL to the root
     */
    protected String getRootURL() {
        return "http://" + getDeploymentHostname() + ":" + getDeploymentPort();
    }

    protected int getDeploymentPort() {
        return SERVER_PORT;
    }

    private static String getDeploymentHostname() {
        return isUsingHub() ? System.getenv("HOSTNAME") : "localhost";
    }

    private static boolean isUsingHub() {
        return Boolean.TRUE.toString().equals(
                System.getProperty(USE_HUB_PROPERTY));
    }

    protected void clickElementWithJs(WebElement element) {
        executeScript("arguments[0].click();", element);
    }

    protected void waitForElementNotPresent(final By by) {
        waitUntil(input -> input.findElements(by).isEmpty());
    }
}