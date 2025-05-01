package no.hvl.dat107;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import no.hvl.dat107.model.Innlegg;

import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.regex;

public class MongoDb_Innlegg {

	public static void main(String[] args) {
    	
		// -------------------------------------
		// Codec
		// -------------------------------------
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(
        		getDefaultCodecRegistry(), 
        		fromProviders(pojoCodecProvider)
        );
        
        String uri = "mongodb://localhost:27017/";
        String databaseName = "forelesning-04";
        
        MongoClient mongoClient = MongoClients.create(uri);
        
        MongoDatabase database = mongoClient.getDatabase(databaseName)
        		.withCodecRegistry(pojoCodecRegistry);
        
        MongoCollection<Innlegg> collInnlegg = 
        		database.getCollection("innlegg", Innlegg.class);
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // CLEAN
        // ---------------------------------------------
        collInnlegg.drop();
        
        //System.exit(0);
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // InsertMany
        // ---------------------------------------------
        overskrift("Eksempel: insertMany() med Objekter ...");
        List<Innlegg> listInnlegg = new ArrayList<Innlegg>();
		
        listInnlegg.add(new Innlegg().setTittel("Post 6").setInnhold("Bla 6, bla, bla ..."));
        listInnlegg.add(new Innlegg().setTittel("Post 7").setInnhold("Bla 7, bla, bla ..."));
        listInnlegg.add(new Innlegg().setTittel("Blog 8").setInnhold("Bla 8, bla, bla ...")
        		.setCreatedDato(LocalDate.now()));
		
        collInnlegg.insertMany(listInnlegg);
        
        System.out.println(listInnlegg);
        
        System.exit(0);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Query 1 - eq
        // ---------------------------------------------
        overskrift("Eksempel: find tittel 'Blog 8' ...");
        Innlegg i = collInnlegg.find(eq("tittel", "Blog 8")).first();
        
        if (i != null) {
            System.out.println(i.toString());
        } else {
            System.out.println("No matching documents found.");
        }
        
        System.exit(0);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Query 2 - Pattern
        // ---------------------------------------------
        overskrift("Eksempel: find med Pattern /^blog/i ...");
        List<Innlegg> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("^"+Pattern.quote("blog"), Pattern.CASE_INSENSITIVE);
        collInnlegg.find(regex("tittel", pattern)).into(list);
        
        System.out.println("Found: " + list.size());
        for (Innlegg inn:list) {
        	System.out.println(inn);
        }

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void overskrift(String string) {
		System.out.println("");
		System.out.println(string + ":");
		System.out.println("-------------------------------------");
	}


}
