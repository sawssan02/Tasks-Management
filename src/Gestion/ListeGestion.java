package Gestion;

import POJO.Liste;
import POJO.Tache;
import org.bson.types.ObjectId;
import persistence.ListeDAO;

import java.util.ArrayList;
import java.util.List;

public class ListeGestion {
    private ListeDAO listeDAO;

    public ListeGestion() {
        this.listeDAO = new ListeDAO();
    }
    public void createListe(Liste projet) {
        listeDAO.create(projet);
    }
    public Liste readListe(ObjectId id) {
        return listeDAO.read(id);
    }
    public void updateListe(Liste updatedListe, ObjectId id) {
        listeDAO.update(updatedListe,id);
    }
    public void deleteListe(ObjectId id) {
        listeDAO.delete(id);
    }
    public ObjectId getID(Liste projet){
        return listeDAO.getID(projet);

    }
    public Liste getListeByID(ObjectId id){
        return listeDAO.getProjetById(id);
    }
    public List<Liste> getAllListe(){
        return listeDAO.getAll();
    }
    public void ajouterTacheAListe(ObjectId listeId, Tache newTache) {
        Liste liste = listeDAO.read(listeId);
        if (liste != null) {
            List<Tache> taches = liste.getTaches();
            if (taches == null) {
                taches = new ArrayList<>();
            }
            taches.add(newTache);
            listeDAO.update(liste, listeId);
        } else {
            System.out.println("La liste avec l'ID " + listeId + " n'existe pas.");
        }
    }
}
