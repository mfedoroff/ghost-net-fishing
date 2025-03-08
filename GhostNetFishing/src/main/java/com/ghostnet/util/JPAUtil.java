package com.ghostnet.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility-Klasse zur Verwaltung der JPA `EntityManagerFactory`.
 * Diese Klasse stellt eine zentrale Möglichkeit bereit, um `EntityManager` zu erzeugen
 * und die `EntityManagerFactory` ordnungsgemäß zu schließen.
 */
public class JPAUtil {

    /**
     * Die `EntityManagerFactory` wird nur einmal für die gesamte Anwendung erstellt.
     * Die `persistence.xml`-Konfigurationsdatei enthält die Einstellungen für `ghostnetfishingPU`.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ghostnetfishingPU");

    /**
     * Erstellt und gibt einen neuen `EntityManager` zurück.
     *
     * @return Ein neuer `EntityManager`.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Schließt die `EntityManagerFactory`, um Ressourcen freizugeben.
     * Sollte nur einmal beim Beenden der Anwendung aufgerufen werden.
     */
    public static void close() {
        emf.close();
    }
}
