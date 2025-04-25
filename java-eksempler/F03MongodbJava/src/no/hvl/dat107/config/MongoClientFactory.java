package no.hvl.dat107.config;

import org.bson.codecs.configuration.CodecRegistry;		// bson
import org.bson.codecs.pojo.PojoCodecProvider;			// bson
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;  // bson
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries; // bson

import com.mongodb.ConnectionString;			// mongodb-driver-core
import com.mongodb.MongoClientSettings;			// mongodb-driver-core
import com.mongodb.client.MongoClient;			// mongodb-driver-sync
import com.mongodb.client.MongoClients;			// mongodb-driver-sync

public class MongoClientFactory {
	public static MongoClient getMongoClient(String uri) {
		ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        
        return MongoClients.create(clientSettings);
	}
}
