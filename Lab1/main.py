from Inventory import Inventory
from Product import Product


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
            filename = input("products filename: ")

            with open(filename) as file:
                line = file.readline()

                while line:
                    tokens = line.split(";")

                    name = tokens[NAME]
                    price = tokens[PRICE]

                    print(name, price)

                    product = Product(name, price)

                    if name in inventory.product_quantity.keys():
                        inventory.product_quantity[name] += 1
                    else:
                        inventory.product_quantity[name] = 1

                    line = file.readline()

            break

        except FileNotFoundError:
            print("[!] The file doesn't exist. Try again.")


def main():
    print_task()

    inventory = Inventory()

    read_products(inventory)

    print(inventory.product_quantity)


if __name__ == "__main__":
    main()
