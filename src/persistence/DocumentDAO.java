package persistence;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import POJO.Doc;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DocumentDAO implements DAO<Doc>{
    MongoDatabase database = Connexion.getDatabase();
    MongoCollection<Document> docCollection = database.getCollection("documents");
    @Override
    public void create(Doc doc) {
        Document document = new Document("description", doc.getDescription())
                .append("dateAjout", doc.getDateAjout());
        docCollection.insertOne(document);
    }
    @Override
    public Doc read(ObjectId docID) {
        Document filter = new Document("_id", docID);
        Document result = docCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            Date dateAjout = result.getDate("dateAjout");
            return new Doc(docID,description, dateAjout);
        }
        return null;
    }

    @Override
    public void update(Doc updateDoc, ObjectId id) {
        Document updatedDocument = new Document("description", updateDoc.getDescription())
                .append("dateAjout", updateDoc.getDateAjout());
        docCollection.replaceOne(Filters.eq("_id", id), updatedDocument);
    }

    @Override
    public void delete(ObjectId id) {
        docCollection.deleteOne(Filters.eq("_id", id));
    }
    public List<Doc> getAll() {
        List<Doc> docs = new ArrayList<>();
        for (Document document : docCollection.find()) {
            ObjectId objectId =document.getObjectId("_id");
            String description = document.getString("description");
            Date dateAjout = document.getDate("dateAjout");
            Doc doc = new Doc(objectId,description, dateAjout);
            docs.add(doc);
        }

        return docs;
    }
    public List<Doc> chercherParDescription(String description){
        List<Doc> docs = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Doc doc : getAll()) {
            if (keyword.matcher(doc.getDescription()).find()) {
                docs.add(doc);
            }
        }
        return docs;
    }
    public ObjectId getID(Doc doc){
        return doc.getID();
    }
    public Doc getDocumentById(ObjectId id) {
        Document filter = new Document("_id", id);
        Document result = docCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            Date dateAjout = result.getDate("dateAjout");
            return new Doc(id,description, dateAjout);
        }
        return null;
    }


}

