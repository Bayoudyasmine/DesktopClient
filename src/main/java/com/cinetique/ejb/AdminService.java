package com.cinetique.ejb;

import com.cinetique.model.CD;
import com.cinetique.model.Emprunt;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateful
public class AdminService implements IAdminService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void ajouterCD(CD cd) {
        em.persist(cd);
    }

    @Override
    public void modifierCD(CD cd) {
        em.merge(cd);
    }

    @Override
    public void supprimerCD(Long cdId) {
        CD cd = em.find(CD.class, cdId);
        if (cd != null) {
            em.remove(cd);
        }
    }

    @Override
    public List<Emprunt> voirEmprunts() {
        return em.createQuery("SELECT e FROM Emprunt e", Emprunt.class).getResultList();
    }
}
