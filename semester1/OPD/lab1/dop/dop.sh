echo "Самый часто запрашиваемый URL:"
awk '{print $7}' file.txt | sort | uniq -c | sort -nr | head -n 1

> browsers.txt
> os.txt
> bots.txt
awk -F '"' '{print $6}' file.txt | sort | uniq | grep -v '^\W*$' > user_agents.txt

echo "Android:" >> os.txt
grep Android user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\nBB10:" >> os.txt
grep BB10 user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\niPad:" | grep -vE "like.*iPhone" >> os.txt
grep "CPU OS" user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\niPhone:" | grep -vE "like.*iPhone" >> os.txt
grep "iPhone OS" user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\nMac OS:" >> os.txt
grep "Mac OS" user_agents.txt | grep -vE "like.*Mac OS" | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\nUbuntu:" >> os.txt
grep Ubuntu user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt
echo -e "\nWindows:" >> os.txt
grep Windows user_agents.txt | grep -vE "[Bb]ot|rob" >> os.txt

echo "Bots:" >> bots.txt
grep -E "[Bb]ot|rob" user_agents.txt >> bots.txt

echo "Chrome:" >> browsers.txt
grep Chrome user_agents.txt >> browsers.txt
echo -e "\nEdge:" >> browsers.txt
grep Edge user_agents.txt >> browsers.txt
echo -e "\nFirefox:" >> browsers.txt
grep Firefox user_agents.txt >> browsers.txt
echo -e "\nOpera:" >> browsers.txt
grep Opera user_agents.txt >> browsers.txt
echo -e "\nSafari:" >> browsers.txt
grep -E "Version/.* Safari" user_agents.txt >> browsers.txt

echo "Парсинг завершён"