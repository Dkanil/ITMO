import re


def XML_to_obj(XML_file):
    s = XML_file.read()
    tags_iters = re.finditer(r'(?:<[^?/].+?>)|(?:</[^?].+?>)', s)

    #переведём из итераторов в многомерные массивы
    tags = []
    for i in tags_iters:
        if re.fullmatch(r'<[^?/].+?>', i.group()):
            tags.append([i.group()[1:-1], "starting_tag", i.end(), []])
            if re.search(r'".*?"', tags[-1][0]) is not None:
                attributes = re.findall(r'\b\S+?=".*?"', tags[-1][0])
                tags[-1][0] = re.search(r'.+?(?= \b\S*="\S+")', tags[-1][0]).group()
                tags[-1][3] = attributes
        else:
            tags.append([i.group()[2:-1], "ending_tag", i.start(), []])

    #Соберём данные в словарь
    stck = []
    parent_map = {}
    child_map = {}
    for i in range(len(tags)):
        if tags[i][1] == "starting_tag":
            stck.append(tags[i])
        else:
            text = s[stck[-1][2]:tags[i][2]]
            #Проверим на наличие атрибутов и добавим их в качестве объекта
            if len(stck[-1][3]) > 0:
                attributes = ''
                for i in stck[-1][3]:
                    buf_tag = re.search(r'\b\w+(?==)', i).group()
                    buf_inf = re.search(r'(?<=").*?(?=")', i).group()
                    attributes += '<' + buf_tag + '>' + buf_inf + '</' + buf_tag + '>'

            #Если элемент содержит дочерние элементы, то переложим его
            if re.search(r'(?:<[^/].+?>)|(?:</.+?>)', text) is not None:
                text = re.findall(r'(?<=<)(.+?)(?: \b\S+?=\".*?\")*(?=>)[\w\W]*?(?<=</)\1(?=>)', text)
                text_set = set()
                j = 0
                while j < len(text):
                    if text[j] in text_set:
                        text.remove(text[j])
                    else:
                        text_set.add(text[j])
                        j += 1

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
    return parent_map

new_file_str = ''
def child_obj(parent_key, parent_map, YAML_file, tab):
    global new_file_str
    if isinstance(parent_map, str):
        tab += 1
        new_file_str += tab * '  ' + parent_key + ': ' + parent_map + '\n'
        tab -= 1
    else:
        tab += 1
        for map_object in parent_map:

            if len(parent_map) > 1:
                new_file_str += (tab - 1) * '  ' + '- ' + parent_key + ':\n'
            else:
                new_file_str += tab * '  ' + parent_key + ':\n'

            for i in map_object.keys():
                child_obj(i, map_object[i], YAML_file, tab)
    YAML_file.write(new_file_str)
    new_file_str = ''
    return parent_map

def obj_to_YAML(obj, YAML_file):
    for i in obj.keys():
        child_obj(i, obj[i], YAML_file, -1)


timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")

obj = XML_to_obj(timetableXML)
obj_to_YAML(obj, timetableYAML)