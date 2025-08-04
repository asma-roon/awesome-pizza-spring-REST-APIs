# Awesome Pizza API  

## Introduzione 
 
AwesomePizza è un sistema semplice per la gestione degli ordini di una pizzeria, sviluppato con Spring Boot e Java 21. Permette ai clienti di inviare degli ordini tramite delle REST API call e seguirne lo stato. Inoltre, permette al pizzaiolo di prendere un ordine per volta e di prepararlo per poi aggiornarne lo stato.  

## **Features**
Quindi, in questa prima fase di sviluppo, sono state sviluppate le seguenti funzionalità: 

- Creazione e tracciamento degli ordini 

- Gestione dello stato degli ordini (in attesa, in preparazione, pronto, ritirato) 

- Priorità FIFO (First-In-First-Out) per la preparazione 

- Integrazione semplice via API REST 

- Il cliente può ritirare l’ordine una volta pronto _  
  

## **Tecnologie utilizzate**  
Java 21 

Spring Boot 3 

Spring Web 

Gradle (o Maven) 

JUnit & MockMvc per i test 

Postman 

## **Come eseguire**  
1. Clonare il progetto
2. Aprire Spring Tool Suite
3. Eseguire AwesomePizzaApplication.java come progetto Spring
4. Inviare richieste tramite Postman o altri tool simili

L'app sarà accessibile su: `http://localhost:8080`  

##API Endpoints
 API Endpoints

## Creare un nuovo ordine 
Permette di creare un nuovo ordine e lo pone in stato di attesa.
**POST** `/ordini`

**Request Body:**
```json
{
  "descrizione": "Margherita"
}
```
**Response Body:**
```json
{
  "id": "5fc7287f-6a43-4c89-b3a6-96b6ea124477",
  "descrizione": "Margherita",
  "statoOrdine": "IN_ATTESA",
  "tempoCreazione": "2025-03-08T14:22:10"
}
```
##Ricerca ordine con ID
Permette di fare il retrieval di un ordine con il suo ID

**GET /ordini/{id}**
**Responde: 200 OK oppure 404 Not Found**

##Preleva il prossimo ordine da preparare
Serve al pizzaiolo per prelevare il prossimo ordine che deve preparare

**POST /ordini/prossimoOrdine**
**Responde: Il prossimo ordine in coda oppure 204 No Content**

##Aggiornare Stato dell'ordine
Aggiorna lo stato dell'ordine a uno stato che viene passato. 

**PUT /ordini/{id}**
**Responde: 200 OK o 404 Not Found**

##Ritirare ordine
Serve per fare in modo che quando l'ordine è pronto il cliente lo possa ritirare e l'ordine venga aggiornato come ritirato.
**GET /ordini/{id}/ritira**
**Responde: 200 OK – ordine pronto per ritiro e viene marcato come RITIRATO

409 Conflict – se l'ordine non era in stato di PRONTO**

Le API seguono lo stile REST. È possibile testarle con strumenti come Postman, cURL o integrarle in altri sistemi.






