import xml.etree.ElementTree as ET
import toml

def xml_to_dict(element):
    """Рекурсивно преобразует XML-элемент в словарь или список."""
    children = list(element)
    if not children:  # Если нет дочерних элементов, возвращаем текст
        return element.text
    grouped = {}
    for child in children:
        if child.tag not in grouped:
            grouped[child.tag] = []
        grouped[child.tag].append(xml_to_dict(child))
    # Если тег содержит только один дочерний элемент, убираем список
    for key in grouped:
        if len(grouped[key]) == 1:
            grouped[key] = grouped[key][0]
    return grouped

def xml_to_toml(xml_string):
    """Преобразует XML-строку в TOML-строку."""
    root = ET.fromstring(xml_string)
    xml_dict = {root.tag: xml_to_dict(root)}
    return toml.dumps(xml_dict)

timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
timetableTOML = open("src/Timetable_task5.toml", mode="w", encoding="utf-8")
s = timetableXML.read()

# Преобразование XML в TOML
toml_data = xml_to_toml(s)
timetableTOML.write(toml_data)
print(toml_data)
