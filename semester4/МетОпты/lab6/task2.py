import random

def main():
    # Определяем узлы и матрицу смежности (веса ребер) на основе вашей картинки
    # inf означает отсутствие прямой дороги
    inf = float('inf')
    nodes = ['A', 'B', 'C', 'D', 'E', 'F', 'G']

    graph = [
        [inf, 17, inf, inf, 4, 7, inf],     # A
        [17, inf, inf, inf, inf, inf, inf], # B
        [inf, inf, inf, 6, 11, 15, 23],     # C
        [inf, inf, 6, inf, 17, inf, 11],    # D
        [4, inf, 11, 17, inf, inf, inf],    # E
        [7, inf, 15, inf, inf, inf, 33],    # F
        [inf, inf, 23, 11, inf, 33, inf]    # G
    ]

    # Параметры муравьиного алгоритма
    num_ants = 10  # Количество муравьев
    num_iterations = 50  # Количество итераций
    alpha = 1.0  # Влияние феромона (степень, в которую муравьи предпочитают пути с большим количеством феромона)
    beta = 2.0  # Влияние эвристики (степень, в которую муравьи предпочитают пути с меньшим расстоянием)
    evaporation_rate = 0.5  # Скорость испарения феромона [от 0 до 1]
    Q = 10  # Коэффициент оставляемого феромона (сколько феромона на всю длину пути между соседями оставит муравей)

    num_nodes = len(nodes)
    # Инициализация феромонов на всех путях (изначально 1.0)
    pheromones = [[1.0 for _ in range(num_nodes)] for _ in range(num_nodes)]

    start_node = 0  # Индекс узла A
    end_node = 6  # Индекс узла G

    best_path = None
    best_length = inf

    for it in range(num_iterations):
        all_paths = []
        print(f"\n--- Итерация {it + 1} ---") 

        for ant in range(num_ants):
            path = [start_node]
            current = start_node
            path_length = 0
            visited = {start_node}

            while current != end_node:
                # Ищем доступных соседей
                neighbors = [nxt for nxt in range(num_nodes)
                             if graph[current][nxt] != inf and nxt not in visited]

                if not neighbors:
                    break  # Зашли в тупик

                # Расчет вероятностей перехода
                probs = []
                for nxt in neighbors:
                    tau = pheromones[current][nxt] ** alpha
                    eta = (1.0 / graph[current][nxt]) ** beta
                    probs.append(tau * eta)
                sum_probs = sum(probs)
                if sum_probs == 0: break
                probs = [p / sum_probs for p in probs]

                # Рулетка (случайный выбор с учетом вероятности)
                r = random.random()
                cumulative = 0.0
                next_node = neighbors[0]
                for i, nxt in enumerate(neighbors):
                    cumulative += probs[i]
                    if r <= cumulative:
                        next_node = nxt
                        break

                path.append(next_node)
                path_length += graph[current][next_node]
                visited.add(next_node)
                current = next_node

            # Если муравей дошел до конца, запоминаем его путь
            path_str = " -> ".join([nodes[i] for i in path]) 
            if current == end_node:
                all_paths.append((path, path_length))
                print(f"Муравей {ant + 1}: {path_str} (длина: {path_length})") 
                if path_length <= best_length:
                    best_length = path_length
                    best_path = path
            else:
                print(f"Муравей {ant + 1}: {path_str} (зашел в тупик)") 

        # Испарение феромонов
        for i in range(num_nodes):
            for j in range(num_nodes):
                pheromones[i][j] *= (1.0 - evaporation_rate)
        # Обновление феромонов муравьями (оставляют след)
        for path, length in all_paths:
            for i in range(len(path) - 1):
                u, v = path[i], path[i + 1]
                pheromones[u][v] += Q / length
                pheromones[v][u] += Q / length  # Граф неориентированный
        print(f"\nСостояние феромонов после итерации {it + 1}:")
        print("    " + "      ".join(nodes))
        for i in range(num_nodes):
            row = [f"{pheromones[i][j]:.4f}" for j in range(num_nodes)]
            print(f"{nodes[i]}: {' '.join(row)}")

    # Вывод результатов
    path_names = [nodes[i] for i in best_path]
    print(f"\nКратчайший найденный путь: {' -> '.join(path_names)}")
    print(f"Длина пути: {best_length}")
    
if __name__ == '__main__':
    main()