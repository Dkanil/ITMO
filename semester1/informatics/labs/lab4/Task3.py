import re
from math import trunc


def XML_to_obj(XML_file):
    s = XML_file.read()
    tags_iters = re.finditer(r'(?:<[^?/].+?>)|(?:</[^?].+?>)', s)

    #переведём из итераторов в многомерные массивы
    tags = []
    for i in tags_iters:
        if re.fullmatch(r'<[^?/].+?>', i.group()):
            tags.append([i.group()[1:-1], "starting_tag", i.end()])
            if re.search(r'".*?"', tags[-1][0]) is not None:
                attributes = re.findall(r'\b\S+?=".*?"', tags[-1][0])
                tags[-1][0] = re.search(r'.+?(?= \b\S*="\S+")', tags[-1][0]).group()
                tags[-1].append(attributes)
        else:
            tags.append([i.group()[2:-1], "ending_tag", i.start()])

    #Соберём данные в словарь
    stck = []
    parent_map = {}
    child_map = {}
    for i in range(len(tags)):
        if tags[i][1] == "starting_tag":
            stck.append(tags[i])
        else:
            text = s[stck[-1][2]:tags[i][2]]
            #Если элемент содержит дочерние элементы, то переложим его
            if re.search(r'(?:<[^/].+?>)|(?:</.+?>)', text) is not None:
                text = set(re.findall(r'(?<=<)(.+?)(?: \b\S+?=\".*?\")*(?=>)[\w\W]*?(?<=</)\1(?=>)', text))

                # Создадим словарь со всеми дочерними объектами
                for child_tag in text:
                    child_map[child_tag] = parent_map[child_tag]
                    parent_map.pop(child_tag)

                if stck[-1][0] in parent_map.keys():
                    parent_map[stck[-1][0]].append(child_map)
                else:
                    parent_map[stck[-1][0]] = [child_map]
                child_map = {}
            else:
                parent_map[stck[-1][0]] = text
            stck.pop()
    print(parent_map)
    return parent_map

def child_obj(parent_map, YAML_file, k):
    if not isinstance(parent_map, str):
        for map_object in parent_map:
            for i in map_object.keys():
                k += 1
                YAML_file.write(str(k) + k * '  ' + i + ': ' + str(child_obj(map_object[i], YAML_file, k)) + '\n')
    # else:
    #     for i in parent_map.keys():
    #         YAML_file.write(i + ': ' + str(child_obj(parent_map[i], YAML_file)) + '\n')
    return parent_map

def obj_to_YAML(obj, YAML_file):
    z = 0
    while z == 0:
        for i in obj.keys():
            YAML_file.write(i + ': ' + str(child_obj(obj[i], YAML_file, 1)) + '\n')
        z = 1


timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")

obj = XML_to_obj(timetableXML)
obj_to_YAML(obj, timetableYAML)