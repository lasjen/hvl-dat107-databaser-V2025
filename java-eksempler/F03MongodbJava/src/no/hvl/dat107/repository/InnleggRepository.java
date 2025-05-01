package no.hvl.dat107.repository;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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
import com.mongodb.client.result.InsertManyResult;

import no.hvl.dat107.model.Innlegg;
import no.hvl.dat107.model.Kommentar;

import java.util.regex.Pattern;

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
		if (innlegg.getOpprettetDato()==null) {
			innlegg.setOpprettetDato(LocalDateTime.now());
		}
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
	
	public List<Innlegg> readAll2() {
		List<Innlegg> alleInnlegg = new ArrayList<Innlegg>();
		collInnlegg.find().into(alleInnlegg);
		return alleInnlegg;
	}
	
	public Innlegg readById(ObjectId id) {
		Bson filter = Filters.eq("_id", id);
		return collInnlegg.find(filter).first();
	}
	
	public List<Innlegg> readByTittelContains(String searchText) { 
		List<Innlegg> alleInnlegg = new ArrayList<Innlegg>();
		
		Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
		Bson filter = Filters.regex("tittel", pattern);
		
		collInnlegg.find(filter).into(alleInnlegg);
		
		return alleInnlegg;
	}
	
	public List<Innlegg> readByTittelStartWith(String searchText) { 
		List<Innlegg> alleInnlegg = new ArrayList<Innlegg>();
		
		Pattern pattern = Pattern.compile("^" + Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
		Bson filter = Filters.regex("tittel", pattern);
		
		collInnlegg.find(filter).into(alleInnlegg);
		
		return alleInnlegg;
	}
	
	// ---------------------------------------------
    // UPDATEs
    // ---------------------------------------------
	public void update(Innlegg innlegg) {
		Bson filter = Filters.eq("_id", innlegg.getId());
		
		Bson oppdateringer = Updates.combine(
				Updates.set("tittel", innlegg.getTittel()),	
				Updates.set("innhold", innlegg.getInnhold()),
				Updates.set("oppdatert", LocalDateTime.now()),              
				Updates.setOnInsert("opprettet", LocalDateTime.now())
				);
		
		collInnlegg.updateOne(filter, oppdateringer);
		
	}
	
	public Innlegg updateAndReturn(Innlegg innlegg) {
		Bson filter = Filters.eq("_id", innlegg.getId());
		FindOneAndUpdateOptions options = 
				new FindOneAndUpdateOptions()
				    .returnDocument(ReturnDocument.AFTER)
					.upsert(true);
		
		Bson oppdateringer = Updates.combine(
				Updates.set("tittel", innlegg.getTittel()),					// set - setOnInsert
				Updates.set("innhold", innlegg.getInnhold()),
				Updates.set("oppdatert", LocalDateTime.now()),              
				Updates.setOnInsert("opprettet", LocalDateTime.now())
				);
		
		return collInnlegg.findOneAndUpdate(
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

	// ---------------------------------------------
    // ADMIN
    // ---------------------------------------------
	public void init() {
		List<Innlegg> alleInnlegg = new ArrayList<Innlegg>();
		
		alleInnlegg.add(new Innlegg().setTittel("Post 2").setInnhold("Bla 2, bla, bla ..."));
		alleInnlegg.add(new Innlegg().setTittel("Post 3").setInnhold("Bla 3, bla, bla ..."));
		alleInnlegg.add(new Innlegg().setTittel("Post 4").setInnhold("Bla 4, bla, bla ..."));
		alleInnlegg.add(new Innlegg().setTittel("Blog 5")
				                    .setInnhold("Siste post i rekken av mange ...")
				                    .setOpprettetDato(LocalDateTime.of(2025, 4, 19, 22, 02, 01)));
        
        collInnlegg.insertMany(alleInnlegg);
	}
}
