package org.vaadin.addons.tatu;

import java.util.Arrays;
import java.util.Set;

import org.vaadin.addons.tatu.ColorPicker.CaptionMode;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport(value = "./color-picker.css", themeFor = "color-picker")
public class View extends VerticalLayout {

    private int eventCount = 0;
    private boolean focuseEventsEnabled;

    public View() {
        setSizeFull();

        Span events = new Span();
        events.setId("events");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLabel("Color");
        colorPicker.setPresets(Arrays.asList(
                new ColorPreset("#00ff00", "Color 1"),
                new ColorPreset("#ff0000", "Color 2"),
                new ColorPreset("#ffffff",
                        "<b style='color: blue'><img src=1 onerror=alert(document.domain)>Color 3</b>",
                        CaptionMode.HTML)));

        colorPicker.addValueChangeListener(event -> {
            Notification.show("" + event.getValue());
            eventCount++;
            events.setText("" + eventCount);
        });
        colorPicker.setTooltipText("This is color picker");

        CheckboxGroup<String> options = new CheckboxGroup<>("Options");
        options.setItems("Helper", "Invalid", "Error", "Value", "Wide",
                "Disabled", "Read only", "Required");
        options.setId("options");
        options.addValueChangeListener(event -> {
            Set<String> selected = event.getValue();
            if (selected.contains("Helper")) {
                colorPicker.setHelperText("Use this field to input a color.");
            }
            if (selected.contains("Invalid")) {
                colorPicker.setInvalid(true);
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
            if (selected.contains("Required")) {
                colorPicker.setRequiredIndicatorVisible(true);
            }
            if (selected.contains("No clear")) {
                colorPicker.setNoClear(true);
            }
        });

        Button clear = new Button("Clear");
        clear.setId("clear");
        clear.addClickListener(event -> {
            if (clear.getText().equals("Clear") || focuseEventsEnabled) {
                options.clear();
                colorPicker.setHelperText(null);
                colorPicker.setReadOnly(false);
                colorPicker.setEnabled(true);
                colorPicker.setValue(null);
                colorPicker.setWidth(null);
                colorPicker.setInvalid(false);
                colorPicker.setNoClear(false);
                colorPicker.setRequiredIndicatorVisible(false);
                if (!focuseEventsEnabled) {
                    clear.setText("Focus/Blur");
                }
            } else {
                focuseEventsEnabled = true;
                colorPicker.addFocusListener(e -> Notification.show("Focused"));
                colorPicker.addBlurListener(e -> Notification.show("Blurred"));
                colorPicker.addThemeName("border");
                clear.setText("Clear");
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

        add(colorPicker, options, variants, clear, events);
    }
}
