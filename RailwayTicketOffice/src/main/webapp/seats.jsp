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

<c:if test="${requestScope.carriage ne null}">
    <c:set var="link" value="" scope="page"/>
    <c:forEach begin="0" items="${requestScope.carriage}" varStatus="loop">
        <c:set var="link" value="${pageScope.link}&carriage=${requestScope.carriage[loop.index]}&seat=${requestScope.seat[loop.index]}&cost=${requestScope.cost[loop.index]}" scope="page"/>
    </c:forEach>
</c:if>
<html>
    <head>
        <title><fmt:message key="seats_jsp.title"/></title>
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
                                        <a class="dropdown-item p-0" href="controller?command=changeSeatsPageLocale&locale=${appLocale.key}&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}&carriageType=${requestScope.carriageType}&carriageNumber=${requestScope.carriageNumber}${pageScope.link}"><img src="resources/images/${appLocale.key}-icon.png" alt="${appLocale.key}"></a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25 px-0">
            <nav class="navbar border border-secondary rounded mb-2">
                <div class="container-fluid justify-content-start">
                    <a href="controller?command=getTrains&page=1&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}"><img src="resources/images/back-icon.png" alt="Back to choice trains"></a>
                </div>
            </nav>
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
                        <td class="p-0">
                            <div class="container">
                                <c:forEach begin="1" end="${requestScope.train.getCarriagesTypesNumber()}" varStatus="loop">
                                    <c:if test="${requestScope.train.getFreeSeatsSumByCarriageType(loop.count) > 0}">
                                        <div class="row justify-content-evenly border-bottom border-secondary border-opacity-25">
                                            <div class="col pt-2 px-0 text-primary">${requestScope.train.getCarriageTypeOrderByMaxSeats(loop.count)}</div>
                                            <div class="col pt-2 px-0">${requestScope.train.getFreeSeatsSumByCarriageType(loop.count)}</div>
                                            <c:choose>
                                                <c:when test="${requestScope.train.getCarriageTypeOrderByMaxSeats(loop.count) eq requestScope.carriageType}">
                                                    <c:set var="disabled" value="disabled" scope="page"/>
                                                    <c:remove var="chooseSeatsByCarriageTypeHref"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="chooseSeatsByCarriageTypeHref" value="href=controller?command=chooseSeatsPage&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}&carriageType=${requestScope.train.getCarriageTypeOrderByMaxSeats(loop.count)}${pageScope.link}" scope="page"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <a class="col btn btn-primary btn-sm ${pageScope.disabled} my-1" role="button" ${pageScope.chooseSeatsByCarriageTypeHref}><fmt:message key="main_jsp.button_for_chooseTicket"/></a>
                                        </div>
                                    </c:if>
                                    <c:remove var="disabled"/>
                                </c:forEach>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <ul class="nav nav-pills bg-secondary bg-opacity-50">
                <li class="nav-item text-light fs-4 fw-semibold text-center pt-1 px-5"><span><fmt:message key="seats_jsp.main.ul.first_li.span"/></span></li>
                <c:forEach items="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).values()}" var="carriage">
                    <c:choose>
                        <c:when test="${requestScope.carriageNumber eq carriage.getNumber()}">
                            <c:set var="border" value="border border-light rounded" scope="page"/>
                            <c:set var="chooseCarriageHref" value="#" scope="page"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="chooseCarriageHref" value="controller?command=chooseSeatsPage&trainId=${requestScope.train.getId()}&from=${requestScope.from}&to=${requestScope.to}&departureDate=${requestScope.departureDate}&carriageType=${carriage.getType()}&carriageNumber=${carriage.getNumber()}${pageScope.link}" scope="page"/>
                        </c:otherwise>
                    </c:choose>
                    <li class="nav-item text-center ${pageScope.border} position-relative" style="width: 113px; height: 50px; background-image: url('resources/images/carriage-icon.png'); background-size: cover">
                        <a class="nav-link text-light fs-4 fw-semibold" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="<fmt:message key="seats_jsp.tooltip_first_part"/>${carriage.getNumber()} <fmt:message key="seats_jsp.tooltip_second_part"/> ${carriage.getSeats().size()}" href="${pageScope.chooseCarriageHref}" aria-current="page">${carriage.getNumber()}</a>
                        <span class="badge position-absolute top-0 fs-5">${carriage.getSeats().size()}</span>
                    </li>
                    <c:remove var="border"/>
                </c:forEach>
            </ul>
            <div class="container w-75 my-3 border border-5 border-primary text-center" style="height: 200px; border-radius: 30px">
                <c:choose>
                    <c:when test="${requestScope.carriageType eq 'Л'}">
                        <div class="row px-5">
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats()}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 120px">
                                    <a class="nav-link pt-4 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${loop.index}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:when test="${requestScope.carriageType eq 'К'}">
                        <div class="row px-5">
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() / 2}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index * 2))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 60px">
                                    <a class="nav-link pt-2 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${loop.index * 2}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                            <div class="w-100"></div>
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() / 2}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index + (loop.index - 1)))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 60px">
                                    <a class="nav-link pt-2 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${loop.index + (loop.index - 1)}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:when test="${requestScope.carriageType eq 'П'}">
                        <div class="row px-5">
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() / 3}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index * 2))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 48px">
                                    <a class="nav-link pt-1 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${loop.index * 2}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                            <div class="w-100"></div>
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() / 3}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index + (loop.index - 1)))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 48px">
                                    <a class="nav-link pt-1 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${loop.index + (loop.index - 1)}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                            <div class="w-100" style="height: 48px"></div>
                            <c:forEach begin="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() / 3 * 2 + 1}" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats()}" varStatus="loop">
                                <c:set var="diff" value="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats() + 1 - loop.count}" scope="page"/>
                                <c:choose>
                                    <c:when test="${loop.index % 2 ne 0}">
                                        <c:set var="divClasses" value="border-start" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="divClasses" value="border-end" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(pageScope.diff))}">
                                        <c:set var="aClasses" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aClasses" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat" scope="page"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col py-1 px-0 ${pageScope.divClasses} border-primary border-1" style="height: 48px">
                                    <a class="nav-link pt-1 d-inline-block ${pageScope.aClasses} text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 40px" ${pageScope.freeSeat}>${pageScope.diff}</a>
                                </div>
                                <c:remove var="divClasses"/>
                                <c:remove var="aClasses"/>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row px-5">
                            <c:forEach begin="1" end="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getMaxSeats()}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${requestScope.train.getCarriagesFilteredByTypeAndSortedByNumber(requestScope.carriageType).get(Integer.parseInt(requestScope.carriageNumber)).getSeats().contains(Integer.parseInt(loop.index))}">
                                        <c:set var="bgProperties" value="bg-primary" scope="page"/>
                                        <c:set var="freeSeat" value="data-cost='${requestScope.train.getRoute().getCostOfTripAsString(requestScope.from, requestScope.to, requestScope.carriageType)}'" scope="page"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="bgProperties" value="bg-secondary bg-opacity-50" scope="page"/>
                                        <c:remove var="freeSeat"/>
                                    </c:otherwise>
                                </c:choose>
                                <div class="col m-1 p-0" style="height: 35px">
                                    <a class="nav-link pt-1 ${pageScope.bgProperties} d-inline-block text-center text-light fs-5 fw-semibold h-100 rounded" style="width: 35px" ${pageScope.freeSeat}>${loop.index}</a>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <form id="chosenSeats" class="container justify-content-end pe-4" action="controller" method="get">
                <label>
                    <input type="text" name="command" value="ticketPage" hidden>
                </label>
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
                    <input type="text" name="departureDate" value="${requestScope.departureDate}" hidden>
                </label>
                <label>
                    <input type="text" name="carriageType" value="${requestScope.carriageType}" hidden>
                </label>
                <div class="row mb-2 justify-content-end">
                    <c:choose>
                        <c:when test="${requestScope.carriage ne null}">
                            <c:forEach begin="0" items="${requestScope.carriage}" varStatus="loop">
                                <fieldset class="col-1 pe-0 me-1 border border-secondary h-75" disabled>
                                    <a type="button" class="btn-close offset-5 opacity-100" aria-label="Close"></a>
                                    <legend class="fs-6"><fmt:message key="seats_jsp.legend"/> ${loop.count}</legend>
                                    <label><fmt:message key="seats_jsp.label_for_carriageInput"/>
                                        <input class="w-25 text-end border-0" name="carriage" value="${requestScope.carriage[loop.index]}">
                                    </label>
                                    <label><fmt:message key="seats_jsp.label_for_seatInput"/>
                                        <input class="w-25 text-end border-0" name="seat" value="${requestScope.seat[loop.index]}">
                                    </label>
                                    <label>
                                        <input class="w-25 text-end border-0" name="cost" value="${requestScope.cost[loop.index]}" hidden>
                                    </label>
                                </fieldset>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <fieldset class="col-1 pe-0 me-1 border border-secondary h-75" disabled hidden>
                                <a type="button" class="btn-close offset-5 opacity-100" aria-label="Close"></a>
                                <legend class="fs-6"><fmt:message key="seats_jsp.legend"/> 1</legend>
                                <label><fmt:message key="seats_jsp.label_for_carriageInput"/>
                                    <input class="w-25 text-end border-0" name="carriage" value="">
                                </label>
                                <label><fmt:message key="seats_jsp.label_for_seatInput"/>
                                    <input class="w-25 text-end border-0" name="seat" value="">
                                </label>
                                <label>
                                    <input class="w-25 text-end border-0" name="cost" value="" hidden>
                                </label>
                            </fieldset>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="row mb-2 justify-content-end">
                    <div id="totalCost" class="col-3 pe-1 text-end" hidden><fmt:message key="seats_jsp.div_totalCost"/> </div>
                </div>
                <div class="row justify-content-end">
                    <button id="issueTickets" class="col-2 btn btn-primary btn-lg"><fmt:message key="seats_jsp.button_issueTickets"/></button>
                </div>
            </form>
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
        </main>
        <script type="text/javascript" src="resources/js/seats.js"></script>
    </body>
</html>
