// UtilisateurService.java
package com.cinetique.ejb;

import com.cinetique.model.CD;
import com.cinetique.model.Emprunt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;

@Stateless
public class UtilisateurService implements IUtilisateurService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CD> listerCDDisponibles() {
        return em.createQuery("SELECT c FROM CD c WHERE c.disponible = true", CD.class).getResultList();
    }

    @Override
    public void emprunterCD(Long cdId, String utilisateur) throws Exception {
        CD cd = em.find(CD.class, cdId);
        if (cd == null) {
            throw new Exception("CD non trouvé");
        }
        if (!cd.isDisponible()) {
            throw new Exception("CD déjà emprunté");
        }
        cd.setDisponible(false);
        Emprunt emprunt = new Emprunt();
        emprunt.setCd(cd);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateEmprunt(new Date());
        em.persist(emprunt);
    }

    @Override
    public List<Emprunt> voirCDsEmpruntes(String utilisateur) {
        return em.createQuery("SELECT e FROM Emprunt e WHERE e.utilisateur = :utilisateur", Emprunt.class)
                .setParameter("utilisateur", utilisateur)
                .getResultList();
    }

    @Override
    public void retournerCD(Long cdId) {
        Emprunt emprunt = em.createQuery("SELECT e FROM Emprunt e WHERE e.cd.id = :cdId", Emprunt.class)
                .setParameter("cdId", cdId)
                .getSingleResult();
        if (emprunt != null) {
            CD cd = emprunt.getCd();
            cd.setDisponible(true);
            em.remove(emprunt);
        }
    }
}
