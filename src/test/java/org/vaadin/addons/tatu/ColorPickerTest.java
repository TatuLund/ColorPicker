package org.vaadin.addons.tatu;

import com.vaadin.flow.dom.ThemeList;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.vaadin.addons.tatu.ColorPicker.ColorPickerVariant;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;

public class ColorPickerTest {

    @Test
    public void setPresets_propertyIsSet() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker
                .setPresets(Arrays.asList(new ColorPreset("#00ff00", "Color 1"),
                        new ColorPreset("#ff0000", "Color 2")));
        JsonArray presetsJson = (JsonArray) colorPicker.getElement()
                .getPropertyRaw("presets");
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
        colorPicker.removeThemeVariants(ColorPickerVariant.LUMO_SMALL);

        ThemeList themeNames = colorPicker.getThemeNames();
        Assert.assertFalse(themeNames
                .contains(ColorPickerVariant.LUMO_SMALL.getVariantName()));
    }
}