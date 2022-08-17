<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Залізнична каса - маршрут поїзда</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </head>
    <body>
        <header class="container bg-secondary bg-opacity-25">
            <div class="row">
                <div class="col-4">
                    <img src="resources/images/train.png" class="img-fluid" alt="">
                </div>
                <div class="col-6 align-self-center text-center text-primary">
                    <p class="fs-2 fw-bolder lh-sm">Залізнична каса</p>
                    <p class="fs-4 fw-semibold lh-1">Вибір потягів та купівля білетів</p>
                </div>
            </div>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25">
            <img src="resources/images/back-icon.png" role="button" onclick="window.history.back()" alt="back">
            <div class="text-center text-primary fs-4 fw-semibold lh-1 py-2">
                Маршрут поїзда
                <img class="img-fluid w-auto" src="resources/images/train-icon.png" alt="">
                <span class="fw-bold">№${requestScope.train.getNumber()}</span>
                ${requestScope.train.getRoute().getDepartureStationName()} - ${requestScope.train.getRoute().getDestinationStationName()}
            </div>
            <div class="row justify-content-center">
                <table class="table table-secondary text-center mb-4 w-50 col-3 table-hover">
                    <thead>
                        <tr>
                            <th><img class="img-fluid w-auto" src="resources/images/clock-icon.png" alt=""></th>
                            <th><img class="img-fluid w-auto" src="resources/images/measure-route-icon.png" alt=""></th>
                            <th><img class="img-fluid w-auto" src="resources/images/destination-icon.png" alt=""></th>
                        </tr>
                        <tr>
                            <th>
                                <div>Час прибуття</div>
                                <div>Час стоянки</div>
                            </th>
                            <th class="align-middle">Відстань, км</th>
                            <th class="align-middle">Назва станції</th>
                        </tr>
                    </thead>
                    <tbody class="table-group-divider">
                    <c:set var="isStationWillBeVisited" value="false" scope="page"/>
                    <c:forEach items="${requestScope.train.getRoute().getStations()}" varStatus="loop" var="station">
                        <tr>
                            <c:choose>
                                <c:when test="${loop.first}">
                                    <td class="position-relative align-bottom py-0" style="height: 70px;">
                                        ${requestScope.train.getRoute().getArrivalTime(station.getId())}
                                        <c:choose>
                                            <c:when test="${station.getId() eq requestScope.fromStationId}">
                                                <c:set var="isStationWillBeVisited" value="true" scope="page"/>
                                                <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-primary bg-primary p-2">
                                                    <span class="visually-hidden"></span>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-secondary border-opacity-75 bg-light p-2">
                                                    <span class="visually-hidden"></span>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:when>
                                <c:when test="${loop.last}">
                                    <c:choose>
                                        <c:when test="${isStationWillBeVisited eq true}">
                                            <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(13, 110, 253, 0.75)">
                                                    ${requestScope.train.getRoute().getArrivalTime(station.getId())}
                                                <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-primary bg-primary p-2">
                                                    <span class="visually-hidden"></span>
                                                </span>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(108, 117, 125, 0.75)">
                                                    ${requestScope.train.getRoute().getArrivalTime(station.getId())}
                                                <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-secondary border-opacity-75 bg-light p-2">
                                                    <span class="visually-hidden"></span>
                                                </span>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${isStationWillBeVisited eq true}">
                                            <c:choose>
                                                <c:when test="${station.getId() eq requestScope.toStationId}">
                                                    <c:set var="isStationWillBeVisited" value="false" scope="page"/>
                                                    <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(13, 110, 253, 0.75)">
                                                        <div>${requestScope.train.getRoute().getArrivalTime(station.getId())}</div>
                                                        <div>${requestScope.train.getRoute().getTimeStopAtStationInMinutes(station.getId())} хв.</div>
                                                        <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-primary bg-primary p-2">
                                                            <span class="visually-hidden"></span>
                                                        </span>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(13, 110, 253, 0.75)">
                                                        <div>${requestScope.train.getRoute().getArrivalTime(station.getId())}</div>
                                                        <div>${requestScope.train.getRoute().getTimeStopAtStationInMinutes(station.getId())} хв.</div>
                                                        <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-primary bg-light p-2">
                                                            <span class="visually-hidden"></span>
                                                        </span>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${station.getId() eq requestScope.fromStationId}">
                                                    <c:set var="isStationWillBeVisited" value="true" scope="page"/>
                                                    <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(108, 117, 125, 0.75)">
                                                        <div>${requestScope.train.getRoute().getArrivalTime(station.getId())}</div>
                                                        <div>${requestScope.train.getRoute().getTimeStopAtStationInMinutes(station.getId())} хв.</div>
                                                        <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-primary bg-primary p-2">
                                                            <span class="visually-hidden"></span>
                                                        </span>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="position-relative align-bottom py-0" style="height: 70px; border-right: 2px dashed rgba(108, 117, 125, 0.75)">
                                                        <div>${requestScope.train.getRoute().getArrivalTime(station.getId())}</div>
                                                        <div>${requestScope.train.getRoute().getTimeStopAtStationInMinutes(station.getId())} хв.</div>
                                                        <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 border-secondary border-opacity-75 bg-light p-2">
                                                            <span class="visually-hidden"></span>
                                                        </span>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <td class="align-bottom py-0">${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}</td>
                            <c:choose>
                                <c:when test="${station.getId() eq requestScope.fromStationId}">
                                    <td class="text-start align-bottom py-0">
                                        <div class="text-primary">Станція відправлення:</div>
                                        ${station.getName()}
                                    </td>
                                </c:when>
                                <c:when test="${station.getId() eq requestScope.toStationId}">
                                    <td class="text-start align-bottom py-0">
                                        <div class="text-primary">Станція призначення:</div>
                                        ${station.getName()}
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="text-start align-bottom py-0">${station.getName()}</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
        <script type="text/javascript" src="resources/js/main.js"></script>
    </body>
</html>
