package no.hvl.dat107;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Projections.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.BsonObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

public class MongoDb_Document {
	
    public static void main( String[] args ) throws Exception {
    	
    	String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        
    	MongoClient mongoClient = MongoClients.create(uri);  			// Throws Exception (if fails) 
    	MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collInnlegg = database.getCollection("innlegg");
       
      
        // ---------------------------------------------
        // CLEAN
        // ---------------------------------------------
        collInnlegg.drop();    
        
        System.exit(0);
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // InsertOne
        // ---------------------------------------------
        overskrift("Eksempel: InsertOne()");
        ObjectId nr1Id = ObjectId.get();
		info("Generert ID for post 1: " + nr1Id);
		
        Document nr1Innlegg = 
        	new Document().append("_id", nr1Id)
				 .append("tittel", "Post 1")
                 .append("innhold", "Min aller første blog")
                 .append("created", LocalDate.of(2025, 4, 18).toString());
        
        InsertOneResult resultat1 = collInnlegg.insertOne(nr1Innlegg);
        
        info(resultat1.toString());
        info("ID from result1 : " + 
           ((BsonObjectId) resultat1.getInsertedId()).getValue().toHexString());
      
        // Men vi kan også hente Id fra Document
        info("ID from document: " + nr1Innlegg.getObjectId("_id"));
        
	    System.exit(0);
        
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    // ---------------------------------------------
        // InsertMany
        // ---------------------------------------------
        overskrift("Eksempel: InsertMany()");
        List<Document> dokumenter = new ArrayList<Document>();
		
        dokumenter.add(new Document().append("tittel", "Post 2").append("innhold", "Bla 2, bla, bla ..."));
        dokumenter.add(new Document().append("tittel", "Post 3").append("innhold", "Bla 3, bla, bla ..."));
        dokumenter.add(new Document().append("tittel", "Post 4").append("innhold", "Bla 4, bla, bla ..."));
        dokumenter.add(new Document().append("tittel", "Blog 5")
				                    .append("innhold", "Siste post i rekken av mange ...")
				                    .append("created", LocalDate.of(2025, 4, 19).toString()));
        
        InsertManyResult resultat2 = collInnlegg.insertMany(dokumenter);
        
        info(resultat2.toString());
        info("IDs from result  : " + resultat2.getInsertedIds());
        System.out.println("IDs from document: ");
        int i=0;
        for (Document d: dokumenter) {
        	System.out.println(i++ + ": " + d.get("_id"));
        }
        System.out.println();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Query 1
        // ---------------------------------------------
        overskrift("Eksempel: Filters.eq('_id',...)");
        
    	Document dokument = collInnlegg.find(eq("_id", nr1Id)).first();
    		
        if (dokument != null) {
            System.out.println(dokument.toJson());
        } else {
        	System.out.println("No matching documents found.");
        }
        
        System.exit(0);
        
        
        
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Query 2
        // ---------------------------------------------
        overskrift("Eksempel: Iterable - Finn alle titler som starter på 'Blog' ...");
        
        // Tilsvarer: db.innlegg.find({tittel:"/^blog/i"}, {_id:0, tittel:1});
        Pattern pattern = Pattern.compile("^"+Pattern.quote("blog"), Pattern.CASE_INSENSITIVE);
        Bson filter = regex("tittel", pattern);
        Bson felt 	= fields(include("tittel"), excludeId());
    	
        Iterable<Document> docs = collInnlegg.find()
        		.projection(felt)
        		.filter(filter);
    	
    	for (Document d:docs) {
    		System.out.println(d.toJson());
    	}
    		
    }
	
    
    
    
    
    
    
    
    
    
    
    
    
    
	private static void overskrift(String string) {
		System.out.println("");
		System.out.println(string + ":");
		System.out.println("-------------------------------------");
	}
	
	private static void info(String string) {
		
		System.out.println(string);
		System.out.println("");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
