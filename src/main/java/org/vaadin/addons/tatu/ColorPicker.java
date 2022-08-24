package org.vaadin.addons.tatu;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;

/**
 * A ColorPicker component.
 */
@JsModule("./color-picker.ts")
@Tag("color-picker")
@Uses(ComboBox.class)
@Uses(CustomField.class)
public class ColorPicker
        extends AbstractSinglePropertyField<ColorPicker, String>
        implements HasSize, HasValidation, Focusable<ColorPicker>, HasHelper,
        HasLabel, HasTheme, HasStyle {

    /**
     * A preset color.
     */
    public static class ColorPreset {
        private String color;
        private String caption;

        /**
         * Constructor.
         * 
         * @param color
         *            Color value in six digits hex string, e.g. #ffffff.
         * @param caption
         *            Displayed name of the color.
         */
        public ColorPreset(String color, String caption) {
            setColor(color);
            setCaption(caption);
        }

        /**
         * Get color value in six digit hex format.
         * 
         * @return String value.
         */
        public String getColor() {
            return color;
        }

        /**
         * Set color value for the preset.
         * 
         * @param color
         *            Color in six digits hex string, e.g. #ffffff.
         */
        public void setColor(String color) {
            Objects.requireNonNull(color, "color can't be null");
            if (color.matches("#......")) {
                this.color = color;
            } else {
                throw new IllegalArgumentException(
                        "Color must be in format #......");
            }
        }

        /**
         * Get the caption.
         * 
         * @return String value.
         */
        public String getCaption() {
            return caption;
        }

        /**
         * Set the caption of the color.
         * 
         * @param caption
         *            Displayed name of the color.
         */
        public void setCaption(String caption) {
            Objects.requireNonNull(caption, "caption can't be null");
            this.caption = caption;
        }
    }

    /**
     * Possible input modes for the field.
     */
    public enum InputMode {
        NOCSSINPUT, PRESETANDCSS;
    }

    /**
     * Default constructor.
     */
    public ColorPicker() {
        super("color", null, true);
    }

    @Override
    public void setValue(String value) {
        if (value.matches("#......")) {
            super.setValue(value);
        } else {
            throw new IllegalArgumentException(
                    "Color must be in format #......");
        }
    }

    public enum ColorPickerVariant {
        COMPACT("compact"), LUMO_SMALL("small"), LUMO_ALIGN_LEFT(
                "align-left"), LUMO_ALIGN_CENTER(
                        "align-center"), LUMO_ALIGN_RIGHT(
                                "align-right"), LUMO_HELPER_ABOVE_FIELD(
                                        "helper-above-field");

        private final String variant;

        ColorPickerVariant(String variant) {
            this.variant = variant;
        }

        /**
         * Gets the variant name.
         *
         * @return variant name
         */
        public String getVariantName() {
            return variant;
        }
    }

    /**
     * Set predefined color presets.
     * 
     * @see ColorPreset
     * 
     * @param presets
     *            List of ColorPreset.
     */
    public void setPresets(List<ColorPreset> presets) {
        Objects.requireNonNull(presets, "values can't be null");
        getElement().setPropertyList("presets", presets);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        getElement().setProperty("errorMessage", errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return getElement().getProperty("errorMessage");
    }

    @Override
    public void setInvalid(boolean invalid) {
        if (invalid) {
            getElement().setProperty("invalid", invalid);
        } else {
            getElement().removeProperty("invalid");
        }
    }

    @Override
    public boolean isInvalid() {
        return getElement().getProperty("invalid", false);
    }

    /**
     * Adds theme variants to the component.
     *
     * @param variants
     *            theme variants to add
     */
    public void addThemeVariants(ColorPickerVariant... variants) {
        getThemeNames().addAll(
                Stream.of(variants).map(ColorPickerVariant::getVariantName)
                        .collect(Collectors.toList()));
    }

    /**
     * Removes theme variants from the component.
     *
     * @param variants
     *            theme variants to remove
     */
    public void removeThemeVariants(ColorPickerVariant... variants) {
        getThemeNames().removeAll(
                Stream.of(variants).map(ColorPickerVariant::getVariantName)
                        .collect(Collectors.toList()));
    }

    /**
     * Defines the input mode of the text field.
     * 
     * @param inputMode
     *            The input mode.
     */
    public void setInputMode(InputMode inputMode) {
        if (inputMode == InputMode.NOCSSINPUT) {
            getElement().setProperty("nocssinput", true);
        } else {
            getElement().removeProperty("nocssinput");
        }
    }
}
