import ctypes
import os
import random

class Point(ctypes.Structure):
    _fields_ = [("x", ctypes.c_int),
                ("y", ctypes.c_int)]

def download_library():
    # Определение имени файла библиотеки в зависимости от ОС
    lib_name = "point_lib.dll" if os.name == 'nt' else "point_lib.so"
    lib_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), lib_name)

    try:
        # Загрузка C-библиотеки
        c_lib = ctypes.CDLL(lib_path)
        print(f"Библиотека успешно загружена: {lib_path}")
    except Exception as e:
        print(f"Ошибка: не удалось загрузить библиотеку: {lib_path}")
        print(f"Детали ошибки: {e}")
        exit()

    c_lib.process_point.argtypes = (ctypes.POINTER(Point), ctypes.POINTER(Point))
    c_lib.process_point.restype = ctypes.c_double
    c_lib.process.argtypes = (ctypes.POINTER(Point), ctypes.c_int, ctypes.POINTER(ctypes.c_double))
    c_lib.process.restype = None
    return c_lib

if __name__ == "__main__":
    print("Start program")
    
    c_lib = download_library()

    # 1. Создание файла с 1000 парами чисел
    f = open("test.txt", "w")
    arr_len = 10 // 2 # Количество пар точек
    range_min = -1000
    range_max = 1000
    for i in range(arr_len):
        f.write(f"{random.randint(range_min, range_max)},{random.randint(range_min, range_max)} {random.randint(range_min, range_max)},{random.randint(range_min, range_max)}\n")
    f.close()

    # 2. Чтение файла и заполнение массива
    data = []
    with open("test.txt", "r") as f:
        for line in f:
            x1, y1, x2, y2 = map(int, line.strip().replace(',', ' ').split())
            data.append(Point(x1, y1))
            data.append(Point(x2, y2))

    # 3. Массив содержит класс Point
    ArrayType = Point * len(data)   # Создаем тип "массив из 2 * 1000 точек"
    c_array = ArrayType(*data)      # Создаем экземпляр этого массива

    # 4. Передаём массив в функцию, написанную на C и возвращаем результат
    results = (ctypes.c_double * (len(c_array) // 2))()  # Массив для результатов
    c_lib.process(c_array, len(c_array), results)

    # 6. Результаты выводим из кода питона
    for i in range(0, arr_len, 2):
        print(f"Расстояние между точками ({c_array[i].x}, {c_array[i].y}) и ({c_array[i + 1].x}, {c_array[i + 1].y}) = {results[i // 2]}")