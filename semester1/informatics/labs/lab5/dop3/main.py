import pandas as pd
import matplotlib.pyplot as plt

file_path = 'data.csv'
data = pd.read_csv(file_path)

data['<DATE>'] = pd.to_datetime(data['<DATE>'], format='%m/%d/%Y')

boxplot_data = []
labels = []
data = data.rename(columns={
    '<OPEN>': 'Открытие',
    '<HIGH>': 'Макс',
    '<LOW>': 'Мин',
    '<CLOSE>': 'Закрытие'
})
target_dates = ['2018-09-18', '2018-10-18', '2018-11-20', '2018-12-18']
for date in target_dates:
    for column in ['Открытие', 'Макс', 'Мин', 'Закрытие']:
        subset = data[data['<DATE>'] == date][column]
        boxplot_data.append(subset)
        labels.append(f"{column}\n{date.split('-')[2]}/{date.split('-')[1]}/{date.split('-')[0]}")
plt.figure(figsize=(14, 8))
plt.boxplot(boxplot_data,meanprops={ "marker": "X"}, tick_labels=labels, showmeans=True, patch_artist=True)
plt.xticks(rotation=45)
plt.grid(axis='y', linestyle='--', alpha=0.8)
plt.title("Диаграмма размаха для цен акций")

plt.tight_layout()
plt.show()
