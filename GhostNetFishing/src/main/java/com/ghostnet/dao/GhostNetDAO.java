package com.ghostnet.dao;

import com.ghostnet.model.GhostNet;
import com.ghostnet.model.User;
import com.ghostnet.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class GhostNetDAO {

    public void create(GhostNet ghostNet) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ghostNet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<GhostNet> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT g FROM GhostNet g", GhostNet.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(GhostNet ghostNet) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ghostNet);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public GhostNet findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(GhostNet.class, id);
        } finally {
            em.close();
        }
    }

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
