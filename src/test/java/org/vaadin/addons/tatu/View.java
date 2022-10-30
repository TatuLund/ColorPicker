package org.vaadin.addons.tatu;

import java.util.Arrays;
import java.util.Set;

import org.vaadin.addons.tatu.ColorPickerVariant;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
            Notification.show(""+event.getValue());
            eventCount++;
            events.setText("" + eventCount);
        });

        CheckboxGroup<String> helper = new CheckboxGroup<>("Options");
        helper.setItems("Helper", "Invalid", "Valid", "Error", "Value", "Wide",
                "Disabled", "Read only");
        helper.setId("options");
        helper.addValueChangeListener(event -> {
            Set<String> selected = event.getValue();
            if (selected.contains("Helper")) {
                colorPicker.setHelperText("Use this field to input a color.");
            }
            if (selected.contains("Invalid")) {
                colorPicker.setInvalid(true);
            }
            if (selected.contains("Valid")) {
                colorPicker.setHelperText(null);
                colorPicker.setReadOnly(false);
                colorPicker.setEnabled(true);
                colorPicker.setValue(null);
                colorPicker.setWidth(null);
                colorPicker.setInvalid(false);
            }
            if (selected.contains("Error")) {
                colorPicker.setErrorMessage("Error message.");
            }
            if (selected.contains("Value")) {
                colorPicker.setValue("#ffffff");
            }
            if (selected.contains("Wide")) {
                colorPicker.setWidth("400px");
            }
            if (selected.contains("Disabled")) {
                colorPicker.setEnabled(false);
            }
            if (selected.contains("Read only")) {
                colorPicker.setReadOnly(true);
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
            colorPicker.focus();
        });

        add(colorPicker, helper, variants, events);
    }
}
