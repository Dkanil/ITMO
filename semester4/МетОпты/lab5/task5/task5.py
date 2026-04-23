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


def kmeans(data, k=2, max_iter=100, tol=1e-6):
    """Простой K-средних для инициализации центров."""
    np.random.seed(42)
    centers = data[np.random.choice(data.shape[0], k, replace=False)]
    
    for _ in range(max_iter):
        distances = np.sqrt(((data[:, None, :] - centers[None, :, :]) ** 2).sum(axis=2))
        labels = np.argmin(distances, axis=1)
        new_centers = np.array([data[labels == j].mean(axis=0) for j in range(k)])
        if np.allclose(centers, new_centers, atol=tol):
            break
        centers = new_centers
    
    return centers


def rbf_basis(x, y, cx, cy, sigma):
    """Считает RBF базис: exp(-(r^2 / (2*sigma^2)))"""
    r_sq = (x - cx) ** 2 + (y - cy) ** 2
    return np.exp(-r_sq / (2 * sigma**2))


def predict(params, x=X, y=Y):
    """Предсказание RBF-сети с двумя базисами."""
    c1x, c1y, sigma1, c2x, c2y, sigma2, w0, w1, w2 = params
    
    basis1 = rbf_basis(x, y, c1x, c1y, sigma1)
    basis2 = rbf_basis(x, y, c2x, c2y, sigma2)
    
    return w0 + w1 * basis1 + w2 * basis2


def mse_loss(params):
    """MSE лосс-функция."""
    predictions = predict(params)
    errors = predictions - Z
    return 0.5 * np.mean(errors**2)

loss = mse_loss


def train(initial_params):
    """Обучение через BFGS с сохранением лосса по итакциям."""
    history = [mse_loss(initial_params)]
    
    def callback(current_params):
        history.append(mse_loss(current_params))
    
    bounds = [
        (0.0, 6.0), (0.0, 6.0), (0.5, 5.0), # c1x, c1y, sigma1
        (0.0, 6.0), (0.0, 6.0), (0.5, 5.0), # c2x, c2y, sigma2
        (None, None), (None, None), (None, None) # w0, w1, w2
    ]
    
    result = minimize(
        mse_loss,
        initial_params.astype(float),
        method="L-BFGS-B",
        bounds=bounds,
        callback=callback,
        options={"gtol": 1e-12, "maxiter": 2000},
    )
    
    return result.x, np.array(history), result


def main():
    # --- Инициализация центров через K-средние ---
    data_2d = np.column_stack([X, Y])
    centers = kmeans(data_2d, k=2, max_iter=100)
    c1x, c1y = centers[0]
    c2x, c2y = centers[1]
    
    # --- Инициализация масштабов и весов ---
    sigma1_start = float(np.std(data_2d))
    sigma2_start = float(np.std(data_2d))
    
    # Инициализируем веса простой линейной регрессией на два базиса
    basis1_train = rbf_basis(X, Y, c1x, c1y, sigma1_start)
    basis2_train = rbf_basis(X, Y, c2x, c2y, sigma2_start)
    
    # Решаем систему для w0, w1, w2
    A = np.column_stack([np.ones_like(X), basis1_train, basis2_train])
    try:
        weights = np.linalg.lstsq(A, Z, rcond=None)[0]
        w0_start, w1_start, w2_start = weights
    except:
        w0_start = float(np.mean(Z))
        w1_start = float((np.max(Z) - np.min(Z)) / 2)
        w2_start = float((np.max(Z) - np.min(Z)) / 2)
    
    initial_params = np.array([
        c1x, c1y, sigma1_start,
        c2x, c2y, sigma2_start,
        w0_start, w1_start, w2_start
    ], dtype=float)
    
    # --- Обучение ---
    params, history, result = train(initial_params)
    
    c1x, c1y, sigma1, c2x, c2y, sigma2, w0, w1, w2 = params
    predictions = predict(params)
    residuals = Z - predictions # Невязки
    
    print("Задание 5. RBF-сеть (две базисные функции)")
    print("=" * 60)
    print(f"Итераций: {result.nit}")
    print(f"Финальный MSE: {history[-1]:.10f}")
    print(f"Финальный loss: {loss(params):.10f}")
    print()
    print("Аналитический вид модели:")
    print("z(x,y) = w0 + w1*exp(-((x-c1x)^2 + (y-c1y)^2)/(2*sigma1^2))")
    print("       + w2*exp(-((x-c2x)^2 + (y-c2y)^2)/(2*sigma2^2))")
    print()
    print("Параметры модели:")
    print(f"  w0 = {w0:.6f}, w1 = {w1:.6f}, w2 = {w2:.6f}")
    print(f"  c1x = {c1x:.6f}, c1y = {c1y:.6f}, sigma1 = {sigma1:.6f}")
    print(f"  c2x = {c2x:.6f}, c2y = {c2y:.6f}, sigma2 = {sigma2:.6f}")
    print()
    print("Невязки:")
    for i, (x, y, z, pred, res) in enumerate(zip(X, Y, Z, predictions, residuals), start=1):
        print(f"  Точка {i}: x={x:.2f}, y={y:.2f}, z={z:.2f}, pred={pred:.6f}, residual={res:.6f}")
    
    # --- Кривая обучения ---
    learning_iterations = np.arange(len(history))
    plt.figure(figsize=(8, 5))
    plt.plot(learning_iterations, history, marker="o", linewidth=1.5, color="tab:blue")
    plt.xlabel("Номер итерации")
    plt.ylabel("MSE")
    plt.title("Кривая обучения для RBF-сети")
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig("task5_learning_curve.png", dpi=300)
    plt.show()
    plt.close()

    # --- 3D и контурный графики ---
    x_grid = np.linspace(0, 6, 80)
    y_grid = np.linspace(0, 6, 80)
    X_grid, Y_grid = np.meshgrid(x_grid, y_grid)
    Z_grid = predict(params, X_grid, Y_grid)
    
    fig = plt.figure(figsize=(14, 6))
    
    ax1 = fig.add_subplot(121, projection="3d")
    ax1.plot_surface(X_grid, Y_grid, Z_grid, cmap="viridis", alpha=0.85)
    ax1.scatter(X, Y, Z, c="crimson", s=55, label="data")
    ax1.set_title("RBF-сеть (две базисные функции)")
    ax1.set_xlabel("X")
    ax1.set_ylabel("Y")
    ax1.set_zlabel("Z")
    ax1.legend(loc="upper left")
    
    ax2 = fig.add_subplot(122)
    contour = ax2.contourf(X_grid, Y_grid, Z_grid, levels=25, cmap="viridis", alpha=0.9)
    ax2.contour(X_grid, Y_grid, Z_grid, levels=10, colors="white", alpha=0.35, linewidths=0.5)
    ax2.scatter(X, Y, c=Z, s=120, edgecolors="black", cmap="viridis", label="data")
    # Отметим центры базисов
    ax2.scatter([c1x, c2x], [c1y, c2y], c="red", marker="x", s=200, linewidths=2, label="centers")
    ax2.set_xlabel("X")
    ax2.set_ylabel("Y")
    ax2.set_title("Линии уровня и точки данных")
    ax2.grid(True, alpha=0.25)
    ax2.legend()
    fig.colorbar(contour, ax=ax2, label="Z")
    
    plt.tight_layout()
    plt.savefig("task5_rbf_network.png", dpi=300)
    plt.show()
    plt.close()

    print()
    print("✅ Графики сохранены:")
    print("   - task5_rbf_network.png (3D и контуры)")
    print("   - task5_learning_curve.png (кривая обучения)")


if __name__ == "__main__":
    main()