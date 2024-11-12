#Используется библиотека xmlplain 1.6.0
import xmlplain

def convert():
    timetableXML = open("src/Timetable.xml", mode="r", encoding="utf-8")
    timetableYAML = open("src/Timetable_task1.yaml", mode="w", encoding="utf-8")

    obj = xmlplain.xml_to_obj(timetableXML, strip_space=True, fold_dict=True)
    xmlplain.obj_to_yaml(obj, timetableYAML)