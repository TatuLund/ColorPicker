package org.vaadin.addons.tatu;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxGroupElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.customfield.testbench.CustomFieldElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.screenshot.ImageFileUtil;

public class ColorPickerIT extends AbstractViewTest {

    private ColorPickerElement colorPicker;
    private CustomFieldElement field;
    private CheckboxGroupElement options;
    private CheckboxGroupElement variants;
    private boolean sleep = false;

    public void blur() {
        executeScript(
                "!!document.activeElement ? document.activeElement.blur() : 0");
    }

    @Override
    public void setup() throws Exception {
        super.setup();
        colorPicker = $(ColorPickerElement.class).first();
        field = colorPicker.getFieldWrapper();
        options = $(CheckboxGroupElement.class).id("options");
        variants = $(CheckboxGroupElement.class).id("variants");

        // Hide dev mode gizmo, it would interfere screenshot tests
        try {
            $("vaadin-dev-tools").first().setProperty("hidden", true);
        } catch (NotFoundException e) {

        }
    }

    @Test
    public void componentWorks() {
        Assert.assertTrue(
                colorPicker.$(TestBenchElement.class).all().size() > 0);
        TestBenchElement picker = colorPicker.getPicker();
        Assert.assertEquals("Input is not a color picker", "color",
                picker.getAttribute("type"));
    }

    @Test
    public void themeAttributeGetsPropagated() {
        colorPicker.setProperty("theme", "theme");
        Assert.assertEquals("theme",
                colorPicker.getComboBox().getAttribute("theme"));
        Assert.assertEquals("theme",
                colorPicker.getPicker().getAttribute("theme"));
        Assert.assertEquals("theme",
                colorPicker.getFieldWrapper().getAttribute("theme"));
    }

    @Test
    public void disabledAttributeGetsPropagated() {
        options.selectByText("Disabled");
        Assert.assertEquals("true",
                colorPicker.getComboBox().getAttribute("disabled"));
        Assert.assertEquals("true",
                colorPicker.getPicker().getAttribute("disabled"));
        Assert.assertEquals("true",
                colorPicker.getFieldWrapper().getAttribute("disabled"));
        options.deselectByText("Disabled");
        clear();
        Assert.assertEquals(null,
                colorPicker.getComboBox().getAttribute("disabled"));
        Assert.assertEquals(null,
                colorPicker.getPicker().getAttribute("disabled"));
        Assert.assertEquals(null,
                colorPicker.getFieldWrapper().getAttribute("disabled"));
    }

    @Test
    public void invalidAttributeGetsPropagated() {
        options.selectByText("Invalid");
        Assert.assertEquals("true",
                colorPicker.getComboBox().getAttribute("invalid"));
        Assert.assertEquals("true",
                colorPicker.getPicker().getAttribute("invalid"));
        Assert.assertEquals("true",
                colorPicker.getFieldWrapper().getAttribute("invalid"));
        clear();
        Assert.assertEquals("false",
                colorPicker.getComboBox().getAttribute("invalid"));
        Assert.assertEquals(null,
                colorPicker.getPicker().getAttribute("invalid"));
        Assert.assertEquals("false",
                colorPicker.getFieldWrapper().getAttribute("invalid"));
    }

    @Test
    public void requiredAttributeGetsPropagated() {
        options.selectByText("Required");
        Assert.assertEquals("true",
                colorPicker.getFieldWrapper().getAttribute("required"));
        clear();
        Assert.assertEquals(null,
                colorPicker.getFieldWrapper().getAttribute("required"));
    }

    @Test
    public void presetWorks() {
        colorPicker.selectPreset("Color 1");
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not set", "#00ff00",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#00ff00", colorPicker.getPicker().getPropertyString("value"));
        colorPicker.setProperty("nocssinput", true);
        colorPicker.selectPreset("Color 2");
        notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not set", "#ff0000",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#ff0000", colorPicker.getPicker().getPropertyString("value"));
        colorPicker.focus();
        colorPicker.sendKeys("Color 1");
        blur();
        notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not set", "#00ff00",
                notification.getText());
    }

    @Test
    public void presetNotSelectedWhenDisabled() {
        options.selectByText("Disabled");
        colorPicker.selectPreset("Color 2");
        WebElement events = findElement(By.id("events"));
        Assert.assertEquals("No event should be triggered", "",
                events.getText());
    }

    @Test
    public void cssInputWorksByName() {
        colorPicker.focus();
        colorPicker.sendKeys("blue");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#0000ff",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#0000ff", colorPicker.getPicker().getPropertyString("value"));
        Assert.assertEquals("Input element should be emptied", "",
                colorPicker.getComboBox().getInputElementValue());
        colorPicker.focus();
        colorPicker.sendKeys("blue");
        blur();
        WebElement events = findElement(By.id("events"));
        Assert.assertEquals(
                "New input with same value should not trigger new value change event",
                "1", events.getText());
    }

    @Test(expected = ElementNotInteractableException.class)
    public void disabledinput() {
        options.selectByText("Disabled");
        colorPicker.focus();
        colorPicker.sendKeys("brown");
    }

    @Test
    public void cssInputWorksByRGB() {
        colorPicker.focus();
        colorPicker.sendKeys("rgb(0,255,255)");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#00ffff",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#00ffff", colorPicker.getPicker().getPropertyString("value"));
        Assert.assertEquals("Input element should be emptied", "",
                colorPicker.getComboBox().getInputElementValue());
        WebElement events = findElement(By.id("events"));
        options.selectByText("Read only");
        colorPicker.focus();
        colorPicker.sendKeys("blue");
        blur();
        Assert.assertEquals("New input in readonly should not trigger event",
                "1", events.getText());
    }

    @Test
    public void cssInputWorksByHSL() {
        colorPicker.focus();
        colorPicker.sendKeys("hsl(89, 43%, 51%)");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#84b84c",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#84b84c", colorPicker.getPicker().getPropertyString("value"));
        Assert.assertEquals("Input element should be emptied", "",
                colorPicker.getComboBox().getInputElementValue());
        WebElement events = findElement(By.id("events"));
        colorPicker.setProperty("nocssinput", true);
        colorPicker.focus();
        colorPicker.sendKeys("blue");
        blur();
        Assert.assertEquals(
                "New input in when text input is disabled should not trigger event",
                "1", events.getText());
    }

    @Test
    public void invalidCssInputIsDetected() {
        options.selectByText("Value");

        colorPicker.focus();
        colorPicker.sendKeys("weriuouwqero");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Invalid input was not detected", "null",
                notification.getText());
        Assert.assertEquals("New value should be null", null,
                colorPicker.getPropertyString("color"));
        Assert.assertEquals("Field should be invalid", "",
                colorPicker.getPropertyString("invalid"));
        colorPicker.focus();
        colorPicker.sendKeys("blue");
        blur();
        Assert.assertEquals("New value should be null", null,
                colorPicker.getPropertyString("invalid"));
    }

    @Test
    public void helperTextWorks() {
        options.selectByText("Helper");
        String helperText = colorPicker.getHelperText();
        Assert.assertEquals("Helper text is not correct.",
                "Use this field to input a color.", helperText);
    }

    @Test
    public void invalidAndErrorWorks() {
        options.selectByText("Invalid");
        options.selectByText("Error");
        String errorMessage = field.getPropertyString("errorMessage");
        Assert.assertEquals("Error text is not correct.", "Error message.",
                errorMessage);
        Assert.assertEquals("ComboBox is not invalid.", true,
                colorPicker.getComboBox().getPropertyBoolean("invalid"));
        Assert.assertEquals("CustomField is not invalid.", true,
                field.getPropertyBoolean("invalid"));
    }

    @Test
    public void setValueFromServerWorks() {
        options.selectByText("Value");
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Value change did not have right color", "#ffffff",
                notification.getText());
        Assert.assertEquals("Color property was not set", "#ffffff",
                colorPicker.getProperty("color"));
        Assert.assertEquals(
                "Color property was not propagated to input element", "#ffffff",
                colorPicker.getPicker().getProperty("value"));
        Assert.assertEquals("Color value was not propagated to picker",
                "#ffffff", colorPicker.getPicker().getPropertyString("value"));
    }

    @Test
    public void setVariantsWorks() {
        variants.selectByText("COMPACT");
        Assert.assertEquals("compact", colorPicker.getAttribute("theme"));
        ComboBoxElement combo = colorPicker.getComboBox();
        waitForElementInvisible(combo);
    }

    @Test
    public void colorPickerOpenScreenshotTest() throws IOException {
        colorPicker.openPopup();
        sleep();
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-popup.png")));
    }

    @Test
    public void colorPickerWideScreenshotTest() throws IOException {
        options.selectByText("Wide");
        Assert.assertTrue(colorPicker.compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-wide.png")));
    }

    @Test
    public void colorPickerInvalidScreenshotTest() throws IOException {
        options.selectByText("Invalid");
        Assert.assertTrue(colorPicker.compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-invalid.png")));
    }

    @Test
    public void colorPickerReadOnlyScreenshotTest() throws IOException {
        options.selectByText("Read only");
        Assert.assertTrue(colorPicker.compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-readonly.png")));
        options.deselectByText("Read only");
        clear();
        Assert.assertTrue(colorPicker.compareScreen(
                ImageFileUtil.getReferenceScreenshotFile("color-picker.png")));
    }

    @Test
    public void colorPickerDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        Assert.assertTrue(colorPicker.compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-disabled.png")));
        options.deselectByText("Disabled");
        clear();
        Assert.assertTrue(colorPicker.compareScreen(
                ImageFileUtil.getReferenceScreenshotFile("color-picker.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        options.selectByText("Read only");
        Assert.assertTrue(colorPicker
                .compareScreen(ImageFileUtil.getReferenceScreenshotFile(
                        "color-picker-readonly-disabled.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledInvalidScreenshotTest()
            throws IOException, InterruptedException {
        options.selectByText("Invalid");
        options.selectByText("Disabled");
        options.selectByText("Read only");
        Assert.assertTrue(colorPicker
                .compareScreen(ImageFileUtil.getReferenceScreenshotFile(
                        "color-picker-readonly-disabled-invalid.png")));
    }

    @Test
    public void colorPickerLabelWorks() {
        Assert.assertEquals("Initial label was not propagated", "Color",
                field.getLabel());
        field.setProperty("label", "Pick a color");
        Assert.assertEquals("Label update was not propagated", "Pick a color",
                field.getLabel());
    }

    @Test
    public void tooltipWorks() {
        Actions action = new Actions(getDriver());
        action.moveToElement(colorPicker).perform();
        TestBenchElement tooltip = $("vaadin-tooltip-overlay").first();
        Assert.assertEquals("Correct tooltip was not found",
                "This is color picker", tooltip.getText());
    }

    @Test(expected = NoSuchElementException.class)
    public void tooltipNotShownWhenOpen() {
        colorPicker.openPopup();
        Actions action = new Actions(getDriver());
        action.moveToElement(colorPicker).perform();
        $("vaadin-tooltip-overlay").first();
    }

    protected void waitForElementInvisible(final WebElement element) {
        waitUntil(ExpectedConditions.invisibilityOf(element));
    }

    private void clear() {
        $(ButtonElement.class).id("clear").click();
    }

    private void sleep() {
        if (!sleep) {
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }
}
