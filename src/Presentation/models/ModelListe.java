package Presentation.models;

import Gestion.ListeGestion;
import POJO.*;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class ModelListe {
    private ListeGestion liste;
    public ModelListe() {
        this.liste = new ListeGestion();
    }

    public ObjectId ajouterListe( String description, List<Tache> taches) {
        Liste nouveauProjet = new Liste();
        nouveauProjet.setDescription(description);
        nouveauProjet.setTaches(taches);
        liste.createListe(nouveauProjet);
        return nouveauProjet.getID();
    }
    public void ModifierListe( String description, ObjectId id) {
        Liste nouveauProjet = new Liste();
        nouveauProjet.setDescription(description);

        liste.updateListe(nouveauProjet,id);
    }
    public void SupprimerListe(ObjectId id) {
        liste.deleteListe(id);
    }
    public List<Liste> readListes() {
        List<Liste> projets = liste.getAllListe();
        for (Liste prj : projets) {
            ObjectId objectId = liste.getID(prj);
            System.out.println(objectId);
            prj.setID(objectId);
        }
        return projets;
    }
    public void ajouterTacheAListe(ObjectId projetId, Tache newTache) {
        liste.ajouterTacheAListe(projetId, newTache);
    }
    public void supprimerTacheDeListe(ObjectId projetId, int index) {
        Liste prj = liste.getListeByID(projetId);

        if (liste != null) {
            List<Tache> taches = prj.getTaches();
            if (index >= 0 && index < taches.size()) {
                taches.remove(index);
                liste.updateListe(prj, projetId);
            }
        }
    }
    public void modifierTacheDeListe(ObjectId projetId, String newDescription, Date newDateDebut, Date newDateFin, int index) {
        Liste prj = liste.getListeByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            if (index >= 0 && index < taches.size()) {
                Tache tache = taches.get(index);
                tache.setDescription(newDescription);
                tache.setDateDebut(newDateDebut);
                tache.setDateFin(newDateFin);
                liste.updateListe(prj, projetId);
            }
        }
    }
}
