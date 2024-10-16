import re

def task1(s):
    s = re.findall(r'\[<\)', s)
    print('Количество эмодзи вида "[<)" = ' + str(len(s)))

def task2(s):
    print('Все слова, в которых 2 гласные стоят подряд, а после идёт слово, в котором не больше 3 согласных:')
    s = re.split(r'\W|\d', s)
    s = list(filter(None, s)) #Убираем пыстые строки
    answ = []
    for i in range(len(s) - 1):
        flag_first_word = bool(re.fullmatch(r'\w*[аеёиоуыэюя]{2}\w*', s[i], flags=re.IGNORECASE))
        flag_second_word = bool(re.fullmatch(r'(?:\w*[бвгджзйклмнпрстфхцчшщ]\w*){4,}', s[i + 1], flags=re.IGNORECASE))
        if flag_first_word and not flag_second_word:
            answ.append(s[i])

    for i in answ:
        print(i)
    if len(answ) == 0:
        print("Ни одно слово не подходит")
    print()

def task3(s):
    print('Слова, в которых встречается строго одна гласная буква:')
    s = re.finditer(r'\b[бвгджзйклмнпрстфхцчшщъь]*([аеёиоуюыэя])[бвгджзйклмнпрстфхцчшщъь]*(?:[бвгджзйклмнпрстфхцчшщъь]*\1[бвгджзйклмнпрстфхцчшщъь]*)*\b', s)
    words = []
    for i in s:
        words.append([len(i[0]), i[0]])

    words.sort()
    for i in words:
        print(i[1])
    if len(words) == 0:
        print("Ни одно слово не подходит")
    print()