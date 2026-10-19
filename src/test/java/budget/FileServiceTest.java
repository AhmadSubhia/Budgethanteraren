package budget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveAndLoadTransactions() throws Exception {
        FileService fileService = new FileService();
        Path file = tempDir.resolve("transactions.csv");

        List<Transaction> original = List.of(
                new Transaction(
                        LocalDate.of(2026, 10, 12),
                        "Lon", 20000, TransactionType.INKOMST),

                new Transaction(LocalDate.of(2026, 10, 12),
                        "Mat", 500, TransactionType.UTGIFT));

        fileService.saveTransactions(original, file);
        List<Transaction> loaded = fileService.loadTransactions(file);

        assertEquals(original, loaded);
    }

    @Test
    void shouldReturnEmptyListWhenFileDoesNotExist()
            throws IOException, FileFormatException {


        FileService fileService = new FileService();
        Path file = tempDir.resolve("missing.csv");

        List<Transaction> result = fileService.loadTransactions(file);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowFileFormatExceptionForInvalidLine()
            throws IOException {
        FileService fileService = new FileService();
        Path file = tempDir.resolve("invalid.csv");

        Files.writeString(file, "this is not valid CSV");


        assertThrows(FileFormatException.class, () -> fileService.loadTransactions(file));
    }
}
