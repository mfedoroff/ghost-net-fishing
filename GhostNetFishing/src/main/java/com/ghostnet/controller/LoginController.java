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
public class LoginController implements Serializable {

    private User user = new User();
    private boolean loggedIn = false;
    private String loginError;

    private UserDAO userDAO = new UserDAO();

    public String login() {
        User foundUser = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            loggedIn = true;
            user = foundUser;
            loginError = null;
            return "index?faces-redirect=true";
        } else {
            loggedIn = false;
            loginError = "Ungültige Zugangsdaten!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, loginError, null));
            return null;
        }
    }

    public String logout() {
        System.out.println("Logout called");
        loggedIn = false;
        user = new User();
        return "index?faces-redirect=true";
    }

    public String getGreeting() {
        if (loggedIn && user != null && user.getName() != null && !user.getName().isEmpty()) {
            return "Hallo, " + user.getName();
        }
        return "Konto";
    }

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
    public String getLoginError() {
        return loginError;
    }
}
