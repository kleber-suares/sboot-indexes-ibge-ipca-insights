# IPCA Insights API

## 📑 Overview
The IPCA Insights API is an application designed to collect, process, store, 
and expose analytical data related to **IPCA (Índice Nacional de Preços ao Consumidor Amplo)**, 
Brazil's official broad consumer price inflation index.

The application consumes public data provided by official government sources, processes it asynchronously, 
and persists structured information that can later be queried for analysis, insights, and experimentation.

This project focuses both on **data exploration** 
and on **software architecture and engineering practices**.

---
## 📉 What is IPCA?
The **IPCA (Extended National Consumer Price Index)** is Brazil's main inflation indicator.
It measures the variation in prices of goods and services consumed by households with incomes ranging 
from 1 to 40 minimum wages, covering several metropolitan regions and major cities across the country.

IPCA is calculated and published monthly 
by [**IBGE (Brazilian Institute of Geography and Statistics)🔗**](https://www.ibge.gov.br/) 
and is widely used for:

- Inflation monitoring
- Economic analysis
- Public policy decisions
- Financial and investment adjustments

---
## 🎓 About this Project

This project was created as a case study with two main objectives:

### 1. Data Exploration
- Consume and persist historical and reference IPCA data
- Enable structured access to inflation indicators
- Prepare the data for future analytical queries and insights

### 2. Technical Experimentation
- Explore modern Java and Spring Boot capabilities
- Practice resilient external API consumption
- Implement asynchronous processing and bulk database operations
- Apply clean architecture, separation of concerns, and test strategies 
 
The project is not intended to be a production-ready system, 
but rather a **learning and experimentation platform** centered around real public economic data.

---
## 💿️Data Sources

The application relies exclusively on official public data provided by Brazilian government institutions.

### IBGE (Instituto Brasileiro de Geografia e Estatística)
IBGE is Brazil's main public institution responsible for producing official statistical, 
geographic, and economic information, including inflation indexes such as IPCA.

### SIDRA (Sistema IBGE de Recuperação Automática)
SIDRA is IBGE's official platform for accessing statistical data programmatically.

It provides:
- Structured datasets
- Public APIs
- Historical economic indicators
- Machine-readable responses (JSON)

Relevant resources:
- **IPCA dataset on dados.gov.br** <br>
https://dados.gov.br/dados/conjuntos-dados/ia-indice-nacional-de-precos-ao-consumidor-amplo-ipca <br> 
Public description of the IPCA dataset and its role as an official inflation indicator.

- **SIDRA API** <br>
https://apisidra.ibge.gov.br/ <br>
The base API endpoint used to retrieve IPCA data programmatically.

- **SIDRA API Documentation** <br>
https://apisidra.ibge.gov.br/home/ajuda#Introducao
Official documentation describing endpoints, parameters, formats, and usage examples.

- **IBGE official web site** <br>
https://www.ibge.gov.br/ <br>
Official documentation describing endpoints, parameters, formats, and usage examples.
  
These sources ensure that all data consumed by the application is 
**official, transparent, and publicly accessible**.

---
## 🚀 Features

- IPCA data fetch from official IBGE/SIDRA APIs
- Cumulative IPCA calculation for a given period range
- Asynchronous data import execution
- Bulk persistence operations for large datasets
- Import process tracking with detailed execution logs
- Retry and timeout handling for external API calls
- Separation between history data and reference/info data
- Designed for extensibility and future analytical endpoints

---
## ⚙️ Tech Stack

### 🛠️ Development
- **Java 21**
- **Spring Boot 4**
- **Maven**
- **Spring WebFlux** (reactive web framework)
- **Project Reactor** (reactive streams implementation used by WebFlux)
- **Spring Data MongoDB**
- **MongoDB 8.0 (Docker containerized image)**
- **Lombok**

### 👁️‍🗨️ Testing 

The project adopts a layered testing strategy:

- **JUnit 5** – unit and slice testing framework
- **Mockito** – mocking dependencies in unit tests
- **@WebMvcTest (Spring Test)** – controller layer tests, validating:
    - request mappings
    - HTTP status codes
    - request/response serialization
- **MockWebServer** – lightweight HTTP server used for fast and deterministic tests of retry and timeout behavior
- **WireMock** – full HTTP server simulation used for more realistic integration-like tests, including:
    - HTTP status handling
    - error scenarios
    - retry flows across multiple requests

---
## 📄 License

This project is licensed under the MIT License.
You are free to use, modify, and distribute this software, provided that the original copyright notice is included.

For more information, refer to the [LICENSE](LICENSE) file.


