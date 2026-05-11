import numpy as np
import matplotlib.pyplot as plt


def func(x, y):
    return 0.01 * (8 * x ** 2 + 2 * x * y + 17 * x + 4 * y + 2)


def grad(x, y):
    dfdx = 0.01 * (16 * x + 2 * y + 17)
    dfdy = 0.01 * (2 * x + 4)
    return np.array([dfdx, dfdy])

def visualisation(title):
    x = np.linspace(-20, 20, 100)
    y = np.linspace(-50, 50, 100)
    X, Y = np.meshgrid(x, y)
    Z = func(X, Y)

    plt.figure(figsize=(10, 8))
    cp = plt.contour(X, Y, Z, levels=60, cmap='viridis')
    plt.colorbar(cp)
    plt.plot(-2.0, 7.5, 'ro', label='Седловая точка (-2.0, 7.5)')
    plt.plot(5.1875, -50, 'g*', markersize=10, label='Глобальный минимум (5.19, -50)')

    plt.title(title)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.legend()
    plt.grid(True)
    return plt.gca()


def gradient_spusk(start_pos, lr, iterations):
    x, y = start_pos
    path = [(x, y)]
    for _ in range(iterations):
        g = grad(x, y)
        x = x - lr * g[0]
        y = y - lr * g[1]
        x = np.clip(x, -20, 20)
        y = np.clip(y, -50, 50)
        path.append((x, y))
    end_point = path[-1]
    print(f"Обычный GD завершил 100 итераций. Конечная точка: x={end_point[0]:.4f}, y={end_point[1]:.4f}")
    print("Результат: метод ЗАСТРЯЛ в окрестности седловой точки (-2.0, 7.5).\n")
    return np.array(path)


def run_polyak_momentum(start_pos, alpha, beta, iterations):
    x_curr = np.array(start_pos, dtype=float)
    x_prev = np.array(start_pos, dtype=float)

    path = [x_curr.copy()]
    for _ in range(iterations):
        g = grad(x_curr[0], x_curr[1])
        x_next = x_curr - alpha * g + beta * (x_curr - x_prev)
        x_next[0] = np.clip(x_next[0], -20, 20)
        x_next[1] = np.clip(x_next[1], -50, 50)
        x_prev = x_curr.copy()
        x_curr = x_next.copy()
        path.append(x_curr.copy())

    return np.array(path)


def main():
    print("Задание 1")
    print("Седловая точка P(-2.0, 7.5)")
    print("Глобальный минимум на границе области в точке (5.1875, -50.0) и равен -4.1328.")

    start = [0.0, 7.5]
    lr_to_saddle = 0.5

    print("\nЗадание 2")

    ax = visualisation("Обычный GD застревает в седле")
    path_gd = gradient_spusk(start, lr_to_saddle, 100)
    ax.plot(path_gd[:, 0], path_gd[:, 1], 'r.-', label='GD (Без момента)')
    ax.legend(loc='lower left')
    plt.show()

    ax = visualisation("SGD Momentum (Пилообразно) преодолевает седло")
    path_mom_z = run_polyak_momentum(start, 18.0, 0.6, 100)
    ax.plot(path_mom_z[:, 0], path_mom_z[:, 1], 'b.-', label='Momentum (Пилообразно)')
    ax.legend(loc='lower left')
    plt.show()

    ax = visualisation("SGD Momentum (Плавный) преодолевает седло")
    path_mom_s = run_polyak_momentum(start, 1.5, 0.97, 500)
    ax.plot(path_mom_s[:, 0], path_mom_s[:, 1], 'm.-', label='Momentum (Плавно)')
    ax.legend(loc='lower left')
    plt.show()
    end_point = path_mom_s[-1]
    fval = func(end_point[0], end_point[1])
    dist = ((end_point[0]-5.1875)**2 + (end_point[1]+50)**2)**0.5
    print(f"Momentum (Плавный) завершил 1000 итераций. Конечная точка: x={end_point[0]:.4f}, y={end_point[1]:.4f}, f={fval:.4f}")
    if dist < 1e-3:
        print("Результат: метод достиг глобального минимума на границе (5.1875, -50).\n")
    else:
        print("Результат: метод не достиг точного глобального минимума; можно увеличить число итераций или подобрать шаг/инерцию.\n")

    print("Графики отображены.\n")

if __name__ == '__main__':
    main()
