let editUserForm = document.querySelector('#editUserForm');
let editUserFormButton = document.querySelector('button[form="editUserForm"]');
let editUserCheckboxes = editUserForm.querySelectorAll('.form-check-input');

editUserFormButton.addEventListener("click", (ev) => {
    let password = document.querySelector("#editUserPasswordField").value;
    let confirmPassword = document.querySelector("#editUserConfirmPasswordField").value;
    if (password !== confirmPassword) {
        ev.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Entered passwords do not match";
        } else {
            modalBody.innerHTML = "Введені паролі не співпадають";
        }
        errorModal.show();
    }
    for (let i = 0; i < editUserCheckboxes.length; i++) {
        if (!editUserCheckboxes[i].checked) {
            let container = editUserCheckboxes[i].parentElement.parentElement.previousElementSibling;
            let inputs = container.querySelectorAll("input");
            if (inputs[1].getAttribute("type") === "password") {
                for (let j = 0; j < inputs.length; j++) {
                    inputs[j].removeAttribute("disabled");
                    inputs[j].removeAttribute("required");
                }
            } else {
                inputs[1].removeAttribute("disabled");
            }
        }
    }
});

for (let i = 0; i < editUserCheckboxes.length; i++) {
    editUserCheckboxes[i].addEventListener("click", evt => {
        let checkbox = evt.currentTarget;
        let container = checkbox.parentElement.parentElement.previousElementSibling;
        let inputs = container.querySelectorAll("input");
        let button = editUserForm.parentElement.nextElementSibling.firstElementChild;
        if (checkbox.hasAttribute("checked")) {
            checkbox.removeAttribute("checked");
            if (inputs[1].getAttribute("type") === "password") {
                for (let j = 0; j < inputs.length; j++) {
                    inputs[j].value = "";
                    inputs[j].setAttribute("disabled", "");
                }
            } else {
                inputs[1].value = inputs[0].value;
                inputs[1].setAttribute("disabled", "");
            }
            for (let j = 0; j < editUserCheckboxes.length; j++) {
                if (editUserCheckboxes[j].hasAttribute("checked")) {
                    break;
                }
                if (j === editUserCheckboxes.length - 1) {
                    button.setAttribute("disabled", "");
                }
            }
        } else {
            checkbox.setAttribute("checked", "");
            if (inputs[1].getAttribute("type") === "password") {
                for (let j = 0; j < inputs.length; j++) {
                    inputs[j].removeAttribute("disabled");
                }
            } else {
                inputs[1].removeAttribute("disabled");
            }
            button.removeAttribute("disabled");
        }
    });
}