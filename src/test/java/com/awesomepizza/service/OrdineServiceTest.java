package com.awesomepizza.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.awesomepizza.model.Ordine;
import com.awesomepizza.model.Ordine.StatoOrdine;

class OrdineServiceTest {

	private OrdineService ordineService;
	
	@BeforeEach
	void setUp() {
		ordineService = new OrdineService();
	}
	
	@Test
	void testCreaOrdine() 
	{
		Ordine testOrdine = ordineService.creaOrdine("Margherita");
		
		assertNotNull(testOrdine.getID());
		assertEquals(testOrdine.getDescrizione(), "Margherita");
		assertEquals(testOrdine.getStatoOrdine(), StatoOrdine.IN_ATTESA);
	}
	
	@Test
	void testGetOrdine()
	{
		Ordine testOrdine = ordineService.creaOrdine("Marinara");
		Optional<Ordine> testGetOrdine = ordineService.getOrdine(testOrdine.getID());
		
		assertTrue(testGetOrdine.isPresent());
		assertEquals(testGetOrdine.get().getDescrizione(), "Marinara");
	}
	
	@Test
	void testPrelevaProssimoOrdine()
	{
		Ordine testOrdine = ordineService.creaOrdine("Tonno e Cipolla");
		Optional<Ordine> testProssimoOrdine = ordineService.prelevaProssimoOrdine();
		
		assertEquals(testProssimoOrdine.get().getStatoOrdine(), StatoOrdine.IN_PREPARAZIONE);
	}
	
	@Test
	void testAggiornaStatoOrdine()
	{
		Ordine testOrdine = ordineService.creaOrdine("Bufala");
		Boolean result = ordineService.aggiornaStatoOrdine(testOrdine.getID(), StatoOrdine.PRONTO);
		
		assertTrue(result);
		assertEquals((ordineService.getOrdine(testOrdine.getID()).get().getStatoOrdine()), StatoOrdine.PRONTO);
	}
	
	@Test
	void testRitiraOrdine()
	{
		Ordine testOrdine = ordineService.creaOrdine("4 Formaggi");
		//controllo che non mi vada a buon fine il ritiro di un ordine che non è pronto
		assertFalse(ordineService.ritiraOrdine(testOrdine.getID()));
		
		//controllo che dopo che è pronto, possa essere ritirato
		ordineService.aggiornaStatoOrdine(testOrdine.getID(), StatoOrdine.PRONTO);
		assertTrue(ordineService.ritiraOrdine(testOrdine.getID()));
		assertEquals(ordineService.getOrdine(testOrdine.getID()).get().getStatoOrdine(), StatoOrdine.RITIRATO);	
	}

}
