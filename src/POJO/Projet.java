package POJO;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Projet {
    private ObjectId ID;
    private String categorie;
    private String type;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private List<Seance> seances;
    private List<Doc> docs;
    private List<Tache> taches;
    public Projet(ObjectId ID,String categorie, String type, String description, Date dateDebut, Date deateFin,
                  List<Seance> seances, List<Doc> docs, List<Tache> taches) {
        super();
        this.ID=ID;
        this.categorie = categorie;
        this.type = type;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = deateFin;
        this.seances = seances;
        this.docs = docs;
        this.taches = taches;
    }
    public Projet() {
        super();
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public List<Seance> getSeances() {
        return seances;
    }
    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }
    public void setDoc(List<Doc> docs) {
        this.docs = docs;
    }

    public List<Tache> getTaches() {
        return taches;
    }
    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    public List<Doc> getDoc() {
        return docs;
    }
}
