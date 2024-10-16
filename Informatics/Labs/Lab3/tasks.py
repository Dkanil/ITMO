import re

def task1(s):
    s = re.findall(r'\[<\)', s)
    print('Количество эмодзи вида "[<)" = ' + str(len(s)))

def task2(s):
    print('Все слова, в которых 2 гласные стоят подряд, а после слова идёт слово, в котором не больше 3 согласных:')
    s = re.split(r'\W|\d', s)
    s = list(filter(None, s)) #Убираем пыстые строки
    answ = []
    for i in range(0, len(s) - 1):
        flag_first_word = bool(re.fullmatch(r'\w*[аеёиоуыэюя]{2}\w*', s[i], flags=re.IGNORECASE))
        flag_second_word = bool(re.fullmatch(r'(\w*[бвгджзйклмнпрстфхцчшщ]\w*){4,}', s[i + 1], flags=re.IGNORECASE))
        if flag_first_word and not flag_second_word:
            answ.append(s[i])

    for i in answ:
        print(i)
    if len(answ) == 0:
        print("Ни одно слово не подходит")
    print()

def task3(s):
    print('Слова, в которых встречается строго одна гласная буква:')
    s = re.split(r'\W|\d', s)
    s = list(filter(None, s)) #Убираем пыстые строки
    words = []
    for i in range(len(s)):
        buf = s[i].lower()
        glasn = set(re.findall(r'[аеёиоуыэюя]', buf, flags=re.IGNORECASE))
        if len(glasn) == 1:
            words.append([len(s[i]), s[i]])

    words.sort()
    for i in words:
        print(i[1])
    if len(words) == 0:
        print("Ни одно слово не подходит")
    print()