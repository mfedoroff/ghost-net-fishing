package com.ghostnet.dao;

import com.ghostnet.model.GhostNet;
import com.ghostnet.model.User;
import com.ghostnet.util.JPAUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Data Access Object (DAO) für die Verwaltung von Geisternetzen in der Datenbank.
 * Diese Klasse bietet Methoden zum Erstellen, Aktualisieren und Abrufen von {@link GhostNet}-Objekten.
 */
@Named
@ApplicationScoped
public class GhostNetDAO {

    /**
     * Speichert ein neues Geisternetz in der Datenbank.
     *
     * @param ghostNet Das zu speichernde {@link GhostNet}-Objekt.
     * @throws Exception Falls ein Fehler beim Speichern auftritt.
     */
    public void create(GhostNet ghostNet) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ghostNet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback bei Fehlern
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Ruft alle gespeicherten Geisternetze aus der Datenbank ab.
     *
     * @return Eine Liste von {@link GhostNet}-Objekten.
     */
    public List<GhostNet> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT g FROM GhostNet g", GhostNet.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Aktualisiert die Daten eines bestehenden Geisternetzes.
     *
     * @param ghostNet Das aktualisierte {@link GhostNet}-Objekt.
     * @throws Exception Falls ein Fehler beim Speichern auftritt.
     */
    public void update(GhostNet ghostNet) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ghostNet); // `merge` wird verwendet, um bestehende Objekte zu aktualisieren
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback(); // Rollback, falls die Transaktion fehlschlägt
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Sucht nach einem Geisternetz anhand seiner ID.
     *
     * @param id Die ID des Geisternetzes.
     * @return Das gefundene {@link GhostNet}-Objekt.
     */
    public GhostNet findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(GhostNet.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Ruft alle Geisternetze ab, die einer bestimmten bergenden Person zugewiesen sind.
     *
     * @param rescuer Die bergende Person als {@link User}-Objekt.
     * @return Eine Liste von {@link GhostNet}-Objekten, die dieser bergenden Person zugewiesen sind.
     */
    public List<GhostNet> findByRescuer(User rescuer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT g FROM GhostNet g WHERE g.rescuer = :rescuer", GhostNet.class)
                    .setParameter("rescuer", rescuer)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
