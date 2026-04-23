import pandas as pd
from math import sqrt
from scipy import stats

X = pd.read_csv("variant_15_sample_X.csv")["x_i"].values
Y = pd.read_csv("variant_15_sample_Y.csv")["y_j"].values

m = len(X)
n = len(Y)
alpha = 0.05

x_mean = X.mean()
y_mean = Y.mean()

sigma_x2 = X.var(ddof=1)
sigma_y2 = Y.var(ddof=1)

sigma_p2 = ((m - 1) * sigma_x2 + (n - 1) * sigma_y2) / (m + n - 2)
sigma_p = sqrt(sigma_p2)

T = (x_mean - y_mean) / (sigma_p * sqrt(1/m + 1/n))

df = m + n - 2
t_crit = stats.t.ppf(1 - alpha/2, df)

print(f"x̄ = {x_mean:.4f}, ȳ = {y_mean:.4f}")
print(f"σ_X² = {sigma_x2:.4f}, σ_Y² = {sigma_y2:.4f}")
print(f"σ_p² = {sigma_p2:.4f}")
print(f"T = {T:.4f}")
print(f"t_кр = {t_crit:.4f}")

if abs(T) > t_crit:
    print("H0 отвергается")
else:
    print("H0 не отвергается")
