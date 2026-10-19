package budget;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public void saveTransactions(
            List<Transaction> transactions,
            Path file) throws IOException {

        AppLogger.debug("Saving transactions to: " + file);

        List<String> lines = transactions.stream()
                .map(transaction -> transaction.date() + ";" + transaction.category() + ";" +
                                transaction.amount() + ";" + transaction.type()).toList();

        Path parent = file.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(file, lines);

        AppLogger.info("Saved " + lines.size() + " transactions.");
    }

    public List<Transaction> loadTransactions(Path file)
            throws IOException, FileFormatException {

        if (!Files.exists(file)) {
            AppLogger.warning("File not found: " + file);
            return List.of();
        }

        AppLogger.debug("Loading transactions from: " + file);

        List<String> lines = Files.readAllLines(file);
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            try {
                Transaction transaction = parseTransaction(line);
                transactions.add(transaction);

            } catch (RuntimeException exception) {

                AppLogger.error("Invalid transaction at line " + (i + 1), exception);

                throw new FileFormatException("Invalid data at line " + (i + 1) + ": " + line, exception);
            }
        }

        AppLogger.info("Loaded " + transactions.size() + " transactions.");

        return transactions;
    }

    private Transaction parseTransaction(String line) {

        String[] parts = line.split(";", -1);

        if (parts.length != 4) {
            throw new IllegalArgumentException("Expected 4 fields");
        }

        LocalDate date = LocalDate.parse(parts[0]);
        String category = parts[1].trim();
        double amount = Double.parseDouble(parts[2]);
        TransactionType type =
                TransactionType.valueOf(parts[3].trim());

        if (category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and non-negative");
        }

        return new Transaction(date, category, amount, type);
    }
}
