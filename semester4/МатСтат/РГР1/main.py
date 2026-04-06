import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats
from sklearn.cluster import KMeans


def plot_histogram_comparison(data, col_name):
    n = len(data)
    std = np.std(data, ddof=1)
    q75, q25 = np.percentile(data, [75, 25])
    iqr = q75 - q25
    data_range = np.max(data) - np.min(data)

    k_sturges = int(1 + np.log2(n))

    h_scott = 3.5 * std * (n ** (-1 / 3))
    k_scott = int(np.ceil(data_range / h_scott))

    h_fd = 2 * iqr * (n ** (-1 / 3))
    k_fd = int(np.ceil(data_range / h_fd))

    fig, axes = plt.subplots(1, 3, figsize=(18, 5), sharey=True)
    fig.suptitle(f'Сравнение гистограмм для столбца {col_name}', fontsize=16)

    methods = [
        (k_sturges, f'Стерджес (k={k_sturges})'),
        (k_scott, f'Скотт (k={k_scott})'),
        (k_fd, f'Фридман-Диаконис (k={k_fd})')
    ]

    for ax, (bins, title) in zip(axes, methods):
        sns.histplot(data, bins=bins, kde=True, ax=ax, color='teal', edgecolor='black', alpha=0.6)
        ax.set_title(title)
        ax.set_xlabel('Значения')
        ax.set_ylabel('Плотность/Частота')

    plt.tight_layout(rect=[0, 0.03, 1, 0.95])
    plt.show()


def get_basic_stats(data):
    return {
        'mean': np.mean(data),
        's2_biased': np.var(data, ddof=0),
        's2_unbiased': np.var(data, ddof=1),
        'std_biased': np.std(data, ddof=0),
        'std_unbiased': np.std(data, ddof=1),
        'median': np.median(data),
        'q1': np.percentile(data, 25),
        'q3': np.percentile(data, 75),
        'min': np.min(data),
        'max': np.max(data)
    }


def analyze_column(name, data, dist_type):
    print(f"\n{'#' * 40}\nАНАЛИЗ СТОЛБЦА {name} ({dist_type})\n{'#' * 40}")
    n = len(data)
    s = get_basic_stats(data)

    plot_histogram_comparison(data, name)

    h = 3.5 * s['std_unbiased'] * n ** (-1 / 3)
    bins = int((s['max'] - s['min']) / h)
    
    plt.figure(figsize=(6, 4))
    plt.step(np.sort(data), np.arange(n) / n, label='ЭФР')
    plt.title(f"ЭФР {name}")
    plt.show()

    print(f"Выборочное среднее: {s['mean']:.3f}")
    print(f"Смещённая дисперсия: {s['s2_biased']:.3f}")
    print(f"Несмещённая дисперсия: {s['s2_unbiased']:.3f}")
    print(f"Смещённое стандартное отклонение: {s['std_biased']:.3f}")
    print(f"Несмещённое стандартное отклонение: {s['std_unbiased']:.3f}")
    print(f"Медиана: {s['median']:.3f}")
    print(f"Квартили: [{s['q1']:.2f}, {s['q3']:.2f}]")

    x0 = (s['mean'] + s['std_unbiased'])
    if dist_type == 'Экспоненциальное':
        c_mmp = s['min']
        l_mmp = 1 / (s['mean'] - c_mmp)
        l_mm = 1 / np.std(data, ddof=0)
        c_mm = s['mean'] - (1 / l_mm)
        print(f"ММ:  lambda={l_mm:.4f}, c={c_mm:.4f}")
        print(f"ММП: lambda={l_mmp:.4f}, c={c_mmp:.4f}")
        p_theo = np.exp(-l_mmp * (x0  - c_mmp))

    elif dist_type == 'Нормальное':
        a_mmp = s['mean']
        sig2_mmp = s['s2_biased']
        print(f"ММ и ММП: a={a_mmp:.4f}, sigma^2={sig2_mmp:.4f}")
        p_theo = 1 - stats.norm.cdf(x0 , loc=a_mmp, scale=np.sqrt(sig2_mmp))

    elif dist_type == 'Равномерное':
        a_mm = s['mean'] - np.sqrt(3 * s['s2_biased'])
        b_mm = s['mean'] + np.sqrt(3 * s['s2_biased'])
        a_mmp, b_mmp = s['min'], s['max']
        print(f"ММ:  a={a_mm:.4f}, b={b_mm:.4f}")
        print(f"ММП: a={a_mmp:.4f}, b={b_mmp:.4f}")
        p_theo = (b_mmp - x0) / (b_mmp - a_mmp) if x0 < b_mmp else 0


    p_emp = np.mean(data > x0)
    print(f"4.4. P(X > {x0:.2f}): Эмпирич={p_emp:.3f}, Параметрич={p_theo:.3f}")

    counts, edges = np.histogram(data, bins=bins)
    mids = (edges[:-1] + edges[1:]) / 2
    x_g = np.sum(counts * mids) / n
    s2_g = np.sum(counts * (mids - x_g) ** 2) / (n - 1)
    print(f"4.5. Группировка: mean_g={x_g:.3f}, var_g={s2_g:.3f}")

    z = stats.norm.ppf(0.975)
    err_asy = z * (s['std_unbiased'] / np.sqrt(n))
    print(f"4.6. Асимптот. ДИ для EX: ({s['mean'] - err_asy:.3f}, {s['mean'] + err_asy:.3f})")

    if dist_type == 'Нормальное':
        t_crit = stats.t.ppf(0.975, n - 1)
        err_t = t_crit * (s['std_unbiased'] / np.sqrt(n))
        print(f"     Точный ДИ для mu: ({s['mean'] - err_t:.3f}, {s['mean'] + err_t:.3f})")
        c1 = stats.chi2.ppf(0.975, n - 1)
        c2 = stats.chi2.ppf(0.025, n - 1)
        print(
            f"     Точный ДИ для sigma^2: ({(n - 1) * s['s2_unbiased'] / c1:.3f}, {(n - 1) * s['s2_unbiased'] / c2:.3f})")

def dop_task(data, col_name):
    print(f"\n{'#' * 40}\nДополнительное задание для столбца {col_name}\n{'#' * 40}")
    n = len(data)

    plt.figure(figsize=(10, 4))
    plt.subplot(1, 2, 1)
    plt.hist(data, bins='auto', density=True, color='purple', edgecolor='black', alpha=0.7)
    plt.title(f"Гистограмма {col_name}")
    plt.subplot(1, 2, 2)
    plt.step(np.sort(data), np.arange(n) / n, label='ЭФР')
    plt.title(f"ЭФР {col_name}")
    plt.show()

    kmeans = KMeans(n_clusters=2, random_state=42, n_init='auto')
    labels = kmeans.fit_predict(data.reshape(-1, 1))
    cluster_0 = data[labels == 0]
    cluster_1 = data[labels == 1]
    print("\nХарактеристики подвыборок (K-Means, k=2):")
    print(f"Кластер 1: размер={len(cluster_0)}, среднее={np.mean(cluster_0):.3f}, std={np.std(cluster_0, ddof=1):.3f}")
    print(f"Кластер 2: размер={len(cluster_1)}, среднее={np.mean(cluster_1):.3f}, std={np.std(cluster_1, ddof=1):.3f}")
    print(f"\nОбщее выборочное среднее: {np.mean(data):.3f}")


def main():
    try:
        df = pd.read_csv('data.csv')
    except Exception as e:
        print("Ошибка: положите файл data.csv в папку со скриптом")
        print(e)
        return

    tasks = {
        'X1': 'Экспоненциальное',
        'X2': 'Нормальное',
        'X3': 'Равномерное'
    }

    for col, dist in tasks.items():
        analyze_column(col, df[col].values, dist)
    dop_task(df['X4'].dropna().values, 'X4')


if __name__ == "__main__":
    main()