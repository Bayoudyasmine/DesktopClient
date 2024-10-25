package com.cinetique.ejb;

import com.cinetique.model.CD;
import com.cinetique.model.Emprunt;

import java.util.List;

public interface IAdminService {
    void ajouterCD(CD cd);
    void modifierCD(CD cd);
    void supprimerCD(Long cdId);
    List<Emprunt> voirEmprunts();
}
