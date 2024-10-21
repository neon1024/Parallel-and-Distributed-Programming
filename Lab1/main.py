import random
import threading
from time import sleep

from Bill import Bill
from Inventory import Inventory


def print_task():
    print("1. Supermarket inventory:")
    print("There are several types of products, each having a known, constant, unit price. In the beginning, we know the quantity of each product.")
    print("We must keep track of the quantity of each product, the amount of money (initially zero), and the list of bills, corresponding to sales. "
          "Each bill is a list of items and quantities sold in a single operation, and their total price.")
    print("We have sale operations running concurrently, on several threads. "
          "Each sale decreases the amounts of available products (corresponding to the sold items), "
          "increases the amount of money, and adds a bill to a record of all sales.")
    print("From time to time, as well as at the end, an inventory check operation shall be run. "
          "It shall check that all the sold products and all the money are justified by the recorded bills.")
    print("Two sales involving distinct products must be able to update their quantities independently (without having to wait for the same mutex).")


def read_products(inventory):
    NAME = 0
    PRICE = 1

    while True:
        try:
            filename = input("Products filename: ")

            with open(filename) as file:
                line = file.readline()

                while line:
                    tokens = line.split(";")

                    name = tokens[NAME].strip()
                    price = float(tokens[PRICE].strip())

                    inventory.set_quantity(name)

                    inventory.set_price(name, price)

                    line = file.readline()

            break

        except FileNotFoundError:
            print("[!] The file doesn't exist. Try again.")


def sell_operation(args):
    MUTEX = 0
    INVENTORY = 1

    mutex = args[MUTEX]
    inventory = args[INVENTORY]

    product_to_sell, quantity_to_sell = choose_a_random_product_to_sell(inventory)

    if not product_to_sell or not quantity_to_sell:
        return

    # remove the amount of products to sell from the inventory
    inventory.set_quantity(product_to_sell, -quantity_to_sell)

    # create the bill
    bill = Bill()

    bill.add_transaction(product_to_sell, inventory.get_price(product_to_sell), quantity_to_sell)

    inventory.add_bill(bill)

    # update the earnings
    inventory.earnings += inventory.get_price(product_to_sell)*quantity_to_sell


def inventory_check(args):
    INVENTORY = 0
    STOP_EVENT = 1

    inventory = args[INVENTORY]
    stop_event = args[STOP_EVENT]

    # TODO condition variable OR lock the transactions when performing a check
    while not stop_event.is_set():
        sleep(2)

        # Compute total sales based on bills
        total_sales = 0
        product_sold_count = {}

        for bill in inventory.get_bills():
            total_sales += bill.get_total()

            for product, quantity in bill.get_products_sold().items():
                if product in product_sold_count:
                    product_sold_count[product] += quantity
                else:
                    product_sold_count[product] = quantity

        # Compare product counts in bills vs remaining inventory
        for product, sold_quantity in product_sold_count.items():
            initial_quantity = inventory.get_quantity(product) + sold_quantity  # Initial = remaining + sold
            print(f"Product: {product}, Initial: {initial_quantity}, Sold: {sold_quantity}, Remaining: {inventory.get_quantity(product)}")

        # Check total earnings
        print(f"Total earnings calculated from bills: {total_sales}")
        print(f"Actual earnings: {inventory.earnings}")

        assert total_sales == inventory.earnings, "[!] Earnings mismatch!"


def inventory_check_thread_work(inventory):
    stop_event = threading.Event()

    thread = threading.Thread(target=inventory_check, args=([inventory, stop_event],))
    thread.start()

    return thread, stop_event


def choose_a_random_product_to_sell(inventory):
    # filter products with quantities greater than 0
    available_products = [product for product in inventory.get_products() if inventory.get_quantity(product) > 0]

    if not available_products:
        return None, None

    # choose a random product from the available ones
    chosen_product = random.choice(available_products)

    # choose randomly how many to sell (1 to available stock)
    sell_quantity = random.randint(1, inventory.get_quantity(chosen_product))

    return chosen_product, sell_quantity


def main():
    print_task()

    inventory = Inventory()

    read_products(inventory)

    maximum_number_of_threads = int(input("Maximum number of threads allowed: "))

    number_of_threads = random.randint(1, maximum_number_of_threads)

    threads = []

    # create a sync mechanism
    mutex = threading.Lock()

    # create the threads
    for _ in range(number_of_threads):
        threads.append(threading.Thread(target=sell_operation, args=([mutex, inventory],)))

    # initialize and start the thread that does the inventory check
    inventory_check_thread, stop_event = inventory_check_thread_work(inventory)

    # start all threads
    for thread in threads:
        thread.start()
        sleep(1)

    # end all threads
    for thread in threads:
        thread.join()

    stop_event.set()

    inventory_check_thread.join()

    print("Remaining products:")
    for product in inventory.get_products():
        print(product, inventory.get_quantity(product))


if __name__ == "__main__":
    main()
