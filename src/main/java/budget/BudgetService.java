package budget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {

    private final Repository<Transaction> repository;

    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {

        if (transaction == null) {
            throw new InvalidTransactionException(
                    "Transaction cannot be null");
        }

        if (transaction.category() == null ||
                transaction.category().isBlank()) {

            throw new InvalidTransactionException(
                    "Category cannot be empty");
        }

        if (transaction.amount() < 0) {
            throw new InvalidTransactionException(
                    "Amount cannot be negative");
        }

        if (transaction.type() == null) {
            throw new InvalidTransactionException(
                    "Transaction type cannot be null");
        }

        repository.add(transaction);
    }
    public double calculateBalance() {
        double income = repository.findWhere(
                        transaction -> transaction.type() == TransactionType.INKOMST).stream()
                .mapToDouble(Transaction::amount)
                .sum();
        double expenses = repository.findWhere(
                        transaction -> transaction.type() == TransactionType.UTGIFT
                ).stream()
                .mapToDouble(Transaction::amount)
                .sum();
        return income - expenses;
    }
    public double totalIncome() {
        return repository.findWhere(
                        transaction -> transaction.type() == TransactionType.INKOMST
                ).stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public double totalExpenses() {
        return repository.findWhere(
                        transaction -> transaction.type() == TransactionType.UTGIFT).stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public Map<String, Double> totalExpensesByCategory() {
        return repository.findWhere(
                        transaction -> transaction.type() == TransactionType.UTGIFT).stream()
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        Collectors.summingDouble(Transaction::amount)));
    }

    public List<Transaction> findByType(TransactionType type) {
        return repository.findWhere(transaction -> transaction.type() == type);
    }
}