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
                    <img src="resources/images/train.png" class="img-fluid">
                </div>
                <div class="col-6 align-self-center text-center text-primary text-opacity-75">
                    <p class="fs-2 fw-bolder lh-sm">Залізнична каса</p>
                    <p class="fs-4 fw-semibold lh-1">Вибір потягів та купівля білетів</p>
                </div>
                <div class="col-2 align-self-center">
                    <a href="#" class="btn btn-primary opacity-75 fs-5 fw-semibold lh-1">Авторизація</a>
                </div>
            </div>
        </header>
        <main class="container bg-secondary bg-gradient bg-opacity-25">
            <form action="controller" method="get">
                <input name="command" value="getTrains" hidden>
                <div class="row pt-4 position-relative">
                    <div class="col-6 pe-4">
                        <label for="fromDatalist" class="form-label text-primary text-opacity-75 fs-5 fw-semibold lh-1">Звідки</label>
                        <input name="from" hidden>
                        <input class="form-control" list="fromDatalistOptions" id="fromDatalist">
                        <datalist id="fromDatalistOptions">
                            <c:forEach items="${requestScope.stations}" var="station">
                                <option value="${station.getName()}" id="${station.getId()}"></option>
                            </c:forEach>
                        </datalist>
                    </div>
                    <img class="img-fluid w-auto position-absolute top-50 start-50 translate-middle-x" role="button" src="resources/images/two%20arrow.png">
                    <div class="col-6 ps-4">
                        <label for="toDatalist" class="form-label text-primary text-opacity-75 fs-5 fw-semibold lh-1">Куди</label>
                        <input name="to" hidden>
                        <input class="form-control" list="toDatalistOptions" id="toDatalist">
                        <datalist id="toDatalistOptions">
                            <c:forEach items="${requestScope.stations}" var="station">
                                <option value="${station.getName()}" id="${station.getId()}"></option>
                            </c:forEach>
                        </datalist>
                    </div>
                </div>
                <div class="row py-3">
                    <div class="col-2">
                        <label for="datePicker" class="form-label text-primary text-opacity-75 fs-5 fw-semibold lh-1">Дата відправлення</label>
                        <input class="form-control fs-5 fw-semibold lh-1 text-center" type="text" name="datePicker" id="datePicker">
                    </div>
                </div>
                <div class="row pb-4 justify-content-center">
                    <div class="col-4">
                        <button class="btn btn-lg btn-primary opacity-75 fs-5 fw-semibold lh-1" datatype="searchTrains">Пошук поїздів</button>
                    </div>
                </div>
            </form>
            <c:if test="${requestScope.trains.size() > 0}">
                <table class="table">
                    <thead>
                        <tr>
                            <th>№ Поїзда</th>
                            <th>Звідки/Куди</th>
                            <th>Дата</th>
                            <th>Відправлення/Прибуття</th>
                            <th>Тривалість</th>
                            <th>Вільних місць</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${requestScope.trains.values()}" var="train">
                        <tr>
                            <td>${train.getNumber()}</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${train.getSeats()}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${requestScope.errorMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-danger fw-semibold">${requestScope.errorMessage}</div>
                </div>
            </c:if>
        </main>
        <script type="text/javascript" src="resources/js/main.js"></script>
    </body>
</html>