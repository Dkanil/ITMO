import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import minimize


data = np.array([
    [0.89, 0.97, 0.41],
    [2.04, 1.83, 1.79],
    [2.82, 3.05, 4.28],
    [3.84, 3.85, 2.54],
    [4.97, 4.85, 0.50]
])

X = data[:, 0]
Y = data[:, 1]
Z = data[:, 2]


def unpack_params(params):
    alpha, beta, gamma, x0, y0, z0 = params
    a = np.exp(alpha)
    b = np.exp(beta)
    c = 2.0 * np.tanh(gamma) * np.sqrt(a * b) * 0.999
    return a, b, c, x0, y0, z0


def paraboloid(x, y, params):
    a, b, c, x0, y0, z0 = unpack_params(params)
    dx = x - x0
    dy = y - y0
    return z0 - (a * dx**2 + b * dy**2 + c * dx * dy)


def mse_loss(params):
    predictions = paraboloid(X, Y, params)
    errors = predictions - Z
    return 0.5 * np.mean(errors**2)

loss = mse_loss


def train(initial_params):
    history = [mse_loss(initial_params)]

    def callback(current_params):
        history.append(mse_loss(current_params))

    bounds = [
        (None, None), (None, None), (None, None), # alpha, beta, gamma
        (0.0, 6.0),  # x0 должен быть в пределах данных
        (0.0, 6.0),  # y0 должен быть в пределах данных 
        (0.0, 10.0)  # z0 ограничиваем сверху, чтобы центр не улетал в бесконечность
    ]

    result = minimize(
        mse_loss,
        initial_params.astype(float),
        method="L-BFGS-B",
        bounds=bounds,
        callback=callback,
        options={"gtol": 1e-12, "maxiter": 1000},
    )
    return result.x, np.array(history), result


def main():
    center_index = int(np.argmax(Z))
    initial_params = np.array([
        0.0, # np.log(1.0) = 0.0 -> a=1.0
        0.0, # np.log(1.0) = 0.0 -> b=1.0
        0.0, # gamma=0 -> c=0
        X[center_index],
        Y[center_index],
        float(Z[center_index]) + 0.5, # стартуем чуть выше максимума
    ])
    params, history, result = train(initial_params)

    a, b, c, x0, y0, z0 = unpack_params(params)
    predictions = paraboloid(X, Y, params)
    residuals = Z - predictions
    elliptic_measure = 4 * a * b - c**2

    print("Задание 2. Эллиптический параболоид")
    print("=" * 60)
    print(f"Итераций: {result.nit}")
    print(f"Финальный MSE: {history[-1]:.10f}")
    print(f"Финальный loss: {loss(params):.10f}")
    print()
    print("Аналитический вид модели:")
    print("z(x,y) = z0 - (a*(x-x0)^2 + b*(y-y0)^2 + c*(x-x0)*(y-y0))")
    print(f"a = {a:.6f}, b = {b:.6f}, c = {c:.6f}, x0 = {x0:.6f}, y0 = {y0:.6f}, z0 = {z0:.6f}")
    print(f"Проверка эллиптичности: 4ab - c^2 = {elliptic_measure:.6f}")
    print()
    print("Невязки:")
    for i, (x, y, z, pred, res) in enumerate(zip(X, Y, Z, predictions, residuals), start=1):
        print(f"  Точка {i}: x={x:.2f}, y={y:.2f}, z={z:.2f}, pred={pred:.6f}, residual={res:.6f}")

    learning_iterations = np.arange(len(history))
    plt.figure(figsize=(8, 5))
    plt.plot(learning_iterations, history, marker="o", linewidth=1.8, color="tab:blue")
    plt.xlabel("Номер итерации")
    plt.ylabel("MSE")
    plt.title("Кривая обучения для эллиптического параболоида")
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig("task2_learning_curve.png", dpi=300)
    plt.show()
    plt.close()

    x_grid = np.linspace(0, 6, 80)
    y_grid = np.linspace(0, 6, 80)
    X_grid, Y_grid = np.meshgrid(x_grid, y_grid)
    Z_grid = paraboloid(X_grid, Y_grid, params)

    fig = plt.figure(figsize=(14, 6))

    ax1 = fig.add_subplot(121, projection="3d")
    ax1.plot_surface(X_grid, Y_grid, Z_grid, cmap="viridis", alpha=0.85)
    ax1.scatter(X, Y, Z, c="crimson", s=55, label="data")
    ax1.set_title("Эллиптический параболоид")
    ax1.set_xlabel("X")
    ax1.set_ylabel("Y")
    ax1.set_zlabel("Z")
    ax1.legend(loc="upper left")

    ax2 = fig.add_subplot(122)
    contour = ax2.contourf(X_grid, Y_grid, Z_grid, levels=25, cmap="viridis", alpha=0.9)
    ax2.contour(X_grid, Y_grid, Z_grid, levels=10, colors="white", alpha=0.35, linewidths=0.5)
    ax2.scatter(X, Y, c=Z, s=120, edgecolors="black", cmap="viridis", label="data")
    ax2.set_xlabel("X")
    ax2.set_ylabel("Y")
    ax2.set_title("Линии уровня и точки данных")
    ax2.grid(True, alpha=0.25)
    ax2.legend()
    fig.colorbar(contour, ax=ax2, label="Z")

    plt.tight_layout()
    plt.savefig("task2_paraboloid.png", dpi=300)
    plt.show()
    print()
    print("График сохранён в task2_paraboloid.png")
    print("Кривая обучения сохранена в task2_learning_curve.png")


if __name__ == "__main__":
    main()