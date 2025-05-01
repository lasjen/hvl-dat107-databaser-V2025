package no.hvl.dat107;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main1_listDatabases {
	
	public static void main(String[] args) throws Exception {
		String databaseName = "DEMO04";
		String uri = "mongodb://localhost:27017";
		//String uriAtlas = "mongodb+srv://lasse:lasse1234@cluster0.jtsnfdr.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		MongoClient mongoClient = MongoClients.create(uri); // uri?
        	
		// ---------------------------------------------
        // List databases (before created new database)
        // ---------------------------------------------
		MongoCursor<String> dbCursor = mongoClient.listDatabaseNames().iterator();
        
		overskrift("Databases (before)");
        while (dbCursor.hasNext()) {
        	System.out.println(dbCursor.next());
        }
        
        //System.exit(0);
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Connect to database and list collections
        // ---------------------------------------------
        MongoDatabase database = mongoClient.getDatabase("forelesning-02");
        dbCursor = database.listCollectionNames().iterator();
        
        overskrift("Collections in 'forelesning-02': ");
        while (dbCursor.hasNext()) {
        	System.out.println(dbCursor.next());
        }
        
        //System.exit(0);
       
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Create new database, collection and document
        // ---------------------------------------------
        database = mongoClient.getDatabase(databaseName); // use DEMO4
        
        //database.createCollection("demo");
        database.getCollection("demo").insertOne(new Document("name","Lasse"));
        
        
        System.exit(0);
        
        
        
        
        
        
        
        
        
        // --------------------------------------------- 
        // List databases (after created new database)
        // ---------------------------------------------
        dbCursor = mongoClient.listDatabaseNames().iterator();
        
        overskrift("Databases (after)");
        while (dbCursor.hasNext()) {
        	System.out.println(dbCursor.next());
        }
       
        System.exit(0);
        
        
        
        
        
        
        
        
        
        
        
        
        // ---------------------------------------------
        // Clean
        // ---------------------------------------------
        database.drop();
        info("Database '" + databaseName + "' dropped!");
	}

	
	
	
	
	
	
	
	
	
	
	
	
	private static void info(String string) {
		System.out.println("");
		System.out.println(string);
	}

	private static void overskrift(String string) {
		System.out.println("");
		System.out.println(string + ":");
		System.out.println("-------------------------------------");
	}
}
