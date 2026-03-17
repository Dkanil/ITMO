import math

def proizv(f, x, eps):
    return (f(x + eps) - f(x)) / eps
def proizv2(f, x, eps):
    return (f(x + eps) - 2 * f(x) + f(x - eps)) / (eps * eps)

def half_del_method(func, otrezok, eps, find_max):
    a, b = otrezok
    while b - a > 2 * eps:
        x1 = (a + b - eps) / 2
        x2 = (a + b + eps) / 2
        f1, f2 = func(x1), func(x2)
        if f1 > f2:
            if not find_max:
                a = x1
            else:
                b = x2
        else:
            if not find_max:
                b = x2
            else:
                a = x1
    return (a + b) / 2, func((a + b) / 2)

def golden_section_method(func, otrezok, eps, find_max):
    a, b = otrezok
    phi = (1 + math.sqrt(5)) / 2
    x1 = a + (b - a) * (1 - 1 / phi) # a + (b - a) * 0.382
    x2 = a + (b - a) / phi # a + (b - a) * 0.618
    f1, f2 = func(x1), func(x2)
    while b - a > eps:
        if ((f1 >= f2) and find_max) or ((f1 < f2) and not find_max):
            b = x2
            x2 = x1
            x1 = a + (b - a) * (1 - 1 / phi)
            f2 = f1
            f1 = func(x1)
        else:
            a = x1
            x1 = x2
            f1 = f2
            x2 = a + (b - a) / phi
            f2 = func(x2)
    return (a + b) / 2, func((a + b) / 2)

def hord_method(func, otrezok, eps):
    a, b = otrezok
    df_a = proizv(func, a, eps)
    df_b = proizv(func, b, eps)
    x = a
    while abs(b - a) > eps:
        x = a - df_a * (a - b) / (df_a - df_b)
        df_x = proizv(func, x, eps)
        if abs(df_x) < eps:
            return x, func(x)
        if df_a * df_x < 0:
            b = x
            df_b = df_x
        else:
            a = x
            df_a = df_x
    return x, func(x)

def nuton_method(func, otrezok, eps):
    a, b = otrezok
    x = (a + b) / 2
    while True:
        df = proizv(func, x, eps)
        ddf = proizv2(func, x, eps)
        if abs(df) <= eps:
            return x, func(x)
        x = x - df / ddf

def main():
    eps = 0.0001
    func = lambda x: 1.826 * (math.sin(x)**3 + math.cos(x)**2 - 0.5 * math.sin(2 * x))
    print("----Поиск 3х минимумов и 2х максимумов 1.826 * (sin^3(x) + cos^2(x) - 0.5sin(2x)) на [0, 8]----\n")

    print("На графике можно заметить, что экстремумы достигаются на отрезках:\n"
          "[0; 1,5], [1,5; 3], [4; 5], [5,5; 6,5], [6,5; 7,5]\n")
    otrezki = [(0, 1.5), (1.5, 3), (4, 5), (5.5, 6.5), (6.5, 7.5)]

    mi = 0
    print("Методом половинного деления найдем экстремумы на каждом отрезке:")
    for i in otrezki:
        point, value = half_del_method(func, i, eps, mi % 2)
        print(f"На отрезке {i}: x = {point:.4f}, y = {value:.4f}")
        mi += 1

    print("\n---Далее рассмотрим только минимум на первом отрезке [0; 1,5]---")
    point, value = golden_section_method(func, otrezki[0], eps, 0)
    print(f"Метод золотого сечения: x = {point:.4f}, y = {value:.4f}")

    point, value = hord_method(func, otrezki[0], eps)
    print(f"Метод хорд: x = {point:.4f}, y = {value:.4f}")

    point, value = nuton_method(func, otrezki[0], eps)
    print(f"Метод Ньютона: x = {point:.4f}, y = {value:.4f}")

if __name__ == '__main__':
    main()