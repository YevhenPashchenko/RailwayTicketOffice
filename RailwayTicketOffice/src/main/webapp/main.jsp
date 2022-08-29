<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.locale ne null}">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="resources"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title><fmt:message key="main_jsp.title"/></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    </head>
    <c:choose>
        <c:when test="${requestScope.trainsSortedCommand ne null}">
            <c:set var="trainsSortedCommand" value="${requestScope.trainsSortedCommand}" scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="trainsSortedCommand" value="getTrains" scope="page"/>
        </c:otherwise>
    </c:choose>
    <body>
        <header class="container bg-secondary bg-opacity-25">
            <div class="row">
                <div class="col-4">
                    <img src="resources/images/train.png" class="img-fluid" alt="Train">
                </div>
                <div class="col-6 align-self-center text-center text-primary">
                    <p class="fs-2 fw-bolder lh-sm"><fmt:message key="main_jsp.body.header.div.div.first_p"/></p>
                    <p class="fs-4 fw-semibold lh-1"><fmt:message key="main_jsp.body.header.div.div.second_p"/></p>
                </div>
                <c:choose>
                    <c:when test="${sessionScope.user eq null}">
                        <div class="col-2">
                            <div class="dropdown mt-1 mb-5 offset-5">
                                <c:choose>
                                    <c:when test="${sessionScope.locale eq null}">
                                        <input type="image" src="resources/images/uk-icon.png" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" alt="uk">
                                        <ul class="dropdown-menu p-0" style="background: none; min-width: 0; border: 0">
                                            <c:forEach items="${applicationScope.locales}" var="locale">
                                                <c:if test="${locale.key ne 'uk'}">
                                                    <li>
                                                        <a class="dropdown-item p-0" href="controller?command=changeMainPageLocale&locale=${locale.key}&trainsSortedCommand=${pageScope.trainsSortedCommand}&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
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
                                                        <a class="dropdown-item p-0" href="controller?command=changeMainPageLocale&locale=${locale.key}&trainsSortedCommand=${pageScope.trainsSortedCommand}&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <button class="btn btn-primary fs-5 fw-semibold lh-1" data-bs-target="#authenticationWindow" data-bs-toggle="modal"><fmt:message key="main_jsp.body.header.div.div.button"/></button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-2">
                            <div class="dropdown mt-1 mb-5 offset-5">
                                <c:choose>
                                    <c:when test="${sessionScope.locale eq null}">
                                        <input type="image" src="resources/images/uk-icon.png" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" alt="uk">
                                        <ul class="dropdown-menu p-0" style="background: none; min-width: 0; border: 0">
                                            <c:forEach items="${applicationScope.locales}" var="locale">
                                                <c:if test="${locale.key ne 'uk'}">
                                                    <li>
                                                        <a class="dropdown-item p-0" href="controller?command=changeMainPageLocale&locale=${locale.key}&trainsSortedCommand=${pageScope.trainsSortedCommand}&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
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
                                                        <a class="dropdown-item p-0" href="controller?command=changeMainPageLocale&locale=${locale.key}&trainsSortedCommand=${pageScope.trainsSortedCommand}&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <a href="controller?command=userLogout" class="d-block mb-2 btn btn-primary fs-5 fw-semibold lh-1"><fmt:message key="main_jsp.body.header.div.div.first_a"/></a>
                            <a class="text-primary fw-semibold" role="button" data-bs-target="#editUserDataWindow" data-bs-toggle="modal"><fmt:message key="main_jsp.body.header.div.div.second_a"/></a>
                        </div>
                        <div class="modal fade" id="editUserDataWindow" aria-hidden="true" aria-labelledby="editUserDataWindowLabel" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content w-75">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editUserDataWindowLabel"><fmt:message key="main_jsp.body.header.div.div.second_a"/></h5>
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
                                                        <label for="editUserEmailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserEmailField"/></label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail">
                                                            </span>
                                                            <input id="editUserEmailField" type="email" class="form-control" aria-label="E-mail" value="${sessionScope.user.getEmail()}" required disabled>
                                                        </div>
                                                    </div>
                                                    <div class="col-10">
                                                        <label for="editUserPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserPasswordField"/></label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                                                <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                                            </span>
                                                            <input id="editUserPasswordField" type="password" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserPasswordField"/> name="password" required disabled>
                                                        </div>
                                                        <label for="editUserConfirmPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserConfirmPasswordField"/></label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                                                <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                                            </span>
                                                            <input id="editUserConfirmPasswordField" type="password" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserConfirmPasswordField"/> name="confirmPassword" required disabled>
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
                                                        <label for="editUserSurnameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserSurnameField"/></label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                                            </span>
                                                            <label>
                                                                <input value="${sessionScope.user.getLastName()}" hidden>
                                                            </label>
                                                            <input id="editUserSurnameField" type="text" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserSurnameField"/> name="userSurname" value="${sessionScope.user.getLastName()}" required disabled>
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
                                                        <label for="editUserNameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserNameField"/></label>
                                                        <div class="input-group mb-3">
                                                            <span class="input-group-text">
                                                                <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                                            </span>
                                                            <label>
                                                                <input value="${sessionScope.user.getFirstName()}" hidden>
                                                            </label>
                                                            <input id="editUserNameField" type="text" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserNameField"/> name="userName" value="${sessionScope.user.getFirstName()}" required disabled>
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
                                        <button class="btn btn-primary" form="editUserForm" disabled><fmt:message key="main_jsp.button_for_editUserForm"/></button>
                                        <button class="btn btn-primary" data-bs-dismiss="modal" aria-label="Close"><fmt:message key="main_jsp.button_for_editUserForm_back"/></button>
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
                                <h5 class="modal-title text-primary fs-4 fw-semibold lh-1" id="authenticationWindowLabel"><fmt:message key="main_jsp.h5_authenticationWindowLabel"/></h5>
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
                                    <label for="emailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserEmailField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text"><img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail"></span>
                                        <input id="emailField" type="email" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserEmailField"/> aria-label="E-mail" name="email" required>
                                    </div>
                                    <label for="passwordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserPasswordField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="passwordField" type="password" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserPasswordField"/> name="password" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary w-100" form="authenticationForm"><fmt:message key="main_jsp.body.header.div.div.button"/></button>
                                <a class="text-primary" role="button" data-bs-target="#registrationWindow" data-bs-toggle="modal"><fmt:message key="main_jsp.a_for_registrationWindow"/></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="registrationWindow" aria-hidden="true" aria-labelledby="registrationWindowLabel" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content w-75">
                            <div class="modal-header">
                                <h5 class="modal-title" id="registrationWindowLabel"><fmt:message key="main_jsp.h5_registrationWindowLabel"/></h5>
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
                                    <label for="registrationEmailField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserEmailField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text"><img class="img-fluid" src="resources/images/email-icon.png" alt="E-mail"></span>
                                        <input id="registrationEmailField" type="email" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserEmailField"/> aria-label="E-mail" name="email" required>
                                    </div>
                                    <label for="registrationPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserPasswordField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="registrationPasswordField" type="password" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserPasswordField"/> name="password" required>
                                    </div>
                                    <label for="registrationConfirmPasswordField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserConfirmPasswordField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid changePasswordVisible" src="resources/images/show-password-icon.png" role="button" alt="Show password">
                                            <img class="img-fluid changePasswordVisible visually-hidden" src="resources/images/hide-password-icon.png" role="button" alt="Hide password">
                                        </span>
                                        <input id="registrationConfirmPasswordField" type="password" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserConfirmPasswordField"/> name="confirmPassword" required>
                                    </div>
                                    <label for="userSurnameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserSurnameField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                        </span>
                                        <input id="userSurnameField" type="text" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserSurnameField"/> name="userSurname" required>
                                    </div>
                                    <label for="userNameField" class="form-label d-block text-primary fs-6 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_editUserNameField"/></label>
                                    <div class="input-group mb-3">
                                        <span class="input-group-text">
                                            <img class="img-fluid" src="resources/images/user-icon.png" alt="User icon">
                                        </span>
                                        <input id="userNameField" type="text" class="form-control" placeholder=<fmt:message key="main_jsp.label_for_editUserNameField"/> name="userName" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary w-100" form="registrationForm"><fmt:message key="main_jsp.a_for_registrationWindow"/></button>
                                <a class="text-primary" role="button" data-bs-target="#authenticationWindow" data-bs-toggle="modal"><fmt:message key="main_jsp.button_for_editUserForm_back"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </header>
        <c:if test="${sessionScope.user.getRole() eq 'admin'}">
            <nav class="navbar bg-primary container px-0">
                <div class="container-fluid">
                    <div class="navbar-brand offset-5 text-light fs-4 fw-semibold lh-1"><fmt:message key="main_jsp.h5_offcanvasNavbarLabel"/></div>
                    <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                        <div class="offcanvas-header bg-secondary bg-opacity-25">
                            <h5 class="offcanvas-title offset-3" id="offcanvasNavbarLabel"><fmt:message key="main_jsp.h5_offcanvasNavbarLabel"/></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                        </div>
                        <div class="offcanvas-body bg-secondary bg-opacity-25">
                            <ul class="navbar-nav justify-content-end flex-grow-1">
                                <li class="nav-item">
                                    <a class="nav-link fs-5 fw-semibold" href="#train" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                        <fmt:message key="main_jsp.a_for_train"/>
                                    </a>
                                    <ul id="train" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_addTrain"/>
                                            </a>
                                            <form id="addTrain" class="collapse ps-2" action="controller?command=addTrain" method="post">
                                                <label for="trainNumber" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <input id="trainNumber" class="form-control w-50 mb-2" type="text" name="trainNumber" autocomplete="off" required>
                                                <label for="trainSeats" class="form-label"><fmt:message key="main_jsp.label_for_trainSeats"/></label>
                                                <input id="trainSeats" class="form-control w-50 mb-2" type="number" name="trainSeats" required>
                                                <label for="trainDepartureTime" class="form-label"><fmt:message key="main_jsp.label_for_trainDepartureTime"/></label>
                                                <input id="trainDepartureTime" class="form-control w-50 mb-2" type="time" name="trainDepartureTime" required>
                                                <button class="btn btn-primary"><fmt:message key="main_jsp.a_for_addTrain"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_deleteTrain"/>
                                            </a>
                                            <form id="deleteTrain" class="collapse ps-2" action="controller?command=deleteTrain" method="post">
                                                <label for="trainNumberForDelete" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForDelete" class="form-control w-50 mb-2" list="trainNumberDatalist" type="text" autocomplete="off" required>
                                                <button id="deleteTrainButton" class="btn btn-primary"><fmt:message key="main_jsp.a_for_deleteTrain"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editTrain" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_editTrain"/>
                                            </a>
                                            <form id="editTrain" class="collapse ps-2" action="controller?command=editTrain" method="post">
                                                <label for="trainNumberForEdit" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <label class="d-block">
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForEdit" class="d-inline-block form-control w-50 mb-2" list="trainNumberDatalist" type="text" name="trainNumber" autocomplete="off" required>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <label for="trainSeatsForEdit" class="d-block form-label"><fmt:message key="main_jsp.label_for_trainSeats"/></label>
                                                <input id="trainSeatsForEdit" class="d-inline-block form-control w-50 mb-2" type="number" name="trainSeats" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <label for="trainDepartureTimeForEdit" class="d-block form-label"><fmt:message key="main_jsp.label_for_trainDepartureTime"/></label>
                                                <input id="trainDepartureTimeForEdit" class="d-inline-block form-control w-50 mb-2" type="time" name="trainDepartureTime" required disabled>
                                                <div class="d-inline-block form-check form-switch ms-2">
                                                    <label>
                                                        <input class="form-check-input" role="switch" type="checkbox" hidden>
                                                    </label>
                                                </div>
                                                <button class="btn btn-primary" disabled><fmt:message key="main_jsp.a_for_editTrain"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editTrainRoute" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_editTrainRoute"/>
                                            </a>
                                            <form id="editTrainRoute" class="collapse ps-2" action="controller" method="get">
                                                <label for="trainNumberForEditRoute" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <label>
                                                    <input name="command" value="showRoute" hidden>
                                                </label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForEditRoute" class="form-control w-50 mb-2" list="trainNumberDatalist" type="text" autocomplete="off" required>
                                                <button class="btn btn-primary"><fmt:message key="main_jsp.a_for_editTrainRoute"/></button>
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
                                        <fmt:message key="main_jsp.a_for_station"/>
                                    </a>
                                    <ul id="station" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_addStation"/>
                                            </a>
                                            <form id="addStation" class="collapse ps-2" action="controller?command=addStation" method="post">
                                                <label for="stationName" class="form-label"><fmt:message key="main_jsp.label_for_stationName"/></label>
                                                <input id="stationName" class="form-control w-50 mb-2" type="text" name="stationName" autocomplete="off" required>
                                                <button class="btn btn-primary"><fmt:message key="main_jsp.a_for_addStation"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_deleteStation"/>
                                            </a>
                                            <form id="deleteStation" class="collapse ps-2" action="controller?command=deleteStation" method="post">
                                                <label for="stationNameForDelete" class="form-label"><fmt:message key="main_jsp.label_for_stationName"/></label>
                                                <label>
                                                    <input type="number" name="stationId" hidden>
                                                </label>
                                                <input id="stationNameForDelete" class="form-control w-50 mb-2" type="text" list="stationsDatalistOptions" autocomplete="off" required>
                                                <button class="btn btn-primary"><fmt:message key="main_jsp.a_for_deleteStation"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#editStation" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_editStation"/>
                                            </a>
                                            <form id="editStation" class="collapse ps-2" action="controller?command=editStation" method="post">
                                                <label for="oldStationNameForEdit" class="form-label"><fmt:message key="main_jsp.label_for_stationName"/></label>
                                                <label>
                                                    <input type="number" name="stationId" hidden>
                                                </label>
                                                <input id="oldStationNameForEdit" class="form-control w-50 mb-2" type="text" list="stationsDatalistOptions" autocomplete="off" required>
                                                <label for="newStationNameForEdit" class="form-label"><fmt:message key="main_jsp.label_for_newStationNameForEdit"/></label>
                                                <input id="newStationNameForEdit" class="form-control w-50 mb-2" type="text" name="stationName" autocomplete="off" required disabled>
                                                <button class="btn btn-primary" disabled><fmt:message key="main_jsp.a_for_editStation"/></button>
                                            </form>
                                        </li>
                                    </ul>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link fs-5 fw-semibold" href="#schedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                        <fmt:message key="main_jsp.a_for_schedule"/>
                                    </a>
                                    <ul id="schedule" class="dropdown-menu bg-secondary bg-opacity-50 ps-2">
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#addTrainToSchedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_addTrainToSchedule"/>
                                            </a>
                                            <form id="addTrainToSchedule" class="collapse ps-2" action="controller?command=addTrainToSchedule" method="post">
                                                <label for="trainNumberForAddToSchedule" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForAddToSchedule" class="form-control w-50 mb-2" type="text" list="trainNumberDatalist" autocomplete="off" required>
                                                <button class="btn btn-primary" disabled><fmt:message key="main_jsp.button_for_addTrainToSchedule"/></button>
                                            </form>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link fw-semibold" href="#deleteTrainFromSchedule" role="button" data-bs-toggle="collapse" aria-expanded="false">
                                                <fmt:message key="main_jsp.a_for_deleteTrainFromSchedule"/>
                                            </a>
                                            <form id="deleteTrainFromSchedule" class="collapse ps-2" action="controller?command=deleteTrainFromSchedule" method="post">
                                                <label for="trainNumberForDeleteFromSchedule" class="form-label"><fmt:message key="main_jsp.label_for_trainNumber"/></label>
                                                <label>
                                                    <input type="number" name="trainId" hidden>
                                                </label>
                                                <input id="trainNumberForDeleteFromSchedule" class="form-control w-50 mb-2" type="text" list="trainNumberDatalist" autocomplete="off" required>
                                                <button class="btn btn-primary" disabled><fmt:message key="main_jsp.button_for_deleteTrainFromSchedule"/></button>
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
                        <label for="fromDatalist" class="form-label text-primary fs-5 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_fromDatalist"/></label>
                        <label>
                            <input type="number" name="from" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="stationsDatalistOptions" id="fromDatalist">
                    </div>
                    <img class="img-fluid w-auto position-absolute top-50 start-50 translate-middle-x reverseRoute" role="button" src="resources/images/two%20arrow.png" alt="">
                    <div class="col-6 ps-4">
                        <label for="toDatalist" class="form-label text-primary fs-5 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_toDatalist"/></label>
                        <label>
                            <input type="number" name="to" hidden>
                        </label>
                        <input class="form-control fs-5 fw-semibold lh-1" list="stationsDatalistOptions" id="toDatalist">
                    </div>
                </div>
                <div class="row py-3">
                    <div class="col-2">
                        <label for="datePicker" class="form-label text-primary fs-5 fw-semibold lh-1"><fmt:message key="main_jsp.label_for_datePicker"/></label>
                        <input class="form-control fs-5 fw-semibold lh-1 text-center" type="text" name="datePicker" id="datePicker">
                    </div>
                </div>
                <div class="row pb-4 justify-content-center">
                    <div class="col-4">
                        <button class="btn btn-lg btn-primary fs-5 fw-semibold lh-1" datatype="searchTrains"><fmt:message key="main_jsp.button_for_searchTrains"/></button>
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
                            <th><fmt:message key="main_jsp.first_th"/></th>
                            <th><fmt:message key="main_jsp.label_for_fromDatalist"/>/<fmt:message key="main_jsp.label_for_toDatalist"/></th>
                            <th><fmt:message key="main_jsp.third_th"/></th>
                            <th class="text-start">
                                <div><fmt:message key="main_jsp.fourth_th.first_div"/>
                                    <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDepartureTime'}">
                                        <a href="controller?command=getTrainsSortedByDepartureTime&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                            <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                        </a>
                                    </c:if>
                                </div>
                                <div><fmt:message key="main_jsp.fourth_th.second_div"/>
                                    <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDestinationTime'}">
                                        <a href="controller?command=getTrainsSortedByDestinationTime&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                            <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                        </a>
                                    </c:if>
                                </div>
                            </th>
                            <th><fmt:message key="main_jsp.fifth_th"/>
                                <c:if test="${requestScope.trainsSortedCommand ne 'getTrainsSortedByDurationTrip'}">
                                    <a href="controller?command=getTrainsSortedByDurationTrip&page=${requestScope.page}&from=${requestScope.fromStationId}&to=${requestScope.toStationId}&datePicker=${requestScope.departureDateForSortingAndPagination}">
                                        <img src="resources/images/sort-icon.png" class="img-fluid align-top" alt="Сортувати">
                                    </a>
                                </c:if>
                            </th>
                            <th><fmt:message key="main_jsp.sixth_th"/></th>
                            <th><fmt:message key="main_jsp.seventh_th"/>
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
                                <a href="controller?command=showRoute&trainId=${train.getId()}&fromStationId=${requestScope.fromStationId}&toStationId=${requestScope.toStationId}" class="fs-6 fw-normal lh-1"><fmt:message key="main_jsp.first_td"/></a>
                            </td>
                            <td class="text-start">
                                <div class="lh-sm">${train.getRoute().getDepartureStationName()}</div>
                                <div>${train.getRoute().getDestinationStationName()}</div>
                            </td>
                            <td class="text-start fs-6 fw-normal">
                                <div class="d-flex justify-content-between mb-1 lh-sm"><span><fmt:message key="main_jsp.fourth_th.first_div"/></span> <span>${train.getRoute().getDepartureDateOfWeekAndDateAsString(requestScope.departureDate)}</span></div>
                                <div class="d-flex justify-content-between"><span><fmt:message key="main_jsp.fourth_th.second_div"/></span> <span>${train.getRoute().getDestinationDateOfWeekAndDateAsString(requestScope.fromStationId, requestScope.toStationId, requestScope.departureDate)}</span></div>
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
                                                <button class="btn btn-primary fs-6 lh-1 mt-2"><fmt:message key="main_jsp.button_for_orderTicket"/></button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="btn btn-primary fs-6 lh-1 mt-2 disabled"><fmt:message key="main_jsp.button_for_orderTicket"/></a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${requestScope.numberOfPages gt 1}">
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
                    <div class="col-4 text-success fw-semibold"><fmt:message key="main_jsp.div_info"/></div>
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
            <c:if test="${sessionScope.user.getRole() eq 'admin'}">
                <div class="modal fade" id="confirmDeleteTrainModal" tabindex="-1" aria-labelledby="confirmDeleteTrainModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteTrainModalLabel"><fmt:message key="main_jsp.h5_confirmDeleteTrainModalLabel"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2"><fmt:message key="main_jsp.div_for_confirmDeleteTrainModal"/></div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteTrain"><fmt:message key="main_jsp.button_for_deleteTrainFromSchedule"/></button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="main_jsp.button_for_confirmDeleteTrainModal_cancel"/></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="confirmDeleteStationModal" tabindex="-1" aria-labelledby="confirmDeleteStationModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteStationModalLabel"><fmt:message key="main_jsp.h5_confirmDeleteStationModalLabel"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2"><fmt:message key="main_jsp.div_for_confirmDeleteStationModal"/></div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteStation"><fmt:message key="main_jsp.button_for_deleteTrainFromSchedule"/></button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="main_jsp.button_for_confirmDeleteTrainModal_cancel"/></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="confirmDeleteTrainFromScheduleModal" tabindex="-1" aria-labelledby="confirmDeleteTrainFromScheduleModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-warning" id="confirmDeleteTrainFromScheduleModalLabel"><fmt:message key="main_jsp.h5_confirmDeleteTrainFromScheduleModalLabel"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-danger fs-5 fw-semibold lh-2"><fmt:message key="main_jsp.div_for_confirmDeleteTrainFromScheduleModal"/></div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" form="deleteTrainFromSchedule"><fmt:message key="main_jsp.button_for_deleteTrainFromSchedule"/></button>
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="main_jsp.button_for_confirmDeleteTrainModal_cancel"/></button>
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