
package budget;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.format.DateTimeParseException;
public class Main {

    private static final Path DATA_FILE = Path.of("data", "transactions.csv");

    public static void main(String[] args) {

        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        FileService fileService = new FileService();

        loadAtStartup(fileService, budgetService);

        try (Scanner scanner = new Scanner(System.in)) {

            boolean running = true;

            while (running) {
                printMenu();

                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim().toLowerCase();

                try {
                    switch (choice) {
                        case "1" -> addTransaction(scanner, budgetService, fileService, repository);

                        case "2" -> showTransactions(repository);

                        case "3" -> showSummary(budgetService);

                        case "4" -> filterTransactions(scanner, budgetService);

                        case "5" -> saveTransactions(fileService, repository);

                        case "e" -> {
                            saveTransactions(fileService, repository);
                            running = false;
                            System.out.println("Goodbye!");
                        }

                        default -> System.out.println("Invalid choice. Try again.");
                    }
                } catch (InvalidTransactionException exception) {
                    AppLogger.warning(exception.getMessage());
                    System.out.println("Invalid transaction: " + exception.getMessage());

                } catch (IOException exception) {
                    AppLogger.error("File operation failed.", exception);
                    System.out.println("Could not access the data file.");

                }

                catch (DateTimeParseException | NumberFormatException exception) {
                    AppLogger.warning("Invalid date or amount entered.");

                    System.out.println("Invalid input. Enter the date as YYYY-MM-DD and amount as a number.");
                }

            }
        }
    }
    private static void printMenu() {
        System.out.println();
        System.out.println(" BUDGET MANAGER ");
        System.out.println("1. Add transaction");
        System.out.println("2. Show all transactions");
        System.out.println("3. Show balance and category summary");
        System.out.println("4. Filter transactions");
        System.out.println("5. Save to file");
        System.out.println("e. Exit");
    }


    private static boolean loadAtStartup(
            FileService fileService,
            BudgetService budgetService) {

        try {
            List<Transaction> transactions = fileService.loadTransactions(DATA_FILE);

            for (Transaction transaction : transactions) {
                budgetService.addTransaction(transaction);
            }

            AppLogger.info("Startup complete. Transactions loaded: " + transactions.size());
            return true;

        } catch (IOException | FileFormatException | InvalidTransactionException exception) {
            AppLogger.error("Could not load transactions at startup.", exception);

            System.out.println("Could not load saved data. Please check the data file.");

            return false;
        }
    }


    private static void addTransaction(Scanner scanner, BudgetService budgetService,
            FileService fileService, Repository<Transaction> repository)
            throws IOException {

        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Type (1 = INKOMST, 2 = UTGIFT): ");
        String typeChoice = scanner.nextLine().trim();

        TransactionType type = switch (typeChoice) {
            case "1" -> TransactionType.INKOMST;
            case "2" -> TransactionType.UTGIFT;
            default -> throw new InvalidTransactionException("Choose 1 for income or 2 for expense.");
        };

        Transaction transaction = new Transaction(date, category, amount, type);
        AppLogger.debug("Attempting to add transaction: " + category);

        budgetService.addTransaction(transaction);
        AppLogger.info("Transaction added: " + category);

        saveTransactions(fileService, repository);

        System.out.println("Transaction added successfully!");
    }

    private static void showTransactions(
            Repository<Transaction> repository) {

        List<Transaction> transactions = repository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::date)).toList();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.printf("%s | %s | %.2f | %s%n",
                    transaction.date(), transaction.category(), transaction.amount(), transaction.type());
        }
    }
    private static void showSummary(BudgetService budgetService) {

        System.out.printf("Total income: %.2f%n",
                budgetService.totalIncome());

        System.out.printf("Total expenses: %.2f%n",
                budgetService.totalExpenses());

        System.out.printf("Balance: %.2f%n", budgetService.calculateBalance());

        System.out.println("Expenses by category:");

        Map<String, Double> categories = budgetService.totalExpensesByCategory();

        if (categories.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        categories.forEach((category, total) ->
                System.out.printf("%s: %.2f%n", category, total));
    }

    private static void filterTransactions(Scanner scanner,
            BudgetService budgetService) {

        System.out.print("Filter type (1 = INKOMST, 2 = UTGIFT): ");
        String choice = scanner.nextLine().trim();

        TransactionType type = switch (choice) {
            case "1" -> TransactionType.INKOMST;
            case "2" -> TransactionType.UTGIFT;
            default -> {
                System.out.println("Invalid type.");
                yield null;
            }
        };
        if (type == null) {
            return;
        }

        List<Transaction> transactions = budgetService.findByType(type);

        if (transactions.isEmpty()) {
            System.out.println("No matching transactions.");
            return;
        }
        transactions.forEach(System.out::println);
    }

    private static void saveTransactions(
            FileService fileService,
            Repository<Transaction> repository) throws IOException {

        fileService.saveTransactions(repository.findAll(), DATA_FILE);
    }
}
