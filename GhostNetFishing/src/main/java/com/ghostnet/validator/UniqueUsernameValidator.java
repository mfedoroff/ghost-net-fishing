package com.ghostnet.validator;

import com.ghostnet.util.JPAUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

/**
 * JSF-Validator zur Überprüfung der Eindeutigkeit von Benutzernamen.
 * Diese Klasse prüft, ob ein Benutzername bereits in der Datenbank existiert,
 * um doppelte Registrierungen zu vermeiden.
 */
@Named
@RequestScoped
public class UniqueUsernameValidator implements Validator {

    /**
     * Validiert, ob der eingegebene Benutzername bereits existiert.
     *
     * @param context   FacesContext der aktuellen Anfrage.
     * @param component Die UI-Komponente, die validiert wird.
     * @param value     Der eingegebene Benutzername.
     * @throws ValidatorException Falls der Benutzername bereits vergeben ist.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String username = value.toString().trim();
        EntityManager em = JPAUtil.getEntityManager();

        try {
            // Prüft, ob der Benutzername bereits in der Datenbank existiert
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (count != null && count > 0) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Benutzername ist bereits vergeben!", null);
                throw new ValidatorException(msg);
            }
        } finally {
            em.close();
        }
    }
}
