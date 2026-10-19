package budget;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceTest {

    @Test
    void shouldCalculateBalance() {
        Repository<Transaction> repository = new Repository<>();

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Lon", 20000, TransactionType.INKOMST));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Mat", 500, TransactionType.UTGIFT));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Hyra", 6000, TransactionType.UTGIFT));

        BudgetService service = new BudgetService(repository);
        double balance = service.calculateBalance();
        assertEquals(13500, balance);
    }
    @Test
    void shouldCalculateTotalIncome() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Lon", 20000, TransactionType.INKOMST));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "CSN", 4000, TransactionType.INKOMST));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Mat", 500, TransactionType.UTGIFT));

        BudgetService service = new BudgetService(repository);
        double totalIncome = service.totalIncome();
        assertEquals(24000, totalIncome);
    }


    @Test
    void shouldCalculateTotalExpenses() {

        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(LocalDate.of(2026, 10, 7), "Lon",
                20000, TransactionType.INKOMST));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Mat", 500, TransactionType.UTGIFT));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Hyra", 6000, TransactionType.UTGIFT));

        BudgetService service = new BudgetService(repository);
        double totalExpenses = service.totalExpenses();
        assertEquals(6500, totalExpenses);
    }
    @Test
    void shouldCalculateExpensesByCategory() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Mat", 500, TransactionType.UTGIFT));
        repository.add(new Transaction(LocalDate.of(2026, 10, 7), "Mat",
                300, TransactionType.UTGIFT));

        repository.add(new Transaction(LocalDate.of(2026, 10, 7),
                "Hyra", 6000, TransactionType.UTGIFT));

        BudgetService service = new BudgetService(repository);
        Map<String, Double> result = service.totalExpensesByCategory();

        assertEquals(800, result.get("Mat"));
        assertEquals(6000, result.get("Hyra"));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        Repository<Transaction> repository = new Repository<>();

        BudgetService service = new BudgetService(repository);

        Transaction transaction = new Transaction(LocalDate.of(2026, 10, 7),
                "Mat", -500, TransactionType.UTGIFT);

        assertThrows(InvalidTransactionException.class, () -> service.addTransaction(transaction));
    }
    @Test
    void shouldThrowExceptionWhenCategoryIsEmpty() {
        Repository<Transaction> repository = new Repository<>();
        BudgetService service = new BudgetService(repository);
        Transaction transaction = new Transaction(LocalDate.of(2026, 10, 7),
                "", 500, TransactionType.UTGIFT);

        assertThrows(InvalidTransactionException.class, () -> service.addTransaction(transaction));
    }
    @Test
    void shouldThrowExceptionWhenTransactionIsNull() {
        Repository<Transaction> repository = new Repository<>();
        BudgetService service = new BudgetService(repository);
        assertThrows(InvalidTransactionException.class, () -> service.addTransaction(null));
    }
}