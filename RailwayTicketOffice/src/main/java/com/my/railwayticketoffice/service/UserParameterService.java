package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that verify user parameters
 *
 * @author Yevhen Pashchenko
 */
public class UserParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        if (parameters.containsKey("email")) {
            String email = parameters.get("email");
            if (email == null || "".equals(email)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Email address is incorrect");
                } else {
                    session.setAttribute("userErrorMessage", "Адреса пошти некоректна");
                }
                return false;
            }
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Email address is incorrect");
                } else {
                    session.setAttribute("userErrorMessage", "Адреса пошти некоректна");
                }
                return false;
            }
        }
        if (parameters.containsKey("password")) {
            String password = parameters.get("password");
            if (password == null || "".equals(password)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Password is not specified");
                } else {
                    session.setAttribute("userErrorMessage", "Пароль не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("password") && parameters.containsKey("confirmPassword")) {
            String confirmPassword = parameters.get("confirmPassword");
            if (confirmPassword == null || "".equals(confirmPassword)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Confirm password is not specified");
                } else {
                    session.setAttribute("userErrorMessage", "Підтвердження пароля не задано");
                }
                return false;
            }
            if (!parameters.get("password").equals(parameters.get("confirmPassword"))) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Entered password do not match");
                } else {
                    session.setAttribute("userErrorMessage", "Введені паролі не співпадають");
                }
                return false;
            }
        }
        if (parameters.containsKey("surname")) {
            String surname = parameters.get("surname");
            if (surname == null || "".equals(surname)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Surname is not specified");
                } else {
                    session.setAttribute("userErrorMessage", "Прізвище не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("name")) {
            String name = parameters.get("name");
            if (name == null || "".equals(name)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("userErrorMessage", "Name is not specified");
                } else {
                    session.setAttribute("userErrorMessage", "Ім'я не задано");
                }
                return false;
            }
        }
        return true;
    }
}
