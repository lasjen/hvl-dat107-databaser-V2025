package no.hvl.dat107.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Innlegg {
	
	private ObjectId id;
	
	private String tittel;
	
	private String innhold;
	
	@BsonProperty(value = "opprettet")
	private LocalDate opprettetDato;
	
	@BsonProperty(value = "kommentarer")
	private List<Kommentar> kommentarer; 
	
	public Innlegg() {
		this.kommentarer = new ArrayList<Kommentar>();
	}

	public Innlegg(String tittel, String innhold) {
		this.tittel = tittel;
		this.innhold = innhold;
		this.opprettetDato = LocalDate.now();
		this.kommentarer = new ArrayList<Kommentar>();
	}
	
	public Innlegg(String tittel, String innhold, LocalDate opprettetDato) {
		this.tittel = tittel;
		this.innhold = innhold;
		this.opprettetDato = opprettetDato;
		this.kommentarer = new ArrayList<Kommentar>();
	}
	
	public Innlegg(String tittel, String innhold, LocalDate opprettetDato, List<Kommentar> kommentarer) {
		this.tittel = tittel;
		this.innhold = innhold;
		this.opprettetDato = opprettetDato;
		this.kommentarer = kommentarer;
	}

	// Getters and Setters
	public ObjectId getId() {
		return id;
	}

	public Innlegg setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getTittel() {
		return tittel;
	}

	public Innlegg setTittel(String tittel) {
		this.tittel = tittel;
		return this;
	}

	public String getInnhold() {
		return innhold;
	}

	public Innlegg setInnhold(String innhold) {
		this.innhold = innhold;
		return this;
	}

	public LocalDate getOpprettetDato() {
		return opprettetDato;
	}

	public Innlegg setOpprettetDato(LocalDate opprettetDato) {
		this.opprettetDato = opprettetDato;
		return this;
	}
	

	public List<Kommentar> getKommentarer() {
		return kommentarer;
	}

	public Innlegg setKommentarer(List<Kommentar> kommentarer) {
		this.kommentarer = kommentarer;
		return this;
	}
	
	public Innlegg addKommentar(Kommentar kommentar) {
		this.kommentarer.add(kommentar);
		return this;
	}
	
	public Innlegg removeKommentar(Kommentar kommentar) {
		this.kommentarer.remove(kommentar);
		return this;
	}

	@Override
	public String toString() {
		return "Innlegg [id=" + id + ", tittel=" + tittel + ", innhold=" + innhold + ", opprettetDato=" + opprettetDato
				+ ", kommentarer=" + kommentarer + "]";
	}


	
	
}
