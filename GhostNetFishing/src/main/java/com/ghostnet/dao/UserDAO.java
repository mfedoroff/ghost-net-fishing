package com.ghostnet.dao;

import com.ghostnet.model.User;
import com.ghostnet.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

/**
 * Data Access Object (DAO) für die Verwaltung von Benutzerdaten in der Datenbank.
 * Diese Klasse bietet Methoden zum Speichern und Abrufen von {@link User}-Objekten.
 */
public class UserDAO {

    /**
     * Sucht einen Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des gesuchten Benutzers.
     * @return Das gefundene {@link User}-Objekt oder {@code null}, falls kein Benutzer existiert.
     */
    public User findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Falls kein Benutzer gefunden wird, wird `null` zurückgegeben
        } finally {
            em.close();
        }
    }

    /**
     * Sucht einen Benutzer anhand von Benutzername und Passwort (zur Authentifizierung).
     *
     * @param username Der Benutzername.
     * @param password Das Passwort (Aktuell unverschlüsselt).
     * @return Das gefundene {@link User}-Objekt oder {@code null}, falls die Zugangsdaten falsch sind.
     */
    public User findByUsernameAndPassword(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Falls keine Übereinstimmung gefunden wurde, wird `null` zurückgegeben
        } finally {
            em.close();
        }
    }

    /**
     * Erstellt einen neuen Benutzer und speichert ihn in der Datenbank.
     *
     * @param user Das zu speichernde {@link User}-Objekt.
     * @throws Exception Falls ein Fehler beim Speichern auftritt.
     */
    public void create(User user) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback der Transaktion bei Fehlern
            throw e;
        } finally {
            em.close();
        }
    }
}
