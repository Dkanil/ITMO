echo "Самый часто запрашиваемый URL:" > result.txt
awk '{print $7}' access.log | sort | uniq -c | sort -nr | head -n 1  >> result.txt

# Разделение User-Agent по типам (браузер и операционная система)
echo -e "\nСписок User-Agent:" >> result.txt
browsers=""
os=""
others=""

awk -F \" '{print $6}' access.log | awk -F "(" '{print $2}' | awk -F ";" '{print $1}' | sort | uniq | grep -v '^$' >> result.txt

#awk -F\" '{print $6}' "$LOG_FILE" | sort | uniq | while read user_agent; do
#    if [[ "$user_agent" =~ (Chrome|Firefox|Safari|Edge|Opera|SamsungBrowser) ]]; then
#        echo "Браузер: $user_agent" >> "$OUTPUT_FILE"
#    elif [[ "$user_agent" =~ (Windows|Linux|Android|iPhone|Macintosh|Ubuntu) ]]; then
#        echo "ОС: $user_agent" >> "$OUTPUT_FILE"
#    else
#        echo "Прочее: $user_agent" >> "$OUTPUT_FILE"
#    fi
#done

echo "Анализ завершён. Результаты сохранены в файл result.txt"
