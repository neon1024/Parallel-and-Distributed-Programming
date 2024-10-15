class Product:
    def __init__(self, name, price):
        self.__name = name
        self.__PRICE = price
        self.__quantity = 1

    def __init(self, name, price, quantity):
        self.__name = name
        self.__PRICE = price
        self.__quantity = quantity

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, new_name):
        self.__name = new_name

    @property
    def price(self):
        return self.__PRICE

    @property
    def quantity(self):
        return self.__quantity

    @quantity.setter
    def quantity(self, new_quantity):
        self.__quantity = new_quantity
