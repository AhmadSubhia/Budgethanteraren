package budget;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    @Test
    void shouldAddAndFindTransaction() {
        Repository <Transaction> repository = new Repository<>();

        Transaction transaction = new Transaction(LocalDate.of(2026,10,7),
                "Mat och Dryckor",600, TransactionType.UTGIFT);

        repository.add(transaction);

        assertEquals(1,repository.size());
        assertEquals(transaction,repository.findAll().get(0));
    }
    @Test
    void shouldFindOnlyExpenses() {
        Repository <Transaction> repository = new Repository<>();
        Transaction income = new Transaction(LocalDate.of(2026,10,7),
                "Lön", 25000,TransactionType.INKOMST);


        Transaction expense = new Transaction(LocalDate.of(2026,10,7),
                "Mat",5000,TransactionType.UTGIFT);
        repository.add(income);
        repository.add(expense);
        List<Transaction> expenses = repository.findWhere
                (transaction -> transaction.type()==TransactionType.UTGIFT);

        assertEquals(1,expenses.size());
        assertEquals(expense,expenses.get(0));
    }
    @Test
    void shouldReturnEmptyListWhereRepositoryIsEmpty() {
        Repository <Transaction> repository = new Repository<>();
        List<Transaction> result = repository.findAll();
        assertTrue(result.isEmpty());
    }
    @Test
    void shouldReturnEmptyListWhenNothingMatches() {
        Repository <Transaction> repository = new Repository<>();
        Transaction transaction = new Transaction(LocalDate.of(2026,10,7),
                "Mat och Dryckor",600, TransactionType.UTGIFT);
        repository.add(transaction);

        List<Transaction> result = repository.findWhere
                (transaction1 -> transaction.category().equals("Bil"));
        assertTrue(result.isEmpty());
    }
}
