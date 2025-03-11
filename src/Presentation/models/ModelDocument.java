package Presentation.models;

import Gestion.DocumentGestion;
import POJO.Doc;
import org.bson.types.ObjectId;

import java.util.List;

public class ModelDocument {
    private DocumentGestion document;

    public ModelDocument() {
        this.document = new DocumentGestion();
    }

    public List<Doc> readDocuments() {
        List<Doc> docs = document.getAllDocument();
        for (Doc doc : docs) {
            ObjectId objectId = document.getID(doc);
            doc.setID(objectId);
        }
        return docs;
    }


}
