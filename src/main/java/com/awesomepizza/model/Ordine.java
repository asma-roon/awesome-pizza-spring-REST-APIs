package com.awesomepizza.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ordine {

	public enum StatoOrdine
	{
		IN_ATTESA,
		IN_PREPARAZIONE,
		PRONTO, 
		RITIRATO
	}
	
	private UUID id;
	private StatoOrdine stato;
	private String descrizione; 
	private LocalDateTime tempoCreazione;
	
	public Ordine(String descrizione)
	{
		this.descrizione=descrizione;
		this.id= UUID.randomUUID();
		this.tempoCreazione = LocalDateTime.now();
		this.stato= StatoOrdine.IN_ATTESA;
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public StatoOrdine getStatoOrdine() 
	{
		return stato;
	}
	
	public String getDescrizione() 
	{
		return descrizione;
	}
	
	public LocalDateTime getTempoCreazione()
	{
		return tempoCreazione;
	}
	
	public void setStatoOrdine(StatoOrdine stato)
	{
		this.stato = stato;
	}
}
