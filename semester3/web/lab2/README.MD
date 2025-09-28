# Вариант: 467826
## Как запустить?
### Для запуска локально:
- Скачиваем "WildFly 37.0.1 Final" с https://www.wildfly.org
- Собираем проект: ```mvn clean package```
- Запускаем сервер: ```.\wildfly-37.0.1.Final\bin\standalone.bat```
- Создаём пользователя для админки: ```.\wildfly-37.0.1.Final\bin\add-user.bat```
- Подключаемся к http://localhost:9990
- Открываем вкладку Deployments -> Add -> Select a deployment -> выбираем ```.\target\lab2.war```
- Заходим на http://localhost:8080/lab2
- ГООООООЛ

### Для запуска на helios:
- Грузим WildFly на гелиос ```scp -P 2222 wildfly-37.0.1.Final.zip s466217@se.ifmo.ru:/home/studs/s466217/web/lab2/```
- хз дальше лабу не дописал


- Собрать jar через ```.\gradlew jar```
- Пробросить порты на гелиосе ```ssh -p 2222 s466217@helios.cs.ifmo.ru -L 4662:localhost:4662```
- Создать на гелиосе кучу директорий со структурой хзхзхз:
```
| web/
| - lab2/
| - - httpd-root/
| - - - conf/ (сюда httpd-conf-helios.conf)
| - - - fcgi-bin/ (здесь jar)
| - - - mutex-dir/
| - - .www/ (здесь всё из static)
```
- ```httpd -f ~/web/lab1/httpd-root/conf/httpd-conf-helios.conf -k start```
- ```java -jar -DFCGI_PORT=4663 httpd-root/fcgi-bin/lab1.jar```
- Подключаемся в браузере к http://localhost:4662