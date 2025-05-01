package no.hvl.dat107;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import no.hvl.dat107.config.MongoClientFactory;
import no.hvl.dat107.model.Innlegg;
import no.hvl.dat107.model.Kommentar;
import no.hvl.dat107.repository.InnleggRepository;

public class MongoDb_EmbeddingCRUD {

	public static void main(String[] args) throws InterruptedException {
    	
		String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        Integer demoCounter = 1;
        
    	MongoClient client = MongoClientFactory.getMongoClient( "mongodb://localhost:27017");
    	InnleggRepository repoInnlegg = new InnleggRepository(client, databaseName);
        
    	repoInnlegg.clean();
    	repoInnlegg.init();
  
    	
        // ---------------------------------------------
        // DEMO 1: Create
        // ---------------------------------------------
    	heading(1, "Legger til et nytt innlegg (nyttInnlegg)", "create(Innlegg innlegg)");
    	
        Innlegg nyttInnlegg = new Innlegg()
        		.setTittel("Et single embedded innlegg")
        		.setInnhold("Mye tekst, men lite innhold. Eller omvendt.")
        		.setOpprettetDato(LocalDateTime.of(2025, 4, 25, 00, 00, 00))
        		;
        
        repoInnlegg.create(nyttInnlegg);
        System.out.println("NyttInnlegg - ID: " + nyttInnlegg.getId());
        
   
        // ---------------------------------------------
        // DEMO 2: Read By ObjectId
        // ---------------------------------------------
        heading(2, "Hentet siste innlegg", "readById(ObjectId id)");
        System.out.println(
        			repoInnlegg.readById(nyttInnlegg.getId())
        		);
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 3: Read 2 Pattern
        // ---------------------------------------------
        heading(3, "Hentet innlegg som starter på 'Post' i tittel" ,"read(Bson filter)");        
        
        List<Innlegg> listInnleggPost = repoInnlegg.read(Filters.regex("tittel","^Post"));
        System.out.println("Fant antall: " + listInnleggPost.size());
        
        for (Innlegg i:listInnleggPost) {
        	System.out.println(" - " + i.getTittel());
        }
        
        //System.exit(0);
              
        
        // ---------------------------------------------
        // DEMO 4: Read by Pattern (start-with)
        // ---------------------------------------------
        heading(4, "Hentet innlegg som starter med 'Post' i tittel", "readByTittelStartWith(String searchText)");
        
        listInnleggPost = repoInnlegg.readByTittelStartWith("post");
        System.out.println("Fant følgende innlegg:");
        
        for (Innlegg i:listInnleggPost) {
        	System.out.println(" - " + i.getTittel());
        }
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 5: Read By Tittel (contains)
        // ---------------------------------------------
        heading(5, "Hentet innlegg som inneholder 'ost' i tittel", "readByTittelContains(String searchText)");
        
        listInnleggPost = repoInnlegg.readByTittelContains("ost");
        System.out.println("Fant følgende innlegg:");
        
        for (Innlegg i:listInnleggPost) {
        	System.out.println(" - " + i.getTittel());
        }
        
        //System.exit(0);

        
        // ---------------------------------------------
        // DEMO 6: Read All ( MongoCursor)
        // ---------------------------------------------
        heading(6, "Henter alle innlegg ...", "readAll()");
        
        FindIterable<Innlegg> listInnleggBla = repoInnlegg.readAll();
		MongoCursor<Innlegg> cursor = listInnleggBla.cursor();
        
        // Kunne også gjort dette direkte (i stedet for to setninger over):
        //MongoCursor<Innlegg> cursor = repoInnlegg.readAll().cursor();
        
        while (cursor.hasNext()) {
        	System.out.println(cursor.next());
        }
        
        //System.exit(0);

        
        // ---------------------------------------------
        // DEMO 7: Update nyttInnlegg (fra tidligere)
        // ---------------------------------------------
        heading(7, "Oppdaterer ID: " + nyttInnlegg.getId(), "update(Innlegg innlegg)");
        
        nyttInnlegg
        	.setTittel("En ny tittel")
        	.setInnhold(nyttInnlegg.getInnhold() + "Legger til litt tekst her.");
        
        repoInnlegg.update(nyttInnlegg);
        
        System.out.println("objekt (sendt til oppdatering)    ->" + nyttInnlegg);
        
        Innlegg oppdatertInnlegg = repoInnlegg.readById(nyttInnlegg.getId());
        
        System.out.println("objekt (hentet etter oppdatering) ->" + oppdatertInnlegg);
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 8: Update uten UPSERT (Ghost - vil ikke bli lagret)
        // ---------------------------------------------
        heading(8, "Bruker update (upsert=false) på noe som ikke finnes:", "update(Innlegg innlegg)");
        
        ObjectId ghostId = new ObjectId();
        
        repoInnlegg.update(new Innlegg()
        		.setId(ghostId)
        		.setTittel("Spøkelses tittel")
        		.setInnhold("Denne vil aldri bli lagret."));
        
        Innlegg oneGhost = repoInnlegg.readById(ghostId);
        
        System.out.println("objekt (hentet etter update (upsert: false) -> " + oneGhost);
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 9: Update MED UPSERT (Finnes ikke fra før)
        // ---------------------------------------------
        heading(9, "Bruker update (upsert=true) på noe som ikke finnes: ", "updateAndReturn(Innlegg innlegg)");
        
        Innlegg sisteNytt = new Innlegg()
        		.setId(new ObjectId())
        		.setTittel("Helt nytt innlegg")
        		.setInnhold("Bla bla ukeblad");
        
        Innlegg sisteNyttRetur = repoInnlegg.updateAndReturn(sisteNytt);
        System.out.println(sisteNyttRetur);
        
        //System.exit(0);
                
        info("Sover i 5 sekunder (vent litt ...)");
		Thread.sleep(5000);
        
        // ---------------------------------------------
        // DEMO 10: Update nyttInnlegg (Finnes fra før)
        // ---------------------------------------------
        heading(10, "Gjør update på samme innlegg på nytt: ", "updateAndReturn(Innlegg innlegg)");
        
        
        sisteNyttRetur = repoInnlegg.updateAndReturn(sisteNytt);
        
        System.out.println(sisteNyttRetur);
        
        // SPørsmål: Hva blir endret? 
        
        //System.exit(0);
        
        
		// ---------------------------------------------
        // DEMO 11: Update nyttInnlegg (Finnes nå fra før) 
        //          - oppdatertDato har endret seg
        // ---------------------------------------------
        heading(11, "Kjører update på nytt: ", "updateAndReturn(Innlegg innlegg)");
        Innlegg sisteNytt2 = repoInnlegg.updateAndReturn(sisteNytt);
        System.out.println(sisteNytt2);
        
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 12: Legger til en kommentar til nyttInnlegg (Embedded)
        // ---------------------------------------------
        heading(12, "Legg til kommentar for ID: " + nyttInnlegg.getId(), "leggTilKommentar(ObjectId id, Kommentar kommentar)");
        Kommentar nyKommentar = new Kommentar("En ny kommentar", LocalDate.now());
        
        Innlegg innleggMedKommentar = repoInnlegg.leggTilKommentar(nyttInnlegg.getId(), nyKommentar);

        System.out.println("-> " + innleggMedKommentar);
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 13: Legger til kommentarer til alle innlegg som starter på "Post"
        // ---------------------------------------------
        heading(13, "Kommentar legges til i alle innlegg med teksten 'post': ", "leggTilKommentar(ObjectId id, Kommentar kommentar)");
  
        Kommentar nyKommentar2 = new Kommentar("Liker temaet om 'Post'", LocalDate.now());
        
        List<Innlegg> postInnlegg = repoInnlegg.readByTittelContains("post");

        for (Innlegg i : postInnlegg) {
        	repoInnlegg.leggTilKommentar(i.getId(), nyKommentar2);
        }
        
        System.out.println("-> Kommentar lagt til i  " + postInnlegg.size() + " innlegg.");
        System.out.println("-> NB! Sjekk i MongoDB Compass.");
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 14: Fjerner kommentaren til nyttInnlegg (Embedded)
        // ---------------------------------------------
        heading(14, "Fjern kommentar for ID: " + nyttInnlegg.getId(), "fjernKommentar(ObjectId id, Kommentar kommentar)");
        System.out.println("Fjerner følgende kommentar: " + nyKommentar);
        
        innleggMedKommentar = repoInnlegg.fjerneKommentar(nyttInnlegg.getId(), nyKommentar);

        System.out.println("-> Innlegg (etter kommentar fjernet): " + innleggMedKommentar);
        
        //System.exit(0);
        
        
        // ---------------------------------------------
        // DEMO 15: DELETE - Sletter innlegget
        // ---------------------------------------------
        heading(15, "Sletter innlegg med ID: " + nyttInnlegg.getId(), "delete(ObjectId id)");
        repoInnlegg.delete(nyttInnlegg.getId());
       
        System.out.println("-> NB! Sjekk at fjernet i MongoDB Compass.");
        System.exit(0);
        
        
        // ---------------------------------------------
        // Clean
        // ---------------------------------------------
        //heading(0, "Slettet alle dokumenter i 'innlegg' samling");
        //repoInnlegg.clean();
        
        
   	}

	
	private static void info(String tekst) {
		System.out.println("");
		System.out.println("--------------------------------------------");
		System.out.println("INFO: " + tekst);
		System.out.println("--------------------------------------------");
	}
	
	private static void heading(Integer demoCounter, String tekst, String metode) {
		System.out.println("");
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("DEMO " + demoCounter + ": " + tekst);
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("Metode: InnleggRepository." + metode);
		System.out.println("-------");
		System.out.println("");
	}

}
