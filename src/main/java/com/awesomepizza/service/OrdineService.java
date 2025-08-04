package com.awesomepizza.service;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import com.awesomepizza.model.Ordine;
import com.awesomepizza.model.Ordine.StatoOrdine;

@Service
public class OrdineService {

	private Queue<Ordine> codaOrdini = new ConcurrentLinkedQueue<>();
	private Map<UUID, Ordine> mappaOrdini = new ConcurrentHashMap<>();
	
	public Ordine creaOrdine(String descrizione)
	{
		Ordine ordine = new Ordine(descrizione);
		codaOrdini.add(ordine);
		mappaOrdini.put(ordine.getID(), ordine);
		
		return ordine;
	}
	
	public Optional<Ordine> getOrdine(UUID ID)
	{
		return Optional.ofNullable(mappaOrdini.get(ID));
	}
	
	public Optional<Ordine> prelevaProssimoOrdine()
	{
		Ordine prossimoOrdine = codaOrdini.poll();
		if(prossimoOrdine != null)
		{
			Ordine mapOrdine = mappaOrdini.get(prossimoOrdine.getID());
	        mapOrdine.setStatoOrdine(StatoOrdine.IN_PREPARAZIONE);
	        return Optional.of(mapOrdine); 
		}
		return Optional.ofNullable(prossimoOrdine);
	}
	
	public boolean aggiornaStatoOrdine(UUID id, StatoOrdine nuovoStato)
	{
		Ordine ordineDaAggiornare = mappaOrdini.get(id);
		
		if(ordineDaAggiornare != null) {
			ordineDaAggiornare.setStatoOrdine(nuovoStato);
			if (nuovoStato == StatoOrdine.PRONTO) {
                System.out.println("Notifica: ordine " + id + " è pronto!");
            }
			return true;
		}
		else return false;
	}
	
	public boolean ritiraOrdine(UUID id)
	{
		Ordine ordineDaRitirare = mappaOrdini.get(id);
		if(ordineDaRitirare != null && ordineDaRitirare.getStatoOrdine() == StatoOrdine.PRONTO)
		{
			ordineDaRitirare.setStatoOrdine(StatoOrdine.RITIRATO);
			return true;
		}
		else return false;
	}
	
}
