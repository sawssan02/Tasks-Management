package Presentation.models;
import Gestion.ProjetGestion;
import POJO.Doc;
import POJO.Projet;

import Gestion.TacheGestion;
import POJO.Seance;
import POJO.Tache;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;


public class ModelProjet {
    private ProjetGestion projet;
    public ModelProjet() {
        this.projet = new ProjetGestion();
    }

    public ObjectId ajouterProjet(String categorie, String type, String description, Date datedebut, Date datefin, List<Seance> seances, List<Tache> taches, List<Doc> docs) {
        Projet nouveauProjet = new Projet();
        nouveauProjet.setCategorie(categorie);
        nouveauProjet.setType(type);
        nouveauProjet.setDescription(description);
        nouveauProjet.setDateDebut(datedebut);
        nouveauProjet.setDateFin(datefin);
        nouveauProjet.setSeances(seances);
        nouveauProjet.setTaches(taches);
        nouveauProjet.setDoc(docs);
        projet.createProjet(nouveauProjet);
        return nouveauProjet.getID();
    }
    public void ModifierProjet(String categorie, String type, String description, Date datedebut, Date datefin, ObjectId id) {
        Projet nouveauProjet = new Projet();
        nouveauProjet.setCategorie(categorie);
        nouveauProjet.setType(type);
        nouveauProjet.setDescription(description);
        nouveauProjet.setDateDebut(datedebut);
        nouveauProjet.setDateFin(datefin);

        projet.updateProjet(nouveauProjet,id);
    }
    public void SupprimerProjet(ObjectId id) {
        projet.deleteProjet(id);
    }
    public List<Projet> readProjets() {
        List<Projet> projets = projet.getAllProjet();
        for (Projet prj : projets) {
            ObjectId objectId = projet.getID(prj);
            prj.setID(objectId);
        }
        return projets;
    }
    public void ajouterDocumentAProjet(ObjectId projetId, Doc newDoc) {
        projet.ajouterDocumentAProjet(projetId, newDoc);
    }
    public void supprimerDocumentDeProjet(ObjectId projetId, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Doc> documents = prj.getDoc();
            if (index >= 0 && index < documents.size()) {
                documents.remove(index);
                projet.updateProjet(prj,projetId);
            }
        }
    }
    public void ModifierDocumentDeProjet(ObjectId projetId,String newDescription, Date newDate, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Doc> documents = prj.getDoc();
            if (index >= 0 && index < documents.size()) {
                Doc doc = documents.get(index);
                doc.setDescription(newDescription);
                doc.setDateAjout(newDate);
                projet.updateProjet(prj, projetId);
            }
        }
    }
    public void ajouterSeanceAProjet(ObjectId projetId, Seance newSeance) {
        projet.ajouterSeanceAProjet(projetId, newSeance);
    }

    public void supprimerSeanceDeProjet(ObjectId projetId, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Seance> seances = prj.getSeances();
            if (index >= 0 && index < seances.size()) {
                seances.remove(index);
                projet.updateProjet(prj, projetId);
            }
        }
    }

    public void modifierSeanceDeProjet(ObjectId projetId, String newDescription, Date newDateDebut, Date newDateFin, String newNote, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Seance> seances = prj.getSeances();
            if (index >= 0 && index < seances.size()) {
                Seance seance = seances.get(index);
                seance.setDescription(newDescription);
                seance.setDateDebut(newDateDebut);
                seance.setDateFin(newDateFin);
                seance.setNote(newNote);
                projet.updateProjet(prj, projetId);
            }
        }
    }

    public void ajouterTacheAProjet(ObjectId projetId, Tache newTache) {
        projet.ajouterTacheAProjet(projetId, newTache);
    }

    public void supprimerTacheDeProjet(ObjectId projetId, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (projet != null) {
            List<Tache> taches = prj.getTaches();
            if (index >= 0 && index < taches.size()) {
                taches.remove(index);
                projet.updateProjet(prj, projetId);
            }
        }
    }

    public void modifierTacheDeProjet(ObjectId projetId, String newDescription, Date newDateDebut, Date newDateFin, int index) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            if (index >= 0 && index < taches.size()) {
                Tache tache = taches.get(index);
                tache.setDescription(newDescription);
                tache.setDateDebut(newDateDebut);
                tache.setDateFin(newDateFin);
                projet.updateProjet(prj, projetId);
            }
        }
    }
    public List<Projet> filtrerParCategorie(String categorie){
        return projet.filtrerProjetCategorie(categorie);
    }
    public List<Projet> filtrerProjetType(String type){
        return projet.filtrerProjetType(type);
    }
    public List<Projet> searchByDescription(String description){
        return projet.searchByDescription(description);
    }
    public List<Tache> filtrerTachesParCategorie(String categorie){
        return projet.filtrerTachesParCategorie(categorie);
    }
    public List<Tache> filtrerTachesParStatut(boolean etat){
        return projet.filtrerTachesParStatut(etat);
    }
    public List<Tache> searchTacheByDescription(String description){
        return projet.searchTacheByDescription(description);
    }
    public List<Doc> chercherDocParDescription(String description){
        return projet.chercherDocParDescription(description);
    }
    public List<Doc> chercherTacheParDescription(String description){
        return projet.chercherTacheParDescription(description);
    }
    public List<Doc> chercherSeanceParDescription(String description){
        return projet.chercherSeanceParDescription(description);
    }
    public ObjectId clonerProjet(ObjectId projetId) {
        return projet.clonerProjet(projetId);
    }
}
