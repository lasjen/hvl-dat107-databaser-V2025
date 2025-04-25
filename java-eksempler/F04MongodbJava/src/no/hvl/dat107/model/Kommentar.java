package no.hvl.dat107.model;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Kommentar {
	
	private ObjectId id;
	
	private String tekst;
	
	@BsonProperty(value = "opprettet")
	private LocalDate opprettetDato;
	
	public Kommentar(){}
	
	public Kommentar(String tekst, LocalDate opprettetDato) {
		this.tekst = tekst;
		this.opprettetDato = opprettetDato;
	}

	public ObjectId getId() {
		return id;
	}

	public Kommentar setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getTekst() {
		return tekst;
	}

	public Kommentar setTekst(String tekst) {
		this.tekst = tekst;
		return this;
	}

	public LocalDate getOpprettetDato() {
		return opprettetDato;
	}

	public Kommentar setOpprettetDato(LocalDate opprettetDato) {
		this.opprettetDato = opprettetDato;
		return this;
	}

	@Override
	public String toString() {
		return "Kommentar [id=" + id + ", tekst=" + tekst + ", opprettetDato=" + opprettetDato + "]";
	}
	
}
