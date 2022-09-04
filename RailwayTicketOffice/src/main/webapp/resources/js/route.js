let tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
[...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
let deleteStationFromTrainRouteButtons = document.querySelectorAll('button[name="deleteStationFromTrainRoute"]');
let confirmDeleteStationFromTrainRouteModal = document.querySelector('#confirmDeleteStationFromTrainRouteModal');
confirmDeleteStationFromTrainRouteModal = new bootstrap.Modal(confirmDeleteStationFromTrainRouteModal, {
    keyboard: false,
});
let stationNameInput = document.querySelector('#stationName');
let addStationToTrainRouteButton = document.querySelector('#addStationToTrainRoute>button');
let modalBody = document.querySelector('.errorModalBody');
let errorModal;
errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
    keyboard: false,
});
let editIconButtons = document.querySelectorAll('button[name="editStationDataOnTrainRoute"]');
let editStationDataOnTrainRouteForms = document.querySelectorAll('#editStationDataOnTrainRoute');
let collapseList;
collapseList = [...editStationDataOnTrainRouteForms].map(collapseEl => new bootstrap.Collapse(collapseEl, {
    toggle: false,
}));
let editStationDataOnTrainRouteCheckboxes = document.querySelectorAll('#editStationDataOnTrainRoute input[class="form-check-input"]');
let editStationDataOnTrainRouteButtons = document.querySelectorAll('#editStationDataOnTrainRoute button');
let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");

for (let i = 0; i < deleteStationFromTrainRouteButtons.length; i++) {
    deleteStationFromTrainRouteButtons[i].addEventListener("click", ev => {
        ev.preventDefault();
        ev.currentTarget.parentElement.setAttribute("id", "deleteStationFromTrainRoute");
        document.querySelector('#confirmDeleteStationFromTrainRouteModal button[type="submit"]').setAttribute("form", "deleteStationFromTrainRoute");
        confirmDeleteStationFromTrainRouteModal.show();
    });
}

stationNameInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let stationIdInput = input.previousElementSibling.firstElementChild;
    if (input.value === "") {
        stationIdInput.removeAttribute("value");
    } else {
        let options = document.querySelector('option[value="' + input.value + '"]');
        if (options === undefined) {
            stationIdInput.removeAttribute("value");
        } else {
            stationIdInput.setAttribute("value", options.getAttribute('id'));
        }
    }
});

addStationToTrainRouteButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    if (!form.querySelector('input[name="stationId"]').hasAttribute("value")) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter an existing station";
        } else {
            modalBody.innerHTML = "Введіть існуючу станцію";
        }
        errorModal.show();
        return;
    }
    let timeSinceStartValue = form.querySelector('#timeSinceStart').value;
    if (timeSinceStartValue === "" && timeSinceStartValue.split(":").length !== 2) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter the time since the departure of the train from the first station of the route";
        } else {
            modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
        }
        errorModal.show();
        return;
    }
    let stopTimeValue = form.querySelector('#stopTime').value;
    if (stopTimeValue === "" && stopTimeValue.split(":").length !== 2) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter the time the train stops at the station";
        } else {
            modalBody.innerHTML = "Введіть час зупинки поїзда на станції";
        }
        errorModal.show();
        return;
    }
    if (form.querySelector('#distanceFromStart').value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter the distance to the first station of the route";
        } else {
            modalBody.innerHTML = "Введіть відстань до першої станції маршруту";
        }
        errorModal.show();
    }
});

for (let i = 0; i < editIconButtons.length; i++) {
    editIconButtons[i].addEventListener("click", () => {
        collapseList[i].toggle();
    });
}

for (let i = 0; i < editStationDataOnTrainRouteCheckboxes.length; i++) {
    editStationDataOnTrainRouteCheckboxes[i].addEventListener("click", evt => {
        let timeSinceStartInput = false;
        let checkbox = evt.currentTarget;
        let input = checkbox.parentElement.parentElement.previousElementSibling;
        if (input.tagName !== "INPUT") {
            input = input.firstElementChild;
            timeSinceStartInput = true;
        }
        let form = checkbox.parentElement.parentElement.parentElement;
        let button = form.querySelector('button[class="btn btn-primary"]');
        if (checkbox.hasAttribute("checked")) {
            let checkboxes = form.querySelectorAll('input[class="form-check-input"]');
            checkbox.removeAttribute("checked");
            input.value = checkbox.parentElement.nextElementSibling.firstElementChild.value;
            input.setAttribute("disabled", "");
            for (let j = 0; j < checkboxes.length; j++) {
                if (checkboxes[j].hasAttribute("checked")) {
                    break;
                }
                if (j === checkboxes.length - 1) {
                    button.setAttribute("disabled", "");
                }
            }
            if (timeSinceStartInput) {
                input.nextElementSibling.setAttribute("hidden", "");
            }
        } else {
            checkbox.setAttribute("checked", "");
            input.removeAttribute("disabled");
            button.removeAttribute("disabled");
            if (timeSinceStartInput) {
                input.nextElementSibling.removeAttribute("hidden");
            }
        }
    });
}

for (let i = 0; i < editStationDataOnTrainRouteButtons.length; i++) {
    editStationDataOnTrainRouteButtons[i].addEventListener("click", evt => {
        let form = evt.currentTarget.parentElement;
        let timeSinceStartValue = form.querySelector('#timeSinceStartForEdit').value;
        if (timeSinceStartValue === "" && timeSinceStartValue.split(":").length !== 2) {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Enter the time since the departure of the train from the first station of the route";
            } else {
                modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
            }
            errorModal.show();
            return;
        }
        let stopTimeValue = form.querySelector('#stopTimeForEdit').value;
        if (stopTimeValue === "" && stopTimeValue.split(":").length !== 2) {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Enter the time the train stops at the station";
            } else {
                modalBody.innerHTML = "Введіть час зупинки поїзда на станції";
            }
            errorModal.show();
            return;
        }
        if (form.querySelector('#distanceFromStartForEdit').value === "") {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Enter the distance to the first station of the route";
            } else {
                modalBody.innerHTML = "Введіть відстань до першої станції маршруту";
            }
            errorModal.show();
            return;
        }
        let inputs = form.querySelectorAll('input[disabled]');
        for (let j = 0; j < inputs.length; j++) {
            inputs[j].removeAttribute("disabled");
        }
    });
}

window.addEventListener("load", () => {
    let increaseDurationButtons = document.querySelectorAll('button[aria-label="Increase duration"]');
    let decreaseDurationButtons = document.querySelectorAll('button[aria-label="Decrease duration"]');
    for (let i = 0; i < increaseDurationButtons.length; i++) {
        let button = increaseDurationButtons[i];
        button.style.top = "3px";
        button.style.padding = "0 3px 0 0";
        button.firstElementChild.style.borderWidth = "0 7px 12px 7px";
        button = decreaseDurationButtons[i];
        button.style.top = "20px";
        button.style.padding = "0 3px 0 0";
        button.firstElementChild.style.borderWidth = "12px 7px 0px 7px";
        if (button.parentElement.parentElement.parentElement.id !== "addStationToTrainRoute") {
            button.parentElement.setAttribute("hidden", "");
        } else {
            button.parentElement.style.left = null;
            button.parentElement.style.right = "20px";
            button.parentElement.parentElement.style.width = "auto";
        }
    }
});
