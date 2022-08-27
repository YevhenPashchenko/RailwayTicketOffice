<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Залізнична каса - маршрут поїзда</title>
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
                            <td class="d-flex text-start align-bottom py-0 justify-content-between align-items-end" style="height: 70px;">
                                <c:choose>
                                    <c:when test="${station.getId() eq requestScope.fromStationId}">
                                        <div>
                                            <div class="text-primary align-self-baseline">Станція відправлення:</div>
                                            ${station.getName()}
                                        </div>
                                    </c:when>
                                    <c:when test="${station.getId() eq requestScope.toStationId}">
                                        <div>
                                            <div class="text-primary align-self-baseline">Станція призначення:</div>
                                            ${station.getName()}
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        ${station.getName()}
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                                    <div>
                                        <div class="d-inline-block position-relative">
                                            <button class="btn border-0" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="Редагувати дані станції на маршруті">
                                                <img src="resources/images/edit-icon.png" alt="Редагувати дані станції на маршруті">
                                            </button>
                                            <form id="editStationDataOnTrainRoute" action="controller?command=editStationDataOnTrainRoute" class="position-absolute end-100 top-0 collapse fw-semibold bg-secondary p-1 rounded" style="width: 200px" method="post">
                                                <label>
                                                    <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="stationId" value="${station.getId()}" hidden>
                                                </label>
                                                <label for="timeSinceStartForEdit">Час з моменту відправлення поїзда з першої станції маршруту</label>
                                                <input id="timeSinceStartForEdit" class="d-inline-block form-control mb-1 html-duration-picker text-start" data-hide-seconds name="timeSinceStart" value="${requestScope.train.getRoute().getTimeSinceStart(station.getId())}" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input value="${requestScope.train.getRoute().getTimeSinceStart(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <label for="stopTimeForEdit">Час зупинки поїзда на станції</label>
                                                <input id="stopTimeForEdit" class="d-inline-block w-50 form-control mb-1" name="stopTime" value="${requestScope.train.getRoute().getStopTime(station.getId())}" type="time" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input type="time" value="${requestScope.train.getRoute().getStopTime(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <label for="distanceFromStartForEdit">Відстань до першої станції маршруту</label>
                                                <input id="distanceFromStartForEdit" class="d-inline-block w-50 form-control mb-1" name="distanceFromStart" value="${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}" type="number" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox">
                                                    </label>
                                                    <label>
                                                        <input type="number" value="${requestScope.train.getRoute().getDistanceFromFirstStation(station.getId())}" hidden>
                                                    </label>
                                                </div>
                                                <button class="btn btn-primary" disabled>Редагувати</button>
                                            </form>
                                        </div>
                                        <form class="align-self-center d-inline-block" action="controller?command=deleteStationFromTrainRoute" method="post">
                                            <label>
                                                <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                                            </label>
                                            <label>
                                                <input type="number" name="stationId" value="${station.getId()}" hidden>
                                            </label>
                                            <button class="btn border-0" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-title="Видалити станцію з маршруту">
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
                            Додати станцію до маршруту поїзда
                        </button>
                        <form id="addStationToTrainRoute" class="collapse fw-semibold bg-secondary bg-opacity-50 p-1 rounded" action="controller?command=addStationToTrainRoute" method="post">
                            <label for="stationName">Назва станції</label>
                            <label>
                                <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                            </label>
                            <label>
                                <input type="number" name="stationId" hidden>
                            </label>
                            <input id="stationName" class="form-control mb-1" list="stationNameDatalist" type="text" autocomplete="off" required>
                            <datalist id="stationNameDatalist">
                                <c:forEach items="${requestScope.stations}" var="station">
                                    <option value="${station.getName()}" id="${station.getId()}"></option>
                                </c:forEach>
                            </datalist>
                            <label for="timeSinceStart">Час з моменту відправлення поїзда з першої станції маршруту</label>
                            <input id="timeSinceStart" class="form-control w-100 mb-1 html-duration-picker text-start" data-hide-seconds name="timeSinceStart" required>
                            <label for="stopTime">Час зупинки поїзда на станції</label>
                            <input id="stopTime" class="form-control mb-1" name="stopTime" type="time" required>
                            <label for="distanceFromStart">Відстань до першої станції маршруту</label>
                            <input id="distanceFromStart" class="form-control mb-1" name="distanceFromStart" type="number" required>
                            <button class="btn btn-primary">Додати станцію</button>
                        </form>
                    </div>
                </c:if>
            </div>
            <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                <div class="modal fade" id="confirmDeleteStationFromTrainRouteModal" tabindex="-1" aria-labelledby="confirmDeleteStationFromTrainRouteModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteStationFromTrainRouteModalLabel">Видалення станції з маршруту поїзда</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2">Ви впевнені, що бажаєте видалити цю станцію з маршруту поїзда?</div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Видалити</button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Скасувати</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-danger" id="errorModalLabel">Помилка</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body fs-5 fw-semibold lh-2 errorModalBody"></div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Закрити</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </main>
        <script type="text/javascript" src="resources/js/route.js"></script>
    </body>
</html>
