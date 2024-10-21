import threading
from time import sleep


def print_task():
    print("- Create two threads, a producer and a consumer, with the producer feeding the consumer.")
    print("- Requirement: Compute the scalar product of two vectors.")
    print("- Create two threads. The first thread (producer) will compute the products of pairs")
    print("of elements (one from each vector) and will feed the second thread.")
    print("- The second thread (consumer) will sum up the products computed by the first one.")
    print("- The two threads will behind synchronized with a condition variable and a mutex.")
    print("- The consumer will be cleared to use each product as soon as it is computed by")
    print("the producer thread.")


def read_vector():
    number_of_elements = int(input("Number of elements: "))

    vector = []

    for index in range(number_of_elements):
        element = int(input(f"Element {index}: "))
        vector.append(element)

    return vector


def consumer_threadwork(args):
    print(args)


def producer_threadwork(args):
    FIRST = 0
    SECOND = 1

    element1 = args[FIRST]
    element2 = args[SECOND]

    print(args)


def main():
    print_task()

    print("Read two vectors with the same length")

    vector1 = read_vector()
    vector2 = read_vector()

    consumer = threading.Thread(target=consumer_threadwork, args=(vector1,))

    producer = threading.Thread(target=producer_threadwork, args=(vector2,))

    consumer.start()
    producer.start()

    consumer.join()
    producer.join()


if __name__ == "__main__":
    main()
