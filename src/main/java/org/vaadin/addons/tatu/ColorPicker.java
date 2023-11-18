package org.vaadin.addons.tatu;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

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
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

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
        HasLabel, HasTheme, HasStyle, HasThemeVariant<ColorPickerVariant>,
        HasValidator<String>, HasTooltip {

    /**
     * Caption mode.
     */
    public enum CaptionMode {
        /**
         * Use caption as plain text.
         */
        TEXT,
        /**
         * Use caption as HTML, will be sanitized.
         */
        HTML;
    }

    /**
     * A preset color.
     */
    public static class ColorPreset implements Serializable {
        private String color;
        private String caption;
        private CaptionMode captionMode = CaptionMode.TEXT;

        /**
         * Constructor.
         * 
         * @param color
         *            Color value in six digits hex string, e.g. #ffffff, not
         *            null.
         * @param caption
         *            Displayed name of the color, not null.
         */
        public ColorPreset(String color, String caption) {
            setColor(color);
            setCaption(caption);
        }

        /**
         * Constructor with @see CaptionMode
         * 
         * @param color
         *            Color value in six digits hex string, e.g. #ffffff, not
         *            null.
         * @param caption
         *            Displayed name of the color, not null.
         * @param captionMode
         *            The CaptionMode, @see CaptionMode
         */
        public ColorPreset(String color, String caption,
                CaptionMode captionMode) {
            setColor(color);
            setCaption(caption);
            setCaptionMode(captionMode);
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
         *            Color in six digits hex string, e.g. #ffffff, not null.
         * @throws IllegalArgumentException
         *             when color string does not match pattern.
         * @throws NullPointerException
         *             when color is null.
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
         *            Displayed name of the color, not null.
         * @throws NullPointerException
         *             when caption is null
         */
        public void setCaption(String caption) {
            Objects.requireNonNull(caption, "caption can't be null");
            if (getCaptionMode() == CaptionMode.HTML) {
                caption = sanitize(caption);
            }
            this.caption = caption;
        }

        /**
         * Set the @see CaptionMode
         * 
         * @param captionMode
         *            CaptionMode.
         */
        public void setCaptionMode(CaptionMode captionMode) {
            this.captionMode = captionMode;
            if (captionMode == CaptionMode.HTML) {
                setCaption(getCaption());
            }
        }

        /**
         * Get the @see CaptionMode.
         * 
         * @return CaptionMode
         */
        public CaptionMode getCaptionMode() {
            return captionMode;
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
        if (value == null) {
            super.setValue(null);
        } else if (value.matches("#......")) {
            super.setValue(value);
        } else {
            throw new IllegalArgumentException(
                    "Color must be in format #......");
        }
    }

    /**
     * Set predefined color presets.
     * 
     * @see ColorPreset
     * 
     * @param presets
     *            List of ColorPreset, not null
     * @throws NullPointerException
     *             when presets is null
     */
    public void setPresets(List<ColorPreset> presets) {
        Objects.requireNonNull(presets, "presets can't be null");
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

    /**
     * Set to true in order not to clear the input after color entry.
     * 
     * @param noclear
     *            boolean value.
     */
    public void setNoClear(boolean noclear) {
        if (noclear) {
            getElement().setProperty("noclear", true);
        } else {
            getElement().removeProperty("noclear");
        }
    }

    @Override
    public Validator<String> getDefaultValidator() {
        return (value, context) -> checkValidity(value);
    }

    private ValidationResult checkValidity(String value) {
        boolean invalid = this.isInvalid() && value == null;
        if (!invalid) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error("Input is not a color");
        }
    }

    private static String sanitize(String html) {
        Safelist safelist = Safelist.relaxed().addAttributes(":all", "style")
                .addEnforcedAttribute("a", "rel", "nofollow");
        String sanitized = Jsoup.clean(html, "", safelist,
                new Document.OutputSettings().prettyPrint(false));
        return sanitized;
    }
}
