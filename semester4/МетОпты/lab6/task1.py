import random

# Матрица расстояний
M = [
    [0, 1, 5, 9, 2],
    [1, 0, 9, 4, 4],
    [5, 9, 0, 8, 3],
    [9, 4, 8, 0, 10],
    [2, 4, 3, 10, 0]
]
NUM_CITIES = len(M)


# Целевая функция (сумма расстояний, включая возврат)
def get_fitness(tour):
    cost = 0
    for i in range(NUM_CITIES):
        cost += M[tour[i] - 1][tour[(i + 1) % NUM_CITIES] - 1]
    return cost


# Оператор скрещивания
def custom_crossover(p1, p2):
    # Выбираем две точки разреза, чтобы длина фрагмента была хотя бы 2
    pt1 = random.randint(1, NUM_CITIES - 3)
    pt2 = random.randint(pt1 + 2, NUM_CITIES - 1)

    c1, c2 = [None] * NUM_CITIES, [None] * NUM_CITIES
    c1[pt1:pt2] = p2[pt1:pt2]
    c2[pt1:pt2] = p1[pt1:pt2]

    # Стартовый индекс для чтения исходного родителя (второй элемент вырезанного фрагмента)
    start_read = (pt1 + 1) % NUM_CITIES

    def fill_child(child, parent):
        seq = [parent[(start_read + i) % NUM_CITIES] for i in range(NUM_CITIES)]
        write_idx = 0
        for val in seq:
            if val not in child:
                # Вставляем слева направо на пустые места
                while write_idx < NUM_CITIES and child[write_idx] is not None:
                    write_idx += 1
                if write_idx < NUM_CITIES:
                    child[write_idx] = val
        return child

    return fill_child(c1, p1), fill_child(c2, p2)


# Оператор мутации (перестановка двух случайных городов)
def mutate(tour, mutation_prob):
    if random.random() < mutation_prob:
        print("----МУТАЦИЯ----")
        print(f"До мутации: {tour}")
        idx1, idx2 = random.sample(range(NUM_CITIES), 2)
        tour[idx1], tour[idx2] = tour[idx2], tour[idx1]
        print(f"После мутации: {tour}")
    return tour

def main():
    population_size = 4
    mutation_prob = 0.1
    num_generations = 10

    # Создание начальной популяции
    population = []
    for _ in range(population_size):
        tour = list(range(1, NUM_CITIES + 1))
        random.shuffle(tour)
        population.append(tour)

    # Основной цикл ГА
    print("--- НАЧАЛО РАБОТЫ ГА ---")
    for gen in range(num_generations):
        print(f"\nПоколение {gen + 1}")
        fitness_values = []
        for p in population:
            f = get_fitness(p)
            fitness_values.append(f)
            print(f"Особь: {p} -> f(x) = {f}")

        # ЭТАП 2: ВЫБОР РОДИТЕЛЕЙ (Слайд 14 и 24)
        # Преобразуем фитнес в вероятности (чем меньше f(x), тем больше вес/вероятность)
        # Используем простейшую инверсию: weight = 1 / f(x)
        weights = [1.0 / f for f in fitness_values]
        offspring = []
        for _ in range(population_size // 2):
            # random.choices выбирает родителей случайным образом с учетом их "веса" (рулетка)
            parent1, parent2 = random.choices(population, weights=weights, k=2)

            # Этап 3: Создание потомков
            c1, c2 = custom_crossover(parent1, parent2)

            # Этап 4: Мутация
            offspring.append(mutate(c1, mutation_prob))
            offspring.append(mutate(c2, mutation_prob))

        # Оператор редукции (отбор по качеству: наименьшая стоимость)
        pool = population + offspring
        pool.sort(key=get_fitness)
        population = pool[:population_size]  # Оставляем 4 лучших

    print("\n--- РЕЗУЛЬТАТ ---")
    best_tour = population[0]
    print(f"Лучший маршрут коммивояжера: {best_tour}")
    print(f"Минимальная стоимость пути: {get_fitness(best_tour)}")

if __name__ == '__main__':
    main()