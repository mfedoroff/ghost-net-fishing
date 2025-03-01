package com.ghostnet.controller;

import com.ghostnet.dao.GhostNetDAO;
import com.ghostnet.model.GhostNet;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
public class GhostNetController {

    private GhostNet ghostNet = new GhostNet();
    private String submissionMessage;

    private GhostNetDAO ghostNetDAO = new GhostNetDAO();

    @Inject
    private LoginController loginController;

    public String submitReport() {
        if (loginController != null && loginController.isLoggedIn()) {
            ghostNet.setReporterName(loginController.getUser().getName());
            ghostNet.setReporterTelephone(loginController.getUser().getTelephone());
        }
        try {
            ghostNetDAO.create(ghostNet);
            submissionMessage = "Geisternetz erfolgreich gemeldet!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, submissionMessage, null));
            ghostNet = new GhostNet();
        } catch (Exception e) {
            submissionMessage = "Fehler beim Melden des Geisternetzes: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, submissionMessage, null));
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // Getter und Setter
    public GhostNet getGhostNet() {
        return ghostNet;
    }
    public void setGhostNet(GhostNet ghostNet) {
        this.ghostNet = ghostNet;
    }
    public String getSubmissionMessage() {
        return submissionMessage;
    }
    public void setSubmissionMessage(String submissionMessage) {
        this.submissionMessage = submissionMessage;
    }
}
