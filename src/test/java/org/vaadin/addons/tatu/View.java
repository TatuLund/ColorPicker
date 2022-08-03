package org.vaadin.addons.tatu;

import java.util.Arrays;

import org.vaadin.addons.tatu.ColorPicker.ColorPickerVariant;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;

@Route("")
public class View extends VerticalLayout {

    public View() {
        setSizeFull();

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLabel("Color");
        colorPicker.setPresets(Arrays.asList(new ColorPreset("#00ff00","Color 1"),new ColorPreset("#ff0000","Color 2")));
        
        colorPicker.addValueChangeListener(event -> {
            Notification.show(event.getValue());
        });

        RadioButtonGroup<String> helper = new RadioButtonGroup<>("Options");
        helper.setItems("Helper","Invalid","Valid","Error","Value","Hidden");
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
            }
        });

        CheckboxGroup<ColorPickerVariant> variants = new CheckboxGroup<>("Variants");
        variants.setId("variants");
        variants.setItems(ColorPickerVariant.values());
        variants.addValueChangeListener(event -> {
            colorPicker.getThemeNames().clear();
            event.getValue().forEach(variant -> colorPicker.addThemeVariants(variant));
        });
        
        add(colorPicker, helper, variants);
    }
}
