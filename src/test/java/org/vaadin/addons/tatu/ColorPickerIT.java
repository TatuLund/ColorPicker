package org.vaadin.addons.tatu;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.customfield.testbench.CustomFieldElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.screenshot.ImageFileUtil;

public class ColorPickerIT extends AbstractViewTest {

    private ColorPickerElement colorPicker;
    private CustomFieldElement field;
    private RadioButtonGroupElement options;
    private TestBenchElement variants;
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
        options = $(RadioButtonGroupElement.class).first();
        variants = $(TestBenchElement.class).first();

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
        Assert.assertEquals("theme",colorPicker.getComboBox().getAttribute("theme"));
        Assert.assertEquals("theme",colorPicker.getPicker().getAttribute("theme"));
        Assert.assertEquals("theme",colorPicker.getFieldWrapper().getAttribute("theme"));
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
        options.selectByText("Disabled");
        colorPicker.focus();
        colorPicker.sendKeys("brown");
        blur();
        Assert.assertEquals("New input in disabled should not trigger event",
                "1", events.getText());
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
    public void invalidCssInputConvertsToBlack() {
        options.selectByText("Value");

        colorPicker.focus();
        colorPicker.sendKeys("weriuouwqero");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#000000",
                notification.getText());
        Assert.assertEquals("Color value was changed while it should not",
                "#000000", colorPicker.getPicker().getPropertyString("value"));

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
        String errorMessage = field.getPropertyString("helperText");
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
        Assert.assertEquals("Color value was not propagated to picker",
                "#ffffff", colorPicker.getPicker().getPropertyString("value"));
    }

    @Test
    public void setVariantsWorks() {
        CheckboxElement compact = variants.$(CheckboxElement.class).first();
        compact.setChecked(true);
        ComboBoxElement combo = colorPicker.getComboBox();
        waitForElementInvisible(combo);
    }

    @Test
    public void colorPickerOpenScreenshotTest() throws IOException {
        colorPicker.openPopup();
        sleep();
        Assert.assertTrue(testBench().compareScreen(
                ImageFileUtil.getReferenceScreenshotFile("color-picker.png")));
    }

    @Test
    public void colorPickerWideScreenshotTest() throws IOException {
        options.selectByText("Wide");
        sleep();
        Assert.assertTrue(testBench().compareScreen(
                ImageFileUtil.getReferenceScreenshotFile("color-picker-wide.png")));
    }

    @Test
    public void colorPickerInvalidScreenshotTest() throws IOException {
        options.selectByText("Invalid");
        sleep();
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-invalid.png")));
    }

    @Test
    public void colorPickerReadOnlyScreenshotTest() throws IOException {
        options.selectByText("Read only");
        sleep();
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-readonly.png")));
    }

    @Test
    public void colorPickerDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        sleep();
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-disabled.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        options.selectByText("Read only");
        sleep();
        Assert.assertTrue(testBench()
                .compareScreen(ImageFileUtil.getReferenceScreenshotFile(
                        "color-picker-readonly-disabled.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledInvalidScreenshotTest()
            throws IOException, InterruptedException {
        options.selectByText("Invalid");
        options.selectByText("Disabled");
        options.selectByText("Read only");
        sleep();
        Assert.assertTrue(testBench()
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

    protected void waitForElementInvisible(final WebElement element) {
        waitUntil(ExpectedConditions.invisibilityOf(element));
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
