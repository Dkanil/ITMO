import re

from Task1 import day_name

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")
s = timetableXML.read()

s = re.sub('  ', ' ', s)
s = re.sub(r'(?:</.*>)|(?:<\?.*\?>)|<', '', s)
s = re.sub('>', ': ', s)
s = re.sub('  lesson:', '- lesson:', s)

days = re.findall(r'day name="\w*":', s)
for day in days:
    day_name = re.search(r'(?<=day name=")\w*', day)
    s = re.sub(f'  day name="{day_name[0]}":', f'- day:\n    \'@name\': {day_name[0]}', s)

#убираем пустые строки
s = re.sub(r'^ *$\n', '', s, flags=re.MULTILINE)
s = s[2:]

timetableYAML.write(s)