package Gestion;
import POJO.Doc;
import org.bson.types.ObjectId;
import persistence.SeanceDAO;
import POJO.Seance;

import java.util.ArrayList;
import java.util.List;

public class SeanceGestion {
    private SeanceDAO seanceDAO;

    public SeanceGestion() {
        this.seanceDAO = new SeanceDAO();
    }
    public void createSeance(Seance seance) {
        seanceDAO.create(seance);
    }
    public Seance readSeance(ObjectId id) {
        return seanceDAO.read(id);
    }
    public void updateSeance(Seance updatedSeance,ObjectId id) {
        seanceDAO.update(updatedSeance,id);
    }
    public void deleteSeance(ObjectId id) {
        seanceDAO.delete(id);
    }
    public ObjectId getID(Seance seance){
        return seanceDAO.getID(seance);

    }
    public Seance getSeanceByID(ObjectId id){
        return seanceDAO.getSeanceById(id);
    }
    public List<Seance> getAllSeance(){
        return seanceDAO.getAll();
    }
}
