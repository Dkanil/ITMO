# Вариант: 466217
## Как запустить?
### Для запуска в docker: 
- Убрать стартовую строку в Main.java из HTTP ответов
- ```docker compose up --build -d```
Подключиться к http://localhost:8080
### Для запуска на helios:
- Собрать jar через ```.\gradlew jar```
- Пробросить порты на гелиосе ```ssh -p 2222 s466217@helios.cs.ifmo.ru -L 4662:localhost:4662```
- Создать на гелиосе кучу директорий со структурой:
```
| web/
| - lab1/
| - - httpd-root/
| - - - conf/ (сюда httpd-conf-helios.conf)
| - - - fcgi-bin/ (здесь jar)
| - - - mutex-dir/
| - - .www/ (здесь всё из static)
```
- ```httpd -f ~/web/lab1/httpd-root/conf/httpd-conf-helios.conf -k start```
- ```java -jar -DFCGI_PORT=4663 httpd-root/fcgi-bin/lab1.jar```
- Подключаемся в браузере к http://localhost:4662