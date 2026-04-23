import numpy as np
import matplotlib.pyplot as plt


data = np.array([
    [0.89, 0.97, 0.41],
    [2.04, 1.83, 1.79],
    [2.82, 3.05, 4.28],
    [3.84, 3.85, 2.54],
    [4.97, 4.85, 0.50]
])

Z = data[:, 2]


def mse_loss(constant_value):
    return np.mean((Z - constant_value) ** 2)

loss = mse_loss


def mae_loss(constant_value):
    return np.mean(np.abs(Z - constant_value))


def main():
    mse_constant = float(np.mean(Z))
    mae_constant = float(np.median(Z))

    mse_value = float(mse_loss(mse_constant))
    mae_value = float(mae_loss(mae_constant))

    print("Задание 3. Константная модель")
    print("=" * 60)
    print(f"MSE-оптимальная константа: {mse_constant:.6f}")
    print(f"  MSE = {mse_value:.6f}")
    print(f"  Финальный loss: {loss(mse_constant):.6f}")
    print()
    print(f"MAE-оптимальная константа: {mae_constant:.6f}")
    print(f"  MAE = {mae_value:.6f}")
    print()
    print("Резюме:")
    print("  Для MSE лучшая константа — среднее значение Z.")
    print("  Для MAE лучшая константа — медиана Z.")

    c_values = np.linspace(np.min(Z) - 2, np.max(Z) + 2, 200)
    mse_values = [mse_loss(c) for c in c_values]
    mae_values = [mae_loss(c) for c in c_values]

    plt.figure(figsize=(8, 5))
    plt.plot(c_values, mse_values, label="MSE Loss", color="tab:blue", linewidth=2)
    plt.axvline(mse_constant, color="blue", linestyle="--", label=f"MSE min ({mse_constant:.2f})")
    
    plt.plot(c_values, mae_values, label="MAE Loss", color="tab:orange", linewidth=2)
    plt.axvline(mae_constant, color="orange", linestyle="--", label=f"MAE min ({mae_constant:.2f})")

    plt.xlabel("Значение константы")
    plt.ylabel("Значение Loss функции")
    plt.title("Функции потерь (Loss) для константной модели")
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig("task3_loss_plot.png", dpi=300)
    plt.show()
    plt.close()


if __name__ == "__main__":
    main()