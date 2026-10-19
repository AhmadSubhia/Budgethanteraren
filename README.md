
# Budgethanteraren (CLI)

## Beskrivning
Budgethanteraren är ett konsolprogram skrivet i Java.
Programmet kan registrera inkomster och utgifter, visa saldo,
filtrera transaktioner och beräkna utgifter per kategori.
Transaktionerna sparas i en CSV-fil.

## Funktioner
- Registrera inkomster och utgifter
- Visa alla transaktioner sorterade efter datum
- Beräkna totala inkomster, utgifter och saldo
- Visa utgifter per kategori
- Filtrera transaktioner efter typ
- Spara och läsa transaktioner från CSV
- Hantera ogiltig inmatning och felaktiga filer
- Loggning med DEBUG, INFO, WARNING och ERROR

## Teknik
- Java 21
- Maven
- JUnit 5
- Generics
- Streams och Predicate
- Enum och Record
- Egna undantag
- Java Logging

## Testning
Projektet innehåller JUnit-tester för Repository,
BudgetService och FileService.

Tester kontrollerar bland annat:
- Att transaktioner kan läggas till och hämtas
- Att saldo och kategorisummor beräknas korrekt
- Att ogiltiga transaktioner avvisas
- Att transaktioner kan sparas och läsas från CSV
- Att felaktiga CSV-rader hanteras

Kör testerna med:

mvn test

## Fel som upptäcktes under utvecklingen
Vid manuell testning upptäcktes att en text som "abc"
som belopp orsakade NumberFormatException och avslutade programmet.

Programmet förbättrades genom att fånga
NumberFormatException och DateTimeParseException i
menyn, så att användaren kan få ett felmeddelande och
försöka igen.

## Reflektion: Generics och Streams
Generics gör Repository återanvändbart för olika datatyper
utan att behöva skapa en separat repository-klass för varje typ.

Streams används för att filtrera, sortera och summera
transaktioner. Det gör beräkningarna mer läsbara och
minskar behovet av manuella loopar.

## Starta programmet
Starta klassen budget.Main från IntelliJ.
