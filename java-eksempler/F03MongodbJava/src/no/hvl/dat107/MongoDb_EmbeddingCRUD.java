package no.hvl.dat107;

import java.time.LocalDate;

import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import no.hvl.dat107.config.MongoClientFactory;
import no.hvl.dat107.model.Innlegg;
import no.hvl.dat107.model.Kommentar;
import no.hvl.dat107.repository.InnleggRepository;

public class MongoDb_EmbeddingCRUD {

	public static void main(String[] args) {
    	
		String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        
    	MongoClient client = MongoClientFactory.getMongoClient( "mongodb://localhost:27017");
    	InnleggRepository repoInnlegg = new InnleggRepository(client, databaseName);
        			
        // ---------------------------------------------
        // Create
        // ---------------------------------------------
        Innlegg nyttInnlegg = new Innlegg()
        		.setTittel("Et single embedded innlegg")
        		.setInnhold("Mye tekst, men lite innhold. Eller omvendt.")
        		.setOpprettetDato(LocalDate.now());
        
        repoInnlegg.create(nyttInnlegg);
        System.out.println("NyttInnlegg - ID: " + nyttInnlegg.getId());
        
        //System.exit(0);
        
        // ---------------------------------------------
        // Read 1 ObjectId
        // ---------------------------------------------
        Innlegg nyttInnleggIgjen = repoInnlegg.getInnleggById(nyttInnlegg.getId());
        heading("Hentet siste innlegg");
        System.out.println(nyttInnleggIgjen);
        
        //System.exit(0);
        // ---------------------------------------------
        // Read 2 Pattern
        // ---------------------------------------------
        List<Innlegg> listInnleggPost = repoInnlegg.read(Filters.regex("tittel","^Post"));
        heading("Hentet innlegg som starter på 'Post' i tittel");
        System.out.println("Fant antall: " + listInnleggPost.size());
        
        //System.exit(0);
        // ---------------------------------------------
        // Read 3 MongoCursor
        // ---------------------------------------------
        FindIterable<Innlegg> listInnleggBla = repoInnlegg.readAll();
        heading("Henter alle innlegg ");
		
        MongoCursor<Innlegg> cursor = listInnleggBla.cursor();
        
        while (cursor.hasNext()) {
        	System.out.println(cursor.next());
        }
        
        //System.exit(0);
        // ---------------------------------------------
        // Update nyttInnlegg
        // ---------------------------------------------
        heading("Oppdaterer ID: " + nyttInnlegg.getId());
        nyttInnlegg
        	.setTittel("En ny tittel")
        	.setInnhold(nyttInnlegg.getInnhold() + "Legger til litt tekst her.");
        
        repoInnlegg.update(nyttInnlegg);
        
        System.out.println("->" + nyttInnlegg);
        
        //System.exit(0);
        // ---------------------------------------------
        // Legg til ny kommentar
        // ---------------------------------------------
        heading("Legg til kommentar for ID: " + nyttInnlegg.getId());
        Kommentar nyKommentar = new Kommentar("En ny kommentar", LocalDate.now());
        
        Innlegg innleggMedKommentar = repoInnlegg.leggTilKommentar(nyttInnlegg.getId(), nyKommentar);

        System.out.println("-> " + innleggMedKommentar);
        
        //System.exit(0);
        // ---------------------------------------------
        // DELETE - Sletter innlegget
        // ---------------------------------------------
        heading("Sletter innlegg med ID: " + nyttInnlegg.getId());
        repoInnlegg.delete(nyttInnlegg.getId());
       
        
        System.exit(0);
        // ---------------------------------------------
        // Clean
        // ---------------------------------------------
        repoInnlegg.clean();
        heading("Slettet alle dokumenter i 'innlegg' samling");
        
   	}

	private static void heading(String tekst) {
		System.out.println("");
		System.out.println(tekst);
		System.out.println("--------------------------------------------");
	}

}
