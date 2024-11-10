#(Вариант 17) 466217 : 17 % 36 = 17
# XML -> YAML Среда, суббота

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_main_task.yaml", mode="w", encoding="utf-8")
s = timetableXML.read()
s = s.split("\n")

for lines in range(1, len(s)):
    line = s[lines]
    new_line = line

    for symbol in range(len(line) - 1):
        if line[symbol] == '<' and line[symbol + 1] == '/':
            current_block_ending = line[line.index('</') + 2:-1]
            new_line = new_line.replace('</' + current_block_ending + '>', '', 1)
            break
        elif line[symbol] == '<':
            current_block = line[line.index('<') + 1:line.index('>')]
            new_line = new_line.replace('<', '', 1)
            new_line = new_line.replace('>', ': ', 1)

    day_name = ''
    if line.find('day name=') > 0:
        day_name = line[line.index('"') + 1:line.index('>') - 1]
        new_line = new_line.replace(f'    day name="{day_name}":', f'- day:\n        \'@name\': {day_name}')
    if new_line.find('lesson') > 0:
        new_line = new_line.replace('    lesson:','- lesson:')

    #Уменьшение табуляции в два раза
    new_line = new_line.replace('  ', '%^')
    new_line = new_line.replace('%^', ' ')

    if new_line.count(' ') != len(new_line):
        timetableYAML.write(new_line + '\n')