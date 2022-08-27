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
                            <a href="controller?command=userLogout" class="d-block mb-2 btn btn-primary fs-5 fw-semibold lh-1">Вийти</a>
                            <a class="text-primary fw-semibold" role="button" data-bs-target="#editUserDataWindow" data-bs-toggle="modal">Редагувати користувача</a>
                        </div>
                        <div class="modal fade" id="editUserDataWindow" aria-hidden="true" aria-labelledby="editUserDataWindowLabel" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content w-75">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editUserDataWindowLabel">Редагувати користувача</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form id="editUserForm" action="controller?command=userEdit" method="post">
                                            <label>
                                                <input type="number" name="from" hidden>
                                            </label>
                                            <label>
                                                <input type="number" name="to" hidden>
                                            </label>
                                            <label>
                                                <input name="datePicker" hidden>
                                            </label>
                                            <div class="container px-0">
                                                <div class="row">
                                                    <div class="col-10">
                                                        <label for="editUserEmailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пошта</label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail">
                                                            </span>
                                                            <input id="editUserEmailField" type="email" class="form-control" aria-label="E-mail" value="${sessionScope.user.getEmail()}" required disabled>
                                                        </div>
                                                    </div>
                                                    <div class="col-10">
                                                        <label for="editUserPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пароль</label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                                                <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                                            </span>
                                                            <input id="editUserPasswordField" type="password" class="form-control" placeholder="Пароль" name="password" required disabled>
                                                        </div>
                                                        <label for="editUserConfirmPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Підтвердити пароль</label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                                                <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                                            </span>
                                                            <input id="editUserConfirmPasswordField" type="password" class="form-control" placeholder="Підтвердити пароль" name="confirmPassword" required disabled>
                                                        </div>
                                                    </div>
                                                    <div class="col-1 form-check form-switch form-check-reverse align-self-center">
                                                        <label>
                                                            <input class="form-check-input" role="switch" type="checkbox">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-10">
                                                        <label for="editUserSurnameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Прізвище</label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                                            </span>
                                                            <label>
                                                                <input value="${sessionScope.user.getLastName()}" hidden>
                                                            </label>
                                                            <input id="editUserSurnameField" type="text" class="form-control" placeholder="Прізвище" name="userSurname" value="${sessionScope.user.getLastName()}" required disabled>
                                                        </div>
                                                    </div>
                                                    <div class="col-1 form-check form-switch form-check-reverse align-self-center">
                                                        <label>
                                                            <input class="form-check-input" role="switch" type="checkbox">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-10">
                                                        <label for="editUserNameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Ім'я</label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                                            </span>
                                                            <label>
                                                                <input value="${sessionScope.user.getFirstName()}" hidden>
                                                            </label>
                                                            <input id="editUserNameField" type="text" class="form-control" name="userName" value="${sessionScope.user.getFirstName()}" required disabled>
                                                        </div>
                                                    </div>
                                                    <div class="col-1 form-check form-switch form-check-reverse align-self-center">
                                                        <label>
                                                            <input class="form-check-input" role="switch" type="checkbox">
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button class="btn btn-primary" form="editUserForm" disabled>Редагувати</button>
                                        <button class="btn btn-primary" data-bs-dismiss="modal" aria-label="Close">Назад</button>
                                    </div>
                                </div>
                            </div>
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
                                <form id="authenticationForm" action="controller?command=userLogin" method="post">
                                    <label>
                                        <input type="number" name="from" hidden>
                                    </label>
                                    <label>
                                        <input type="number" name="to" hidden>
                                    </label>
                                    <label>
                                        <input name="datePicker" hidden>
                                    </label>
                                    <label for="emailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пошта</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text"><img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail"></span>
                                        <input id="emailField" type="email" class="form-control" placeholder="Пошта" aria-label="E-mail" name="email" required>
                                    </div>
                                    <label for="passwordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пароль</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="passwordField" type="password" class="form-control" placeholder="Пароль" name="password" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary w-100" form="authenticationForm">Увійти</button>
                                <a class="text-primary" role="button" data-bs-target="#registrationWindow" data-bs-toggle="modal">Зареєструватися</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="registrationWindow" aria-hidden="true" aria-labelledby="registrationWindowLabel" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content w-75">
                            <div class="modal-header">
                                <h5 class="modal-title" id="registrationWindowLabel">Реєстрація</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form id="registrationForm" action="controller?command=userRegistration" method="post">
                                    <label>
                                        <input type="number" name="from" hidden>
                                    </label>
                                    <label>
                                        <input type="number" name="to" hidden>
                                    </label>
                                    <label>
                                        <input name="datePicker" hidden>
                                    </label>
                                    <label for="registrationEmailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пошта</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text"><img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail"></span>
                                        <input id="registrationEmailField" type="email" class="form-control" placeholder="Пошта" aria-label="E-mail" name="email" required>
                                    </div>
                                    <label for="registrationPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Пароль</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="registrationPasswordField" type="password" class="form-control" placeholder="Пароль" name="password" required>
                                    </div>
                                    <label for="registrationConfirmPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Підтвердити пароль</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="registrationConfirmPasswordField" type="password" class="form-control" placeholder="Підтвердити пароль" name="confirmPassword" required>
                                    </div>
                                    <label for="userSurnameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Прізвище</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                        </span>
                                        <input id="userSurnameField" type="text" class="form-control" placeholder="Прізвище" name="userSurname" required>
                                    </div>
                                    <label for="userNameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1">Ім'я</label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                        </span>
                                        <input id="userNameField" type="text" class="form-control" placeholder="Ім'я" name="userName" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary w-100" form="registrationForm">Зареєструватися</button>
                                <a class="text-primary" role="button" data-bs-target="#authenticationWindow" data-bs-toggle="modal">Назад</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </header>
        <c:if test="${sessionScope.user.getRole() eq 'admin'}">
            <nav class="navbar bg-primary container px-0">
                <div class="container-fluid">
                    <div class="navbar-brand offset-5 text-light fs-4 fw-semibold lh-1">Адміністрування</div>
                    <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                        <div class="offcanvas-header bg-secondary bg-opacity-25">
                            <h5 class="offcanvas-title offset-3" id="offcanvasNavbarLabel">Адміністрування</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                        </div>
                        <div class="offcanvas-body bg-secondary bg-opacity-25">
                            <ul class="navbar-nav justify-content-end flex-grow-1">
                                <li class="nav-item">
                                    <a class="nav-link fs-5 fw-semibold" href="#train" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                        Поїзд
                                    </a>
                                    <ul id="train" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Додати поїзд
                                            </a>
                                            <form id="addTrain" class="collapse ps-2" action="controller?command=addTrain" method="post">
                                                <label for="trainNumber" class="form-label">Номер поїзда</label>
                                                <input id="trainNumber" class="form-control w-50 mb-2" type="text" name="trainNumber" autocomplete="off" required>
                                                <label for="trainSeats" class="form-label">Кількість місць поїзда</label>
                                                <input id="trainSeats" class="form-control w-50 mb-2" type="number" name="trainSeats" required>
                                                <label for="trainDepartureTime" class="form-label">Час відправлення поїзда</label>
                                                <input id="trainDepartureTime" class="form-control w-50 mb-2" type="time" name="trainDepartureTime" required>
                                                <button class="btn btn-primary">Додати поїзд</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Видалити поїзд
                                            </a>
                                            <form id="deleteTrain" class="collapse ps-2" action="controller?command=deleteTrain" method="post">
                                                <label for="trainNumberForDelete" class="form-label">Номер поїзда</label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForDelete" class="form-control w-50 mb-2" list="trainNumberDatalist" type="text" autocomplete="off" required>
                                                <button id="deleteTrainButton" class="btn btn-primary">Видалити поїзд</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Редагувати поїзд
                                            </a>
                                            <form id="editTrain" class="collapse ps-2" action="controller?command=editTrain" method="post">
                                                <label for="trainNumberForEdit" class="form-label">Номер поїзда</label>
                                                <label class="d-block">
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForEdit" class="d-inline-block form-control w-50 mb-2" list="trainNumberDatalist" type="text" name="trainNumber" autocomplete="off" required>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <label for="trainSeatsForEdit" class="d-block form-label">Кількість місць поїзда</label>
                                                <input id="trainSeatsForEdit" class="d-inline-block form-control w-50 mb-2" type="number" name="trainSeats" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <label for="trainDepartureTimeForEdit" class="d-block form-label">Час відправлення поїзда</label>
                                                <input id="trainDepartureTimeForEdit" class="d-inline-block form-control w-50 mb-2" type="time" name="trainDepartureTime" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <button class="btn btn-primary" disabled>Редагувати поїзд</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editTrainRoute" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Редагувати маршрут поїзда
                                            </a>
                                            <form id="editTrainRoute" class="collapse ps-2" action="controller" method="get">
                                                <label for="trainNumberForEditRoute" class="form-label">Номер поїзда</label>
                                                <label>
                                                    <input name="command" value="showRoute" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForEditRoute" class="form-control w-50 mb-2" list="trainNumberDatalist" type="text" autocomplete="off" required>
                                                <button class="btn btn-primary">Редагувати маршрут поїзда</button>
                                            </form>
                                        </li>
                                        <li>
                                            <datalist id="trainNumberDatalist">
                                                <c:forEach items="${requestScope.trainsForAdmin}" var="train">
                                                    <option value="${train.getNumber()}" id="${train.getId()}"></option>
                                                </c:forEach>
                                            </datalist>
                                            <datalist id="trainSeatsDatalist">
                                                <c:forEach items="${requestScope.trainsForAdmin}" var="train">
                                                    <option value="${train.getSeats()}" id="${train.getId()}"></option>
                                                </c:forEach>
                                            </datalist>
                                            <datalist id="trainDepartureTimeDatalist">
                                                <c:forEach items="${requestScope.trainsForAdmin}" var="train">
                                                    <option value="${train.getDepartureTime()}" id="${train.getId()}"></option>
                                                </c:forEach>
                                            </datalist>
                                        </li>
                                    </ul>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link fs-5 fw-semibold" href="#station" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                        Станція
                                    </a>
                                    <ul id="station" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Додати станцію
                                            </a>
                                            <form id="addStation" class="collapse ps-2" action="controller?command=addStation" method="post">
                                                <label for="stationName" class="form-label">Назва станції</label>
                                                <input id="stationName" class="form-control w-50 mb-2" type="text" name="stationName" autocomplete="off" required>
                                                <button class="btn btn-primary">Додати станцію</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Видалити станцію
                                            </a>
                                            <form id="deleteStation" class="collapse ps-2" action="controller?command=deleteStation" method="post">
                                                <label for="stationNameForDelete" class="form-label">Назва станції</label>
                                                <label>
                                                    <input type="number" name="stationId" hidden>
                                                </label>
                                                <input id="stationNameForDelete" class="form-control w-50 mb-2" type="text" list="stationsDatalistOptions" autocomplete="off" required>
                                                <button class="btn btn-primary">Видалити станцію</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Редагувати станцію
                                            </a>
                                            <form id="editStation" class="collapse ps-2" action="controller?command=editStation" method="post">
                                                <label for="oldStationNameForEdit" class="form-label">Назва станції</label>
                                                <label>
                                                    <input type="number" name="stationId" hidden>
                                                </label>
                                                <input id="oldStationNameForEdit" class="form-control w-50 mb-2" type="text" list="stationsDatalistOptions" autocomplete="off" required>
                                                <label for="newStationNameForEdit" class="form-label">Назва станції</label>
                                                <input id="newStationNameForEdit" class="form-control w-50 mb-2" type="text" name="stationName" autocomplete="off" required disabled>
                                                <button class="btn btn-primary" disabled>Редагувати станцію</button>
                                            </form>
                                        </li>
                                    </ul>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link fs-5 fw-semibold" href="#schedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                        Розклад
                                    </a>
                                    <ul id="schedule" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addTrainToSchedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Додати поїзд до розкладу
                                            </a>
                                            <form id="addTrainToSchedule" class="collapse ps-2" action="controller?command=addTrainToSchedule" method="post">
                                                <label for="trainNumberForAddToSchedule" class="form-label">Назва поїзда</label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForAddToSchedule" class="form-control w-50 mb-2" type="text" list="trainNumberDatalist" autocomplete="off" required>
                                                <button class="btn btn-primary" disabled>Додати</button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteTrainFromSchedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                Видалити поїзд з розкладу
                                            </a>
                                            <form id="deleteTrainFromSchedule" class="collapse ps-2" action="controller?command=deleteTrainFromSchedule" method="post">
                                                <label for="trainNumberForDeleteFromSchedule" class="form-label">Назва поїзда</label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForDeleteFromSchedule" class="form-control w-50 mb-2" type="text" list="trainNumberDatalist" autocomplete="off" required>
                                                <button class="btn btn-primary" disabled>Видалити</button>
                                            </form>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </nav>
        </c:if>
        <main class="container bg-secondary bg-gradient bg-opacity-25">
            <form action="controller" method="get">
                <label>
                    <input name="command" value="getTrains" hidden>
                </label>
                <label>
                    <input type="number" name="page" value="1" hidden>
                </label>
                <div class="row pt-4 position-relative">
                    <div class="col-6 pe-4">
                        <label for="fromDatalist" class="form-label text-primary fs-5 fw-semibold lh-1">Звідки</label>
                        <label>
                            <input type="number" name="from" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="stationsDatalistOptions" id="fromDatalist">
                    </div>
                    <img class="img-fluid w-auto position-absolute top-50 start-50 translate-middle-x reverseRoute" role="button" src="resources/images/two%20arrow.png" alt="">
                    <div class="col-6 ps-4">
                        <label for="toDatalist" class="form-label text-primary fs-5 fw-semibold lh-1">Куди</label>
                        <label>
                            <input type="number" name="to" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="stationsDatalistOptions" id="toDatalist">
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
                <datalist id="stationsDatalistOptions">
                    <c:forEach items="${requestScope.stations}" var="station">
                        <option value="${station.getName()}" id="${station.getId()}"></option>
                    </c:forEach>
                </datalist>
            </form>
            <c:if test="${requestScope.trains.size() > 0}">
                <table class="table table-secondary table-bordered text-center table-hover align-middle">
                    <thead class="text-primary fs-5 fw-semibold lh-1">
                        <tr class="align-middle">
                            <th>№ Поїзда</th>
                            <th>Звідки/Куди</th>
                            <th>Дата</th>
                            <th class="text-start">
                                <div>Відправлення
                                    <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDepartureTime'}">
                                        <a href="controller?command=getTrainsSortedByDepartureTime&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                            <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                        </a>
                                    </c:if>
                                </div>
                                <div>Прибуття
                                    <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDestinationTime'}">
                                        <a href="controller?command=getTrainsSortedByDestinationTime&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                            <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                        </a>
                                    </c:if>
                                </div>
                            </th>
                            <th>Тривалість
                                <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDurationTrip'}">
                                    <a href="controller?command=getTrainsSortedByDurationTrip&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                        <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                    </a>
                                </c:if>
                            </th>
                            <th>Ціна проїзду, грн</th>
                            <th>Вільних місць
                                <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByAvailableSeats'}">
                                    <a href="controller?command=getTrainsSortedByAvailableSeats&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                        <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                    </a>
                                </c:if>
                            </th>
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
                                <div class="d-flex justify-content-between mb-1 lh-sm"><span>Відправлення</span> <span>${train.getRoute().getDepartureDateOfWeekAndDateAsString(requestScope.departureDate)}</span></div>
                                <div class="d-flex justify-content-between"><span>Прибуття</span> <span>${train.getRoute().getDestinationDateOfWeekAndDateAsString(requestScope.fromStationId, requestScope.toStationId, requestScope.departureDate)}</span></div>
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
                                            <form action="controller" method="get">
                                                <label>
                                                    <input name="command" value="ticketPage" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="trainId" value="${train.getId()}" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="from" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="to" hidden>
                                                </label>
                                                <label>
                                                    <input name="datePicker" hidden>
                                                </label>
                                                <button class="btn btn-primary fs-6 lh-1 mt-2">Замовити</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="btn btn-primary fs-6 lh-1 mt-2 disabled">Замовити</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${requestScope.numberOfPages gt 1}">
                    <c:choose>
                        <c:when test="${requestScope.trainsSortedCommand ne null}">
                            <c:set var="trainsSortedCommand" value="${requestScope.trainsSortedCommand}" scope="page"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="trainsSortedCommand" value="getTrains" scope="page"/>
                        </c:otherwise>
                    </c:choose>
                    <nav class="d-flex justify-content-center">
                        <ul class="pagination">
                            <c:choose>
                                <c:when test="${requestScope.page eq 1}">
                                    <li class="page-item disabled">
                                        <a class="page-link" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link" href="controller?command=${pageScope.trainsSortedCommand}&page=${requestScope.page - 1}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="1" end="${requestScope.numberOfPages}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${requestScope.page eq loop.count}">
                                        <li class="page-item active">
                                            <a class="page-link">${loop.count}</a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item">
                                            <a class="page-link" href="controller?command=${pageScope.trainsSortedCommand}&page=${loop.count}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">${loop.count}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${requestScope.page eq requestScope.numberOfPages}">
                                    <li class="page-item disabled">
                                        <a class="page-link" aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link" href="controller?command=${pageScope.trainsSortedCommand}&page=${requestScope.page + 1}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}" aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </c:if>
            </c:if>
            <c:if test="${requestScope.trains.size() eq 0}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-success fw-semibold">Не знайдено поїздів на цю дату за даним маршрутом</div>
                </div>
            </c:if>
            <c:if test="${sessionScope.successMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-success fs-4 fw-semibold">${sessionScope.successMessage}</div>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            <c:if test="${sessionScope.errorMessage ne null}">
                <div class="row pb-4 justify-content-center">
                    <div class="col-4 text-danger fw-semibold">${sessionScope.errorMessage}</div>
                </div>
                <c:remove var="errorMessage" scope="session"/>
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
            <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                <div class="modal fade" id="confirmDeleteTrainModal" tabindex="-1" aria-labelledby="confirmDeleteTrainModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteTrainModalLabel">Видалення поїзда</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2">Ви впевнені, що бажаєте видалити цей поїзд?</div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteTrain">Видалити</button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Скасувати</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="confirmDeleteStationModal" tabindex="-1" aria-labelledby="confirmDeleteStationModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteStationModalLabel">Видалення станції</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2">Ви впевнені, що бажаєте видалити цю станцію?</div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteStation">Видалити</button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Скасувати</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="confirmDeleteTrainFromScheduleModal" tabindex="-1" aria-labelledby="confirmDeleteTrainFromScheduleModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteTrainFromScheduleModalLabel">Видалення поїзда з розкладу</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2">Ви впевнені, що бажаєте видалити цей поїзд з розкладу?</div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteTrainFromSchedule">Видалити</button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Скасувати</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </main>
        <script type="text/javascript" src="resources/js/main.js"></script>
        <c:if test="${requestScope.departureDate ne null}">
            <script>
                for (let i = 0; i < inputsFrom.length; i++) {
                    inputsFrom[i].setAttribute("value", ${requestScope.fromStationId});
                }
                inputFrom.setAttribute("value", document.querySelector('#stationsDatalistOptions option[id="' + ${requestScope.fromStationId} + '"]').value);
                for (let i = 0; i < inputsTo.length; i++) {
                    inputsTo[i].setAttribute("value", ${requestScope.toStationId});
                }
                inputTo.setAttribute("value", document.querySelector('#stationsDatalistOptions option[id="' + ${requestScope.toStationId} + '"]').value);
            </script>
            <span id="checkedDate" hidden>${requestScope.departureDate}</span>
        </c:if>
    </body>
</html>