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
    <a href="index.jsp" style="margin: 10px 0; display: inline-block;">Пальнуть ещё разочек</a>
</header>
<main>
    <section class="graph-section">
        <h2>Карта боевых действий</h2>
        <div style="position: relative; width: 600px; height: 600px;">
            <canvas id="bgCanvas" width="600" height="600" style="position:absolute; z-index:0;"></canvas>
            <canvas id="graphCanvas" width="600" height="600" style="position:absolute; z-index:1;"></canvas>
        </div>
        <img id="boom-gif" src="resources/boom.gif"
             style="display:none;position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);z-index:10;" alt="">
        <img id="miss-gif" src="resources/miss.gif"
             style="display:none;position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);z-index:10;" alt="">
    </section>
    <section class="results-section">
        <h2>Куда упала бомба?</h2>
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
                    <c:set var="lastResult" value="${resultsBean.results[resultsBean.results.size() - 1]}"/>
                    <tr>
                        <td><fmt:formatNumber value="${lastResult.x}" minFractionDigits="0"
                                              maxFractionDigits="15"/></td>
                        <td><fmt:formatNumber value="${lastResult.y}" minFractionDigits="0"
                                              maxFractionDigits="15"/></td>
                        <td><fmt:formatNumber value="${lastResult.r}" minFractionDigits="0"/></td>
                        <td>${lastResult.hit ? "Прилёт" : "mOzIlA"}</td>
                        <td><fmt:formatDate value="${lastResult.timestamp}" pattern="dd.MM.yyyy, HH:mm:ss"/></td>
                        <td>${lastResult.executionTime}ms</td>
                    </tr>
                    </tbody>
                </table>
                <div id="lastResult" data-x="${lastResult.x}" data-y="${lastResult.y}" data-r="${lastResult.r}"
                     data-hit="${lastResult.hit}" data-timestamp="${lastResult.timestamp}"
                     data-executionTime="${lastResult.executionTime}"></div>
            </div>
        </div>
    </section>
</main>
<script src="index.js"></script>
</body>
</html>
