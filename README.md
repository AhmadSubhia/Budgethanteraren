# Budgethanteraren (CLI)

## Beskrivning
Budgethanteraren är ett konsolbaserat program utvecklat i Java 21. Programmet hjälper användaren att registrera och hantera inkomster och utgifter, beräkna ekonomiskt saldo och få en översikt över utgifter per kategori.

Transaktionerna sparas i en CSV-fil så att informationen kan läsas in igen när programmet startas.

## Funktioner
- Registrera inkomster och utgifter med datum, kategori, belopp och typ.
- Visa alla transaktioner sorterade efter datum.
- Beräkna totala inkomster, totala utgifter och aktuellt saldo.
- Sammanställa utgifter per kategori.
- Filtrera transaktioner efter typ.
- Spara och läsa transaktioner från en CSV-fil.
- Hantera ogiltig användarinmatning och felaktigt formaterade filer.
- Logga programmets händelser och fel med nivåerna DEBUG, INFO, WARNING och ERROR.

## Teknik och struktur
- **Java 21** – programmets programmeringsspråk.
- **Maven** – hantering av projekt och beroenden.
- **JUnit 5** – automatiserade enhetstester.
- **Generics** – `Repository<T>` kan lagra olika typer av objekt.
- **Streams** – används för filtrering, sortering och beräkningar.
- **Predicate<T>** – används för att beskriva villkor vid filtrering.
- **Enum och Record** – `TransactionType` representerar transaktionstypen och `Transaction` representerar en transaktion.
- **Egna undantag** – `InvalidTransactionException` och `FileFormatException` används för att hantera olika fel.
- **Java Logging** – används för att registrera händelser och fel.

## Testning
Projektet innehåller JUnit 5-tester för `Repository`, `BudgetService` och `FileService`.

Testerna kontrollerar bland annat:
- Att transaktioner kan läggas till och hämtas.
- Att filtrering fungerar och att tomma resultat hanteras.
- Att inkomster, utgifter, saldo och kategorisummor beräknas korrekt.
- Att ogiltiga transaktioner, exempelvis negativa eller icke-ändliga belopp, avvisas.
- Att transaktioner kan sparas och läsas från CSV.
- Att saknade filer och felaktiga CSV-rader hanteras.

Kör alla tester från projektets rotmapp med:

```bash
mvn test
```

## Fel som upptäcktes under utvecklingen
Vid manuell testning upptäcktes att texten `abc` i beloppsfältet orsakade `NumberFormatException`. Felet gjorde att programmet avslutades i stället för att låta användaren försöka igen.

Problemet åtgärdades genom att fånga `NumberFormatException` och `DateTimeParseException` i menyn och visa ett felmeddelande. Därefter testades inmatningen igen för att kontrollera att programmet kunde fortsätta köras.

Ett annat problem uppmärksammades vid validering av belopp: en kontroll som endast jämför beloppet med noll räcker inte för att avvisa `NaN` och `Infinity`. Valideringen kompletterades därför med `Double.isFinite()`, och ett enhetstest lades till för att kontrollera att ett icke-ändligt belopp avvisas.

## Reflektion: Generics och Streams
Generics gör att `Repository<T>` kan återanvändas för olika datatyper utan att en separat repository-klass behöver skapas för varje typ. Det ger bättre återanvändbarhet och typsäkerhet.

Streams gör det möjligt att uttrycka filtrering, sortering och summering på ett deklarativt sätt. Exempelvis kan utgifter filtreras efter typ och sedan summeras med `mapToDouble()` och `sum()`. Det gör beräkningslogiken mer kompakt och lättare att läsa.

En viktig lärdom är att strömmar inte automatiskt gör all kod bättre: enklare operationer kan ibland vara tydligare med vanliga loopar. Det är viktigt att välja den lösning som bäst beskriver uppgiften och är lättast att underhålla.

## Starta programmet
1. Öppna projektet i IntelliJ IDEA.
2. Kontrollera att projektet använder JDK 21 och att Maven har laddat projektets beroenden.
3. Starta klassen `budget.Main`.

Programmet använder sökvägen `data/transactions.csv` för att spara transaktioner. Om filen saknas kan programmet börja med en tom transaktionslista.
