# DAT107 - NoSQL: MongoDB & Java (Eksempler)

Folderen inneholder 4 eksempel prosjekter (for Eclipse). Under følger en kort beskrivelse av hvert prosjekt.

NB! Det viktigste prosjektet å bruke tid på er prosjeket F03MongoDBJava og filene:
- no/hvl/dat107/MongoDb_EmbeddingCRUD.java
- no/hvl/dat107/repository/InnleggRepository.java

## Prosjekt 1: F01MongoDBJava
Dette prosjektet demonstrerer kort det grunnleggende om hvordan man oppretter en kobling til en MongoDB-database.
NB! Du vil finne mye av det samme i prosjekt F02MongoDBJava og F03MongoDBJava, så dere trenger ikke å bruke mye tid på dette prosjektet.

- `MongoDBClient`: Oppretter en kobling til MongoDB server/kluster og skriver ut alle databasene i systemet.
- `MongoDBDatabase`: Oppretter en kobling til en spesifikk database og skriver ut alle samlingene i databasen.
- `MongoDBCollection`: Oppretter en kobling til en spesifikk samling og skriver ut alle dokumentene i samlingen.

## Prosjekt 2: F02MongoDBJava
Dette prosjektet demonstrerer forskjellen på MongoCollection<Document> og MongoCollection<Innlegg>.
Dette prosjektet er mest ment for at jeg som foreleser skal demonstrer forskjellen.
Dere kan heller bruke tid på prosjekt "F03MongoDBJava".

## Prosjekt 3: F03MongoDBJava
Dette er det mest viktige prosjektet for dere å bruke tid på.
Følgende filer demonstrerer hvordan CRUD-operasjoner kan gjøres med MongoDB og Java.

- no/hvl/dat107/MongoDb_EmbeddingCRUD.java
- no/hvl/dat107/repository/InnleggRepository.java