import java.util.*;

public class Bill {
    private final Map<String, Integer> productQuantity = new HashMap<>();
    private double total = 0;

    public void addTransaction(String product, double price, int quantity) {
        productQuantity.put(product, productQuantity.getOrDefault(product, 0) + quantity);
        total += price * quantity;
    }

    public double getTotal() {
        return total;
    }

    public Map<String, Integer> getProductsSold() {
        return Collections.unmodifiableMap(productQuantity);
    }
}
