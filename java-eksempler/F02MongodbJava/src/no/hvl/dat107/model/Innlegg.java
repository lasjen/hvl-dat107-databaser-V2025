package no.hvl.dat107.model;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Innlegg {
	
	private ObjectId id;
	
	private String tittel;
	
	private String innhold;
	
	@BsonProperty(value = "created")
	private LocalDate createdDato;
	
	public Innlegg() {}

	public Innlegg(String tittel, String innhold) {
		this.tittel = tittel;
		this.innhold = innhold;
		this.createdDato = LocalDate.now();
	}
	
	public Innlegg(String tittel, String innhold, LocalDate createdDato) {
		this.tittel = tittel;
		this.innhold = innhold;
		this.createdDato = createdDato;
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

	public LocalDate getCreatedDato() {
		return createdDato;
	}

	public Innlegg setCreatedDato(LocalDate createdDato) {
		this.createdDato = createdDato;
		return this;
	}

	@Override
	public String toString() {
		return "Innlegg [id=" + id + ", tittel=" + tittel + ", innhold=" + innhold + ", createdDato=" + createdDato
				+ "]";
	}
	
	
	
	
}
