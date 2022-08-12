package org.vaadin.addons.tatu;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
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
    private ComboBoxElement combo;
    private CustomFieldElement field;
    private RadioButtonGroupElement options;
    private TestBenchElement variants;

    public void blur() {
        executeScript(
                "!!document.activeElement ? document.activeElement.blur() : 0");
    }

    @Override
    public void setup() throws Exception {
        super.setup();
        colorPicker = $(ColorPickerElement.class).first();
        combo = colorPicker.getComboBox();
        field = colorPicker.getFieldWrapper();
        options = $(RadioButtonGroupElement.class).first();
        variants = $(TestBenchElement.class).first();

        // Hide dev mode gizmo, it would interfere screenshot tests
        $("vaadin-dev-tools").first().setProperty("hidden", true);
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
    public void presetWorks() {
        combo.selectByText("Color 1");
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not set", "#00ff00",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#00ff00", colorPicker.getPicker().getPropertyString("value"));
    }

    @Test
    public void cssInputWorksByName() {
        combo.focus();
        combo.sendKeys("blue");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#0000ff",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#0000ff", colorPicker.getPicker().getPropertyString("value"));
        combo.focus();
        combo.sendKeys("blue");
        blur();
        WebElement events = findElement(By.id("events"));
        Assert.assertEquals(
                "New input with same value should not trigger new value change event",
                "1", events.getText());
        options.selectByText("Disabled");
        combo.focus();
        combo.sendKeys("blue");
        blur();
        Assert.assertEquals(
                "New input in disabled should not trigger event",
                "1", events.getText());
    }

    @Test
    public void cssInputWorksByRGB() {
        combo.focus();
        combo.sendKeys("rgb(0,255,255)");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#00ffff",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#00ffff", colorPicker.getPicker().getPropertyString("value"));
        WebElement events = findElement(By.id("events"));
        options.selectByText("Read only");
        combo.focus();
        combo.sendKeys("blue");
        blur();
        Assert.assertEquals(
                "New input in readonly should not trigger event",
                "1", events.getText());
    }

    @Test
    public void cssInputWorksByHSL() {
        combo.focus();
        combo.sendKeys("hsl(89, 43%, 51%)");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("Color was not converted correctly", "#84b84c",
                notification.getText());
        Assert.assertEquals("Color value was not propagated to picker",
                "#84b84c", colorPicker.getPicker().getPropertyString("value"));
    }

    @Test
    public void invalidCssInputConvertsToBlack() {
        options.selectByText("Value");

        combo.focus();
        combo.sendKeys("weriuouwqero");
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
                combo.getPropertyBoolean("invalid"));
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
        ComboBoxElement combo = colorPicker.$(ComboBoxElement.class).first();
        waitForElementInvisible(combo);
    }

    @Test
    public void colorPickerOpenScreenshotTest() throws IOException {
        combo.openPopup();
        Assert.assertTrue(testBench().compareScreen(
                ImageFileUtil.getReferenceScreenshotFile("color-picker.png")));
    }

    @Test
    public void colorPickerInvalidScreenshotTest() throws IOException {
        options.selectByText("Invalid");
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-invalid.png")));
    }

    @Test
    public void colorPickerReadOnlyScreenshotTest() throws IOException {
        options.selectByText("Read only");
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-readonly.png")));
    }

    @Test
    public void colorPickerDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-disabled.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledScreenshotTest() throws IOException {
        options.selectByText("Disabled");
        options.selectByText("Read only");
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-readonly-disabled.png")));
    }

    @Test
    public void colorPickerReadOnlyDisabledInvalidScreenshotTest() throws IOException {
        options.selectByText("Invalid");
        options.selectByText("Disabled");
        options.selectByText("Read only");
        Assert.assertTrue(testBench().compareScreen(ImageFileUtil
                .getReferenceScreenshotFile("color-picker-readonly-disabled-invalid.png")));
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
}
