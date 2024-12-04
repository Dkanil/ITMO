import pandas as pd
import matplotlib.pyplot as plt

# Загрузка данных из CSV
file_path = 'data.csv'  # Укажите путь к вашему CSV-файлу
data = pd.read_csv(file_path)

# Преобразование колонки DATE в формат даты
data['<DATE>'] = pd.to_datetime(data['<DATE>'], format='%m/%d/%Y')

# Отфильтруем данные по нужным датам
target_dates = ['2018-09-18', '2018-10-18', '2018-11-20', '2018-12-18']
filtered_data = data[data['<DATE>'].isin(pd.to_datetime(target_dates))]

# Создадим структуру для построения
boxplot_data = []
labels = []

for date in target_dates:
    for column in ['<OPEN>', '<HIGH>', '<LOW>', '<CLOSE>']:
        subset = filtered_data[filtered_data['<DATE>'] == date][column]
        boxplot_data.append(subset)
        labels.append(f"{column}\n{date.split('-')[1]}/{date.split('-')[2]}")  # Формат меток

# Построение диаграммы
plt.figure(figsize=(14, 8))
plt.boxplot(boxplot_data, labels=labels, patch_artist=True)

# Настройка графика
plt.title('Диаграмма "ящик с усами" по свечам за 4 даты', fontsize=16)
plt.xlabel('Тип данных и дата', fontsize=12)
plt.ylabel('Цена', fontsize=12)
plt.xticks(rotation=45)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()

# Показ графика
plt.show()
