import java.util.HashMap;
import java.util.Map;

public class InventoryCheck implements Runnable {
    private final Inventory inventory;
    private volatile boolean stop = false;

    public InventoryCheck(Inventory inventory) {
        this.inventory = inventory;
    }

    public void run() {
        while (!stop) {
            try {
                Thread.sleep(2000);
                double totalSales = 0;
                Map<String, Integer> productSoldCount = new HashMap<>();

                for (Bill bill : inventory.getBills()) {
                    totalSales += bill.getTotal();
                    for (Map.Entry<String, Integer> entry : bill.getProductsSold().entrySet()) {
                        productSoldCount.put(entry.getKey(), productSoldCount.getOrDefault(entry.getKey(), 0) + entry.getValue());
                    }
                }

                for (Map.Entry<String, Integer> entry : productSoldCount.entrySet()) {
                    String product = entry.getKey();
                    int soldQuantity = entry.getValue();
                    int initialQuantity = inventory.getQuantity(product) + soldQuantity;
                    System.out.println("Product: " + product + ", Initial: " + initialQuantity + ", Sold: " + soldQuantity + ", Remaining: " + inventory.getQuantity(product));
                }

                System.out.println("Total earnings calculated from bills: " + totalSales);
                System.out.println("Actual earnings: " + inventory.getEarnings());

                assert totalSales == inventory.getEarnings() : "[!] Earnings mismatch!";

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopChecking() {
        stop = true;
    }
}
