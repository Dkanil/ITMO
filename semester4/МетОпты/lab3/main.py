def f(x):
    return (x ** 4) / 4 + x ** 2 - 8 * x + 12


def quadratic_approximation(x1, dx, eps, iteration):
    x2 = x1 + dx
    f1, f2 = f(x1), f(x2)

    if f1 > f2:
        x3 = x1 + 2 * dx
    else:
        x3 = x1 - dx
    f3 = f(x3)

    while True:
        iteration += 1
        points = [(x1, f1), (x2, f2), (x3, f3)]
        points.sort(key=lambda x: x[1])
        xmin, fmin = points[0]

        num = (x2 ** 2 - x3 ** 2) * f1 + (x3 ** 2 - x1 ** 2) * f2 + (x1 ** 2 - x2 ** 2) * f3
        den = 2 * ((x2 - x3) * f1 + (x3 - x1) * f2 + (x1 - x2) * f3)

        if den == 0:
            return quadratic_approximation(xmin, dx, eps, iteration)

        x_bar = num / den
        f_bar = f(x_bar)
        err_f = abs((fmin - f_bar) / f_bar) if f_bar != 0 else 0
        err_x = abs((xmin - x_bar) / x_bar) if x_bar != 0 else 0

        print(
            f"{iteration:<7} | {x1:<8.4f} | {x2:<8.4f} | {x3:<8.4f} | {x_bar:<8.4f} | {f_bar:<10.5f} | {err_f:<10.6f} | {err_x:.6f}")

        if abs((fmin - f_bar) / f_bar) < eps and abs((xmin - x_bar) / x_bar) < eps:
            return x_bar, f_bar, iteration

        new_points = sorted([(x1, f1), (x2, f2), (x3, f3), (x_bar, f_bar)])

        idx = 1
        for i in range(len(new_points)):
            if new_points[i][0] == (x_bar if f_bar < fmin else xmin):
                idx = i
        if idx == 0 or idx == 3:
            return quadratic_approximation(x_bar, dx, eps, iteration)
        (x1, f1), (x2, f2), (x3, f3) = new_points[idx - 1], new_points[idx], new_points[idx + 1]


if __name__ == '__main__':
    print(
        f"{'Итер.':<7} | {'x1':<8} | {'x2':<8} | {'x3':<8} | {'x_bar':<8} | {'f(x_bar)':<10} | {'Погр. f':<10} | {'Погр. x'}")
    print("-" * 105)
    res_x, res_f, iters = quadratic_approximation(0, 0.5, 0.0001, 0)
    print(f"Минимум найден в точке x = {res_x:.6f}")
    print(f"Значение функции f(x) = {res_f:.6f}")
    print(f"Количество итераций: {iters}")
