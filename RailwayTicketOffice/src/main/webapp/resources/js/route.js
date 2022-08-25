let tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
[...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
let deleteStationFromTrainRouteButtons = document.querySelectorAll('button[data-bs-title="Видалити станцію з маршруту"]');
let confirmDeleteStationFromTrainRouteModal = document.querySelector('#confirmDeleteStationFromTrainRouteModal');
if (confirmDeleteStationFromTrainRouteModal != null) {
    confirmDeleteStationFromTrainRouteModal = new bootstrap.Modal(confirmDeleteStationFromTrainRouteModal, {
        keyboard: false,
    });
}
let stationNameInput = document.querySelector('#stationName');
let addStationToTrainRouteButton = document.querySelector('#addStationToTrainRoute>button');
let modalBody = document.querySelector('.errorModalBody');
let errorModal;
if (modalBody != null) {
    errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
        keyboard: false,
    });
}
let editIconButtons = document.querySelectorAll('button[data-bs-title="Редагувати дані станції на маршруті"]');
let editStationDataOnTrainRouteForms = document.querySelectorAll('#editStationDataOnTrainRoute');
let collapseList;
if (editStationDataOnTrainRouteForms != null) {
    collapseList = [...editStationDataOnTrainRouteForms].map(collapseEl => new bootstrap.Collapse(collapseEl, {
        toggle: false,
    }));
}
let editStationDataOnTrainRouteCheckboxes = document.querySelectorAll('#editStationDataOnTrainRoute input[class="form-check-input"]');
let editStationDataOnTrainRouteButtons = document.querySelectorAll('#editStationDataOnTrainRoute button');

if (deleteStationFromTrainRouteButtons != null) {
    for (let i = 0; i < deleteStationFromTrainRouteButtons.length; i++) {
        deleteStationFromTrainRouteButtons[i].addEventListener("click", ev => {
            ev.preventDefault();
            ev.currentTarget.parentElement.setAttribute("id", "deleteStationFromTrainRoute");
            document.querySelector('#confirmDeleteStationFromTrainRouteModal button[type="submit"]').setAttribute("form", "deleteStationFromTrainRoute");
            confirmDeleteStationFromTrainRouteModal.show();
        });
    }
}

if (stationNameInput != null) {
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
}

if (addStationToTrainRouteButton != null) {
    addStationToTrainRouteButton.addEventListener("click", evt => {
        let form = evt.currentTarget.parentElement;
        if (!form.querySelector('input[name="stationId"]').hasAttribute("value")) {
            evt.preventDefault();
            modalBody.innerHTML = "Введіть існуючу станцію";
            errorModal.show();
            return;
        }
        let timeSinceStartValue = form.querySelector('#timeSinceStart').value;
        if (timeSinceStartValue === "" && timeSinceStartValue.split(":").length !== 2) {
            evt.preventDefault();
            modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
            errorModal.show();
            return;
        }
        let stopTimeValue = form.querySelector('#stopTime').value;
        if (stopTimeValue === "" && stopTimeValue.split(":").length !== 2) {
            evt.preventDefault();
            modalBody.innerHTML = "Введіть час зупинки поїзда на станції";
            errorModal.show();
            return;
        }
        if (form.querySelector('#distanceFromStart').value === "") {
            evt.preventDefault();
            modalBody.innerHTML = "Введіть відстань до першої станції маршруту";
            errorModal.show();
        }
    });
}

if (editIconButtons != null) {
    for (let i = 0; i < editIconButtons.length; i++) {
        editIconButtons[i].addEventListener("click", () => {
            collapseList[i].toggle();
        });
    }
}

if (editStationDataOnTrainRouteCheckboxes != null) {
    for (let i = 0; i < editStationDataOnTrainRouteCheckboxes.length; i++) {
        editStationDataOnTrainRouteCheckboxes[i].addEventListener("click", evt => {
            let checkbox = evt.currentTarget;
            let input = checkbox.parentElement.parentElement.previousElementSibling;
            let form = checkbox.parentElement.parentElement.parentElement;
            let button = form.querySelector('button');
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
            } else {
                checkbox.setAttribute("checked", "");
                input.removeAttribute("disabled");
                button.removeAttribute("disabled");
            }
        });
    }
}

if (editStationDataOnTrainRouteButtons != null) {
    for (let i = 0; i < editStationDataOnTrainRouteButtons.length; i++) {
        editStationDataOnTrainRouteButtons[i].addEventListener("click", evt => {
            let form = evt.currentTarget.parentElement;
            let timeSinceStartValue = form.querySelector('#timeSinceStartForEdit').value;
            if (timeSinceStartValue === "" && timeSinceStartValue.split(":").length !== 2) {
                evt.preventDefault();
                modalBody.innerHTML = "Введіть час з моменту відправлення поїзда з першої станції маршруту";
                errorModal.show();
                return;
            }
            let stopTimeValue = form.querySelector('#stopTimeForEdit').value;
            if (stopTimeValue === "" && stopTimeValue.split(":").length !== 2) {
                evt.preventDefault();
                modalBody.innerHTML = "Введіть час зупинки поїзда на станції";
                errorModal.show();
                return;
            }
            if (form.querySelector('#distanceFromStartForEdit').value === "") {
                evt.preventDefault();
                modalBody.innerHTML = "Введіть відстань до першої станції маршруту";
                errorModal.show();
                return;
            }
            let inputs = form.querySelectorAll('input[disabled]');
            for (let j = 0; j < inputs.length; j++) {
                inputs[j].removeAttribute("disabled");
            }
        });
    }
}