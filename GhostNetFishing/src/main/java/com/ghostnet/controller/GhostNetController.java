package com.ghostnet.controller;

import com.ghostnet.dao.GhostNetDAO;
import com.ghostnet.model.GhostNet;
import com.ghostnet.model.GhostNetStatus;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;  // Verwende ViewScoped aus jakarta.faces.view
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class GhostNetController implements Serializable {

    private GhostNet ghostNet = new GhostNet();
    private String submissionMessage;
    private String returnPage;
    private GhostNetDAO ghostNetDAO = new GhostNetDAO();

    @Inject
    private LoginController loginController;

    public void loadGhostNet() {
        if (ghostNet.getId() != null) {
            GhostNet loaded = ghostNetDAO.findById(ghostNet.getId());
            if (loaded != null) {
                ghostNet = loaded;
            }
        }
    }

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

    public List<GhostNet> getGhostNets() {
        return ghostNetDAO.findAll();
    }

    public String registerForRescue() {
        // Stelle sicher, dass ghostNet vollständig geladen ist
        if (ghostNet.getId() != null) {
            GhostNet loaded = ghostNetDAO.findById(ghostNet.getId());
            if (loaded != null) {
                ghostNet = loaded;
            }
        }
        if (loginController != null && loginController.isLoggedIn()) {
            ghostNet.setRescuer(loginController.getUser());
            ghostNet.setStatus(GhostNetStatus.RESCUE_PENDING);
            try {
                ghostNetDAO.update(ghostNet);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sie wurden für die Bergung eingetragen.", null));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler bei der Registrierung für Bergung: " + e.getMessage(), null));
            }
        }
        return null;
    }

    public String markAsRescued() {
        if (ghostNet.getId() != null) {
            GhostNet loaded = ghostNetDAO.findById(ghostNet.getId());
            if (loaded != null) {
                ghostNet = loaded;
            }
        }
        if (loginController != null && loginController.isLoggedIn() &&
                ghostNet.getRescuer() != null &&
                ghostNet.getRescuer().getUsername().equals(loginController.getUser().getUsername())) {
            ghostNet.setStatus(GhostNetStatus.RESCUED);
            try {
                ghostNetDAO.update(ghostNet);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Netz wurde als geborgen gemeldet.", null));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Melden als geborgen: " + e.getMessage(), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Sie sind nicht als Bergender eingetragen.", null));
        }
        return null;
    }

    public String releaseRescue() {
        if (loginController != null && loginController.isLoggedIn() &&
                ghostNet.getRescuer() != null &&
                ghostNet.getRescuer().getUsername().equals(loginController.getUser().getUsername())) {
            ghostNet.setRescuer(null);
            ghostNet.setStatus(GhostNetStatus.REPORTED);
            try {
                ghostNetDAO.update(ghostNet);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Bergung freigegeben.", null));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Freigeben der Bergung: " + e.getMessage(), null));
            }
        }
        return null;
    }

    public String goBack() {
        if (returnPage != null && !returnPage.isEmpty()) {
            return returnPage + "?faces-redirect=true";
        }
        return "netOverview.xhtml?faces-redirect=true";
    }

    public List<GhostNet> getMyRecoveries() {
        if (loginController != null && loginController.isLoggedIn()) {
            return ghostNetDAO.findByRescuer(loginController.getUser());
        } else {
            return new ArrayList<>();
        }
    }

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
    public String getReturnPage() {
        return returnPage;
    }
    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }
}
