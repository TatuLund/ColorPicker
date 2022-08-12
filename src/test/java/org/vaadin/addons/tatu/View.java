package org.vaadin.addons.tatu;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.tatu.ColorPicker.ColorPickerVariant;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;

@Route("")
public class View extends VerticalLayout {

    private int eventCount = 0;

    public View() {
        setSizeFull();

        Span events = new Span();
        events.setId("events");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLabel("Color");
        colorPicker
                .setPresets(Arrays.asList(new ColorPreset("#00ff00", "Color 1"),
                        new ColorPreset("#ff0000", "Color 2")));

        colorPicker.addValueChangeListener(event -> {
            Notification.show(event.getValue());
            eventCount++;
            events.setText("" + eventCount);
        });

        RadioButtonGroup<String> helper = new RadioButtonGroup<>("Options");
        helper.setItems("Helper", "Invalid", "Valid", "Error", "Value",
                "Hidden", "Disabled", "Read only");
        helper.setId("options");
        helper.addValueChangeListener(event -> {
            switch (event.getValue()) {
            case "Helper": {
                colorPicker.setInvalid(false);
                colorPicker.setHelperText("Use this field to input a color.");
                break;
            }
            case "Invalid": {
                colorPicker.setInvalid(true);
                break;
            }
            case "Valid": {
                colorPicker.setInvalid(false);
                break;
            }
            case "Error": {
                colorPicker.setHelperText("Error message.");
                break;
            }
            case "Value": {
                colorPicker.setValue("#ffffff");
                break;
            }
            case "Hidden": {
                colorPicker.setVisible(false);
                break;
            }
            case "Disabled": {
                colorPicker.setEnabled(false);
                break;
            }
            case "Read only": {
                colorPicker.setReadOnly(true);
                break;
            }
            }
        });

        CheckboxGroup<ColorPickerVariant> variants = new CheckboxGroup<>(
                "Variants");
        variants.setId("variants");
        variants.setItems(ColorPickerVariant.values());
        variants.addValueChangeListener(event -> {
            colorPicker.getThemeNames().clear();
            event.getValue()
                    .forEach(variant -> colorPicker.addThemeVariants(variant));
        });

        add(colorPicker, helper, variants, events);
    }
}
