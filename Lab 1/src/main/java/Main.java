import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        printTask();

        Inventory inventory = new Inventory();

        readProducts(inventory);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Maximum number of threads allowed: ");

        int maximumNumberOfThreads = scanner.nextInt();
        scanner.close();

        int numberOfThreads = new Random().nextInt(maximumNumberOfThreads);

        List<Thread> threads = new ArrayList<>();

        Lock mutex = new ReentrantLock();

        // Create threads
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(() -> sellOperation(mutex, inventory)));
        }

        // Start inventory check thread
        InventoryCheck inventoryCheck = new InventoryCheck(inventory);
        Thread inventoryCheckThread = new Thread(inventoryCheck);

        inventoryCheckThread.start();

        // Start sale threads
        for (Thread thread : threads) {
            thread.start();
            Thread.sleep(1000);
        }

        // Join sale threads
        for (Thread thread : threads) {
            thread.join();
        }

        // Signal inventory check to stop
        inventoryCheck.stopChecking();
        inventoryCheckThread.join();

        System.out.println("Remaining products:");
        for (String product : inventory.getProducts()) {
            System.out.println(product + ": " + inventory.getQuantity(product));
        }
    }

    public static void printTask() {
        System.out.println("1. Supermarket inventory:");
        System.out.println("There are several types of products, each having a known, constant, unit price. In the beginning, we know the quantity of each product.");
        System.out.println("We must keep track of the quantity of each product, the amount of money (initially zero), and the list of bills, corresponding to sales.");
        System.out.println("Each bill is a list of items and quantities sold in a single operation, and their total price.");
        System.out.println("We have sale operations running concurrently, on several threads. Each sale decreases the amounts of available products, increases the amount of money, and adds a bill to a record of all sales.");
        System.out.println("From time to time, as well as at the end, an inventory check operation shall be run. It shall check that all the sold products and all the money are justified by the recorded bills.");
        System.out.println("Two sales involving distinct products must be able to update their quantities independently.");
    }

    public static void readProducts(Inventory inventory) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Products filename: ");
                String filename = scanner.nextLine();

                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(";");
                    String name = tokens[0].trim();
                    double price = Double.parseDouble(tokens[1].trim());

                    inventory.setQuantity(name, 1);
                    inventory.setPrice(name, price);
                }

                reader.close();
                break;

            } catch (FileNotFoundException e) {
                System.out.println("[!] The file doesn't exist. Try again.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sellOperation(Lock mutex, Inventory inventory) {
        String productToSell;
        int quantityToSell;

        mutex.lock();
        try {
            Map.Entry<String, Integer> productEntry = chooseRandomProductToSell(inventory);
            if (productEntry == null) return;

            productToSell = productEntry.getKey();
            quantityToSell = productEntry.getValue();

            inventory.setQuantity(productToSell, -quantityToSell);

            Bill bill = new Bill();
            double price = inventory.getPrice(productToSell);
            bill.addTransaction(productToSell, price, quantityToSell);

            inventory.addBill(bill);

            // Update earnings
            inventory.addEarnings(price * quantityToSell);

        } finally {
            mutex.unlock();
        }
    }

    public static Map.Entry<String, Integer> chooseRandomProductToSell(Inventory inventory) {
        List<String> availableProducts = inventory.getProducts()
                .stream()
                .filter(product -> inventory.getQuantity(product) > 0)
                .collect(Collectors.toList());

        if (availableProducts.isEmpty()) {
            return null;
        }

        String chosenProduct = availableProducts.get(new Random().nextInt(availableProducts.size()));
        int sellQuantity = new Random().nextInt(inventory.getQuantity(chosenProduct)) + 1;

        return new AbstractMap.SimpleEntry<>(chosenProduct, sellQuantity);
    }
}
