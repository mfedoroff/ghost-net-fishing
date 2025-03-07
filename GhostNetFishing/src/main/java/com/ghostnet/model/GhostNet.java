package com.ghostnet.model;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;

@Entity
public class GhostNet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String location;
    private Double size;

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status;

    // Angaben des Meldenden (Reporter) als Strings, da auch anonyme Meldungen möglich sind
    private String reporterName;
    private String reporterTelephone;

    // Bergende Person als vollständiger User (bei registrierten Nutzern)
    @ManyToOne
    private User rescuer;

    public GhostNet() {
        // Standardstatus wird auf REPORTED gesetzt
        this.status = GhostNetStatus.REPORTED;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Double getSize() {
        return size;
    }
    public void setSize(Double size) {
        this.size = size;
    }
    public GhostNetStatus getStatus() {
        return status;
    }
    public void setStatus(GhostNetStatus status) {
        this.status = status;
    }
    public String getReporterName() {
        return reporterName;
    }
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
    public String getReporterTelephone() {
        return reporterTelephone;
    }
    public void setReporterTelephone(String reporterTelephone) {
        this.reporterTelephone = reporterTelephone;
    }
    public User getRescuer() {
        return rescuer;
    }
    public void setRescuer(User rescuer) {
        this.rescuer = rescuer;
    }

    // Methode zur Lokalisierung des Status
    public String getLocalizedStatus() {
        if (status == null) return "";
        switch(status) {
            case REPORTED:
                return "Gemeldet";
            case RESCUE_PENDING:
                return "Bergung bevorstehend";
            case RESCUED:
                return "Geborgen";
            case LOST:
                return "Verschollen";
            default:
                return status.toString();
        }
    }

    public String getStatusStyle() {
        if (status == null) return "info";
        switch(status) {
            case REPORTED:
                return "info";
            case RESCUE_PENDING:
                return "warning";
            case RESCUED:
                return "success";
            case LOST:
                return "danger";
            default:
                return "info";
        }
    }
}
