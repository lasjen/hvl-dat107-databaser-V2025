package no.hvl.dat107.repository;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

import no.hvl.dat107.model.Innlegg;
import no.hvl.dat107.model.Kommentar;

public class InnleggRepository {
	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Innlegg> collInnlegg;

	
	public InnleggRepository(MongoClient client, String dbName) {
		super();
		this.client = client;
		this.db = client.getDatabase(dbName);
		this.collInnlegg = db.getCollection("innlegg", Innlegg.class);
	}
	
	// ---------------------------------------------
    // CREATE
    // ---------------------------------------------
	public void create(Innlegg innlegg) {
		collInnlegg.insertOne(innlegg);
	}
	
	// ---------------------------------------------
    // READ(s)
    // ---------------------------------------------
	public List<Innlegg> read(Bson filter) {
		FindIterable<Innlegg> funnetInnlegg = collInnlegg.find(filter);
		List<Innlegg> returnInnlegg = new ArrayList<Innlegg>();
		
		//for (Innlegg i:funnetInnlegg) {
		//	returnInnlegg.add(i);
		//}
		funnetInnlegg.into(returnInnlegg);
		
		return returnInnlegg;
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
			oppdateringer						    // Updates
			, options);								// Options
	}
	
	public Innlegg replace(Innlegg innlegg) {
		Bson filterByGradeId = eq("_id", innlegg.getId());
        FindOneAndReplaceOptions returnDocAfterReplace = 
        		new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER); //.upsert(true);
        Innlegg updatedInnlegg = collInnlegg.findOneAndReplace(filterByGradeId, innlegg, returnDocAfterReplace);
		
        return updatedInnlegg;
	}
	
	public Innlegg leggTilKommentar(ObjectId id, Kommentar nyKommentar) {
		Bson filterById = Filters.eq("_id", id);
		Bson oppdateringer = Updates.push("kommentarer", nyKommentar);
		
		FindOneAndUpdateOptions returnDocAfterReplace =
				new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
		
		return collInnlegg.findOneAndUpdate(filterById, oppdateringer, returnDocAfterReplace);
	}
	
	public Innlegg fjerneKommentar(ObjectId id, Kommentar nyKommentar) {
		Bson filterById = Filters.eq("_id", id);
		Bson oppdateringer = Updates.pull("kommentarer", nyKommentar);
		
		FindOneAndUpdateOptions returnDocAfterReplace =
				new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
		
		return collInnlegg.findOneAndUpdate(filterById, oppdateringer, returnDocAfterReplace);
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
	}

}
