package com.ghostnet.controller;

import com.ghostnet.dao.UserDAO;
import com.ghostnet.model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Controller für das Login- und Sitzungsmanagement.
 * Diese Klasse verwaltet die Authentifizierung und speichert den aktuellen Benutzer in der Session.
 * Sie ermöglicht das Anmelden, Abmelden und Abrufen einer personalisierten Begrüßung.
 */
@Named
@SessionScoped
public class LoginController implements Serializable {

    @Inject
    private UserDAO userDAO; // DAO für den Zugriff auf Benutzerinformationen

    private User user = new User(); // Enthält die Daten des eingeloggten Benutzers
    private boolean loggedIn = false; // Gibt an, ob der Benutzer eingeloggt ist

    // Getter und Setter
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Authentifiziert den Benutzer anhand des Benutzernamens und Passworts.
     * Falls erfolgreich, wird der Benutzer als eingeloggt markiert und zur Startseite weitergeleitet.
     *
     * @return Die Navigationsseite nach erfolgreicher Anmeldung oder {@code null} bei Fehlschlag.
     */
    public String login() {
        User foundUser = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        String loginError;

        if (foundUser != null) {
            loggedIn = true;
            user = foundUser;
            return "index?faces-redirect=true"; // Weiterleitung zur Startseite
        } else {
            loggedIn = false;
            loginError = "Ungültige Zugangsdaten!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, loginError, null));
            return null; // Verbleiben auf der Login-Seite
        }
    }

    /**
     * Loggt den aktuellen Benutzer aus, setzt die Sitzung zurück und leitet zur Startseite weiter.
     *
     * @return Die URL zur Startseite mit Redirect.
     */
    public String logout() {
        loggedIn = false;
        user = new User(); // Reset des Benutzerobjekts
        return "index?faces-redirect=true"; // Weiterleitung zur Startseite
    }

    /**
     * Gibt eine personalisierte Begrüßung zurück, wenn der Benutzer eingeloggt ist.
     *
     * @return Eine Begrüßung mit dem Benutzernamen oder "Konto" als Standardtext.
     */
    public String getGreeting() {
        if (loggedIn && user != null && user.getName() != null && !user.getName().isEmpty()) {
            return "Hallo, " + user.getName();
        }
        return "Konto"; // Standardanzeige, falls kein Benutzer angemeldet ist
    }
}
