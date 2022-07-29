package org.vaadin.addons.tatu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.customfield.testbench.CustomFieldElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.testbench.TestBenchElement;

public class ColorPickerIT extends AbstractViewTest {

    private TestBenchElement colorPicker;
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
        colorPicker = $("color-picker").first();
        combo = colorPicker.$(ComboBoxElement.class).first();
        field = colorPicker.$(CustomFieldElement.class).first();
        options = $(RadioButtonGroupElement.class).first();
        variants = $("vaadin-checkbox-group").first();
    }

    @Test
    public void componentWorks() {
        Assert.assertTrue(
                colorPicker.$(TestBenchElement.class).all().size() > 0);
    }

    @Test
    public void presetWorks() {
        combo.selectByText("Color 1");
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("color is not #00ff00", "#00ff00",
                notification.getText());
    }

    @Test
    public void cssInputWorks() {
        combo.focus();
        combo.sendKeys("blue");
        blur();
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("color is not #0000ff", "#0000ff",
                notification.getText());
    }

    @Test
    public void helperTextWorks() {
        options.selectByText("Helper");
        String helperText = field.getPropertyString("helperText");
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
    public void setValueWorks() {
        options.selectByText("Value");
        NotificationElement notification = $(NotificationElement.class).last();
        Assert.assertEquals("color is not #ffffff", "#ffffff",
                notification.getText());
        Assert.assertEquals("property color is not #ffffff", "#ffffff",
                colorPicker.getProperty("color"));
    }

    @Test
    public void setVariantsWorks() {
        CheckboxElement compact = variants.$(CheckboxElement.class).first();
        compact.setChecked(true);
        ComboBoxElement combo = colorPicker.$(ComboBoxElement.class).first();
        waitForElementInvisible(combo);
    }

    protected void waitForElementInvisible(final WebElement element) {
        waitUntil(ExpectedConditions.invisibilityOf(element));
    }
}
