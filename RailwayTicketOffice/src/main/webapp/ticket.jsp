<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.user eq null}">
    <c:redirect url="controller?command=mainPage"/>
</c:if>

<c:if test="${sessionScope.locale ne null}">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="resources"/>
</c:if>

<c:set var="link" value="" scope="page"/>
<c:forEach begin="0" items="${requestScope.carriage}" varStatus="loop">
    <c:set var="link" value="${pageScope.link}&carriage=${requestScope.carriage[loop.index]}&seat=${requestScope.seat[loop.index]}&cost=${requestScope.cost[loop.index]}" scope="page"/>
</c:forEach>

<html>
    <head>
        <title><fmt:message key="ticket_jsp.title"/></title>
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
                                        <a class="dropdown-item p-0" href="controller?command=changeTicketPageLocale&locale=${appLocale.key}&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}&carriageType=${requestScope.carriageType}${pageScope.link}"><img src="resources/images/${appLocale.key}-icon.png" alt="${appLocale.key}"></a>
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
                    <a href="controller?command=chooseSeatsPage&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}&carriageType=${requestScope.carriageType}${pageScope.link}"><img src="resources/images/back-icon.png" alt="Back to choice trains"></a>
                    <a type="button" class="btn btn-primary ms-3 fs-5" href="controller?command=getTrains&page=1&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}">До вибору поїздів</a>
                </div>
            </nav>
            <div class="text-center text-primary fs-4 fw-semibold lh-1 py-2">
                <fmt:message key="ticket_jsp.main.first_div"/>
                <img class="img-fluid w-auto" src="resources/images/train-icon.png" alt="">
                <span class="fw-bold">№${requestScope.train.getNumber()}</span>
                ${requestScope.train.getRoute().getDepartureStationName()} - ${requestScope.train.getRoute().getDestinationStationName()}
            </div>
            <table class="table table-secondary table-bordered text-center mb-4 table-hover align-middle">
                <thead class="text-primary fs-5 fw-semibold lh-1">
                    <tr class="align-middle">
                        <th><fmt:message key="main_jsp.first_th"/></th>
                        <th><fmt:message key="main_jsp.label_for_fromDatalist"/>/<fmt:message key="main_jsp.label_for_toDatalist"/></th>
                        <th><fmt:message key="main_jsp.third_th"/></th>
                        <th class="text-start">
                            <div><fmt:message key="main_jsp.fourth_th.first_div"/></div>
                            <div><fmt:message key="main_jsp.fourth_th.second_div"/></div>
                        </th>
                        <th><fmt:message key="main_jsp.fifth_th"/></th>
                    </tr>
                </thead>
                <tbody class="fs-5 fw-semibold lh-1">
                    <tr>
                        <td>
                            <div class="lh-sm">${requestScope.train.getNumber()}</div>
                            <a href="controller?command=showRoute&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}" class="fs-6 fw-normal lh-1"><fmt:message key="main_jsp.first_td"/></a>
                        </td>
                        <td class="text-start">
                            <div class="lh-sm">${requestScope.train.getRoute().getStationNameByStationId(requestScope.from)}</div>
                            <div>${requestScope.train.getRoute().getStationNameByStationId(requestScope.to)}</div>
                        </td>
                        <td class="text-start fs-6 fw-normal">
                            <div class="d-flex justify-content-between mb-1 lh-sm">
                                <span><fmt:message key="main_jsp.fourth_th.first_div"/></span>
                                <span>${requestScope.train.getRoute().getDepartureDayOfWeekAndDateAsString(requestScope.departureDate, sessionScope.locale)}</span>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span><fmt:message key="main_jsp.fourth_th.second_div"/></span>
                                <span>${requestScope.train.getRoute().getDestinationDayOfWeekAndDateAsString(requestScope.from, requestScope.to, requestScope.departureDate, sessionScope.locale)}</span>
                            </div>
                        </td>
                        <td>
                            <div class="lh-sm">${requestScope.train.getRoute().getArrivalTime(requestScope.from)}</div>
                            <div>${requestScope.train.getRoute().getArrivalTime(requestScope.to)}</div>
                        </td>
                        <td>
                            <div>${requestScope.train.getRoute().getDurationTrip(requestScope.from, requestScope.to, sessionScope.locale)}</div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <form action="controller?command=buyTicket" method="post">
                <label>
                    <input type="number" name="trainId" value="${requestScope.train.getId()}" hidden>
                </label>
                <label>
                    <input type="number" name="from" value="${requestScope.from}" hidden>
                </label>
                <label>
                    <input type="number" name="to" value="${requestScope.to}" hidden>
                </label>
                <label>
                    <input name="departureDate" value="${requestScope.departureDate}" hidden>
                </label>
                <div class="text-center fs-5 fw-semibold text-primary mb-1"><fmt:message key="ticket_jsp.main.form.first_div"/></div>
                <c:forEach begin="0" items="${requestScope.carriage}" varStatus="loop">
                    <div class="container px-0" datatype="passenger">
                        <div class="fs-5 fw-semibold text-primary bg-secondary bg-opacity-50 mb-1"><fmt:message key="seats_jsp.legend"/> ${loop.count}</div>
                        <div class="row mb-3">
                            <div class="col-4">
                                <label for="passengerSurname" class="form-label text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserSurnameField"/></label>
                                <input id="passengerSurname" class="form-control" name="passengerSurname" placeholder=<fmt:message key="main_jsp.label_for_editUserSurnameField"/> required>
                            </div>
                            <div class="col-4">
                                <label for="passengerName" class="form-label text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserNameField"/></label>
                                <input id="passengerName" class="form-control" name="passengerName" placeholder=<fmt:message key="main_jsp.label_for_editUserNameField"/> required>
                            </div>
                            <div class="col-2 offset-1 btn-sm align-self-end">
                                <a class="btn btn-primary" type="cancel"><fmt:message key="main_jsp.button_for_confirmDeleteTrainModal_cancel"/></a>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-2">
                                <label><fmt:message key="seats_jsp.label_for_carriageInput"/>
                                    <input class="w-25 text-end border-0 opacity-100" name="carriage" value="${requestScope.carriage[loop.index]}" disabled>
                                </label>
                            </div>
                            <div class="col-2">
                                <label><fmt:message key="seats_jsp.label_for_seatInput"/>
                                    <input class="w-25 text-end border-0 opacity-100" name="seat" value="${requestScope.seat[loop.index]}" disabled>
                                </label>
                            </div>
                            <div class="col-2">
                                <label><fmt:message key="ticket_jsp.label_for_costInput"/>
                                    <input class="w-50 text-end border-0 opacity-100" name="cost" value="${requestScope.cost[loop.index]}" disabled> <fmt:message key="ticket_jsp.label_for_costInput_UAH"/>
                                </label>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <div id="totalCost" class="fs-5 text-end text-primary mb-2"><fmt:message key="seats_jsp.div_totalCost"/> <fmt:message key="ticket_jsp.label_for_costInput_UAH"/></div>
                <div class="container">
                    <div class="row justify-content-end">
                        <button id="buyTicket" class="col-1 btn btn-lg btn-primary mb-3"><fmt:message key="ticket_jsp.button_for_buyTicket"/></button>
                    </div>
                </div>
            </form>
            <div class="modal fade" id="cancelModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="cancelModalLabel"><fmt:message key="ticket_jsp.h5_cancelModalLabel"/></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body"><fmt:message key="ticket_jsp.div_for_cancelModal"/></div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary"><fmt:message key="ticket_jsp.div_cancelModal.first_button"/></button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="ticket_jsp.div_cancelModal.second_button"/></button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <script type="text/javascript" src="resources/js/ticket.js"></script>
    </body>
</html>
