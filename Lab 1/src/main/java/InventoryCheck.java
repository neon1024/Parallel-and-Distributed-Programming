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

                // lock, get the bills, unlock
                for (Bill bill : inventory.getBills()) {
                    // lock
                    totalSales += bill.getTotal();
                    // unlock
                    // same here
                    for (Map.Entry<String, Integer> entry : bill.getProductsSold().entrySet()) {
                        productSoldCount.put(entry.getKey(), productSoldCount.getOrDefault(entry.getKey(), 0) + entry.getValue());
                    }
                }

                for (Map.Entry<String, Integer> entry : productSoldCount.entrySet()) {
                    String product = entry.getKey();
                    int soldQuantity = entry.getValue();
                    // lock
                    int initialQuantity = inventory.getQuantity(product) + soldQuantity;
                    System.out.println("Product: " + product + ", Initial: " + initialQuantity + ", Sold: " + soldQuantity + ", Remaining: " + inventory.getQuantity(product));
                    // unlock
                }

                System.out.println("Total earnings calculated from bills: " + totalSales);
                // lock
                System.out.println("Actual earnings: " + inventory.getEarnings());
                // get earnings
                // unlock
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
