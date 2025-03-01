package com.ghostnet.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@RequestScoped
public class GpsCoordinatesValidator implements Validator {

    private static final Pattern COORDINATE_PATTERN = Pattern.compile("^\\s*(-?\\d+(\\.\\d+)?)\\s*,\\s*(-?\\d+(\\.\\d+)?)\\s*$");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().trim().isEmpty()) {
            return;
        }
        String coordinates = value.toString().trim();
        Matcher matcher = COORDINATE_PATTERN.matcher(coordinates);
        if (!matcher.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ungültiges Koordinatenformat. Bitte geben Sie Latitude und Longitude im Format 'lat, long' ein. z.B. 48.137154, -11.576124", null));
        }

        // Werte extrahieren und auf Gültigkeit prüfen
        double latitude = Double.parseDouble(matcher.group(1));
        double longitude = Double.parseDouble(matcher.group(3));
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Koordinaten außerhalb des gültigen Bereichs (Latitude: -90 bis 90, Longitude: -180 bis 180).", null));
        }
    }
}
