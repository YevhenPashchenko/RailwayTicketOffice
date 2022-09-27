mdtimepicker(document.querySelectorAll('.time'), {
    format: 'hh:mm',
    is24hour: true,
});
let tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
[...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

let editIconButtons = document.querySelectorAll('button[name="editStationDataOnTrainRoute"]');
let editStationDataOnTrainRouteForms = [...editIconButtons].map(button => button.nextElementSibling);
let collapseList = [...editStationDataOnTrainRouteForms].map(collapseEl => new bootstrap.Collapse(collapseEl, {
    toggle: false,
}));

let deleteStationFromTrainRouteButtons = document.querySelectorAll('button[name="deleteStationFromTrainRoute"]');
let confirmDeleteStationFromTrainRouteModal = document.querySelector('#confirmDeleteStationFromTrainRouteModal');
confirmDeleteStationFromTrainRouteModal = new bootstrap.Modal(confirmDeleteStationFromTrainRouteModal, {
    keyboard: false,
});

let stationNameInput = document.querySelector('#stationName');
let timesSinceStart = editStationDataOnTrainRouteForms.map(form => form.querySelector('label[for="stopTimeForEdit"]').previousElementSibling.lastElementChild.firstElementChild.value);
let distancesFromStart = editStationDataOnTrainRouteForms.map(form => form.querySelector('input[name="distanceFromStart"]').nextElementSibling.lastElementChild.firstElementChild.value);
let timeSinceStartInput = document.querySelector('#timeSinceStart');
let distanceFromStartInput = document.querySelector('#distanceFromStart');
let addStationToTrainRouteButton = document.querySelector('#addStationToTrainRoute>button');

let modalBody = document.querySelector('.errorModalBody');
let errorModal;
errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
    keyboard: false,
});

let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");

for (let i = 0; i < editIconButtons.length; i++) {
    editIconButtons[i].addEventListener("click", evt => {
        let editIconButton = evt.currentTarget;
        if (editIconButton.classList.contains("show")) {
            let checkboxes = editIconButton.nextElementSibling.querySelectorAll('input[role="switch"]');
            for (let j = 0; j < checkboxes.length; j++) {
                if (checkboxes[j].hasAttribute('checked')) {
                    checkboxes[j].click();
                }
            }
        }
        collapseList[i].toggle();
    });
}

for (let i = 0; i < editStationDataOnTrainRouteForms.length; i++) {
    let form = editStationDataOnTrainRouteForms[i];
    let checkboxes = form.querySelectorAll('input[role="switch"]');
    let timeSinceStart = form.querySelector('input[name="timeSinceStart"]');
    let stopTime = form.querySelector('input[name="stopTime"]');
    let distanceFromStart = form.querySelector('input[name="distanceFromStart"]');
    let button = form.querySelector('button');
    for (let j = 0; j < checkboxes.length; j++) {
        let checkboxesNumber = 0;
        checkboxes[j].addEventListener("click", evt => {
            let checkbox = evt.currentTarget;
            let input;
            if (j === 0) {
                input = checkbox.parentElement.parentElement.previousElementSibling.firstElementChild;
            } else {
                input = checkbox.parentElement.parentElement.previousElementSibling;
            }
            if (checkbox.hasAttribute("checked")) {
                let hiddenInput = checkbox.parentElement.nextElementSibling.firstElementChild;
                input.value = hiddenInput.value;
                if (j === 0) {
                    input.nextElementSibling.setAttribute("hidden", "");
                }
                input.setAttribute("disabled", "");
                checkbox.removeAttribute("checked");
                checkboxesNumber--;
            } else {
                if (j === 0) {
                    input.nextElementSibling.removeAttribute("hidden");
                }
                input.removeAttribute("disabled");
                checkbox.setAttribute("checked", "");
                checkboxesNumber++;
            }
            if (checkboxesNumber === 0) {
                button.setAttribute("disabled", "");
            } else {
                button.removeAttribute("disabled");
            }
        });
    }
    if (editStationDataOnTrainRouteForms.length > 1) {
        let forms;
        if (i === 0) {
            forms = [editStationDataOnTrainRouteForms[i + 1]];
        } else if (i === editStationDataOnTrainRouteForms.length - 1) {
            forms = [editStationDataOnTrainRouteForms[i - 1]];
        } else {
            forms = [editStationDataOnTrainRouteForms[i - 1], editStationDataOnTrainRouteForms[i + 1]];
        }
        let timesSinceStart = [];
        let distancesFromStart = [];
        for (let j = 0; j < forms.length; j++) {
            let divs = forms[j].querySelectorAll('.form-switch');
            timesSinceStart.push(divs[0].querySelector('input[hidden]').value);
            distancesFromStart.push(divs[2].querySelector('input[hidden]').value);
        }
        timeSinceStart.addEventListener("change", evt => {
            let input = evt.currentTarget;
            let hiddenInput = input.parentElement.nextElementSibling.lastElementChild.firstElementChild;
            if (i === 0) {
                if (input.value > timesSinceStart[0]) {
                    input.value = hiddenInput.value;
                }
            } else if (i === editStationDataOnTrainRouteForms.length - 1) {
                if (input.value < timesSinceStart[0]) {
                    input.value = hiddenInput.value;
                }
            } else {
                if (input.value < timesSinceStart[0] || input.value > timesSinceStart[1]) {
                    input.value = hiddenInput.value;
                }
            }
        });
        distanceFromStart.addEventListener("change", evt => {
            let input = evt.currentTarget;
            let hiddenInput = input.nextElementSibling.lastElementChild.firstElementChild;
            if (i === 0) {
                if (+input.value > distancesFromStart[0]) {
                    input.value = hiddenInput.value;
                }
            } else if (i === editStationDataOnTrainRouteForms.length - 1) {
                if (+input.value < distancesFromStart[0]) {
                    input.value = hiddenInput.value;
                }
            } else {
                if (+input.value < distancesFromStart[0] || +input.value > distancesFromStart[1]) {
                    input.value = hiddenInput.value;
                }
            }
        });
    }
    button.addEventListener("click", evt => {
        if (timeSinceStart.value === "" && timeSinceStart.value.split(":").length !== 2) {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Enter the time since the departure of the train from the first station of the route";
            } else {
                modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
            }
            errorModal.show();
            return;
        }
        if (stopTime.value === "" && stopTime.value.split(":").length !== 2) {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Enter the time the train stops at the station";
            } else {
                modalBody.innerHTML = "Введіть час зупинки поїзда на станції";
            }
            errorModal.show();
            return;
        }
        if (distanceFromStart.value === "") {
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
        let input = button.parentElement.previousElementSibling;
        button.style.top = "3px";
        button.style.padding = "0 3px 0 0";
        button.firstElementChild.style.borderWidth = "0 7px 12px 7px";
        button.addEventListener("click", () => {
            input.dispatchEvent(new Event("change"));
        });
        button = decreaseDurationButtons[i];
        button.style.top = "20px";
        button.style.padding = "0 3px 0 0";
        button.firstElementChild.style.borderWidth = "12px 7px 0px 7px";
        button.addEventListener("click", () => {
            input.dispatchEvent(new Event("change"));
        });
        if (button.parentElement.parentElement.parentElement.id !== "addStationToTrainRoute") {
            button.parentElement.setAttribute("hidden", "");
        } else {
            button.parentElement.style.left = null;
            button.parentElement.style.right = "20px";
            button.parentElement.parentElement.style.width = "auto";
        }
    }
});

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

timeSinceStartInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let index = 0;
    while (timesSinceStart[index] < input.value) {
        index++;
    }
    if (index !== 0) {
        index--;
    }
    distanceFromStartInput.value = distancesFromStart[index];
});

distanceFromStartInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let index = 0;
    while (timesSinceStart[index] < timeSinceStartInput.value) {
        index++;
    }
    if (index !== 0) {
        index--;
    }
    if (+distancesFromStart[index] > +input.value) {
        input.value = distancesFromStart[index];
    }
    if (index + 1 < distancesFromStart.length - 1) {
        if (+distancesFromStart[index + 1] < +input.value) {
            input.value = distancesFromStart[index + 1];
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
    if (timeSinceStartInput.value === "" && timeSinceStartInput.value.split(":").length !== 2) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter the time since the departure of the train from the first station of the route";
        } else {
            modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
        }
        errorModal.show();
        return;
    }
    if (timesSinceStart.includes(timeSinceStartInput.value)) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Such time since the departure of the train from the first station of the route already exists";
        } else {
            modalBody.innerHTML = "Такий час з моменту відправлення поїзда з першої станції маршруту вже існує";
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
    if (distanceFromStartInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter the distance to the first station of the route";
        } else {
            modalBody.innerHTML = "Введіть відстань до першої станції маршруту";
        }
        errorModal.show();
        return;
    }
    if (distancesFromStart.includes(distanceFromStartInput.value)) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Such distance to the first station of the route already exists";
        } else {
            modalBody.innerHTML = "Така відстань до першої станції маршруту вже існує";
        }
        errorModal.show();
    }
});
