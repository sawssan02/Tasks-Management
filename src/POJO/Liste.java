package POJO;

import org.bson.types.ObjectId;

import java.util.List;

public class Liste {
    private ObjectId ID;
    private String description;
    private List<Tache> taches;
    public Liste() {
        super();
    }
    public Liste(ObjectId ID,String description, List<Tache> taches) {
        super();
        this.ID=ID;
        this.description = description;
        this.taches = taches;
    }
    public ObjectId getID() {
        return ID;
    }
    public void setID(ObjectId iD) {
        ID = iD;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Tache> getTaches() {
        return taches;
    }
    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }
}
