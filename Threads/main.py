import threading
import time


def thread_work1():
    print("Teo")


def thread_work2(parameter):
    print(parameter)


def thread_work3():
    print("Buna")
    time.sleep(5)
    print("Siua")


def main():
    thread1 = threading.Thread(target=thread_work1)
    thread2 = threading.Thread(target=thread_work2, args=("Teo\n",))

    thread1.start()
    thread2.start()

    print(thread1.name)
    print(thread2.name)

    print(thread1.is_alive())
    print(thread2.is_alive())

    thread3 = threading.Thread(target=thread_work3)
    thread4 = threading.Thread(target=thread_work3)

    thread3.start()

    thread3.join()
    print("thread 3 finished")

    thread4.start()

    thread1.join()
    thread2.join()
    thread4.join()


if __name__ == '__main__':
    main()
