package com.ghostnet.dao;

import com.ghostnet.model.GhostNet;
import com.ghostnet.util.JPAUtil;
import jakarta.persistence.EntityManager;

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
}
