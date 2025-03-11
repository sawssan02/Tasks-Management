package POJO;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Tache {
    private ObjectId ID;
    private String categorie;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private List<Doc> docs;
    public Tache() {
    }
    public Tache(ObjectId ID,String categorie, String description, Date dateDebut, Date dateFin,
                 List<Doc> docs) {
        super();
        this.ID=ID;
        this.categorie = categorie;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.docs = docs;
    }
    public ObjectId getID() {
        return ID;
    }
    public void setID(ObjectId iD) {
        ID = iD;
    }
    public String getCategorie() {
        return categorie;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    public Date getDateFin() {
        return dateFin;
    }
    public void setDateFin(Date deateFin) {
        this.dateFin = deateFin;
    }
    public List<Doc> getDoc() {
        return docs;
    }
    public void setDoc(List<Doc> docs) {
        this.docs = docs;
    }
}
