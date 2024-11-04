import java.util.ArrayList;
import java.util.Scanner;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ScalarProduct {
    // Shared resources between Producer and Consumer threads
    private static final ArrayList<Integer> vectorA = new ArrayList<>();
    private static final ArrayList<Integer> vectorB = new ArrayList<>();
    private static int product = 0;
    private static boolean available = false; // flag to indicate if a product is available
    private static int scalarProductSum = 0;

    // Mutex and condition variable for synchronization
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    static class Producer extends Thread {
        public void run() {
            for (int i = 0; i < vectorA.size(); i++) {
                lock.lock();
                try {
                    while (available) {
                        // Wait until the product is consumed by consumer
                        condition.await();
                    }

                    product = vectorA.get(i) * vectorB.get(i);
                    System.out.println("Producer computed: " + vectorA.get(i) + " * " + vectorB.get(i) + " = " + product);

                    available = true;
                    condition.signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class Consumer extends Thread {
        public void run() {
            for (int i = 0; i < vectorA.size(); i++) {
                lock.lock();
                try {
                    while (!available) {
                        // Wait until a product is available from producer
                        condition.await();
                    }

                    scalarProductSum += product;
                    System.out.println("Consumer added product: " + product + ", total sum = " + scalarProductSum);

                    available = false;
                    condition.signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void read_vector(ArrayList<Integer> vector, int number_of_elements) {
        Scanner scanner = new Scanner(System.in);

        int element;

        for(int i = 0; i < number_of_elements; ++i) {
            System.out.println("Element " + i + ": ");

            element = scanner.nextInt();

            vector.add(element);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Number of elements: ");

        Scanner scanner = new Scanner(System.in);

        int number_of_elements = scanner.nextInt();

        System.out.println("Vector A:");

        read_vector(vectorA, number_of_elements);

        System.out.println("Vector B:");

        read_vector(vectorB, number_of_elements);

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Final scalar product: " + scalarProductSum);
    }
}
