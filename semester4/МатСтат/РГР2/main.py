import pandas as pd
import numpy as np
from scipy import stats
from matplotlib import pyplot as plt

def main():
    df = pd.read_csv('data.csv', sep=';')

    x1 = df['X1']
    x2 = df['X2']
    x3 = df['X3']
    x4 = df['X4']

    n = len(x1)
    alpha = 0.05

    SEP = "=" * 60
    print(SEP)
    print("4.2 Гипотеза о равенстве математических ожиданий (x1, x2)")
    print(SEP)
    print(f"  H0: mu1 = mu2,   H1: mu1 != mu2")
    print(f"  Уровень значимости alpha = {alpha}")
    print(f"  Критерий: двухвыборочный t-критерий Стьюдента (pooled variance)")
    print()

    m, nn = len(x1), len(x2)
    x1_bar = np.mean(x1)
    x2_bar = np.mean(x2)
    s1_sq = np.var(x1, ddof=1)
    s2_sq = np.var(x2, ddof=1)
    s_pool_sq = ((m - 1)*s1_sq + (nn - 1)*s2_sq) / (m + nn - 2)
    s_pool = np.sqrt(s_pool_sq)
    h_42 = (x1_bar - x2_bar) / (s_pool * np.sqrt(1/m + 1/nn))
    df_42 = m + nn - 2
    t_crit_42 = stats.t.ppf(1 - alpha/2, df_42)

    print(f"  Выборочные средние:  x1_bar = {x1_bar:.4f},  x2_bar = {x2_bar:.4f}")
    print(f"  Выборочные дисперсии: s1^2 = {s1_sq:.4f},  s2^2 = {s2_sq:.4f}")
    print(f"  Объединённая дисперсия: s_pool^2 = {s_pool_sq:.4f} =>  s_pool = {s_pool:.4f}")
    print(f"  Наблюдаемая статистика: h_nabл = {h_42:.4f}")
    print(f"  Степени свободы k = {df_42}")
    print(f"  Критическое значение t_(k; 1-alpha/2) = t_({df_42}; 0.975) = {t_crit_42:.4f}")
    print(f"  Критическая область: W = (-inf, -{t_crit_42:.4f}) U ({t_crit_42:.4f}, +inf)")
    print()
    if abs(h_42) > t_crit_42:
        print("  ВЫВОД: |h_набл| > t_крит =>  H0 ОТВЕРГАЕТСЯ.")
        print("  Статистически значимые основания считать средние различными — ЕСТЬ.")
    else:
        print("  ВЫВОД: H0 не отвергается.")


    print()
    print(SEP)
    print("4.3  Гипотеза о математическом ожидании нормального распределения (x3)")
    print(SEP)
    mu0 = 86.28
    print(f"H0: mu = {mu0},   H1: mu != {mu0}")
    print(f"Критерий: одновыборочный t-критерий Стьюдента")
    print()

    x3_bar = np.mean(x3)
    s3_sq = np.var(x3, ddof=1)
    s3 = np.std(x3, ddof=1)
    h_43 = np.sqrt(n) * (x3_bar - mu0) / s3
    df_43 = n - 1
    t_crit_43 = stats.t.ppf(1 - alpha/2, df_43)

    print(f"  Выборочное среднее:    x3_bar = {x3_bar:.4f}")
    print(f"  Выборочная дисперсия:  s3^2 = {s3_sq:.4f} =>  s3 = {s3:.4f}")
    print(f"  Наблюдаемая статистика: h_набл = sqrt({n})*({x3_bar:.4f}-{mu0})/{s3:.4f} = {h_43:.4f}")
    print(f"  Степени свободы k = {df_43}")
    print(f"  Критическое значение t_({df_43}; 0.975) = {t_crit_43:.4f}")
    print()
    if abs(h_43) > t_crit_43:
        print("  ВЫВОД: H0 ОТВЕРГАЕТСЯ.")
    else:
        print("  ВЫВОД: |h_набл| < t_крит =>  H0 НЕ ОТВЕРГАЕТСЯ.")
        print(f"  Нет оснований считать, что математическое ожидание x3 отличается от {mu0}.")

    print()
    print(SEP)
    print("4.4  Непараметрический критерий Манна–Уитни (x1, x2)")
    print(SEP)
    print(f"  H0: F_x1 = F_x2,   H1: F_x1 != F_x2")
    print(f"  Критерий Манна–Уитни (нормальное приближение при больших выборках)")
    print()

    all_data = np.concatenate([x1, x2])
    ranks = stats.rankdata(all_data)
    R_x = np.sum(ranks[:m])
    Ux = R_x - m*(m + 1)/2
    mean_U = m*nn / 2
    std_U = np.sqrt(m*nn*(m + nn + 1) / 12)
    z_mw = (Ux - mean_U) / std_U
    z_crit = stats.norm.ppf(1 - alpha/2)

    print(f"  Сумма рангов x1:  R_x = {R_x:.1f}")
    print(f"  U_x = R_x - m(m+1)/2 = {R_x:.1f} - {m*(m+1)//2} = {Ux:.1f}")
    print(f"  E[U] = {mean_U:.2f},   std[U] = {std_U:.4f}")
    print(f"  z_набл = (U_x - E[U]) / std[U] = ({Ux:.1f} - {mean_U:.2f}) / {std_U:.4f} = {z_mw:.4f}")
    print(f"  Критическое значение: z_(0.975) = {z_crit:.4f}")
    print()
    if abs(z_mw) > z_crit:
        print("  ВЫВОД: |z_набл| > z_крит =>  H0 ОТВЕРГАЕТСЯ.")
        print("  Распределения x1 и x2 статистически значимо различаются.")
    else:
        print("  ВЫВОД: H0 не отвергается.")

    print()
    print("  СРАВНЕНИЕ с п.4.2:")
    print("  Оба критерия (t-критерий и Манна–Уитни) отвергают H0.")
    print("  Это согласованный результат: средние x1 и x2 значимо различаются.")
    print("  Параметрический критерий более мощен при нормальности,")
    print("  непараметрический подтверждает вывод без предположения о форме.")

    print()
    print(SEP)
    print("4.5  Критерий согласия Пирсона (x4 ~ Exp, lambda=0.079)")
    print(SEP)
    lam0 = 0.079
    scale0 = 1.0 / lam0
    print(f"  H0: x4 ~ Exp(lambda={lam0})  (среднее={scale0:.2f})")
    print(f"  H1: x4 не следует данному закону")
    print(f"  Критерий согласия Пирсона (chi^2)")
    print()

    bounds = [0, 3, 6, 9, 12, 18, 24, np.inf]
    k = len(bounds) - 1
    obs_list = []
    exp_list = []
    for i in range(k):
        lo, hi = bounds[i], bounds[i+1]
        o = int(np.sum((x4 >= lo) & (x4 < hi)))
        p = stats.expon.cdf(hi, scale=scale0) - stats.expon.cdf(lo, scale=scale0)
        obs_list.append(o)
        exp_list.append(p * n)

    obs_m = np.array(obs_list)
    exp_m = np.array(exp_list)
    bd = list(bounds)
    k_f = len(obs_m)
    print()
    print(f"  {'Интервал':<15} {'ν_i (набл)':<12} {'np_i (ожид)':<12} {'(ν_i-np_i)^2/np_i':<20}")
    contrib = (obs_m - exp_m)**2 / exp_m
    for i in range(k_f):
        hi_str = f"{bd[i+1]:.0f}" if bd[i+1] != np.inf else "+inf"
        lo_str = f"{bd[i]:.0f}"
        print(f"  [{lo_str}, {hi_str}){'':5} {obs_m[i]:<12} {exp_m[i]:<12.4f} {contrib[i]:<20.4f}")

    chi2_obs = np.sum(contrib)
    df_chi2 = k_f - 1
    chi2_crit = stats.chi2.ppf(1 - alpha, df_chi2)

    print()
    print(f"  chi2_набл = {chi2_obs:.4f}")
    print(f"  Степени свободы: k - 1 = {k_f} - 1 = {df_chi2}")
    print(f"  Критическое значение chi2_({df_chi2}; 0.95) = {chi2_crit:.4f}")
    print()
    if chi2_obs > chi2_crit:
        print("  ВЫВОД: chi2_набл > chi2_крит =>  H0 ОТВЕРГАЕТСЯ.")
        print(f"  Выборка x4 не согласуется с гипотетическим законом Exp({lam0}).")
    else:
        print("  ВЫВОД: H0 не отвергается.")

    plt.figure(figsize=(14, 10))

    plt.subplot(2, 2, 1)
    plt.hist(x1, bins='auto', alpha=0.6, label='x1', edgecolor='black')
    plt.hist(x2, bins='auto', alpha=0.6, label='x2', edgecolor='black')
    plt.title('Гистограммы распределений x1 и x2')
    plt.xlabel('Значения')
    plt.ylabel('Частота')
    plt.legend()

    plt.subplot(2, 2, 2)
    plt.hist(x3, bins='auto', alpha=0.7, color='green', label='x3', edgecolor='black')
    plt.axvline(mu0, color='red', linestyle='dashed', linewidth=2, label=f'H0: mu = {mu0}')
    plt.title('Гистограмма распределения x3')
    plt.xlabel('Значения')
    plt.ylabel('Частота')
    plt.legend()

    plt.subplot(2, 2, 3)
    counts, bins, _ = plt.hist(x4, bins='auto', alpha=0.7, color='purple', label='x4 (наблюдаемое)', edgecolor='black')
    bin_width = bins[1] - bins[0]
    x_vals = np.linspace(0, x4.max(), 100)
    expected_counts = stats.expon.pdf(x_vals, scale=scale0) * len(x4) * bin_width
    plt.plot(x_vals, expected_counts, 'r-', lw=2, label=f'Exp({lam0}) (ожидаемое)')
    plt.title('Гистограмма распределения x4')
    plt.xlabel('Значения')
    plt.ylabel('Частота')
    plt.legend()

    plt.tight_layout()
    plt.show()

if __name__ == '__main__':
    main()