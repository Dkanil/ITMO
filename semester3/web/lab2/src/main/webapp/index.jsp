<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en"/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>lab2</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<header>
    <h1>Козаченко Данил Александрович</h1>
    <h1>Группа: P3212 Вариант: 466217</h1>
</header>
<main>
    <section class="input-section">
        <form id="pointForm">
            <div class="form-group">
                <label>Координата X:</label>
                <div id="x-buttons">
                    <button type="button" value="-4">-4</button>
                    <button type="button" value="-3">-3</button>
                    <button type="button" value="-2">-2</button>
                    <button type="button" value="-1">-1</button>
                    <button type="button" value="0" class="selected">0</button>
                    <button type="button" value="1">1</button>
                    <button type="button" value="2">2</button>
                    <button type="button" value="3">3</button>
                    <button type="button" value="4">4</button>
                </div>
                <input type="hidden" id="x" name="x" value="0" min="-5" max="5" required>
            </div>

            <div class="form-group">
                <label for="y">Координата Y:</label>
                <input type="text" id="y" name="y" placeholder="-5...5" required>
            </div>

            <div class="form-group">
                <label for="r">Радиус района дислокации ВСУ:</label>
                <div id="r-buttons">
                    <label>
                        1
                        <input type="radio" name="r" value="1" checked>
                    </label>
                    <label>
                        1.5
                        <input type="radio" name="r" value="1.5">
                    </label>
                    <label>
                        2
                        <input type="radio" name="r" value="2">
                    </label>
                    <label>
                        2.5
                        <input type="radio" name="r" value="2.5">
                    </label>
                    <label>
                        3
                        <input type="radio" name="r" value="3">
                    </label>
                </div>
                <input type="hidden" id="r" name="r" value="1" required>
            </div>
            <button type="submit">Бахнуть орешником</button>
        </form>
    </section>

    <section class="graph-section">
        <h2>Карта боевых действий</h2>
        <div style="position: relative; width: 300px; height: 300px;">
            <canvas id="bgCanvas" width="300" height="300" style="position:absolute; z-index:0;"></canvas>
            <canvas id="graphCanvas" width="300" height="300" style="position:absolute; z-index:1;"></canvas>
        </div>
        <img id="boom-gif" src="resources/boom.gif"
             style="display:none;position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);z-index:10;" alt="">
        <img id="miss-gif" src="resources/miss.gif"
             style="display:none;position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);z-index:10;" alt="">
    </section>

    <section class="results-section">
        <h2>Результаты</h2>
        <div class="resultsTable">
            <table id="table-header">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>mOzIlA?</th>
                    <th>Время запроса</th>
                    <th>Время работы</th>
                </tr>
                </thead>
            </table>
            <div class="table-scroll">
                <table id="table-content">
                    <tbody>
                    <jsp:useBean id="resultsBean" scope="session" class="org.example.models.ResultsBean"/>
                    <c:forEach var="resultsBean" items="${resultsBean.results}">
                        <tr>
                            <td><fmt:formatNumber value="${resultsBean.x}" minFractionDigits="0"
                                                  maxFractionDigits="15"/></td>
                            <td><fmt:formatNumber value="${resultsBean.y}" minFractionDigits="0"
                                                  maxFractionDigits="15"/></td>
                            <td><fmt:formatNumber value="${resultsBean.r}" minFractionDigits="0"/></td>
                            <td>${resultsBean.hit ? "Прилёт" : "mOzIlA"}</td>
                            <td><fmt:formatDate value="${resultsBean.timestamp}" pattern="dd.MM.yyyy, HH:mm:ss"/></td>
                            <td>${resultsBean.executionTime}ms</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</main>
<script>
    const results = [
        <c:forEach var="res" items="${resultsBean.results}" varStatus="loop">
        {
            x: ${res.x},
            y: ${res.y},
            r: ${res.r},
            hit: ${res.hit ? 'true' : 'false'},
            timestamp: "${res.timestamp}",
            executionTime: ${res.executionTime}
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
</script>
<script src="index.js"></script>
</body>
</html>