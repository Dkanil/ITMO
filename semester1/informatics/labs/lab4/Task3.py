import re

def XML_to_obj(XML_file):
    s = XML_file.read()
    start_tags_iters = re.finditer(r'<[^?/].+?>', s)
    end_tags_iters = re.finditer(r'</[^?].+?>', s)
    obj = []

    #переведём из итераторов в многомерные массивы
    start_tags = []
    for i in start_tags_iters:
        start_tags.append([i.group(), i.start(), i.end()])
    end_tags = []
    for i in end_tags_iters:
        end_tags.append([i.group(), i.start(), i.end()])

    for i in range(len(start_tags)):
        j = len(start_tags) - 1 - i
        #print(end_tags[i])
        #new_obj = re.findall(fr'(?<=<{start_tags[i][2]}>).*?(?=</{end_tags[i][1]}>)', s, flags=re.MULTILINE)
        new_obj = s[start_tags[i][2]:end_tags[j][1]]
        obj.append(new_obj)
        print(str(i) + ': ', new_obj)
    print(obj)

    return obj

def obj_to_YAML(obj, YAML_file):
    obj = '0'
    s = obj
    YAML_file.write(s)

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableYAML = open("src/Timetable_task3.yaml", mode="w", encoding="utf-8")

obj = XML_to_obj(timetableXML)
obj_to_YAML(obj, timetableYAML)