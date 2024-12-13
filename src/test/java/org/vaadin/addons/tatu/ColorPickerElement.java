package org.vaadin.addons.tatu;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.customfield.testbench.CustomFieldElement;
import com.vaadin.testbench.HasHelper;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

/**
 * This is helper class for the ColorPicker, which simplifies the actual test
 * code and makes it more readable.
 */
@Element("color-picker")
public class ColorPickerElement extends TestBenchElement implements HasHelper {

    public ComboBoxElement getComboBox() {
        return this.$(ComboBoxElement.class).first();
    }

    public CustomFieldElement getFieldWrapper() {
        return this.$(CustomFieldElement.class).first();
    }

    public TestBenchElement getPicker() {
        return getFieldWrapper().$("input").first();
    }

    public void sendKeys(CharSequence keysToSend) {
        getComboBox().sendKeys(keysToSend);
    }

    public void selectPreset(String preset) {
        getComboBox().selectByText(preset);
    }

    @Override
    public String getHelperText() {
        return getFieldWrapper().getHelperText();
    }

    @Override
    public TestBenchElement getHelperComponent() {
        return getFieldWrapper().getHelperComponent();
    }

    public void openPopup() {
        getComboBox().openPopup();
    }

    public void clear() {
        executeScript("arguments[0].clear();", getComboBox());
    }
}
