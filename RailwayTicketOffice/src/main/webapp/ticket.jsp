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
                                <input type="image" src="resources/images/uk-icon.png" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" alt="uk">
                                <ul class="dropdown-menu p-0" style="background: none; min-width: 0; border: 0">
                                    <c:forEach items="${applicationScope.locales}" var="locale">
                                        <c:if test="${locale.key ne 'uk'}">
                                            <li>
                                                <a class="dropdown-item p-0" href="controller?command=changeTicketPageLocale&locale=${locale.key}&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <input type="image" src="resources/images/${sessionScope.locale}-icon.png" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" alt="${sessionScope.locale}">
                                <ul class="dropdown-menu p-0" style="background: none; min-width: 0; border: 0">
                                    <c:forEach items="${applicationScope.locales}" var="locale">
                                        <c:if test="${locale.key ne sessionScope.locale}">
                                            <li>
                                                <a class="dropdown-item p-0" href="controller?command=changeTicketPageLocale&locale=${locale.key}&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25 px-0">
            <nav class="navbar border border-secondary rounded">
                <div class="container-fluid justify-content-start">
                    <img src="resources/images/back-icon.png" role="button" onclick="window.history.back()" alt="back">
                    <a class="navbar-brand offset-1 text-primary fs-4 fw-bold" href="controller?command=mainPage"><fmt:message key="route_jsp.nav.a"/></a>
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
                    <th><fmt:message key="main_jsp.sixth_th"/></th>
                    <th><fmt:message key="main_jsp.seventh_th"/></th>
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
                            <div>${requestScope.train.getRoute().getDurationTrip(requestScope.from, requestScope.to)}</div>
                        </td>
                        <td id="ticketCost">${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to)}</td>
                        <td>
                            <div>${requestScope.train.getSeats()}</div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="container">
                <div class="row justify-content-center">
                    <label for="ticketCounter" class="fs-5 text-primary text-center mb-2"><fmt:message key="ticket_jsp.label_for_ticketCounter"/></label>
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
                    <input type="number" name="from" value="${requestScope.from}" hidden>
                </label>
                <label>
                    <input type="number" name="to" value="${requestScope.to}" hidden>
                </label>
                <label>
                    <input name="departureDate" value="${requestScope.departureDate}" hidden>
                </label>
                <div class="text-center fs-5 fw-semibold text-primary mb-1"><fmt:message key="ticket_jsp.main.form.first_div"/></div>
                <div class="container px-0" datatype="passenger">
                    <div class="fs-5 fw-semibold text-primary bg-secondary bg-opacity-50 mb-1"><fmt:message key="ticket_jsp.main.form.second_div.first_div"/> 1</div>
                    <div class="row mb-3">
                        <div class="col-4">
                            <label for="passengerSurname" class="form-label text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserSurnameField"/></label>
                            <input id="passengerSurname" class="form-control" name="passengerSurname" placeholder=<fmt:message key="main_jsp.label_for_editUserSurnameField"/> value="${sessionScope.user.getLastName()}" required>
                        </div>
                        <div class="col-4">
                            <label for="passengerName" class="form-label text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserNameField"/></label>
                            <input id="passengerName" class="form-control" name="passengerName" placeholder=<fmt:message key="main_jsp.label_for_editUserNameField"/> value="${sessionScope.user.getFirstName()}" required>
                        </div>
                    </div>
                </div>
                <div id="totalCost" class="fs-5 text-end text-primary mb-2"><fmt:message key="ticket_jsp.div_totalCost"/>: ${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to)} <fmt:message key="ticket_jsp.div_totalCost_UAH"/>.</div>
                <div class="container">
                    <div class="row justify-content-end">
                        <button class="col-1 btn btn-lg btn-primary mb-3"><fmt:message key="ticket_jsp.button_for_buyTicket"/></button>
                    </div>
                </div>
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
            </form>
        </main>
        <script type="text/javascript" src="resources/js/ticket.js"></script>
    </body>
</html>
