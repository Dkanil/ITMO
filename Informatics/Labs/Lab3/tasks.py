import re

def task1(s):
    s = re.findall(r'\[<\)', s)
    print('Количество эмодзи вида "[<)" = ' + str(len(s)))

def task2(s):
    print('Все слова, в которых две гласные стоят подряд, а после слова идёт слово, в котором не больше 3 согласных:')
    buf = s.lower()
    buf = re.split(r'\W', buf)
    s = re.split(r'\W', s)
    answ = []
    for i in range(0, len(s) - 1):
        flag1 = re.findall(r'[аеёиоуыэюя]{2}', buf[i])
        if len(flag1) != 0:
            flag2 = re.findall(r'[бвгджзйклмнпрстфхцчшщ]', buf[i + 1])
            if len(flag2) <= 3:
                answ.append(s[i])
    for i in answ:
        print(i)
    if len(answ) == 0:
        print("Ни одно слово не подходит")
    print()

def task3(s):
    print('Слова, в которых встречается строго одна гласная буква:')
    buf = s.lower()
    buf = re.split(r'\W', buf)
    s = re.split(r'\W', s)
    words = []

    for i in range(len(s)):
        flag = True
        glasn = re.findall(r'[аеёиоуыэюя]', buf[i])
        if len(glasn) >= 2:
            for j in range(len(glasn) - 1):
                if glasn[j] != glasn[j + 1]:
                    flag = False
        elif len(glasn) == 0:
            flag = False
        if flag:
            words.append([len(s[i]), s[i]])
    words.sort()
    for i in words:
        print(i[1])
    print()