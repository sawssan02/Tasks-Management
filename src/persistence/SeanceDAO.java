package persistence;

import POJO.Doc;
import POJO.Seance;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
public class SeanceDAO implements DAO<Seance>{
    MongoDatabase database = Connexion.getDatabase();
    MongoCollection<Document> seanceCollection = database.getCollection("seances");

    @Override
    public void create(Seance seance) {
        List<Document> docs = new ArrayList<>();
        if (seance.getDoc() != null) {
            for (Doc doc : seance.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        Document document = new Document("description", seance.getDescription())
                .append("dateDebut", seance.getDateDebut())
                .append("dateFin", seance.getDateFin())
                .append("note", seance.getNote())
                .append("docs", docs);

        seanceCollection.insertOne(document);
    }

    @Override
    public Seance read(ObjectId seanceID) {
        Document filter = new Document("_id", seanceID);
        Document result = seanceCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            Date dateDebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            String note = result.getString("note");
            List<Doc> docs = new ArrayList<>();
            List<Document> docDocs = (List<Document>) result.get("docs");
            if (docDocs != null) {
                for (Document docDoc : docDocs) {
                    String docDescription = docDoc.getString("description");
                    Date docDateAjout = docDoc.getDate("dateAjout");
                    docs.add(new Doc(seanceID, docDescription, docDateAjout));
                }
            }
            return new Seance( seanceID,description ,dateDebut, dateFin,note, docs);
        }
        return null;
    }

    @Override
    public void update(Seance updateSeance, ObjectId id) {
        Document document = new Document("description", updateSeance.getDescription())
                .append("dateDebut", updateSeance.getDateDebut())
                .append("dateFin", updateSeance.getDateFin())
                .append("note",updateSeance.getNote());
        List<Document> docs = new ArrayList<>();
        if (updateSeance.getDoc() != null) {
            for (Doc doc : updateSeance.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        document.append("docs", docs);
        seanceCollection.replaceOne(Filters.eq("_id", id), document);
    }

    @Override
    public void delete(ObjectId id) {
        seanceCollection.deleteOne(new Document("_id", id));
    }
    public  List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        try (MongoCursor<Document> cursor = seanceCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                Date datedebut = result.getDate("dateDebut");
                Date dateFin = result.getDate("dateFin");
                String note = result.getString("note");
                List<Document> docs = (List<Document>) result.get("docs");
                List<Doc> documents = new ArrayList<>();
                if (docs != null) {
                    for (Document doc : docs) {
                        ObjectId docId = doc.getObjectId("_id");
                        String docDescription = doc.getString("description");
                        Date date = doc.getDate("dateAjout");
                        documents.add(new Doc(docId, docDescription, date));
                    }
                }
                Seance seance = new Seance(id, description, datedebut, dateFin, note, documents);
                seances.add(seance);
            }
            return seances;
        }
    }


    public  ObjectId getID(Seance seance){
        return seance.getID();
    }
    public Seance getSeanceById(ObjectId id) {
        Document filter = new Document("_id", id);
        Document result = seanceCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            Date datedebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            String note = result.getString("note");
            List<Document> docs = (List<Document>) result.get("docs");
            List<Doc> documents = new ArrayList<>();
            if (docs != null) {
                for (Document doc : docs) {
                    ObjectId docId = doc.getObjectId("_id");
                    String docDescription = doc.getString("description");
                    Date date = doc.getDate("dateAjout");
                    documents.add(new Doc(docId, docDescription, date));
                }
            }
            return new Seance(id, description,datedebut, dateFin,note ,documents);
        } else {
            System.out.println("Tâche non trouvée pour l'ID: " + id);
            return null;
        }
    }

}