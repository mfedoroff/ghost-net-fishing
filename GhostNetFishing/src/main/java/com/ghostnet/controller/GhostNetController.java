package com.ghostnet.controller;

import com.ghostnet.dao.GhostNetDAO;
import com.ghostnet.model.GhostNet;
import com.ghostnet.util.GhostNetStatus;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller für das Verwalten von Geisternetzen in der Anwendung.
 * Diese Klasse dient als Schnittstelle zwischen der Benutzeroberfläche und der Geschäftslogik.
 */
@Named
@ViewScoped
public class GhostNetController implements Serializable {

    @Inject
    private GhostNetDAO ghostNetDAO;  // DAO für den Zugriff auf die Datenbank
    @Inject
    private LoginController loginController;  // Injected LoginController für Benutzerverifikation

    private GhostNet ghostNet = new GhostNet();  // Repräsentiert das aktuelle Geisternetz
    private String returnPage;  // Speichert die vorherige Seite für Navigation


    // Getter und Setter
    public GhostNet getGhostNet() {
        return ghostNet;
    }
    public void setGhostNet(GhostNet ghostNet) {
        this.ghostNet = ghostNet;
    }
    public String getReturnPage() {
        return returnPage;
    }
    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    /**
     * Lädt ein Geisternetz aus der Datenbank, falls eine ID vorhanden ist.
     */
    public void loadGhostNet() {
        if (ghostNet.getId() != null) {
            GhostNet loaded = ghostNetDAO.findById(ghostNet.getId());
            if (loaded != null) {
                ghostNet = loaded;
            }
        }
    }

    /**
     * Leitet den Benutzer zur vorherigen Seite oder zur Netz-Übersicht weiter.
     *
     * @return Die URL der Zielseite mit `faces-redirect`
     */
    public String goBack() {
        if (returnPage != null && !returnPage.isEmpty()) {
            return returnPage + "?faces-redirect=true";
        }
        return "netOverview.xhtml?faces-redirect=true";
    }

    /**
     * Ruft alle registrierten Geisternetze aus der Datenbank ab.
     *
     * @return Eine Liste von {@link GhostNet} Objekten.
     */
    public List<GhostNet> getGhostNets() {
        return ghostNetDAO.findAll();
    }

    /**
     * Ruft alle Geisternetze ab, für die der aktuell eingeloggte Nutzer als bergende Person eingetragen ist.
     *
     * @return Eine Liste der vom Benutzer zu rettenden Geisternetze.
     */
    public List<GhostNet> getMyRecoveries() {
        if (loginController != null && loginController.isLoggedIn()) {
            return ghostNetDAO.findByRescuer(loginController.getUser());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Meldet ein neues Geisternetz. Falls der Benutzer eingeloggt ist,
     * werden seine Daten automatisch als Reporter-Informationen gesetzt.
     */
    public void submitReport() {
        String submissionMessage;

        if (loginController != null && loginController.isLoggedIn()) {
            // Setzt automatisch die Kontaktdaten des Nutzers
            ghostNet.setReporterName(loginController.getUser().getName());
            ghostNet.setReporterTelephone(loginController.getUser().getTelephone());
        }
        try {
            ghostNetDAO.create(ghostNet);
            submissionMessage = "Geisternetz erfolgreich gemeldet!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, submissionMessage, null));
            ghostNet = new GhostNet(); // Zurücksetzen nach erfolgreicher Meldung
        } catch (Exception e) {
            submissionMessage = "Fehler beim Melden des Geisternetzes: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, submissionMessage, null));
            e.printStackTrace();
        }
    }

    /**
     * Registriert den eingeloggten Benutzer als bergende Person für das aktuelle Geisternetz.
     */
    public void registerForRescue() {
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
    }

    /**
     * Markiert ein Geisternetz als geborgen, wenn der eingeloggte Benutzer die zugewiesene bergende Person ist.
     */
    public void markAsRescued() {
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
    }

    /**
     * Gibt die Rettung eines Geisternetzes wieder frei, falls der eingeloggte Benutzer als bergende Person eingetragen war.
     */
    public void releaseRescue() {
        if (loginController != null && loginController.isLoggedIn() && ghostNet.getRescuer() != null &&
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
    }
}
