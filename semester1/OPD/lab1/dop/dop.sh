echo "Самый часто запрашиваемый URL:" > result.txt
awk '{print $7}' access.log | sort | uniq -c | sort -nr | head -n 1  >> result.txt
echo -e "\nСписок User-Agent:" >> result.txt
awk -F '"' '{print $6}' access.log | awk '{print $1}' | awk -F '/[0-9]' '{print $1}' | sort | uniq | grep -v '^\W*$' >> result.txt

echo "Парсинг завершён. Результаты сохранены в файл result.txt"
