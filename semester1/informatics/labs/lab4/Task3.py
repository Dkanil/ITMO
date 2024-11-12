import re

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

    print(s)
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

            print(stck[-1][0] + ':', text)
            stck.pop()
    print()
    print(parent_map)

    return parent_map

def obj_to_YAML(obj, YAML_file):
    obj = '0'
    s = obj
    YAML_file.write(s)

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")

obj = XML_to_obj(timetableXML)
obj_to_YAML(obj, timetableYAML)