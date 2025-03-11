package POJO;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Seance {
    private ObjectId ID;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String note;
    private List<Doc> docs;
    public Seance() {
    }
    public Seance(ObjectId ID,String description, Date dateDebut, Date dateFin, String note,List<Doc> docs) {
        super();
        this.ID=ID;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.note = note;
        this.docs = docs;
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
    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    public Date getDateFin() {
        return dateFin;
    }
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public List<Doc> getDoc() {
        return docs;
    }
    public void setDoc(List<Doc> docs) {
        this.docs = docs;
    }
}

