package Presentation.models;

import Gestion.ProjetGestion;
import Gestion.TacheGestion;
import POJO.Doc;
import POJO.Projet;
import POJO.Tache;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class ModelTask {
    private TacheGestion task;
    private ProjetGestion projet;
    public ModelTask() {
        this.task = new TacheGestion();
        this.projet = new ProjetGestion();
    }
    public List<Tache> readTaches() {
        List<Tache> taches = task.getAllTache();
        for (Tache tache : taches) {
            ObjectId objectId = task.getID(tache);
            tache.setID(objectId);
        }
        return taches;
    }
    public void ajouterDocumentATache(int index, Doc newDoc,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    tache.getDoc().add(newDoc);
                    projet.updateProjet(prj, projetId);
                    return;
                }
            }
        }
    }
    public void supprimerDocumentDeTache( int index,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    tache.getDoc().remove(index);
                    projet.updateProjet(prj, projetId);
                }
            }
        }
    }
    public void ModifierDocumentDeTache( String newDescription, Date newDate, int index,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    Doc doc = tache.getDoc().get(index);
                    doc.setDescription(newDescription);
                    doc.setDateAjout(newDate);
                    projet.updateProjet(prj, projetId);
                }
            }
        }

    }





}
