package Presentation.models;

import Gestion.ProjetGestion;
import Gestion.SeanceGestion;
import POJO.Doc;
import POJO.Projet;
import POJO.Seance;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class ModelSeance {
    private SeanceGestion seance;
    private ProjetGestion projet;
    public ModelSeance() {
        this.seance = new SeanceGestion();
        this.projet = new ProjetGestion();
    }
    public List<Seance> readSeances() {
        List<Seance> seances = seance.getAllSeance();
        for (Seance seanc : seances) {
            ObjectId objectId = seance.getID(seanc);
            seanc.setID(objectId);
        }
        return seances;
    }
    public void ajouterDocumentASeance(int index, Doc newDoc,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Seance> seances = prj.getSeances();
            for (Seance sean : seances) {
                if (index >= 0 && index < seances.size()) {
                    sean.getDoc().add(newDoc);
                    projet.updateProjet(prj, projetId);
                    return;
                }
            }
        }
    }
    public void supprimerDocumentDeSeance( int index,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Seance> seances = prj.getSeances();
            for (Seance sean : seances) {
                if (index >= 0 && index < seances.size()) {
                    sean.getDoc().remove(index);
                    projet.updateProjet(prj, projetId);
                }
            }
        }
    }
    public void ModifierDocumentDeSeance( String newDescription, Date newDate, int index,ObjectId projetId) {
        Projet prj = projet.getProjetByID(projetId);

        if (prj != null) {
            List<Seance> seances = prj.getSeances();
            for (Seance sean : seances) {
                if (index >= 0 && index < seances.size()) {
                    Doc doc = sean.getDoc().get(index);
                    doc.setDescription(newDescription);
                    doc.setDateAjout(newDate);
                    projet.updateProjet(prj, projetId);
                }
            }
        }

    }
}
