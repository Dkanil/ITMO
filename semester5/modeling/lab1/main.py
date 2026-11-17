from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
from scipy import stats


LAB_DIR = Path(__file__).resolve().parent
REPORT_DIR = LAB_DIR / "src"
SAMPLE_SIZES = (10, 20, 50, 100, 200, 300)
LAGS = np.arange(1, 11)
SEED = 132

# Quantiles from table 1 of the course notes.
CONFIDENCE_QUANTILES = np.array([1.643, 1.960, 2.576])
CHARACTERISTICS = (
    (r"\textbf{Мат.ож.}", 2),
    ("Дов. инт. (0,9)", 2),
    ("Дов. инт. (0,95)", 2),
    ("Дов. инт. (0,99)", 2),
    (r"\textbf{Дисперсия}", 1),
    (r"\textbf{С.к.о.}", 2),
    (r"\textbf{К-т вариации}", 3),
)


def load_sample():
    lines = (LAB_DIR / "data.txt").read_text(encoding="utf-8").splitlines()
    values = np.array([float(line.replace(",", ".")) for line in lines if line.strip()])
    size = SAMPLE_SIZES[-1]
    if values.size < size:
        raise ValueError(f"Expected at least {size} observations, got {values.size}")
    return values[:size]


def sample_statistics(sample):
    mean = sample.mean()
    variance = sample.var(ddof=1)
    std = np.sqrt(variance)
    half_widths = CONFIDENCE_QUANTILES * std / np.sqrt(sample.size)
    return np.array([mean, *half_widths, variance, std, std / mean])


def autocorrelation(sample, lags):
    centered = sample - sample.mean()
    total = centered @ centered
    return np.array([centered[:-lag] @ centered[lag:] / total for lag in lags])


def relative_deviation(values, reference):
    return np.divide(
        100 * np.abs(values - reference),
        np.abs(reference),
        out=np.full_like(values, np.nan),
        where=reference != 0,
    )


def decimal(value, digits):
    return f"{value:.{digits}f}".replace(".", "{,}")


def percent(value):
    return r"---" if np.isnan(value) else decimal(value, 1) + r"\,\%"


def table_row(label, cells):
    return " & ".join([label, *cells]) + r" \\"


def statistics_rows(values, reference, *, merge_baseline=False):
    rows = []
    last_column = len(SAMPLE_SIZES) + 2
    split_column = last_column - int(merge_baseline)
    for index, (label, digits) in enumerate(CHARACTERISTICS):
        formatted = [decimal(value, digits) for value in values[:, index]]
        if 1 <= index <= 3:
            formatted = [rf"$\pm {value}$" for value in formatted]
        deviations = relative_deviation(values[:, index], reference[:, index])
        percentages = [percent(value) for value in deviations]
        if merge_baseline:
            formatted[-1] = rf"\multirow{{2}}{{*}}{{{formatted[-1]}}}"
            percentages[-1] = ""
        heading = rf"\multirow{{2}}{{*}}{{{label}}} & Знач."
        rows.append(table_row(heading, formatted) + rf" \cline{{2-{split_column}}}")
        rows.append(table_row(r" & \%", percentages) + r" \hline")
    return rows


def write_report_data(original, generated, acf_original, acf_generated, order, rate, r_xy, cdf_distance):
    reference = np.broadcast_to(original[-1], original.shape)
    acf_rows = [
        table_row(r"\textbf{К-т АК} для задан. \textbf{ЧП}", (decimal(value, 4) for value in acf_original)),
        table_row(r"\textbf{К-т АК} для сгенерир. \textbf{ЧП}", (decimal(value, 4) for value in acf_generated)),
        table_row(r"\textit{\%}", map(percent, relative_deviation(acf_generated, acf_original))),
    ]
    deviations = relative_deviation(generated[-1], original[-1])
    values = {
        "ErlangOrder": str(order),
        "ErlangRate": decimal(rate, 6),
        "UnroundedOrder": decimal(1 / original[-1, -1] ** 2, 3),
        "OriginalMean": decimal(original[-1, 0], 2),
        "OriginalVariance": decimal(original[-1, 4], 1),
        "OriginalCV": decimal(original[-1, -1], 3),
        "TheoreticalVariance": decimal(order / rate**2, 1),
        "CdfDistance": decimal(cdf_distance, 3),
        "CrossCorrelation": decimal(r_xy, 4),
        "OriginalMaxACF": decimal(np.max(np.abs(acf_original)), 4),
        "GeneratedMaxACF": decimal(np.max(np.abs(acf_generated)), 4),
        "MeanDeviation": percent(deviations[0]),
        "VarianceDeviation": percent(deviations[4]),
        "StdDeviation": percent(deviations[5]),
        "CVDeviation": percent(deviations[6]),
        "FirstMeanInterval": decimal(original[0, 2], 2),
        "LastMeanInterval": decimal(original[-1, 2], 2),
        "GeneratorSeed": str(SEED),
    }
    lines = []
    lines.extend(rf"\newcommand{{\{name}}}{{{value}}}" for name, value in values.items())
    for name, rows in (
        ("OriginalRows", statistics_rows(original, reference, merge_baseline=True)),
        ("GeneratedRows", statistics_rows(generated, original)),
        ("AutocorrelationRows", [row + r" \hline" for row in acf_rows]),
    ):
        lines.extend([rf"\newcommand{{\{name}}}{{%", *rows, "}"])
    report = REPORT_DIR / "main.tex"
    source = report.read_text(encoding="utf-8")
    start_marker = "% BEGIN GENERATED RESULTS\n"
    end_marker = "% END GENERATED RESULTS\n"
    start = source.index(start_marker) + len(start_marker)
    end = source.index(end_marker, start)
    updated = source[:start] + "\n".join(lines) + "\n" + source[end:]
    if updated != source:
        report.write_text(updated, encoding="utf-8")


def save_figure(fig, filename):
    fig.savefig(REPORT_DIR / filename, dpi=180, bbox_inches="tight")
    plt.close(fig)


def plot_results(original, generated, acf_original, acf_generated, distribution):
    indices = np.arange(1, original.size + 1)
    original_bins = np.histogram_bin_edges(original, bins=20)

    fig, ax = plt.subplots(figsize=(8, 2.8), layout="constrained")
    ax.plot(indices, original, lw=0.65)
    ax.set(xlabel="Номер наблюдения", ylabel="Значение")
    ax.grid(alpha=0.25)
    save_figure(fig, "graph1.png")

    fig, ax = plt.subplots(figsize=(8, 2.8), layout="constrained")
    ax.plot(LAGS, acf_original, "o-", label="Исходная ЧП")
    ax.plot(LAGS, acf_generated, "s--", label="Сгенерированная ЧП")
    ax.axhline(0, color="0.35", lw=0.8)
    ax.set(xlabel="Сдвиг", ylabel="Автокорреляция", xticks=LAGS)
    ax.legend()
    ax.grid(alpha=0.25)
    save_figure(fig, "autocorr.png")

    fig, ax = plt.subplots(figsize=(8, 2.8), layout="constrained")
    ax.hist(original, bins=original_bins, weights=np.full(original.size, 1 / original.size))
    ax.set(xlabel="Значение", ylabel="Относительная частота")
    ax.grid(axis="y", alpha=0.25)
    save_figure(fig, "graph2.png")

    fig, ax = plt.subplots(figsize=(8, 2.8), layout="constrained")
    ax.hist(original, bins=original_bins, density=True, alpha=0.55, label="Исходная ЧП")
    x = np.linspace(0, max(original.max(), distribution.ppf(0.995)), 500)
    ax.plot(x, distribution.pdf(x), color="tab:red", label="Плотность Эрланга")
    ax.set(xlabel="Значение", ylabel="Плотность")
    ax.legend()
    ax.grid(alpha=0.25)
    save_figure(fig, "graph3.png")

    fig, (series, histogram) = plt.subplots(2, 1, figsize=(8, 5), layout="constrained")
    shared_bins = np.histogram_bin_edges(np.concatenate([original, generated]), bins=20)
    for sample, color, label in (
        (original, "tab:blue", "Исходная ЧП"),
        (generated, "tab:orange", "Сгенерированная ЧП"),
    ):
        series.scatter(indices, sample, s=7, alpha=0.65, color=color, label=label)
        histogram.hist(
            sample, bins=shared_bins, weights=np.full(sample.size, 1 / sample.size),
            histtype="step", lw=1.6, color=color, label=label,
        )
    series.set(xlabel="Номер наблюдения", ylabel="Значение")
    histogram.set(xlabel="Значение", ylabel="Относительная частота")
    for ax in (series, histogram):
        ax.legend()
        ax.grid(alpha=0.25)
    save_figure(fig, "comparison.png")


def main():
    original = load_sample()
    original_stats = np.array([sample_statistics(original[:n]) for n in SAMPLE_SIZES])
    mean, *_, cv = original_stats[-1]
    order = round(1 / cv**2)
    rate = order / mean

    rng = np.random.default_rng(SEED)
    generated = rng.exponential(scale=1 / rate, size=(original.size, order)).sum(axis=1)
    generated_stats = np.array([sample_statistics(generated[:n]) for n in SAMPLE_SIZES])
    acf_original = autocorrelation(original, LAGS)
    acf_generated = autocorrelation(generated, LAGS)
    r_xy = np.corrcoef(original, generated)[0, 1]
    distribution = stats.gamma(a=order, scale=1 / rate)
    cdf_distance = stats.kstest(original, distribution.cdf).statistic

    write_report_data(
        original_stats, generated_stats, acf_original, acf_generated,
        order, rate, r_xy, cdf_distance,
    )
    plot_results(original, generated, acf_original, acf_generated, distribution)
    print(f"Erlang: k={order}, rate={rate:.6f}, seed={SEED}")
    print(f"Pearson correlation: {r_xy:.4f}")
    print(f"Report tables and plots: {REPORT_DIR}")


if __name__ == "__main__":
    main()
