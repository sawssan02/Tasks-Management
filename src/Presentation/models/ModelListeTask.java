package Presentation.models;

import Gestion.ListeGestion;
import Gestion.TacheGestion;
import POJO.Doc;
import POJO.Liste;
import POJO.Tache;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class ModelListeTask {
    private TacheGestion task;
    private ListeGestion liste;
    public ModelListeTask() {
        this.task = new TacheGestion();
        this.liste = new ListeGestion();
    }
    public List<Tache> readTaches() {
        List<Tache> taches = task.getAllTache();
        for (Tache tache : taches) {
            ObjectId objectId = task.getID(tache);
            tache.setID(objectId);
        }
        return taches;
    }
    public void ajouterDocumentATache(int index, Doc newDoc, ObjectId projetId) {
        Liste prj = liste.getListeByID(projetId);
        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    tache.getDoc().add(newDoc);
                    liste.updateListe(prj, projetId);
                    return;
                }
            }
        }
    }
    public void supprimerDocumentDeTache( int index,ObjectId projetId) {
        Liste prj = liste.getListeByID(projetId);

        if (prj != null) {
            List<Tache> taches = prj.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    tache.getDoc().remove(index);
                    liste.updateListe(prj, projetId);
                }
            }
        }
    }
    public void ModifierDocumentDeTache(String newDescription, Date newDate, int index, ObjectId liId) {
        Liste li = liste.getListeByID(liId);

        if (li != null) {
            List<Tache> taches = li.getTaches();
            for (Tache tache : taches) {
                if (index >= 0 && index < taches.size()) {
                    Doc doc = tache.getDoc().get(index);
                    doc.setDescription(newDescription);
                    doc.setDateAjout(newDate);
                    liste.updateListe(li, liId);
                }
            }
        }

    }
}
