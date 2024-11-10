import re

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml.yaml", mode="w", encoding="utf-8")
s = timetableXML.read()


timetableYAML.write(s)