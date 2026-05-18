import numpy as np
import pandas as pd
from scipy import stats
from matplotlib import pyplot as plt

def linear(y, y_func, n, tss):
    e = y - y_func
    rss = np.sum(e ** 2) # Остаточная сумма квадратов
    s2 = rss / (n - 2) # Несмещённая оценка дисперсии ошибки
    s = np.sqrt(s2)
    r2 = 1 - rss / tss # Коэффициент детерминации
    a = 100 * np.mean(np.abs(e / y)) # Средняя ошибка аппроксимации
    return s, r2, a, e, rss

def errors(y, y_func, n, tss):
    s, r2, a_err, e, _ = linear(y, y_func, n, tss)
    return s, r2, a_err, e

def main():
    data = pd.read_csv('data.csv')
    x = data['x'].values.astype(float)
    y = data['y'].values.astype(float)
    n = len(x)
    x_star = 494.1171
    x_mean = x.mean()
    y_mean = y.mean()
    print(f"n = {n}")
    print(f"x* = {x_star}")
    print(f"x_mean = {x.mean():.4f}")
    print(f"y_mean = {y.mean():.4f}")


    print("\n" + "-" * 20 + " Линейная модель y = a + bx " + "-" * 20)
    sxx_lin = np.sum((x - x_mean) ** 2)
    sxy_lin = np.sum((x - x_mean) * (y - y_mean))
    print(f"sxx = {sxx_lin:.4f}")
    print(f"sxy = {sxy_lin:.4f}")
    tss = np.sum((y - y_mean) ** 2)
    print(f"syy (tss) = {tss:.4f}")
    b_lin = sxy_lin / sxx_lin
    a_lin = y_mean - b_lin * x_mean
    print(f"a = {a_lin:.4f}")
    print(f"b = {b_lin:.4f}")
    y_lin = a_lin + b_lin * x
    s_lin, r2_lin, a_err_lin, e_lin, rss_lin = linear(y, y_lin, n, tss)

    print("\n" + "-" * 20 + " Статистические показатели " + "-" * 20)
    print(f"\nrss = {rss_lin:.4f}")
    print(f"s^2 = {s_lin**2:.4f}")
    print(f"s = {s_lin:.4f}")
    se_b = s_lin / np.sqrt(sxx_lin)
    se_a = s_lin * np.sqrt(1 / n + x_mean ** 2 / sxx_lin)
    print(f"se_a = {se_a:.4f}")
    print(f"se_b = {se_b:.4f}")
    t_crit = stats.t.ppf(0.975, df=n - 2)
    print(f"t_crit = {t_crit:.4f}")
    print(f"ДИ для b: [{b_lin - t_crit * se_b:.6f}; {b_lin + t_crit * se_b:.6f}]")
    print(f"ДИ для a: [{a_lin - t_crit * se_a:.6f}; {a_lin + t_crit * se_a:.6f}]")
    t_nabl = b_lin / se_b
    print(f"t_набл = {t_nabl:.4f}")

    print("\n" + "-" * 20 + " Таблица остатков " + "-" * 20)
    print(f"\n{'i':>3} {'x':>6} {'y':>6} {'y_func':>8} {'e':>8}")
    for i in range(n):
        print(f"{i+1:>3} {x[i]:>6.1f} {y[i]:>6.1f} {y_lin[i]:>8.4f} {e_lin[i]:>8.4f}")


    print("\n" + "-" * 20 + " Квадратичная модель y = a + bx + cx^2 " + "-" * 20)
    coeffs_quad = np.polyfit(x, y, 2)
    c_quad, b_quad, a_quad = coeffs_quad
    print(f"a = {a_quad:.4f}")
    print(f"b = {b_quad:.4f}")
    print(f"c = {c_quad:.4f}")
    y_quad = np.polyval(coeffs_quad, x)
    s_quad, r2_quad, a_err_quad, e_quad = errors(y, y_quad, n, tss)


    print("\n" + "-" * 20 + " Степенная модель y =  a * x^b " + "-" * 20)
    ln_x = np.log(x)
    ln_y = np.log(y)
    ln_x_mean = ln_x.mean()
    ln_y_mean = ln_y.mean()
    print(f"ln_x_mean = {ln_x_mean:.4f}")
    print(f"ln_y_mean = {ln_y_mean:.4f}")
    sxx_pw = np.sum((ln_x - ln_x_mean) ** 2)
    sxy_pw = np.sum((ln_x - ln_x_mean) * (ln_y - ln_y_mean))
    print(f"sxx_pw = {sxx_pw:.4f}")
    print(f"sxy_pw = {sxy_pw:.4f}")
    b_pow = sxy_pw / sxx_pw
    ln_a_pow = ln_y_mean - b_pow * ln_x_mean
    a_pow = np.exp(ln_a_pow)
    print(f"a_pow = {a_pow:.4f}")
    print(f"b_pow = {b_pow:.4f}")
    y_pow = a_pow * x**b_pow
    s_pow, r2_pow, a_err_pow, e_pow = errors(y, y_pow, n, tss)


    print("\n" + "-" * 20 + " Сравнение моделей " + "-" * 20)
    print(f"  {'Модель':<18}  {'R^2':>8}  {'A':>8}")
    print(f"  {'Линейная':<18}  {r2_lin:>8.4f}  {a_err_lin:>8.4f}")
    print(f"  {'Квадратичная':<18}  {r2_quad:>8.4f}  {a_err_quad:>8.4f}")
    print(f"  {'Степенная':<18}  {r2_pow:>8.4f}  {a_err_pow:>8.4f}")


    print("\n" + "-" * 20 + f" Прогноз для x* = {x_star} " + "-" * 20)
    y_star_lin = a_lin + b_lin * x_star
    y_star_quad = a_quad + b_quad * x_star + c_quad * x_star**2
    y_star_pow = a_pow * x_star**b_pow
    print(f"Линейная: {y_star_lin:.4f}")
    print(f"Квадратичная: {y_star_quad:.4f}")
    print(f"Степенная: {y_star_pow:.4f}")

    x_smooth = np.linspace(x.min() - 5, x_star + 5, 400)

    fig, axes = plt.subplots(2, 2, figsize=(13, 10))
    fig.suptitle("SSD: скорость записи vs скорость чтения (non-NVMe)",
                 fontsize=13, fontweight='bold')

    # Диаграмма рассеяния и модели
    ax = axes[0, 0]
    ax.scatter(x, y, color='steelblue', s=55, zorder=5, label='Данные')
    ax.plot(x_smooth, a_lin + b_lin * x_smooth, 'r-', lw=2, label=f'Линейная  R^2={r2_lin:.3f}')
    ax.plot(x_smooth, a_quad + b_quad*x_smooth + c_quad*x_smooth**2, 'g--', lw=2,
            label=f'Квадратичная  R^2={r2_quad:.3f}')
    ax.plot(x_smooth, a_pow * x_smooth**b_pow, 'm:', lw=2.5,
            label=f'Степенная  R^2={r2_pow:.3f}')
    ax.axvline(x_star, color='orange', lw=1.2, ls='-.', label=f'x*={x_star}')
    ax.set_xlabel('Скорость чтения, МБ/с')
    ax.set_ylabel('Скорость записи, МБ/с')
    ax.set_title('Диаграмма рассеяния и модели')
    ax.legend(fontsize=8)
    ax.grid(True, alpha=0.35)

    # Остатки линейной модели
    ax = axes[0, 1]
    ax.scatter(y_lin, e_lin, color='steelblue', s=45)
    ax.axhline(0, color='red', lw=1.5)
    ax.set_xlabel('y (линейная)')
    ax.set_ylabel('Остаток e')
    ax.set_title('Остатки линейной модели')
    ax.grid(True, alpha=0.35)

    # Остатки квадратичной модели
    ax = axes[1, 0]
    ax.scatter(y_quad, e_quad, color='green', s=45)
    ax.axhline(0, color='red', lw=1.5)
    ax.set_xlabel('y (квадратичная)')
    ax.set_ylabel('Остаток e')
    ax.set_title('Остатки квадратичной модели')
    ax.grid(True, alpha=0.35)

    # Остатки степенной модели
    ax = axes[1, 1]
    ax.scatter(y_pow, e_pow, color='purple', s=45)
    ax.axhline(0, color='red', lw=1.5)
    ax.set_xlabel('y (степенная)')
    ax.set_ylabel('Остаток e')
    ax.set_title('Остатки степенной модели')
    ax.grid(True, alpha=0.35)

    plt.tight_layout()

    # Диаграмма рассеяния
    fig2, ax2 = plt.subplots(figsize=(7, 5))
    ax2.scatter(x, y, color='steelblue', s=60, edgecolors='navy', lw=0.5)
    ax2.set_xlabel('Скорость чтения, МБ/с', fontsize=11)
    ax2.set_ylabel('Скорость записи, МБ/с', fontsize=11)
    ax2.set_title('Диаграмма рассеяния', fontsize=12)
    ax2.grid(True, alpha=0.35)
    plt.tight_layout()
    plt.show()

if __name__ == '__main__':
    main()