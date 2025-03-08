package com.ghostnet.model;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entitätsklasse zur Darstellung eines Benutzers im System.
 * Diese Klasse repräsentiert einen registrierten Nutzer, der Geisternetze melden oder retten kann.
 */
@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Eindeutige Benutzer-ID

    private String username; // Benutzername (muss eindeutig sein)
    private String password; // Passwort (wurde aufgrund des Prototyps nicht verschlüsselt)
    private String name; // Name des Benutzers
    private String telephone; // Telefonnummer des Benutzers

    // Getter und Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
