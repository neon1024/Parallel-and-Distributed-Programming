class Data:
    def __init__(self):
        self.__money = 0
        self.__product_quantity = {}
        self.__bills = []  # TODO Each bill is a list of items and quantities sold in a single operation, and their total price

    @property
    def money(self):
        return self.__money

    @money.setter
    def money(self, new_amount):
        self.__money = new_amount

    @property
    def product_quantity(self):
        return self.__product_quantity

    @product_quantity.setter
    def product_quantity(self, new_data):
        self.__product_quantity = new_data

    @property
    def bills(self):
        return self.__bills

    @bills.setter
    def bills(self, new_bills):
        self.__bills = new_bills
