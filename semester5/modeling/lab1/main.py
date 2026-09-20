import numpy as np
import matplotlib.pyplot as plt
from scipy import stats
from math import sqrt, ceil

with open('data.txt', 'r') as f:
    data = np.array([float(line.strip().replace(',','.')) for line in f if line.strip()])

n_list = [10, 20, 50, 100, 200, 300]
t_p = {0.90: 1.643, 0.95: 1.960, 0.99: 2.576}

def calc_stats(sample):
    n = len(sample)
    m = np.mean(sample)
    d = np.var(sample, ddof=1)
    s = np.sqrt(d)
    v = s / m
    ci = {p: t * s / sqrt(n) for p, t in t_p.items()}
    return m, d, s, v, ci

ref_m, ref_d, ref_s, ref_v, _ = calc_stats(data[:300])

print("\n")
for n in n_list:
    sample = data[:n]
    m, d, s, v, ci = calc_stats(sample)
    print(f"n={n:3d} | M: {m:.2f} ({abs(m-ref_m)/ref_m*100:.1f}%) | "
          f"D: {d:.1f} | S: {s:.2f} | V: {v:.3f} | "
          f"CI 0.95: ±{ci[0.95]:.2f}")

plt.figure(figsize=(10,4))
plt.plot(data[:300], marker='.', linestyle='-', linewidth=0.5)
plt.title("Значения исходной ЧП")
plt.grid()
plt.savefig('graph1.png')
plt.close()

def autocorr(x, lag):
    n = len(x)
    m = np.mean(x)
    num = sum((x[i] - m) * (x[i+lag] - m) for i in range(n-lag))
    den = sum((xi - m)**2 for xi in x)
    return num / den

lags = list(range(1, 11))
r_k_orig = [autocorr(data, k) for k in lags]

k_erlang = max(1, round(1 / (ref_v**2)))
lambda_erlang = k_erlang / ref_m
print(f"\nАппроксимация: Эрланга k={k_erlang}, lambda={lambda_erlang:.4f}")

gen_data = np.sum(np.random.exponential(1/lambda_erlang, (k_erlang, 300)), axis=0)

ref_gen_m, _, _, _, _ = calc_stats(gen_data)
print("\n")
for n in n_list:
    sample = gen_data[:n]
    m, d, s, v, ci = calc_stats(sample)
    print(f"n={n:3d} | M: {m:.2f} | D: {d:.1f} | S: {s:.2f} | V: {v:.3f}")

r_k_gen = [autocorr(gen_data, k) for k in lags]

print("\n")
for i, k in enumerate(lags):
    print(f"Сдвиг {k}: Исходная {r_k_orig[i]:.4f} | Сгенер {r_k_gen[i]:.4f}")

plt.figure(figsize=(8,4))
plt.plot(lags, r_k_orig, label='Исходная ЧП', marker='o')
plt.plot(lags, r_k_gen, label='Сгенерированная ЧП', marker='x')
plt.grid()
plt.legend()
plt.title("Автокорреляция")
plt.savefig('autocorr.png')
plt.close()

plt.figure(figsize=(8,4))
plt.hist(data, bins=20, density=True, alpha=0.6, color='b')
plt.title("Гистограмма исходной ЧП (График 2)")
plt.grid()
plt.savefig('graph2.png')
plt.close()

plt.figure(figsize=(8,4))
plt.hist(data, bins=20, density=True, alpha=0.5, label='Исходная гистограмма')
x_val = np.linspace(0, max(data), 100)
pdf_erlang = stats.gamma.pdf(x_val, a=k_erlang, scale=1/lambda_erlang)
plt.plot(x_val, pdf_erlang, 'r-', lw=2, label='Плотность Эрланга')
plt.legend()
plt.title("Сравнение гистограммы и аппроксимирующего закона (График 3)")
plt.grid()
plt.savefig('graph3.png')
plt.close()