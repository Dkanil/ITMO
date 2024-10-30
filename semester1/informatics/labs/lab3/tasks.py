import re

def task1(s):
    s = re.findall(r'\[<\)', s)
    print('Количество эмодзи вида "[<)" = ' + str(len(s)))

def task2(s):
    #print('Все слова, в которых 2 гласные стоят подряд, а после идёт слово, в котором не больше 3 согласных:')
    answ = re.findall(r'\w*[аеёиоуыэюя]{2}\w*\b(?![\d\W]*(?:\w*[бвгджзйклмнпрстфхцчшщ]\w*){4,}\b)', s, flags=re.IGNORECASE)

    if len(answ) != 0:
        for i in answ:
            print(i, end=' ')
    else:
        print("Ни одно слово не подходит")
    print()

def task3(s):
    print('Слова, в которых встречается строго одна гласная буква:')
    s = re.finditer(r'\b[бвгджзйклмнпрстфхцчшщъь]*([аеёиоуюыэя])[бвгджзйклмнпрстфхцчшщъь]*(?:[бвгджзйклмнпрстфхцчшщъь]*\1[бвгджзйклмнпрстфхцчшщъь]*)*\b', s, flags=re.IGNORECASE)

    words = []
    for i in s:
        words.append([len(i[0]), i[0]])
    words.sort()
    for i in words:
        print(i[1])
    if len(words) == 0:
        print("Ни одно слово не подходит")
    print()