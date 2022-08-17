<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Залізнична каса - продаж квитків онлайн</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
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
                <c:choose>
                    <c:when test="${sessionScope.user eq null}">
                        <div class="col-2 align-self-center">
                            <button class="btn btn-primary fs-5 fw-semibold lh-1" data-bs-target="#authenticationWindow" data-bs-toggle="modal">Авторизуватись</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-2 align-self-center">
                            <a href="controller?command=userLogout" class="btn btn-primary fs-5 fw-semibold lh-1">Вийти</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${sessionScope.user eq null}">
                <div class="modal fade" id="authenticationWindow" aria-hidden="true" aria-labelledby="authenticationWindowLabel" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered justify-content-center">
                        <div class="modal-content w-75">
                            <div class="modal-header">
                                <h5 class="modal-title text-primary fs-4 fw-semibold lh-1" id="authenticationWindowLabel">Авторизація</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form id="authenticateForm" action="controller?command=userLogin" method="post">
                                    <label>
                                        <input name="from" hidden>
                                    </label>
                                    <label>
                                        <input name="to" hidden>
                                    </label>
                                    <label>
                                        <input name="datePicker" hidden>
                                    </label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text"><img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail"></span>
                                        <input type="text" class="form-control" placeholder="E-mail" aria-label="E-mail" name="email">
                                    </div>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <label>
                                            <input type="password" class="form-control" placeholder="Пароль" name="password" required>
                                        </label>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary w-100" form="authenticateForm">Увійти</button>
                                <a class="text-primary" role="button" data-bs-target="#registrationWindow" data-bs-toggle="modal">Зареєструватися</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="registrationWindow" aria-hidden="true" aria-labelledby="registrationWindowLabel" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="registrationWindowLabel">Реєстрація</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                Hide this modal and show the first with the button below.
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary" data-bs-target="#authenticationWindow" data-bs-toggle="modal">Назад до авторизації</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25">
            <form action="controller" method="get">
                <label>
                    <input name="command" value="getTrains" hidden>
                </label>
                <div class="row pt-4 position-relative">
                    <div class="col-6 pe-4">
                        <label for="fromDatalist" class="form-label text-primary fs-5 fw-semibold lh-1">Звідки</label>
                        <label>
                            <input name="from" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="fromDatalistOptions" id="fromDatalist">
                        <datalist id="fromDatalistOptions">
                            <c:forEach items="${requestScope.stations}" var="station">
                                <option value="${station.getName()}" id="${station.getId()}"></option>
                            </c:forEach>
                        </datalist>
                    </div>
                    <img class="img-fluid w-auto position-absolute top-50 start-50 translate-middle-x reverseRoute" role="button" src="resources/images/two%20arrow.png" alt="">
                    <div class="col-6 ps-4">
                        <label for="toDatalist" class="form-label text-primary fs-5 fw-semibold lh-1">Куди</label>
                        <label>
                            <input name="to" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="toDatalistOptions" id="toDatalist">
                        <datalist id="toDatalistOptions">
                            <c:forEach items="${requestScope.stations}" var="station">
                                <option value="${station.getName()}" id="${station.getId()}"></option>
                            </c:forEach>
                        </datalist>
                    </div>
                </div>
                <div class="row py-3">
                    <div class="col-2">
                        <label for="datePicker" class="form-label text-primary fs-5 fw-semibold lh-1">Дата відправлення</label>
                        <input class="form-control fs-5 fw-semibold lh-1 text-center" type="text" name="datePicker" id="datePicker">
                    </div>
                </div>
                <div class="row pb-4 justify-content-center">
                    <div class="col-4">
                        <button class="btn btn-lg btn-primary fs-5 fw-semibold lh-1" datatype="searchTrains">Пошук поїздів</button>
                    </div>
                </div>
            </form>
            <c:if test="${requestScope.trains.size() > 0}">
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
                    <c:forEach items="${requestScope.trains}" var="train">
                        <tr>
                            <td>
                                <div class="lh-sm">${train.getNumber()}</div>
                                <a href="controller?command=showRoute&trainId=${train.getId()}&fromStationId=${requestScope.fromStationId}&toStationId=${requestScope.toStationId}" class="fs-6 fw-normal lh-1">Маршрут</a>
                            </td>
                            <td class="text-start">
                                <div class="lh-sm">${train.getRoute().getDepartureStationName()}</div>
                                <div>${train.getRoute().getDestinationStationName()}</div>
                            </td>
                            <td class="text-start fs-6 fw-normal">
                                <div class="d-flex justify-content-between mb-1 lh-sm"><span>Відправлення</span> <span>${train.getRoute().getDateOfWeekAndDateAsString(requestScope.fromStationId, requestScope.departureDate)}</span></div>
                                <div class="d-flex justify-content-between"><span>Прибуття</span> <span>${train.getRoute().getDateOfWeekAndDateAsString(requestScope.toStationId, requestScope.departureDate)}</span></div>
                            </td>
                            <td>
                                <div class="lh-sm">${train.getRoute().getArrivalTime(requestScope.fromStationId)}</div>
                                <div>${train.getRoute().getArrivalTime(requestScope.toStationId)}</div>
                            </td>
                            <td>
                                <div>${train.getRoute().getDurationTrip(requestScope.fromStationId, requestScope.toStationId)}</div>
                            </td>
                            <td>${train.getRoute().getCostOfTripAsString(requestScope.fromStationId, requestScope.toStationId)}</td>
                            <td>
                                <div>${train.getSeats()}</div>
                                <c:if test="${sessionScope.user ne null}">
                                    <c:choose>
                                        <c:when test="${train.getSeats() gt 0}">
                                            <a href="#" class="btn btn-primary fs-6 lh-1 mt-2">Купити</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#" class="btn btn-primary fs-6 lh-1 mt-2 disabled">Купити</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${requestScope.trains.size() eq 0}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-success fw-semibold">Не знайдено поїздів на цю дату за даним маршрутом</div>
                </div>
            </c:if>
            <c:if test="${requestScope.errorMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-danger fw-semibold">${requestScope.errorMessage}</div>
                </div>
            </c:if>
            <c:if test="${sessionScope.loginErrorMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-danger fw-semibold">${sessionScope.loginErrorMessage}</div>
                </div>
                <c:remove var="loginErrorMessage" scope="session"/>
            </c:if>
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
        </main>
        <c:if test="${requestScope.departureDate ne null}">
            <script>
                let inputsFrom = document.querySelectorAll('input[name="from"]');
                let inputsTo = document.querySelectorAll('input[name="to"]');
                for (let i = 0; i < inputsFrom.length; i++) {
                    inputsFrom[i].setAttribute("value", ${requestScope.fromStationId});
                }
                $('#fromDatalist')[0].setAttribute("value", $('#' + ${requestScope.fromStationId})[0].value);
                for (let i = 0; i < inputsTo.length; i++) {
                    inputsTo[i].setAttribute("value", ${requestScope.toStationId});
                }
                $('#toDatalist')[0].setAttribute("value", $('#' + ${requestScope.toStationId})[0].value);
            </script>
            <span id="checkedDate" hidden>${requestScope.departureDate}</span>
        </c:if>
        <script type="text/javascript" src="resources/js/main.js"></script>
    </body>
</html>