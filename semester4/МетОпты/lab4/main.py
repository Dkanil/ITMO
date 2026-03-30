import numpy as np

def func(x1, x2):
    return 2 * x1 ** 2 + 2 * x1 * x2 + 3 * x2 ** 2 - 10 * x1 - 10 * x2 + 15

def df(f, x, eps):
    return (f(x + eps) - f(x)) / eps

def ddf(f, x, eps):
    return (f(x + eps) - 2 * f(x) + f(x - eps)) / (eps * eps)

def nuton_method(f, x, eps):
    while True:
        df1 = df(f, x, eps)
        ddf1 = ddf(f, x, eps)
        if abs(df1) <= eps:
            return x
        x = x - df1 / ddf1

def coordinate_spusk(x, eps):
    iters = 0
    while True:
        prev = x.copy()
        x[0] = nuton_method(lambda x1: func(x1, x[1]), x[0], eps)
        x[1] = nuton_method(lambda x2: func(x[0], x2), x[1], eps)
        iters += 1
        if abs(func(prev[0], prev[1]) - func(x[0], x[1])) <= eps or np.linalg.norm(x - prev) <= eps:
            return x, func(x[0], x[1]), iters


def grad_f(x1, x2):
    return np.array([4 * x1 + 2 * x2 - 10, 2 * x1 + 6 * x2 - 10])

def gradient_spusk(x, eps, alpha=0.1, find_min=True):
    iters = 0
    while True:
        prev = x.copy()
        g = grad_f(x[0], x[1])
        if find_min:
            g = -g
        x = x + alpha * g
        iters += 1
        if np.linalg.norm(g) <= eps or abs(func(prev[0], prev[1]) - func(x[0], x[1])) <= eps:
            return x, func(x[0], x[1]), iters

def fastest_spusk(x, eps, find_min=True):
    iters = 0
    while True:
        prev = x.copy()
        g = grad_f(x[0], x[1])
        s_k = g / np.linalg.norm(g)
        if find_min:
            s_k = -s_k
        phi = lambda h: func(x[0] + h * s_k[0], x[1] + h * s_k[1]) # подставляем в функцию следующие координаты, которые зависят от h
        h = nuton_method(phi, 1, eps)

        x = x + h * s_k
        iters += 1
        if np.linalg.norm(g) < eps or abs(func(prev[0], prev[1]) - func(x[0], x[1])) <= eps:
            return x, func(x[0], x[1]), iters


def print_results(method_name, result):
    print(f"{method_name}:")
    print(f"Корень: ({result[0][0]:.5f}, {result[0][1]:.5f})")
    print(f"f(x, y): {result[1]:.5f}")
    print("Число итераций:", result[2], "\n")

def main():
    x0 = np.array([-2.0, -2.0])
    eps = 0.0001

    print("Поиск экстремума функции f(x, y) = 2x^2 + 2xy + 3y^2 - 10x - 10y + 15")
    print(f"Начальная точка: ({x0[0]}, {x0[1]})")
    print("Точность:", eps, "\n")

    print_results("Покоординатный спуск", coordinate_spusk(x0.copy(), eps))
    print_results("Градиентный спуск", gradient_spusk(x0.copy(), eps))
    print_results("Наискорейший спуск", fastest_spusk(x0.copy(), eps))

if __name__ == "__main__":
    main()