```
using System;
using System.Collections.Generic;
using System.Linq;

// Фнкциональное программирование в C#
// 1. Всё является функцией
var result = Enumerable.Range(1, 10)
    .Where(x => x % 2 == 0) // Выбираем только чётные числа
    .Select(x => x * 2)// Удваиваем каждое число после фильтрации
    .Aggregate((acc, next) => acc += next); // Суммируем все удвоенные числа
Console.WriteLine(result);

var slow = Enumerable.Range(1, 1000000000)
    .Where(x => x % 2 == 0)
    .Select(x => SlowOperation(x));
int SlowOperation(int x)
{
    Thread.Sleep(1000);
    return x;
}

var group = Enumerable.Range(1, 20)
    .GroupBy(x => x % 3) // Группируем по остатку от деления на 3
    .Select(x => x.Max()) // Выбираем максимальное значение в каждой группе
    .ToList();

foreach (var item in group) 
    Console.WriteLine(item);

var groupList = Enumerable.Range(1, 20).GroupBy(x => x % 3) // Группируем по остатку от деления на 3
    .SelectMany(x => x) // Преобразуем каждую группу в список и объединяем все списки
    .TakeWhile(x => x < 15); // Берём элементы, пока они меньше 15

foreach (var item in groupList)
    Console.WriteLine(item);
```
