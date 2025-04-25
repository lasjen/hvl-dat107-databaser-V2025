package no.hvl.dat107;

import java.time.LocalDate;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

import no.hvl.dat107.config.MongoClientFactory;
import no.hvl.dat107.model.*;

public class MongoDb_Linking {
    public static void main( String[] args ) throws Exception {
    	
    	String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        
    	MongoClient client = MongoClientFactory.getMongoClient(uri);
        MongoDatabase database = client.getDatabase(databaseName);
        
        MongoCollection<Innlegg> collInnlegg = database.getCollection("innlegg", Innlegg.class);
        MongoCollection<Kommentar> collKommentar = database.getCollection("kommentar", Kommentar.class);
        
        // ---------------------------------------------
        // Sletter alt i "Innlegg" og "Kommentar"
        // ---------------------------------------------
        collInnlegg.drop(); 
        collKommentar.drop();
        info("Collections Cleaned - ready to start!");
        
        System.exit(0);
        // ---------------------------------------------
        // CREATE - Innlegg
        // ---------------------------------------------
        overskrift("Insert innlegg ...");
        Innlegg innlegg1 = new Innlegg()
				.setTittel("Et ensomt innlegg")
				.setInnhold("Som satt helt alene i samlingen.")
				.setOpprettetDato(LocalDate.now());
        collInnlegg.insertOne(innlegg1);
        
        System.exit(0);
        // ---------------------------------------------
        // CREATE - Kommentar (del 1)
        // ---------------------------------------------
        overskrift("Legg til en kommentar");
        Kommentar kommentar1 = new Kommentar("En ensom kommentar", LocalDate.now());
        collKommentar.insertOne(kommentar1);
        
        System.exit(0);
        // ---------------------------------------------
        // CREATE - Kommenter (del 2 - UPDATE)
        // ---------------------------------------------
        Bson filterInnleggId = Filters.eq("_id", innlegg1.getId());
        Bson updateKommentar = Updates.push("kommentarer", kommentar1.getId());
        
        FindOneAndUpdateOptions etterUpdateOpsjon = 
        		new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        
        Innlegg oppdatertInnlegg = collInnlegg.findOneAndUpdate(
        		filterInnleggId, 
        		updateKommentar, 
        		etterUpdateOpsjon
        );
        
        System.out.println(oppdatertInnlegg);
       
    }

	private static void info(String string) {
		System.out.println("");
		System.out.println("INFO: " + string);
		System.out.println("");
	}

	private static void overskrift(String string) {
		System.out.println("");
		System.out.println(string + ":");
		System.out.println("-------------------------------------");
	}
}
