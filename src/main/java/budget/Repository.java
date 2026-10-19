package budget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    public List<T> findWhere(Predicate<T> condition) {
        return items.stream().filter(condition).toList();
    }

    public int size() {
        return items.size();
    }
}