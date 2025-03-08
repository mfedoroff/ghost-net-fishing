package com.ghostnet.model;

import java.io.Serializable;

import com.ghostnet.util.GhostNetStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;

/**
 * Entitätsklasse zur Darstellung eines Geisternetzes.
 * Diese Klasse repräsentiert ein herrenloses Fischernetz, das gemeldet und geborgen werden kann.
 */
@Entity
public class GhostNet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Eindeutige ID des Geisternetzes

    private String location; // Geografische Position des Netzes
    private Double size; // Größe des Netzes in Quadratmetern oder einer anderen Einheit

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status; // Status des Netzes (Enum: REPORTED, RESCUE_PENDING, RESCUED, LOST)

    private String reporterName; // Name der Person, die das Netz gemeldet hat
    private String reporterTelephone; // Telefonnummer des Melders

    @ManyToOne
    private User rescuer; // Der Nutzer, der das Netz bergen möchte

    /**
     * Standardkonstruktor, setzt den Standardstatus auf "REPORTED".
     */
    public GhostNet() {
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

    /**
     * Gibt eine lokalisierte Textdarstellung des Status zurück.
     *
     * @return Der Status als lesbare Zeichenkette (z. B. "Gemeldet" statt "REPORTED").
     */
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

    /**
     * Gibt eine CSS-Klassenbezeichnung für die Statusanzeige zurück.
     * Dies wird für eine visuelle Darstellung des Status verwendet.
     *
     * @return Ein CSS-Klassenname für den Status.
     */
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
