<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="controller?command=mainPage"/>
</c:if>
<html>
    <head>
        <title>Залізнична каса - оформлення квитків</title>
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
                Оформлення квитків на поїзд
                <img class="img-fluid w-auto" src="resources/images/train-icon.png" alt="">
                <span class="fw-bold">№${requestScope.train.getNumber()}</span>
                ${requestScope.train.getRoute().getDepartureStationName()} - ${requestScope.train.getRoute().getDestinationStationName()}
            </div>
            <table class="table table-secondary table-bordered text-center mb-4 table-hover align-middle">
                <thead class="text-primary fs-5 fw-semibold lh-1">
                <tr class="align-middle">
                    <th>№ Поїзда</th>
                    <th>Звідки/Куди</th>
                    <th>Дата</th>
                    <th class="text-start">
                        <div>Відправлення</div>
                        <div>Прибуття</div>
                    </th>
                    <th>Тривалість</th>
                    <th>Ціна проїзду, грн</th>
                    <th>Вільних місць</th>
                </tr>
                </thead>
                <tbody class="fs-5 fw-semibold lh-1">
                    <tr>
                        <td>
                            <div class="lh-sm">${requestScope.train.getNumber()}</div>
                            <a href="controller?command=showRoute&trainId=${requestScope.train.getId()}&fromStationId=${requestScope.fromStationId}&toStationId=${requestScope.toStationId}" class="fs-6 fw-normal lh-1">Маршрут</a>
                        </td>
                        <td class="text-start">
                            <div class="lh-sm">${requestScope.train.getRoute().getStationNameByStationId(requestScope.fromStationId)}</div>
                            <div>${requestScope.train.getRoute().getStationNameByStationId(requestScope.toStationId)}</div>
                        </td>
                        <td class="text-start fs-6 fw-normal">
                            <div class="d-flex justify-content-between mb-1 lh-sm">
                                <span>Відправлення</span>
                                <span>${requestScope.train.getRoute().getDateOfWeekAndDateAsString(requestScope.fromStationId, requestScope.departureDate)}</span>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span>Прибуття</span>
                                <span>${requestScope.train.getRoute().getDateOfWeekAndDateAsString(requestScope.toStationId, requestScope.departureDate)}</span>
                            </div>
                        </td>
                        <td>
                            <div class="lh-sm">${requestScope.train.getRoute().getArrivalTime(requestScope.fromStationId)}</div>
                            <div>${requestScope.train.getRoute().getArrivalTime(requestScope.toStationId)}</div>
                        </td>
                        <td>
                            <div>${requestScope.train.getRoute().getDurationTrip(requestScope.fromStationId, requestScope.toStationId)}</div>
                        </td>
                        <td id="ticketCost">${requestScope.train.getRoute().getCostOfTripAsString(requestScope.fromStationId, requestScope.toStationId)}</td>
                        <td>
                            <div>${requestScope.train.getSeats()}</div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="container">
                <div class="row justify-content-center">
                    <label for="ticketCounter" class="fs-5 text-primary text-center mb-2">Виберіть кількість квитків</label>
                    <div class="col-1">
                        <select class="form-select form-select-sm mb-2 text-center fw-semibold" id="ticketCounter">
                            <c:forEach begin="1" end="${requestScope.train.getSeats()}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.first}">
                                        <option value="${loop.count}" selected>${loop.count}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${loop.count}">${loop.count}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <form action="controller?command=buyTicket" method="post">
                <label>
                    <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                </label>
                <label>
                    <input type="number" name="fromStationId" value="${requestScope.fromStationId}" hidden>
                </label>
                <label>
                    <input type="number" name="toStationId" value="${requestScope.toStationId}" hidden>
                </label>
                <label>
                    <input name="departureDate" value="${requestScope.departureDate}" hidden>
                </label>
                <div class="text-center fs-5 fw-semibold text-primary mb-1">Введіть дані пасажирів</div>
                <div class="container px-0" datatype="passenger">
                    <div class="fs-5 fw-semibold text-primary bg-secondary bg-opacity-50 mb-1">Пасажир 1</div>
                    <div class="row mb-3">
                        <div class="col-4">
                            <label for="passengerSurname" class="form-label text-primary fs-6 fw-semibold lh-1">Прізвище</label>
                            <input id="passengerSurname" class="form-control" name="passengerSurname" value="${sessionScope.user.getLastName()}" required>
                        </div>
                        <div class="col-4">
                            <label for="passengerName" class="form-label text-primary fs-6 fw-semibold lh-1">Ім'я</label>
                            <input id="passengerName" class="form-control" name="passengerName" value="${sessionScope.user.getFirstName()}" required>
                        </div>
                    </div>
                </div>
                <div id="totalCost" class="fs-5 text-end text-primary mb-2">Загальна вартість: ${requestScope.train.getRoute().getCostOfTripAsString(requestScope.fromStationId, requestScope.toStationId)} грн.</div>
                <div class="container">
                    <div class="row justify-content-end">
                        <button class="col-1 btn btn-lg btn-primary mb-3">Купити</button>
                    </div>
                </div>
                <div class="modal fade" id="cancelModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="cancelModalLabel">Попередження</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">Ви впевнені, що бажаєте скасувати квиток?</div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary">Так</button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Ні</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </main>
        <script type="text/javascript" src="resources/js/ticket.js"></script>
    </body>
</html>
