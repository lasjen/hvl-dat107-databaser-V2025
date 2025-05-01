package no.hvl.dat107;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;

import no.hvl.dat107.config.MongoClientFactory;
import no.hvl.dat107.model.*;

public class MongoDb_Embedding {
    public static void main( String[] args ) throws Exception {
    	
    	String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        
    	MongoClient client = MongoClientFactory.getMongoClient(uri);
        MongoDatabase database = client.getDatabase(databaseName);
        
        MongoCollection<Innlegg> collInnlegg = database.getCollection("innlegg", Innlegg.class);
        
        // ---------------------------------------------
        // Sletter alt i "Innlegg" og "Kommentar"
        // ---------------------------------------------
        collInnlegg.drop(); 
        info("Collections Cleaned - ready to start!");
        
        //System.exit(0);
        // ---------------------------------------------
        // InsertMany - TestData
        // ---------------------------------------------
        overskrift("Insert Innlegg med 'Embedded' kommentarer ...");
        List<Innlegg> alleInnlegg = lagListeMedInnlegg();
        
        InsertManyResult manyInnleggResult = collInnlegg.insertMany(alleInnlegg);
        BsonObjectId id = visResultatOgFinnNr1ObjectId(manyInnleggResult);
        
    }

	private static List<Innlegg> lagListeMedInnlegg() {
		List<Innlegg> innlegg = new ArrayList<Innlegg>();
		
		innlegg.add(
			new Innlegg()
				.setTittel("Post 10")
				.setInnhold("Bla 10, bla, bla ...")
				.setOpprettetDato(LocalDateTime.now())
				.setKommentarer(List.of(
						new Kommentar().setTekst("Helt konge.").setOpprettetDato(LocalDate.now()),
						new Kommentar().setTekst("Kunne ha vært litt mer innhold.").setOpprettetDato(LocalDate.of(2024,12,25)),
						new Kommentar().setTekst("Kunne ha vært litt mindre innhold.").setOpprettetDato(LocalDate.of(2025, 1, 2)))
					)
		);
		innlegg.add(
				new Innlegg()
					.setTittel("Post 11")
					.setInnhold("Bla 11, bla, bla ...")
					.setOpprettetDato(LocalDateTime.now())
					.setKommentarer(List.of(
							new Kommentar().setTekst("Det innlegget var bra.").setOpprettetDato(LocalDate.of(2023,2,2)),
							new Kommentar().setTekst("Beste jeg har lest.").setOpprettetDato(LocalDate.of(2024,7,9)))
					)
			);
		return innlegg;
	}
	
	private static BsonObjectId visResultatOgFinnNr1ObjectId(InsertManyResult manyResult) {
		System.out.println(manyResult);
        System.out.println("IDs from result  : " + manyResult.getInsertedIds());
        System.out.println();
		return (BsonObjectId) manyResult.getInsertedIds().get(0);
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
