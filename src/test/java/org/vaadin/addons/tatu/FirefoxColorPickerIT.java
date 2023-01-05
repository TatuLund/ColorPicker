package org.vaadin.addons.tatu;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;

/**
 * This class has just '@Test' annotations for the tests to be run with Firefox.
 * 
 * See AbstractColorPickerIT for actual test code.
 */
public class FirefoxColorPickerIT extends FirefoxTest {

    @Test
    public void componentWorks() {
        super.componentWorks();
    }

    @Test
    public void themeAttributeGetsPropagated() {
        super.themeAttributeGetsPropagated();
    }

    @Test
    public void disabledAttributeGetsPropagated() {
        super.disabledAttributeGetsPropagated();
    }

    @Test
    public void invalidAttributeGetsPropagated() {
        super.invalidAttributeGetsPropagated();
    }

    @Test
    public void requiredAttributeGetsPropagated() {
        super.requiredAttributeGetsPropagated();
    }

    @Test
    public void presetWorks() {
        super.presetWorks();
    }

    @Test
    public void presetNotSelectedWhenDisabled() {
        super.presetNotSelectedWhenDisabled();
    }

    @Test
    public void cssInputWorksByName() {
        super.cssInputWorksByName();
    }

    @Test(expected = ElementNotInteractableException.class)
    public void disabledinput() {
        super.disabledinput();
    }

    @Test
    public void cssInputWorksByRGB() {
        super.cssInputWorksByRGB();
    }

    @Test
    public void cssInputWorksByHSL() {
        super.cssInputWorksByHSL();
    }

    @Test
    public void invalidCssInputIsDetected() {
        super.invalidCssInputIsDetected();
    }

    @Test
    public void helperTextWorks() {
        super.helperTextWorks();
    }

    @Test
    public void invalidAndErrorWorks() {
        super.invalidAndErrorWorks();
    }

    @Test
    public void setValueFromServerWorks() {
        super.setValueFromServerWorks();
    }

    @Test
    public void setVariantsWorks() {
        super.setVariantsWorks();
    }

    @Test
    public void colorPickerOpenScreenshotTest() throws IOException {
        super.colorPickerOpenScreenshotTest();
    }

    @Test
    public void colorPickerWideScreenshotTest() throws IOException {
        super.colorPickerWideScreenshotTest();
    }

    @Test
    public void colorPickerInvalidScreenshotTest() throws IOException {
        super.colorPickerInvalidScreenshotTest();
    }

    @Test
    public void colorPickerReadOnlyScreenshotTest() throws IOException {
        super.colorPickerReadOnlyScreenshotTest();
    }

    @Test
    public void colorPickerDisabledScreenshotTest() throws IOException {
        super.colorPickerDisabledScreenshotTest();
    }

    @Test
    public void colorPickerReadOnlyDisabledScreenshotTest() throws IOException {
        super.colorPickerReadOnlyDisabledScreenshotTest();
    }

    @Test
    public void colorPickerReadOnlyDisabledInvalidScreenshotTest()
            throws IOException {
        super.colorPickerReadOnlyDisabledInvalidScreenshotTest();
    }

    @Test
    public void colorPickerThemableMixinScreenshotTest() throws IOException {
        super.colorPickerThemableMixinScreenshotTest();
    }

    @Test
    public void colorPickerLabelWorks() {
        super.colorPickerLabelWorks();
    }

    @Test
    public void tooltipWorks() {
        super.tooltipWorks();
    }

    @Test(expected = NoSuchElementException.class)
    public void tooltipNotShownWhenOpen() {
        super.tooltipNotShownWhenOpen();
    }

    @Test
    public void focusBlurEventWork() {
        super.focusBlurEventWork();
    }
}
