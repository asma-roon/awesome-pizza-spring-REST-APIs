package com.awesomepizza.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.awesomepizza.model.Ordine;
import com.awesomepizza.service.OrdineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@WebMvcTest(OrdineController.class)
class OrdineControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdineService ordineService;

    @Autowired
    private ObjectMapper objectMapper;

    private Ordine ordine;

    @BeforeEach
    void setUp() {
        ordine = new Ordine("Margherita");
    }
	@Test
	void testCreaOrdine() throws Exception{
		when(ordineService.creaOrdine("Margherita")).thenReturn(ordine);
		
		mockMvc.perform(post("/ordini")
				.contentType(MediaType.APPLICATION_JSON)
                .content("{\"descrizione\":\"Napoli\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descrizione").value("Margherita"))
            .andExpect(jsonPath("$.stato").value("IN_ATTESA"));
	}
	
	@Test
	void testGetOrdine() throws Exception
	{
		UUID id = ordine.getID();
		when(ordineService.getOrdine(id)).thenReturn(Optional.of(ordine));
		
		mockMvc.perform(get("/ordini/" + id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.descrizione").value("Margherita"));	
	}
	
   @Test
    void testAggiornaStato() throws Exception {
        UUID id = ordine.getID();
        when(ordineService.aggiornaStatoOrdine(id, Ordine.StatoOrdine.PRONTO)).thenReturn(true);

        mockMvc.perform(put("/ordini/" + id)
        		.contentType(MediaType.APPLICATION_JSON)
                .content("{\"stato\":\"PRONTO\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void testAggiornaStato_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(ordineService.aggiornaStatoOrdine(id, Ordine.StatoOrdine.PRONTO)).thenReturn(false);

        mockMvc.perform(put("/ordini/" + id)
        		.contentType(MediaType.APPLICATION_JSON)
                .content("{\"stato\":\"PRONTO\"}"))
            .andExpect(status().isNotFound());
    }
	    
	@Test
	void testRitiraOrdine_NonPronto() throws Exception
	{
		when(ordineService.ritiraOrdine(ordine.getID())).thenReturn(false);
		
		mockMvc.perform(put("/ordini/" + ordine.getID() + "/ritira"))
		.andExpect(status().isConflict());
	}
	
	@Test
	void testRitiraOrdine_Pronto() throws Exception
	{
		when(ordineService.ritiraOrdine(ordine.getID())).thenReturn(true);
		
		mockMvc.perform(put("/ordini/" + ordine.getID() + "/ritira"))
		.andExpect(status().isOk());
	}
}
