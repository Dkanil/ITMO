import re
from inspect import stack

def XML_to_obj(XML_file):
    s = XML_file.read()
    tags_iters = re.finditer(r'(?:<[^?/].+?>)|(?:</[^?].+?>)', s)

    #переведём из итераторов в многомерные массивы
    tags = []
    for i in tags_iters:
        if re.fullmatch(r'<[^?/].+?>', i.group()):
            tags.append([i.group()[1:-1], "starting_tag", i.end()])
        else:
            tags.append([i.group()[2:-1], "ending_tag", i.start()])

    #Соберём данные в словарь
    stck = []
    ma = {}
    for i in range(len(tags)):
        if tags[i][1] == "starting_tag":
            stck.append(tags[i])
        else:
            new_obj = s[stck[-1][2]:tags[i][2]]
            ma[stck[-1][0]] = new_obj
            print(stck[-1][0] + ': ', new_obj)
            stck.pop()

    print(ma)

    return ma

def obj_to_YAML(obj, YAML_file):
    obj = '0'
    s = obj
    YAML_file.write(s)

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")

obj = XML_to_obj(timetableXML)
obj_to_YAML(obj, timetableYAML)