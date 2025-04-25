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

public class MongoDb_LinkingCRUD {

	public static void main(String[] args) {
    	
		String uri = "mongodb://127.0.0.1:27017";
    	String databaseName = "forelesning-04";
        
    	MongoClient client = MongoClientFactory.getMongoClient(uri);
    	InnleggRepository repoInnlegg = new InnleggRepository(client, databaseName);
        
        // ---------------------------------------------
        // Sletter alt i "Innlegg" og "Kommentar"
        // ---------------------------------------------
        repoInnlegg.clean();
        info("Collections Cleaned - ready to start!");
        
        //System.exit(0);
        
        // ---------------------------------------------
        // CREATE - Nytt innlegg
        // ---------------------------------------------
        overskrift("Nytt innlegg");
        Innlegg nyttInnlegg = new Innlegg()
        		.setTittel("Et single embedded innlegg")
        		.setInnhold("Mye tekst, men lite innhold. Eller omvendt.")
        		.setOpprettetDato(LocalDate.now());
        
        repoInnlegg.nyttInnlegg(nyttInnlegg);
        System.out.println("NyttInnlegg - ID: " + nyttInnlegg.getId());
        
        //System.exit(0);
        // ---------------------------------------------
        // CREATE & UPDATE - Ny kommentar på innlegg
        // ---------------------------------------------
        overskrift("Legger til kommentar");
        Kommentar nyKommentar = new Kommentar("Dette er en ny kommentar", LocalDate.now());
        repoInnlegg.nyKommentar(nyttInnlegg, nyKommentar);
        System.out.println(nyttInnlegg);
        
        //System.exit(0);
        // ---------------------------------------------
        // CREATE & UPDATE - Enda en kommentar
        // ---------------------------------------------
        overskrift("Legger til enda en kommentar");
        System.out.println(
        	repoInnlegg.nyKommentar(
        			nyttInnlegg.getId(),
        			new Kommentar("Enda en slitsom kommentar.", LocalDate.now())
        	)
        );
        
        //System.exit(0);
        // ---------------------------------------------
        // READ siste versjon av innlegg og kommentarer
        // ---------------------------------------------
        overskrift("Leser sist versjon av innlegg og kommentarer ...");
        Innlegg sisteVersjon = repoInnlegg.getInnleggById(nyttInnlegg.getId());
        List<Kommentar> sisteKommentarer = repoInnlegg.readKommentarerInnlegg(sisteVersjon);
        visInnleggOgKommentarer(sisteVersjon, sisteKommentarer);
        
   	}

	private static void visInnleggOgKommentarer(Innlegg innlegg, List<Kommentar> kommentarer) {
		System.out.println(innlegg);
		System.out.println(" --> Kommentarer:");
		for (Kommentar k:kommentarer) {
        	System.out.println("       - " + k);
        }
	}

	private static void visListOfInnlegg(List<Innlegg> listInnleggPost) {
		for (Innlegg i:listInnleggPost) {
			System.out.println(i);
		}
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
