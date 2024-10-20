class Bill:
    def __init__(self):
        self.__product_quantity: dict[str, int] = {}
        self.__total: float = 0

    def add_transaction(self, name, cost, quantity=1):
        if name in self.__product_quantity.keys():
            self.__product_quantity[name] += quantity
        else:
            self.__product_quantity[name] = quantity

        self.__total += cost
