<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="http://com.my.railwayticketoffice.tag" %>

<c:if test="${sessionScope.locale ne null}">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="resources"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title><fmt:message key="route_jsp.title"/></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/html-duration-picker@latest/dist/html-duration-picker.min.js"></script>
    </head>
    <body>
        <header class="container bg-secondary bg-opacity-25">
            <div class="row">
                <div class="col-4">
                    <img src="resources/images/train.png" class="img-fluid" alt="">
                </div>
                <div class="col-6 align-self-center text-center text-primary">
                    <p class="fs-2 fw-bolder lh-sm"><fmt:message key="main_jsp.body.header.div.div.first_p"/></p>
                    <p class="fs-4 fw-semibold lh-1"><fmt:message key="main_jsp.body.header.div.div.second_p"/></p>
                </div>
                <div class="col-2">
                    <div class="dropdown mt-1 mb-5 offset-5">
                        <c:choose>
                            <c:when test="${sessionScope.locale eq null}">
                                <c:set var="locale" value="uk" scope="page"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="locale" value="${sessionScope.locale}" scope="page"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="image" src="resources/images/${pageScope.locale}-icon.png" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" alt="${pageScope.locale}">
                        <ul class="dropdown-menu p-0" style="background: none; min-width: 0; border: 0">
                            <c:forEach items="${applicationScope.locales}" var="appLocale">
                                <c:if test="${appLocale.key ne pageScope.locale}">
                                    <li>
                                        <a class="dropdown-item p-0" href="controller?command=changeRoutePageLocale&locale=${appLocale.key}&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}"><img src="resources/images/${appLocale.key}-icon.png" alt="${appLocale.key}"></a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25 px-0">
            <nav class="navbar border border-secondary rounded">
                <div class="container-fluid justify-content-start">
                    <img src="resources/images/back-icon.png" role="button" onclick="window.history.back()" alt="back">
                    <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                        <a class="navbar-brand offset-1 text-primary fs-4 fw-bold" href="controller?command=mainPage"><fmt:message key="route_jsp.nav.a"/></a>
                    </c:if>
                </div>
            </nav>
            <c:if test="${sessionScope.successMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col text-center text-success fs-4 fw-semibold">${sessionScope.successMessage}</div>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            <c:if test="${sessionScope.errorMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col text-center text-danger fs-4 fw-semibold">${sessionScope.errorMessage}</div>
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>
            <div class="text-center text-primary fs-4 fw-semibold lh-1 py-2">
                <fmt:message key="route_jsp.main.first_div"/>
                <img class="img-fluid w-auto" src="resources/images/train-icon.png" alt="">
                <span class="fw-bold">№${requestScope.train.getNumber()}</span>
                <c:if test="${requestScope.train.getRoute().getStations().size() gt 0}">
                    ${requestScope.train.getRoute().getDepartureStationName()} - ${requestScope.train.getRoute().getDestinationStationName()}
                </c:if>
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
                                <div><fmt:message key="route_jsp.thead.second_tr.first_th.first_div"/></div>
                                <div><fmt:message key="route_jsp.thead.second_tr.first_th.second_div"/></div>
                            </th>
                            <th class="align-middle"><fmt:message key="route_jsp.thead.second_tr.second_th"/></th>
                            <th class="align-middle"><fmt:message key="main_jsp.label_for_stationNameForDelete"/></th>
                        </tr>
                    </thead>
                    <tbody class="table-group-divider">
                    <c:set var="isStationWillBeVisited" value="false" scope="page"/>
                    <c:forEach items="${requestScope.train.getRoute().getStations()}" varStatus="loop" var="station">
                        <tr>
                            <c:choose>
                                <c:when test="${loop.first}">
                                    <c:choose>
                                        <c:when test="${station.getId() eq requestScope.from}">
                                            <c:set var="isStationWillBeVisited" value="true" scope="page"/>
                                            <c:set var="spanBorderColor" value="border-primary bg-primary" scope="page"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="spanBorderColor" value="border-secondary border-opacity-75 bg-light" scope="page"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:when test="${loop.last}">
                                    <c:choose>
                                        <c:when test="${isStationWillBeVisited eq true}">
                                            <c:set var="borderRight" value="border-right: 2px dashed rgba(13, 110, 253, 0.75);" scope="page"/>
                                            <c:set var="spanBorderColor" value="border-primary bg-primary" scope="page"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="borderRight" value="border-right: 2px dashed rgba(108, 117, 125, 0.75);" scope="page"/>
                                            <c:set var="spanBorderColor" value="border-secondary border-opacity-75 bg-light" scope="page"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${isStationWillBeVisited eq true}">
                                            <c:choose>
                                                <c:when test="${station.getId() eq requestScope.to}">
                                                    <c:set var="isStationWillBeVisited" value="false" scope="page"/>
                                                    <c:set var="borderRight" value="border-right: 2px dashed rgba(13, 110, 253, 0.75);" scope="page"/>
                                                    <c:set var="timeInMinutes" scope="page">
                                                        <div><my:timeInMinutes time="${requestScope.train.getRoute().getTimeStop(station.getId())}"/> <fmt:message key="route_jsp.td.div_minutes"/>.</div>
                                                    </c:set>
                                                    <c:set var="spanBorderColor" value="border-primary bg-primary" scope="page"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="borderRight" value="border-right: 2px dashed rgba(13, 110, 253, 0.75);" scope="page"/>
                                                    <c:set var="timeInMinutes" scope="page">
                                                        <div><my:timeInMinutes time="${requestScope.train.getRoute().getTimeStop(station.getId())}"/> <fmt:message key="route_jsp.td.div_minutes"/>.</div>
                                                    </c:set>
                                                    <c:set var="spanBorderColor" value="border-primary bg-light" scope="page"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${station.getId() eq requestScope.from}">
                                                    <c:set var="isStationWillBeVisited" value="true" scope="page"/>
                                                    <c:set var="borderRight" value="border-right: 2px dashed rgba(108, 117, 125, 0.75);" scope="page"/>
                                                    <c:set var="timeInMinutes" scope="page">
                                                        <div><my:timeInMinutes time="${requestScope.train.getRoute().getTimeStop(station.getId())}"/> <fmt:message key="route_jsp.td.div_minutes"/>.</div>
                                                    </c:set>
                                                    <c:set var="spanBorderColor" value="border-primary bg-primary" scope="page"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="borderRight" value="border-right: 2px dashed rgba(108, 117, 125, 0.75);" scope="page"/>
                                                    <c:set var="timeInMinutes" scope="page">
                                                        <div><my:timeInMinutes time="${requestScope.train.getRoute().getTimeStop(station.getId())}"/> <fmt:message key="route_jsp.td.div_minutes"/>.</div>
                                                    </c:set>
                                                    <c:set var="spanBorderColor" value="border-secondary border-opacity-75 bg-light" scope="page"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <td class="position-relative align-bottom py-0" style="height: 70px; ${pageScope.borderRight}">
                                <div>${requestScope.train.getRoute().getArrivalTime(station.getId())}</div>
                                ${pageScope.timeInMinutes}
                                <span class="position-absolute start-100 bottom-0 translate-middle-x badge rounded-circle border border-2 ${pageScope.spanBorderColor} p-2">
                                    <span class="visually-hidden"></span>
                                </span>
                                <c:remove var="borderRight" scope="page"/>
                                <c:remove var="timeInMinutes" scope="page"/>
                            </td>
                            <td class="align-bottom py-0">${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}</td>
                            <td class="d-flex text-start align-bottom py-0 justify-content-between align-items-end" style="height: 70px;">
                                <c:choose>
                                    <c:when test="${station.getId() eq requestScope.from}">
                                        <c:set var="divStation" scope="page">
                                            <div class="text-primary align-self-baseline"><fmt:message key="route_jsp.td.div_departureStation"/>:</div>
                                        </c:set>
                                    </c:when>
                                    <c:when test="${station.getId() eq requestScope.to}">
                                        <c:set var="divStation" scope="page">
                                            <div class="text-primary align-self-baseline"><fmt:message key="route_jsp.td.div_destinationStation"/>:</div>
                                        </c:set>
                                    </c:when>
                                </c:choose>
                                <div>
                                    ${pageScope.divStation}
                                    ${station.getName()}
                                    <c:remove var="divStation" scope="page"/>
                                </div>
                                <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                                    <div>
                                        <div class="d-inline-block position-relative">
                                            <button class="btn border-0" data-bs-toggle="tooltip" data-bs-placement="top" name="editStationDataOnTrainRoute" data-bs-title=<fmt:message key="route_jsp.button_data_bs_title_editStationDataOnTrainRoute"/>>
                                                <img src="resources/images/edit-icon.png" alt="Редагувати дані станції на маршруті">
                                            </button>
                                            <form id="editStationDataOnTrainRoute" action="controller?command=editStationDataOnTrainRoute" class="position-absolute end-100 top-0 collapse fw-semibold bg-secondary p-1 rounded" style="width: 200px" method="post">
                                                <label>
                                                    <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="stationId" value="${station.getId()}" hidden>
                                                </label>
                                                <label for="timeSinceStartForEdit"><fmt:message key="route_jsp.label_for_timeSinceStartForEdit"/></label>
                                                <input id="timeSinceStartForEdit" class="d-inline-block form-control mb-1 html-duration-picker text-start" data-hide-seconds name="timeSinceStart" value="${requestScope.train.getRoute().getTimeSinceStart(station.getId())}" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input value="${requestScope.train.getRoute().getTimeSinceStart(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <label for="stopTimeForEdit"><fmt:message key="route_jsp.label_for_stopTimeForEdit"/></label>
                                                <input id="stopTimeForEdit" class="d-inline-block w-50 form-control mb-1 time" name="stopTime" value="${requestScope.train.getRoute().getStopTime(station.getId())}" type="text" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input type="time" value="${requestScope.train.getRoute().getStopTime(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <label for="distanceFromStartForEdit"><fmt:message key="route_jsp.label_for_distanceFromStartForEdit"/></label>
                                                <input id="distanceFromStartForEdit" class="d-inline-block w-50 form-control mb-1" name="distanceFromStart" value="${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}" type="number" min="0" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input type="number" value="${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <button class="btn btn-primary" disabled><fmt:message key="main_jsp.button_for_editUserForm"/></button>
                                            </form>
                                        </div>
                                        <form class="align-self-center d-inline-block" action="controller?command=deleteStationFromTrainRoute" method="post">
                                            <label>
                                                <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                                            </label>
                                            <label>
                                                <input type="number" name="stationId" value="${station.getId()}" hidden>
                                            </label>
                                            <button class="btn border-0" data-bs-toggle="tooltip" data-bs-placement="right" name="deleteStationFromTrainRoute" data-bs-title=<fmt:message key="route_jsp.button_data_bs_title_deleteStationFromTrainRoute"/>>
                                                <img src="resources/images/minus-icon.png" alt="Видалити станцію з маршруту">
                                            </button>
                                        </form>
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                    <div class="col-2 offset-1">
                        <button class="btn btn-primary fw-semibold" data-bs-toggle="collapse" data-bs-target="#addStationToTrainRoute" aria-expanded="false" aria-controls="addStationToTrainRoute">
                            <fmt:message key="route_jsp.button_data_bs_target_addStationToTrainRoute"/>
                        </button>
                        <form id="addStationToTrainRoute" class="collapse fw-semibold bg-secondary bg-opacity-50 p-1 rounded" action="controller?command=addStationToTrainRoute" method="post">
                            <label for="stationName"><fmt:message key="main_jsp.label_for_stationNameForDelete"/></label>
                            <label>
                                <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                            </label>
                            <label>
                                <input type="number" name="stationId" hidden>
                            </label>
                            <input id="stationName" class="form-control mb-1" list="stationNameDatalist" type="text" autocomplete="off" required>
                            <datalist id="stationNameDatalist">
                                <c:forEach items="${requestScope.stations}" var="station">
                                    <c:if test="${!requestScope.train.getRoute().checkIfStationIsOnTheRoute(station.getId())}">
                                        <option value="${station.getName()}" id="${station.getId()}"></option>
                                    </c:if>
                                </c:forEach>
                            </datalist>
                            <label for="timeSinceStart"><fmt:message key="route_jsp.label_for_timeSinceStartForEdit"/></label>
                            <input id="timeSinceStart" class="form-control w-100 mb-1 html-duration-picker text-start" data-hide-seconds name="timeSinceStart" required>
                            <label for="stopTime"><fmt:message key="route_jsp.label_for_stopTimeForEdit"/></label>
                            <input id="stopTime" class="form-control mb-1 time" name="stopTime" type="text" required>
                            <label for="distanceFromStart"><fmt:message key="route_jsp.label_for_distanceFromStartForEdit"/></label>
                            <input id="distanceFromStart" class="form-control mb-1" name="distanceFromStart" type="number" min="0" required>
                            <button class="btn btn-primary"><fmt:message key="main_jsp.a_for_addStation"/></button>
                        </form>
                    </div>
                    <div class="modal fade" id="confirmDeleteStationFromTrainRouteModal" tabindex="-1" aria-labelledby="confirmDeleteStationFromTrainRouteModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title text-warning" id="confirmDeleteStationFromTrainRouteModalLabel"><fmt:message key="route_jsp.h5_confirmDeleteStationFromTrainRouteModalLabel"/></h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body text-danger fs-5 fw-semibold lh-2"><fmt:message key="route_jsp.div_for_confirmDeleteStationFromTrainRouteModal"/></div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary"><fmt:message key="main_jsp.button_for_deleteTrainFromSchedule"/></button>
                                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="main_jsp.button_for_confirmDeleteTrainModal_cancel"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title text-danger" id="errorModalLabel"><fmt:message key="main_jsp.h5_errorModalLabel"/></h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body fs-5 fw-semibold lh-2 errorModalBody"></div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="main_jsp.button_for_errorModal"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/gh/dmuy/MDTimePicker@2.0.0/dist/mdtimepicker.min.css">
                    <script type="text/javascript" src="https://cdn.jsdelivr.net/gh/dmuy/MDTimePicker@2.0.0/dist/mdtimepicker.min.js"></script>
                    <script type="text/javascript" src="resources/js/route.js"></script>
                </c:if>
            </div>
        </main>
    </body>
</html>
