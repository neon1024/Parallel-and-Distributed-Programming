import threading

from Bill import Bill


class Inventory:
    def __init__(self):
        self.__earnings: float = 0
        self.__product_quantity: dict[str, int] = {}
        self.__product_price: dict[str, float] = {}
        self.__bills: list[Bill] = []

    def get_products(self):
        return list(self.__product_quantity.keys())

    @property
    def earnings(self):
        return self.__earnings

    @earnings.setter
    def earnings(self, new_amount):
        self.__earnings = new_amount

    def get_quantity(self, product):
        if product in self.__product_quantity.keys():
            return self.__product_quantity[product]

        raise "[!] Product doesn't exist"

    def set_quantity(self, product, quantity=1):
        if product in self.__product_quantity.keys():
            self.__product_quantity[product] += quantity
        else:
            self.__product_quantity[product] = quantity if quantity >= 0 else 0

    def get_price(self, product):
        if product in self.__product_price.keys():
            return self.__product_price[product]

        raise "[!] Product doesn't exist"

    def set_price(self, product, price=0):
        self.__product_price[product] = price

    def get_bills(self):
        return self.__bills

    def add_bill(self, bill):
        self.__bills.append(bill)
