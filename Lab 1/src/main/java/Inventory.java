import java.util.*;
import java.util.concurrent.locks.*;

public class Inventory {
    private double earnings = 0;
    private final Map<String, Integer> productQuantity = new HashMap<>();
    private final Map<String, Double> productPrice = new HashMap<>();
    private final List<Bill> bills = new ArrayList<>();
    private final Map<String, Lock> locks = new HashMap<>();
    private final Lock earningsLock = new ReentrantLock();
    private final Lock billsLock = new ReentrantLock();
    private final Lock productsLock = new ReentrantLock();

    public List<String> getProducts() {
        productsLock.lock();
        try {
            return new ArrayList<>(productQuantity.keySet());
        } finally {
            productsLock.unlock();
        }
    }

    public double getEarnings() {
        earningsLock.lock();
        try {
            return earnings;
        } finally {
            earningsLock.unlock();
        }
    }

    public void addEarnings(double amount) {
        earningsLock.lock();
        try {
            earnings += amount;
        } finally {
            earningsLock.unlock();
        }
    }

    public int getQuantity(String product) {
        synchronized (getLockForProduct(product)) {
            return productQuantity.getOrDefault(product, 0);
        }
    }

    public void setQuantity(String product, int quantity) {
        synchronized (getLockForProduct(product)) {
            productQuantity.put(product, productQuantity.getOrDefault(product, 0) + quantity);
        }
    }

    public double getPrice(String product) {
        synchronized (getLockForProduct(product)) {
            return productPrice.getOrDefault(product, 0.0);
        }
    }

    public void setPrice(String product, double price) {
        synchronized (getLockForProduct(product)) {
            productPrice.put(product, price);
        }
    }

    public void addBill(Bill bill) {
        billsLock.lock();
        try {
            bills.add(bill);
        } finally {
            billsLock.unlock();
        }
    }

    public List<Bill> getBills() {
        billsLock.lock();
        try {
            return new ArrayList<>(bills);
        } finally {
            billsLock.unlock();
        }
    }

    private synchronized Lock getLockForProduct(String product) {
        return locks.computeIfAbsent(product, k -> new ReentrantLock());
    }
}
