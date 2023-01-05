package org.vaadin.addons.tatu;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.dom.ThemeList;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;
import org.vaadin.addons.tatu.ColorPicker.InputMode;

public class ColorPickerTest {

    @Test
    public void setPresets_propertyIsSet() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker
                .setPresets(Arrays.asList(new ColorPreset("#00ff00", "Color 1"),
                        new ColorPreset("#ff0000", "Color 2")));
        JsonArray presetsJson = (JsonArray) colorPicker.getElement()
                .getPropertyRaw("presets");
        Assert.assertEquals("Array should have 2 items", 2,
                presetsJson.length());
        JsonObject colorJson = presetsJson.get(0);
        Assert.assertEquals("Color is not correct", "#00ff00",
                colorJson.getString("color"));
        Assert.assertEquals("Caption is not correct", "Color 1",
                colorJson.getString("caption"));
        colorJson = presetsJson.get(1);
        Assert.assertEquals("Color is not correct", "#ff0000",
                colorJson.getString("color"));
        Assert.assertEquals("Caption is not correct", "Color 2",
                colorJson.getString("caption"));
    }

    @Test
    public void setValue_nullWorks() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(null);
        Assert.assertEquals(null, colorPicker.getValue());
    }

    @Test
    public void setValue_valueChangeIsTriggered() {
        ColorPicker colorPicker = new ColorPicker();
        AtomicInteger count = new AtomicInteger(0);
        colorPicker.addValueChangeListener(event -> {
            count.addAndGet(1);
            Assert.assertEquals("Value is not #ffffff", "#ffffff",
                    event.getValue());
        });
        colorPicker.setValue("#ffffff");
        Assert.assertEquals("Value change was not triggered", 1, count.get());
        Assert.assertEquals("Value is not #ffffff", "#ffffff",
                colorPicker.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void colorPicker_setValue_wrongFormat() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue("blue");
    }

    @Test(expected = NullPointerException.class)
    public void colorPicker_setPreset_null() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setPresets(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void colorPreset_setValue_wrongFormat() {
        ColorPreset preset = new ColorPreset("blue", "Blue");
    }

    @Test(expected = NullPointerException.class)
    public void colorPreset_setValue_null() {
        ColorPreset preset = new ColorPreset(null, null);
    }

    @Test
    public void addThemeVariant_themeNamesContainsThemeVariant() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.addThemeVariants(ColorPickerVariant.LUMO_SMALL);

        ThemeList themeNames = colorPicker.getThemeNames();
        Assert.assertTrue(themeNames
                .contains(ColorPickerVariant.LUMO_SMALL.getVariantName()));
    }

    @Test
    public void addThemeVariant_removeThemeVariant_themeNamesDoesNotContainThemeVariant() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.addThemeVariants(ColorPickerVariant.LUMO_SMALL);
        colorPicker.addThemeVariants(ColorPickerVariant.COMPACT);
        ThemeList themeNames = colorPicker.getThemeNames();
        Assert.assertTrue(themeNames
                .contains(ColorPickerVariant.LUMO_SMALL.getVariantName()));
        themeNames = colorPicker.getThemeNames();
        Assert.assertTrue(themeNames
                .contains(ColorPickerVariant.COMPACT.getVariantName()));
        colorPicker.removeThemeVariants(ColorPickerVariant.LUMO_SMALL);

        themeNames = colorPicker.getThemeNames();
        Assert.assertFalse(themeNames
                .contains(ColorPickerVariant.LUMO_SMALL.getVariantName()));
        Assert.assertTrue(themeNames
                .contains(ColorPickerVariant.COMPACT.getVariantName()));
    }

    @Test
    public void noCssInputPropertySet() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setInputMode(InputMode.NOCSSINPUT);
        Assert.assertTrue(
                colorPicker.getElement().getProperty("nocssinput") != null);
        colorPicker.setInputMode(InputMode.PRESETANDCSS);
        Assert.assertTrue(
                colorPicker.getElement().getProperty("nocssinput") == null);
    }

    @Test
    public void invalidPropertySet() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setInvalid(true);
        Assert.assertTrue(colorPicker.isInvalid());
        colorPicker.setInvalid(false);
        Assert.assertFalse(colorPicker.isInvalid());
    }

    @Test
    public void errorMessageSet() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setErrorMessage("This is an error");
        Assert.assertEquals("This is an error", colorPicker.getErrorMessage());
        colorPicker.setErrorMessage(null);
        Assert.assertEquals(null, colorPicker.getErrorMessage());
    }

    @Test
    public void colorPickerSerializable() throws IOException {
        ColorPicker colorPicker = new ColorPicker();
        new ObjectOutputStream(new ByteArrayOutputStream())
                .writeObject(colorPicker);
    }

    @Test
    public void colorPresetSerializable() throws IOException {
        ColorPreset preset = new ColorPreset("#00ff00", "Color 1");
        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(preset);
    }

    @Test
    public void colorInvalid() {
        ColorPicker colorPicker = new ColorPicker();
        ValidationResult status = colorPicker.getDefaultValidator().apply(null,
                null);
        Assert.assertFalse(status.isError());
        colorPicker.setInvalid(true);
        status = colorPicker.getDefaultValidator().apply(null, null);
        Assert.assertTrue(status.isError());
    }

    @Test
    public void assertImplementedInterfaces() {
        ColorPicker colorPicker = new ColorPicker();
        Assert.assertTrue(colorPicker instanceof HasTooltip);
        Assert.assertTrue(colorPicker instanceof Focusable);
        Assert.assertTrue(colorPicker instanceof HasValue);
        Assert.assertTrue(colorPicker instanceof HasThemeVariant);
        Assert.assertTrue(colorPicker instanceof HasTheme);
        Assert.assertTrue(colorPicker instanceof HasHelper);
        Assert.assertTrue(colorPicker instanceof HasSize);
        Assert.assertTrue(colorPicker instanceof HasLabel);
        Assert.assertTrue(colorPicker instanceof HasValidation);
    }
}