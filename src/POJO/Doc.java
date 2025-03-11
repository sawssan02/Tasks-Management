package POJO;

import java.util.Date;
import org.bson.types.ObjectId;
public class Doc {
    private ObjectId objectId;
    private String description;
    private Date dateAjout;
    public Doc() {
        super();
    }
    public Doc(ObjectId objectId,String description, Date dateAjout) {
        super();
        this.objectId = objectId;
        this.description = description;
        this.dateAjout = dateAjout;
    }
    public Doc(String description, Date dateAjout) {
        this.description = description;
        this.dateAjout = dateAjout;
    }
    public ObjectId getID() {
        return objectId;
    }
    public void setID(ObjectId iD) {
        objectId = iD;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDateAjout() {
        return dateAjout;
    }
    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }
}
