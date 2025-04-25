package no.hvl.dat107.repository;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import no.hvl.dat107.model.Innlegg;
import no.hvl.dat107.model.Kommentar;

public class InnleggRepository {
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Innlegg> collInnlegg;
	private MongoCollection<Kommentar> collKommentar;

	
	public InnleggRepository(MongoClient client, String dbName) {
		super();
		this.client = client;
		this.db = client.getDatabase(dbName);
		this.collInnlegg = db.getCollection("innlegg", Innlegg.class);
		this.collKommentar = db.getCollection("kommentar", Kommentar.class);
	}
	
	// ---------------------------------------------
    // CREATE
    // ---------------------------------------------
	public void nyttInnlegg(Innlegg innlegg) {
		collInnlegg.insertOne(innlegg);
		// NB! Finnes jo ikke kommentarer enda, så her trenger vi ikke å gjøre noe mht kommentarer
	}
	
	// ---------------------------------------------
    // CREATE + UPDATE
    // ---------------------------------------------
	public void nyKommentar(Innlegg innlegg, Kommentar nyKommentar) {
		
		// Først sett inn kommentar samling (for å generere ID)
		BsonValue kommentarId = collKommentar.insertOne(nyKommentar).getInsertedId();
		
		// DA kan vi oppdatere innlegg
		innlegg.addKommentar(nyKommentar.getId());
				
		// Så sett inn kommentarId i Innlegg samling
		Bson filterId = Filters.eq("_id", innlegg.getId());
		Bson update = Updates.push("kommentarer",kommentarId);
		
		// Alternativt kunne vi gjort findOneAndReplace
		collInnlegg.updateOne(filterId, update);
		
		/*
		FindOneAndReplaceOptions returnDocAfterReplace = 
        		new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER); //.upsert(true);
        Innlegg updatedInnlegg = collInnlegg.findOneAndReplace(filterId, innlegg, returnDocAfterReplace);
		
        return updatedInnlegg;
        */
		
	}
	
	public Innlegg nyKommentar(ObjectId id, Kommentar nyKommentar) {
		// Samling: Kommentar
		collKommentar.insertOne(nyKommentar);
		
		// Samling: Innlegg
		Bson filterById = Filters.eq("_id", id);
		Bson oppdateringer = Updates.push("kommentarer", nyKommentar.getId());
		
		
		FindOneAndUpdateOptions returnDocAfterUpdate = 
        		new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER); //.upsert(true);
		
		return collInnlegg.findOneAndUpdate(filterById, oppdateringer, returnDocAfterUpdate);
	}
	
	// ---------------------------------------------
    // READ(s)
    // ---------------------------------------------
	public List<Innlegg> read(Bson filter) {
		FindIterable<Innlegg> funnetInnlegg = collInnlegg.find(filter);
		List<Innlegg> returnInnlegg = new ArrayList<Innlegg>();
		
		funnetInnlegg.into(returnInnlegg);
		
		return returnInnlegg;
	}
	
	public List<Kommentar> readKommentarerInnlegg(Innlegg innlegg) {
		
		List<ObjectId> listId = innlegg.getKommentarer();
		List<Kommentar> result = new ArrayList<>();
		
 		for (ObjectId i:listId) {
			Bson filterId = Filters.eq("_id",i);
			//collKommentar.find(filterId);
			result.add(collKommentar.find(filterId).first());
		}
		
		return result;
	}
	
	public FindIterable<Innlegg> readAll() {
		return collInnlegg.find();
	}
	
	public Innlegg getInnleggById(ObjectId id) {
		Bson filter = Filters.eq("_id", id);
		return collInnlegg.find(filter).first();
	}
	
	// ---------------------------------------------
    // UPDATEs
    // ---------------------------------------------
	public void update(Innlegg innlegg) {
		Bson filter = Filters.eq("_id", innlegg.getId());
		UpdateOptions options = new UpdateOptions().upsert(true);
		
		Bson oppdateringer = Updates.combine(
				Updates.set("tittel", innlegg.getTittel()),					// set - setOnInsert
				Updates.set("innhold", innlegg.getInnhold()),
				Updates.set("kommentarer", innlegg.getKommentarer()),
				Updates.setOnInsert("opprettet", LocalDate.now())
				);
		
		collInnlegg.updateOne(
			filter, 								// filter
			oppdateringer
			, options);
	}
	
	public Innlegg replace(Innlegg innlegg) {
		Bson filterByGradeId = eq("_id", innlegg.getId());
        FindOneAndReplaceOptions returnDocAfterReplace = 
        		new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER); //.upsert(true);
        Innlegg updatedInnlegg = collInnlegg.findOneAndReplace(filterByGradeId, innlegg, returnDocAfterReplace);
		
        return updatedInnlegg;
	}
	
	
	public Innlegg fjernKommentar(ObjectId innleggId, ObjectId kommentarId) {
		Bson filterKommentarById = Filters.eq("_id", kommentarId);
		
		long cnt = collKommentar.deleteOne(filterKommentarById).getDeletedCount();
		
		Bson filterInnleggById = Filters.eq("_id", innleggId);
		Bson deleteKommentar = Updates.pull("kommentarer", filterKommentarById);
		
		return collInnlegg.findOneAndUpdate(filterInnleggById, deleteKommentar);
	}
	
	// ---------------------------------------------
    // DELETE
    // ---------------------------------------------
	public Innlegg delete(ObjectId id) {
		return collInnlegg.findOneAndDelete(eq("_id", id));
	}
	
	
	// ---------------------------------------------
    // ADMIN
    // ---------------------------------------------
	public void clean() {
		collInnlegg.drop(); 
        collKommentar.drop();
	}

	

}
