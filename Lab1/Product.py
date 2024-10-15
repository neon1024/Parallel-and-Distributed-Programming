class Product:
    def __init__(self, name, price):
        self.__name = name
        self.__PRICE = price

    @property
    def name(self):
        return self.__name

    @property
    def price(self):
        return self.__PRICE
