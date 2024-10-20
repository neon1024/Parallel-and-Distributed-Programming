from Bill import Bill


class Inventory:
    def __init__(self):
        self.__earnings: float = 0
        self.__product_quantity: dict[str, int] = {}
        self.__bills: list[Bill] = []

    @property
    def earnings(self):
        return self.__earnings

    @earnings.setter
    def earnings(self, new_amount):
        self.__earnings = new_amount

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
    def bills(self, new_data):
        self.__bills = new_data
