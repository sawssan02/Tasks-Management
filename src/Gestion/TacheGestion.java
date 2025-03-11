package Gestion;

import POJO.Doc;
import POJO.Projet;
import POJO.Seance;
import POJO.Tache;
import org.bson.types.ObjectId;
import persistence.TacheDAO;

import java.util.ArrayList;
import java.util.List;

public class TacheGestion {
    private TacheDAO tacheDAO;

    public TacheGestion() {
        this.tacheDAO = new TacheDAO();
    }
    public void createTache(Tache projet) {
        tacheDAO.create(projet);
    }
    public Tache readTache(ObjectId id) {
        return tacheDAO.read(id);
    }
    public void updateTache(Tache updatedTache, ObjectId id) {
        tacheDAO.update(updatedTache,id);
    }
    public void deleteTache(ObjectId id) {
        tacheDAO.delete(id);
    }
    public ObjectId getID(Tache tache){
        return tacheDAO.getID(tache);
    }
    public Tache getTacheByID(ObjectId id){
        return tacheDAO.getTacheById(id);
    }
    public List<Tache> getAllTache(){
        return tacheDAO.getAll();
    }


}
