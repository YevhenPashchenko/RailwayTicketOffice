<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.locale ne null}">
    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="resources"/>
</c:if>
<html>
    <head>
        <title><fmt:message key="success_jsp.title"/></title>
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
                                                <a class="dropdown-item p-0" href="controller?command=changeSuccessPageLocale&locale=${locale.key}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
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
                                                <a class="dropdown-item p-0" href="controller?command=changeSuccessPageLocale&locale=${locale.key}"><img src="resources/images/${locale.key}-icon.png" alt="${locale.key}"></a>
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
                    <a class="navbar-brand offset-1 text-primary fs-4 fw-bold" href="controller?command=mainPage"><fmt:message key="route_jsp.nav.a"/></a>
                </div>
            </nav>
            <div class="text-center text-success fs-2 fw-bold ps-4"><fmt:message key="success_jsp.main.div"/></div>
        </main>
    </body>
</html>
