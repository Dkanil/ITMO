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
            text = s[stck[-1][2]:tags[i][2]]
            #Если тег содержит дочерние теги, то изменим его
            if re.search(r'(?:<[^/].+?>)|(?:</.+?>)', text) is not None:
                text = re.findall(r'(?<=<)(.+?)(?=>)[\w\W]*?(?<=</)\1(?=>)', text)

            ma[stck[-1][0]] = text
            print(stck[-1][0] + ': ', text)
            stck.pop()
    print()
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