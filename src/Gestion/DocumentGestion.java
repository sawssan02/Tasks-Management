package Gestion;

import POJO.Doc;
import org.bson.types.ObjectId;
import persistence.DocumentDAO;

import java.util.List;

public class DocumentGestion {
    private DocumentDAO documentDAO;

    public DocumentGestion() {
        this.documentDAO = new DocumentDAO();
    }
    public void createDocument(Doc projet) {
        documentDAO.create(projet);
    }
    public Doc readDocument(ObjectId id) {
        return documentDAO.read(id);
    }
    public void updateDocument(Doc updatedDocument, ObjectId id) {
        documentDAO.update(updatedDocument,id);
    }
    public void deleteDocument(ObjectId id) {
        documentDAO.delete(id);
    }
    public List<Doc> getAllDocument(){
        return documentDAO.getAll();
    }
    public List<Doc> searchByDescription(String description){
        return documentDAO.chercherParDescription(description);
    }
    public ObjectId getID(Doc doc){
        return documentDAO.getID(doc);
    }
    public Doc getDocumentByID(ObjectId id){
        return documentDAO.getDocumentById(id);
    }

}
