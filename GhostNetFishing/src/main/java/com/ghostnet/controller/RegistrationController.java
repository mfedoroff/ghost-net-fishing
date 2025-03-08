package com.ghostnet.controller;

import com.ghostnet.dao.UserDAO;
import com.ghostnet.model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Controller für die Benutzerregistrierung.
 * Diese Klasse ermöglicht die Erstellung neuer Benutzerkonten, einschließlich der Validierung von Passwörtern
 * und der Prüfung auf bereits vergebene Benutzernamen.
 */
@Named
@SessionScoped
public class RegistrationController implements Serializable {

    private final UserDAO userDAO = new UserDAO(); // DAO für den Zugriff auf Benutzerdaten
    private User user = new User(); // Repräsentiert den neuen Benutzer, der registriert wird
    private String password; // Das eingegebene Passwort
    private String confirmPassword; // Das Bestätigungspasswort

    // Getter und Setter
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Führt die Registrierung eines neuen Benutzers durch.
     * Falls die Registrierung erfolgreich ist, wird zur Login-Seite weitergeleitet.
     *
     * @return Die Login-Seite oder {@code null} bei einem Fehler.
     */
    public String register() {
        String registrationError;

        // Prüft, ob das eingegebene Passwort mit dem Bestätigungspasswort übereinstimmt
        if (password == null || !password.equals(confirmPassword)) {
            registrationError = "Die Passwörter stimmen nicht überein!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            return null;
        }

        // Prüft, ob der Benutzername bereits existiert
        if (userDAO.findByUsername(user.getUsername()) != null) {
            registrationError = "Der Benutzername ist bereits vergeben!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            return null;
        }

        /*
        * Speichert das Passwort im Benutzerobjekt
        * Hinweis: Sollte verschlüsselt gespeichert werden, darauf wurde aufgrund des Prototyps verzichtet.
        */
        user.setPassword(password);

        try {
            // Speichert den neuen Benutzer in der Datenbank
            userDAO.create(user);
            registrationError = null;
        } catch (Exception e) {
            // Fehlerbehandlung für Probleme während der Registrierung
            registrationError = "Registrierung fehlgeschlagen: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            e.printStackTrace();
            return null;
        }

        // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
        return "login?faces-redirect=true";
    }
}
