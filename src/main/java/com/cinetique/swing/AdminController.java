package com.cinetique.swing;

import com.cinetique.ejb.IAdminService;
import com.cinetique.model.CD;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.List;

public class AdminController {
    private IAdminService adminService;

    // Constructeur : initialise la connexion JNDI pour obtenir une référence vers l'EJB
    public AdminController() throws NamingException {
        adminService = lookupAdminService();
    }

    // Méthode pour rechercher l'EJB via JNDI
    private IAdminService lookupAdminService() throws NamingException {
        // Configuration de l'environnement JNDI pour accéder à l'EJB
        Hashtable<String, Object> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        env.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");  // Adresse du serveur WildFly où le backend est déployé

        // Création du contexte initial
        Context context = new InitialContext(env);

        // Recherche de l'EJB déployé par son nom JNDI
        return (IAdminService) context.lookup("ejb:/gestion-cinetique-backend/AdminService!com.cinetique.ejb.IAdminService");
    }

    // Méthode pour obtenir tous les CD via l'EJB distant
    public List<CD> getAllCDs() {
        // Appel à la méthode voirEmprunts() pour récupérer la liste des CD liés aux emprunts
        return adminService.voirEmprunts().stream().map(e -> e.getCd()).toList();
    }

    // Méthodes pour gérer les opérations de CRUD sur les CD
    public void ajouterCD(CD cd) {
        adminService.ajouterCD(cd);
    }

    public void modifierCD(CD cd) {
        adminService.modifierCD(cd);
    }

    public void supprimerCD(Long cdId) {
        adminService.supprimerCD(cdId);
    }
}
