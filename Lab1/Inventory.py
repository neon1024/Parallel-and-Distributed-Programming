import threading

from Bill import Bill


class Inventory:
    def __init__(self):
        self.__earnings: float = 0
        self.__product_quantity: dict[str, int] = {}
        self.__product_price: dict[str, float] = {}
        self.__bills: list[Bill] = []
        self.__locks: dict[str, threading.Lock] = {}
        self.__products_lock = threading.Lock()
        self.__earnings_lock = threading.Lock()
        self.__bills_lock = threading.Lock()

    def get_products(self):
        with self.__products_lock:
            return list(self.__product_quantity.keys())

    @property
    def earnings(self):
        with self.__earnings_lock:
            return self.__earnings

    @earnings.setter
    def earnings(self, new_amount):
        with self.__earnings_lock:
            self.__earnings = new_amount

    def get_quantity(self, product):
        if product not in self.__locks.keys():
            self.__locks[product] = threading.Lock()

        with self.__locks[product]:
            if product in self.__product_quantity.keys():
                return self.__product_quantity[product]

            raise "[!] Product doesn't exist"

    def set_quantity(self, product, quantity=1):
        if product not in self.__locks.keys():
            self.__locks[product] = threading.Lock()

        with self.__locks[product]:
            if product in self.__product_quantity.keys():
                self.__product_quantity[product] += quantity
            else:
                self.__product_quantity[product] = quantity

    def get_price(self, product):
        if product not in self.__locks.keys():
            self.__locks[product] = threading.Lock()

        with self.__locks[product]:
            if product in self.__product_price.keys():
                return self.__product_price[product]

            raise "[!] Product doesn't exist"

    def set_price(self, product, price=0):
        if product not in self.__locks.keys():
            self.__locks[product] = threading.Lock()

        with self.__locks[product]:
            self.__product_price[product] = price

    def get_bills(self):
        with self.__bills_lock:
            return self.__bills

    def add_bill(self, bill):
        with self.__bills_lock:
            self.__bills.append(bill)
