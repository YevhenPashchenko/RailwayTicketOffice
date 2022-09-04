let registrationFormButton = document.querySelector('button[form="registrationForm"]');

registrationFormButton.addEventListener("click", (ev) => {
    let password = document.querySelector("#registrationPasswordField").value;
    let confirmPassword = document.querySelector("#registrationConfirmPasswordField").value;
    if (password !== confirmPassword) {
        ev.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Entered passwords do not match";
        } else {
            modalBody.innerHTML = "Введені паролі не співпадають";
        }
        errorModal.show();
    }
});