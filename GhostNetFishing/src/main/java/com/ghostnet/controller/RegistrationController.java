package com.ghostnet.controller;

import com.ghostnet.dao.UserDAO;
import com.ghostnet.model.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RegistrationController implements Serializable {

    private User user = new User();
    private String password;
    private String confirmPassword;
    private String registrationError;

    private UserDAO userDAO = new UserDAO();

    public String register() {
        if (password == null || !password.equals(confirmPassword)) {
            registrationError = "Die Passwörter stimmen nicht überein!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            return null;
        }

        if (userDAO.findByUsername(user.getUsername()) != null) {
            registrationError = "Der Benutzername ist bereits vergeben!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            return null;
        }

        user.setPassword(password);

        try {
            userDAO.create(user);
            registrationError = null;
        } catch (Exception e) {
            registrationError = "Registrierung fehlgeschlagen: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, registrationError, null));
            e.printStackTrace();
            return null;
        }

        return "login?faces-redirect=true";
    }

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
    public String getRegistrationError() {
        return registrationError;
    }
    public void setRegistrationError(String registrationError) {
        this.registrationError = registrationError;
    }
}
