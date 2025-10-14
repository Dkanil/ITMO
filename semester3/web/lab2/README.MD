# Вариант: 467826
## Как запустить?
### Для запуска локально:
- Скачиваем "WildFly 37.0.1 Final" с https://www.wildfly.org
- Собираем проект: ```mvn clean package```
- Создаём пользователя для админки: ```.\wildfly-37.0.1.Final\bin\add-user.bat```
- Запускаем сервер: ```.\wildfly-37.0.1.Final\bin\standalone.bat```
- Подключаемся к http://localhost:9990
- Открываем вкладку Deployments -> Add -> Select a deployment -> выбираем ```.\target\lab2.war```
- Заходим на http://localhost:8080/lab2
- ГООООООЛ

### Для запуска в Docker:
- Собираем Docker образ: ```docker build -t lab2 .```
- Запускаем контейнер: ```docker run -p 8080:8080 -p 9990:9990 lab2```
- Заходим на http://localhost:8080/lab2
- ГООООООЛ

### Для запуска на helios:
- Грузим WildFly на гелиос ```scp -P 2222 wildfly-37.0.1.Final.zip s466217@se.ifmo.ru:/home/studs/s466217/web/lab2/```
- Распаковываем ```unzip wildfly-37.0.1.Final.zip```
- Создаём пользователя для админки: ```sh .\wildfly-37.0.1.Final\bin\add-user.sh```
- Меняем в wildfly-37.0.1.Final\standalone\configuration\wilstandalone.xml все 127.0.0.1 на 0.0.0.0
- Меняем в standalone.xml все порты, кроме последнего на свои
- Указываем другую версию джавы на гелиосе ```JAVA_VERSION=21```
- Запускаем сервер: ```sh .\wildfly-37.0.1.Final\bin\standalone.sh```
- Пробрасываем два порта на гелиос: http и http-management (их мы в standalone.xml указывали)
- Собираем проект: ```mvn clean package```
- Заходим на http://localhost:{http-management}
- Открываем вкладку Deployments -> Add -> Select a deployment -> выбираем ```.\target\lab2.war```
- Заходим на http://localhost:{http}/lab2
- ГООООООЛ