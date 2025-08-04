package com.awesomepizza.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.awesomepizza.model.Ordine;
import com.awesomepizza.model.Ordine.StatoOrdine;
import com.awesomepizza.service.OrdineService;

@RestController
@RequestMapping("/ordini")
public class OrdineController {

	private final OrdineService ordineService;
	
	public OrdineController(OrdineService ordineService)
	{
		this.ordineService=ordineService;
	}
	
	@PostMapping
	public ResponseEntity<?> creaOrdine(@RequestBody Map<String, String> request)
	{
		String descrizioneNuovoOrdine = request.get("descrizione");
		if (descrizioneNuovoOrdine == null || descrizioneNuovoOrdine.isBlank()) {
		    return ResponseEntity.badRequest()
		        .body("La descrizione dell'ordine è obbligatoria");
		}
		Ordine ordine = ordineService.creaOrdine(descrizioneNuovoOrdine);
		return ResponseEntity.ok(ordine);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Ordine>> getOrdine(@PathVariable("id") UUID id)
	{
		Optional<Ordine> ordine = ordineService.getOrdine(id);
		if(ordine.isPresent()) return ResponseEntity.ok(ordine);
		else return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/prossimoOrdine")
	public ResponseEntity<?> prendiProssimoOrdine()
	{
		Optional<Ordine> prossimoOrdine = ordineService.prelevaProssimoOrdine();
		if(prossimoOrdine.isPresent()) return ResponseEntity.ok(prossimoOrdine);
		else return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> aggiornaStatoOrdine(@PathVariable("id") UUID id, @RequestBody Map<String, String> request)
	{
		StatoOrdine nuovoStato = StatoOrdine.valueOf(request.get("stato"));
		boolean aggiornato = ordineService.aggiornaStatoOrdine(id, nuovoStato);
		return aggiornato ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}/ritira")
	public ResponseEntity<?> ritiraOrdine(@PathVariable("id") UUID id)
	{
		if(ordineService.ritiraOrdine(id)) return ResponseEntity.ok().build();
		else return ResponseEntity.status(HttpStatus.CONFLICT).body("Ordine non ancora pronto");
	}
	
}
