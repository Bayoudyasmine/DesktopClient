package com.cinetique.ejb;

import com.cinetique.model.CD;
import com.cinetique.model.Emprunt;

import java.util.List;

public interface IUtilisateurService {
    List<CD> listerCDDisponibles();
    void emprunterCD(Long cdId, String utilisateur);
    List<Emprunt> voirCDsEmpruntes(String utilisateur);
    void retournerCD(Long cdId);
}
