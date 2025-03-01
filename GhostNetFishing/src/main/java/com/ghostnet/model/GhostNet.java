package com.ghostnet.model;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class GhostNet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String location;
    private Double size;

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status;

    private String reporterName;
    private String reporterTelephone;

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
}
