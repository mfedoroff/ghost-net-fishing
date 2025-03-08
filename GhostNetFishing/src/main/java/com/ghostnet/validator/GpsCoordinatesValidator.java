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

/**
 * JSF-Validator zur Überprüfung der Eingabe von GPS-Koordinaten.
 * Dieser Validator überprüft, ob eine Eingabe das Format "latitude, longitude" erfüllt
 * und ob die Werte im zulässigen Bereich liegen (-90 bis 90 für Breite, -180 bis 180 für Länge).
 */
@Named
@RequestScoped
public class GpsCoordinatesValidator implements Validator {

    /**
     * Regulärer Ausdruck zur Validierung des Koordinatenformats.
     * Akzeptiert Breiten- und Längengrade im Dezimalformat, getrennt durch ein Komma.
     */
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("^\\s*(-?\\d+(\\.\\d+)?)\\s*,\\s*(-?\\d+(\\.\\d+)?)\\s*$");

    /**
     * Validiert die eingegebenen GPS-Koordinaten.
     *
     * @param context   FacesContext der aktuellen Anfrage.
     * @param component Die UI-Komponente, die validiert wird.
     * @param value     Der eingegebene Wert.
     * @throws ValidatorException Falls das Format oder der Wertebereich ungültig ist.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().trim().isEmpty()) {
            return; // Leere Werte werden akzeptiert (keine Validierung erforderlich)
        }

        String coordinates = value.toString().trim();
        Matcher matcher = COORDINATE_PATTERN.matcher(coordinates);
        if (!matcher.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ungültiges Koordinatenformat. Bitte geben Sie Latitude und Longitude im Format 'lat, long' ein. Beispiel: 48.137154, -11.576124", null));
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
