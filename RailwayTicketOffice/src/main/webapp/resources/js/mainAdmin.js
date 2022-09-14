let trainNumberForDeleteInput = document.querySelector('#trainNumberForDelete');
let deleteTrainButton = document.querySelector('#deleteTrainButton');
let confirmDeleteTrainModal = document.querySelector('#confirmDeleteTrainModal');
confirmDeleteTrainModal = new bootstrap.Modal(confirmDeleteTrainModal, {
    keyboard: false,
});

let oldTrainNumberForEditInput = document.querySelector('#oldTrainNumberForEdit');
let editTrainForm = document.querySelector('#editTrain');
let editTrainCheckboxes = editTrainForm.querySelectorAll('input[type="checkbox"]');
let editTrainButton = editTrainForm.querySelector('button');

let trainNumberForEditRouteInput = document.querySelector('#trainNumberForEditRoute');

let trainNumberForAddCarriageToTrainInput = document.querySelector('#trainNumberForAddCarriageToTrain');
let carriageTypeForAddCarriageToTrainInput = document.querySelector('#carriageTypeForAddCarriageToTrain');
let addCarriageToTrainButton = document.querySelector('#addCarriageToTrain button');

let trainNumberForDeleteCarriageFromTrainInput = document.querySelector('#trainNumberForDeleteCarriageFromTrain');
let carriageNumberForDeleteCarriageFromTrainInput = document.querySelector('#carriageNumberForDeleteCarriageFromTrain');
let carriageTypeForDeleteCarriageFromTrainInput = document.querySelector('#carriageTypeForDeleteCarriageFromTrain');
let deleteCarriageFromTrainButton= document.querySelector('#deleteCarriageFromTrain button');
let confirmDeleteCarriageFromTrainModal = document.querySelector('#confirmDeleteCarriageFromTrainModal');
confirmDeleteCarriageFromTrainModal = new bootstrap.Modal(confirmDeleteCarriageFromTrainModal, {
    keyboard: false,
});

let addCarriageTypeButton = document.querySelector('#addCarriageType button');

let carriageTypeForDeleteInput = document.querySelector('#carriageTypeForDelete');
let deleteCarriageTypeButton = document.querySelector('#deleteCarriageType button');
let confirmDeleteCarriageTypeModal = document.querySelector('#confirmDeleteCarriageTypeModal');
confirmDeleteCarriageTypeModal = new bootstrap.Modal(confirmDeleteCarriageTypeModal, {
    keyboard: false,
});

let carriageTypeForEditInput = document.querySelector('#carriageTypeForEdit');
let editCarriageTypeButton = document.querySelector('#editCarriageType button');

let addStationButton = document.querySelector('#addStation button');

let stationNameForDeleteInput = document.querySelector('#stationNameForDelete');
let deleteStationButton = document.querySelector('#deleteStation button');
let confirmDeleteStationModal = document.querySelector('#confirmDeleteStationModal');
confirmDeleteStationModal = new bootstrap.Modal(confirmDeleteStationModal, {
    keyboard: false,
});

let oldStationNameForEditInput = document.querySelector('#oldStationNameForEdit');
let newStationNameForEditInput = document.querySelector('#newStationNameForEdit');
let editStationButton = document.querySelector('#editStation button');

let trainNumberForAddToScheduleInput = document.querySelector('#trainNumberForAddToSchedule');
let addTrainToScheduleButton = document.querySelector('#addTrainToSchedule button');

let trainNumberForDeleteFromScheduleInput = document.querySelector('#trainNumberForDeleteFromSchedule');
let deleteTrainFromScheduleButton = document.querySelector('#deleteTrainFromSchedule button');
let confirmDeleteTrainFromScheduleModal = document.querySelector('#confirmDeleteTrainFromScheduleModal');
confirmDeleteTrainFromScheduleModal = new bootstrap.Modal(confirmDeleteTrainFromScheduleModal, {
    keyboard: false,
});

trainNumberForDeleteInput.addEventListener("change", e => {
    let input = e.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
});

deleteTrainButton.addEventListener("click", e => {
    e.preventDefault();
    if (!trainNumberForDeleteInput.previousElementSibling.firstElementChild.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing train";
        } else {
            modalBody.innerHTML = "Виберіть існуючий поїзд";
        }
        errorModal.show();
        return;
    }
    confirmDeleteTrainModal.show();
});

oldTrainNumberForEditInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let trainIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    let trainNumberForEditInput = document.querySelector('#trainNumberForEdit');
    let departureTimeInput = document.querySelector('#trainDepartureTimeForEdit');
    let trainDepartureTimeDatalist = document.querySelector('#trainDepartureTimeDatalist');
    changeAttribute(input.value, trainIdInput, datalist);
    if (trainIdInput.value !== "") {
        trainNumberForEditInput.setAttribute("disabled", "");
        departureTimeInput.value = trainDepartureTimeDatalist.querySelector('option[id="' +  trainIdInput.value + '"]').value;
        departureTimeInput.setAttribute("disabled", "");
        for (let i = 0; i < editTrainCheckboxes.length; i++) {
            editTrainCheckboxes[i].removeAttribute("hidden");
            if (i === 0 && editTrainCheckboxes[i].hasAttribute("checked")) {
                editTrainCheckboxes[i].click();
                editTrainForm.querySelector('button').setAttribute("disabled", "");
            }
        }
    } else {
        trainNumberForEditInput.value = "";
        trainNumberForEditInput.setAttribute("disabled", "");
        departureTimeInput.value = "";
        departureTimeInput.setAttribute("disabled", "");
        for (let i = 0; i < editTrainCheckboxes.length; i++) {
            editTrainCheckboxes[i].setAttribute("hidden", "");
            if (i === 0 && editTrainCheckboxes[i].hasAttribute("checked")) {
                editTrainCheckboxes[i].click();
                editTrainForm.querySelector('button').setAttribute("disabled", "");
            }
        }
    }
})

for (let i = 0; i < editTrainCheckboxes.length; i++) {
    editTrainCheckboxes[i].addEventListener("click", (e) => {
        let checkbox = e.currentTarget;
        let input = checkbox.parentElement.parentElement.previousElementSibling;
        if (checkbox.hasAttribute("checked")) {
            checkbox.removeAttribute("checked");
            if (input.id === "trainNumberForEdit") {
                input.value = "";
            } else {
                let trainIdInput = editTrainForm.querySelector('input[name="trainId"]');
                let trainDepartureTimeDatalist = document.querySelector('#trainDepartureTimeDatalist');
                input.value = trainDepartureTimeDatalist.querySelector('option[id="' +  trainIdInput.value + '"]').value;
            }
            input.setAttribute("disabled", "");
            for (let j = 0; j < editTrainCheckboxes.length; j++) {
                if (editTrainCheckboxes[j].hasAttribute("checked")) {
                    break;
                }
                if (j === editTrainCheckboxes.length - 1) {
                    editTrainButton.setAttribute("disabled", "");
                }
            }
        } else {
            checkbox.setAttribute("checked", "");
            input.removeAttribute("disabled");
            editTrainButton.removeAttribute("disabled");
        }
    });
}

editTrainButton.addEventListener("click", evt => {
    let trainIdInput = editTrainForm.querySelector('input[name="trainId"]');
    let trainNumberForEdit = editTrainForm.querySelector('#trainNumberForEdit');
    let trainDepartureTimeForEdit = editTrainForm.querySelector('#trainDepartureTimeForEdit').value.split(":");
    if (trainIdInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train number is not specified";
        } else {
            modalBody.innerHTML = "Номер поїзда не задано";
        }
        errorModal.show();
        return;
    }
    if (trainNumberForEdit.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New train number is not specified";
        } else {
            modalBody.innerHTML = "Новий номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (trainDepartureTimeForEdit.length !== 2) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train departure time is not specified";
        } else {
            modalBody.innerHTML = "Час відправлення поїзда не задано";
        }
        errorModal.show();
        return;
    }
    for (let i = 0; i < trainDepartureTimeForEdit.length; i++) {
        if (trainDepartureTimeForEdit[i] === "") {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Train departure time is not specified";
            } else {
                modalBody.innerHTML = "Час відправлення поїзда не задано";
            }
            errorModal.show();
            return;
        }
    }
    let inputs = editTrainForm.querySelectorAll("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].removeAttribute("disabled");
    }
});

trainNumberForEditRouteInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
});

trainNumberForAddCarriageToTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let trainIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, trainIdInput, datalist);
});

carriageTypeForAddCarriageToTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let typeIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, typeIdInput, datalist);
});

addCarriageToTrainButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let trainIdInput = form.querySelector('input[name="trainId"]');
    let carriageNumberInput = form.querySelector('input[name="carriageNumber"]');
    let typeIdInput = form.querySelector('input[name="typeId"]');
    if (trainIdInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train number is not specified";
        } else {
            modalBody.innerHTML = "Номер поїзда не задано";
        }
        errorModal.show();
        return;
    }
    if (carriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "Номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
    }
});

trainNumberForDeleteCarriageFromTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let trainIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, trainIdInput, datalist);
    if (trainIdInput.value !== "") {
        carriageNumberForDeleteCarriageFromTrainInput.removeAttribute("disabled");
        carriageNumberForDeleteCarriageFromTrainInput.setAttribute("list", "train" + trainIdInput.value + "Carriages");
    } else {
        carriageNumberForDeleteCarriageFromTrainInput.removeAttribute("list");
        carriageNumberForDeleteCarriageFromTrainInput.value = "";
        carriageNumberForDeleteCarriageFromTrainInput.setAttribute("disabled", "");
        carriageNumberForDeleteCarriageFromTrainInput.dispatchEvent(new Event("change"));
    }
});

carriageNumberForDeleteCarriageFromTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    if (input.value === "") {
        carriageTypeForDeleteCarriageFromTrainInput.removeAttribute("value");
    } else {
        let datalist = input.getAttribute('list');
        let option = document.querySelector('datalist[id="' + datalist +'"]').querySelector('option[value="' + input.value + '"]');
        if (option === null) {
            carriageTypeForDeleteCarriageFromTrainInput.removeAttribute("value");
        } else {
            carriageTypeForDeleteCarriageFromTrainInput.setAttribute("value", option.getAttribute('data-type'));
        }
    }
    carriageTypeForDeleteCarriageFromTrainInput.dispatchEvent(new Event("change"));
});

carriageTypeForDeleteCarriageFromTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let typeIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, typeIdInput, datalist);
});

deleteCarriageFromTrainButton.addEventListener("click", evt => {
    evt.preventDefault();
    let form = evt.currentTarget.parentElement;
    let trainIdInput = form.querySelector('input[name="trainId"]');
    let carriageNumberInput = form.querySelector('input[name="carriageNumber"]');
    let typeIdInput = form.querySelector('input[name="typeId"]');
    if (trainIdInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train number is not specified";
        } else {
            modalBody.innerHTML = "Номер поїзда не задано";
        }
        errorModal.show();
        return;
    }
    if (carriageNumberInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "Номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    confirmDeleteCarriageFromTrainModal.show();
});

addCarriageTypeButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let carriageType = form.querySelector('input[name="carriageType"]');
    let maxSeats = form.querySelector('input[name="maxSeats"]');
    if (carriageType.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (maxSeats.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Maximum number seats this carriage type is not specified";
        } else {
            modalBody.innerHTML = "Максимальну кількість місць вагона цього типу не задано";
        }
        errorModal.show();
    }
});

carriageTypeForDeleteInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let typeIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, typeIdInput, datalist);
});

deleteCarriageTypeButton.addEventListener("click", evt => {
    evt.preventDefault();
    let form = evt.currentTarget.parentElement;
    let typeIdInput = form.querySelector('input[name="typeId"]');
    let carriageType = form.querySelector('input[name="carriageType"]');
    if (typeIdInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (carriageType.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    confirmDeleteCarriageTypeModal.show();
});

carriageTypeForEditInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let typeIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, typeIdInput, datalist);
});

editCarriageTypeButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let typeIdInput = form.querySelector('input[name="typeId"]');
    let carriageType = form.querySelector('input[name="carriageType"]');
    let newCarriageType = form.querySelector('input[name="newCarriageType"]');
    if (typeIdInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (newCarriageType.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New carriage type is not specified";
        } else {
            modalBody.innerHTML = "Нова назва вагонів цього типу не задана";
        }
        errorModal.show();
        return;
    }
    if (carriageType.value === newCarriageType.value) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Old carriage type and new carriage type are match";
        } else {
            modalBody.innerHTML = "Нова та стара назви вагонів цього типу однакові";
        }
        errorModal.show();
    }
});

addStationButton.addEventListener("click", evt => {
    let stationNameUA = evt.currentTarget.previousElementSibling.previousElementSibling.previousElementSibling.value;
    let stationNameEN = evt.currentTarget.previousElementSibling.value;
    if (stationNameEN === "" || stationNameUA === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Station name cannot be empty";
        } else {
            modalBody.innerHTML = "Ім'я станції не може бути пустим";
        }
        errorModal.show();
    }
});

stationNameForDeleteInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
});

deleteStationButton.addEventListener("click", evt => {
    let stationIdInput = evt.currentTarget.previousElementSibling.previousElementSibling.firstElementChild;
    evt.preventDefault();
    if (!stationIdInput.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing station";
        } else {
            modalBody.innerHTML = "Виберіть існуючу станцію";
        }
        errorModal.show();
        return;
    }
    confirmDeleteStationModal.show();
});

oldStationNameForEditInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
    if (input.value !== "") {
        newStationNameForEditInput.removeAttribute("disabled");
    } else {
        newStationNameForEditInput.setAttribute("disabled", "");
    }
});

newStationNameForEditInput.addEventListener("change", evt => {
    let inputValue = evt.currentTarget.value;
    if (inputValue !== "") {
        editStationButton.removeAttribute("disabled");
    } else {
        editStationButton.setAttribute("disabled", "");
    }
});

editStationButton.addEventListener("click", evt => {
    if (!oldStationNameForEditInput.previousElementSibling.firstElementChild.hasAttribute("value")) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing station";
        } else {
            modalBody.innerHTML = "Виберіть існуючу станцію";
        }
        errorModal.show();
        return;
    }
    if (newStationNameForEditInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Station name cannot be empty";
        } else {
            modalBody.innerHTML = "Ім'я станції не може бути пустим";
        }
        errorModal.show();
        return;
    }
    if (oldStationNameForEditInput.value === newStationNameForEditInput.value) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New and old station name match";
        } else {
            modalBody.innerHTML = "Нове та старе імена станції однакові";
        }
        errorModal.show();
    }
});

trainNumberForAddToScheduleInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
    if (input.value !== "") {
        addTrainToScheduleButton.removeAttribute("disabled");
    } else {
        addTrainToScheduleButton.setAttribute("disabled", "");
    }
});

addTrainToScheduleButton.addEventListener("click", evt => {
    let trainIdInput = evt.currentTarget.previousElementSibling.previousElementSibling.firstElementChild;
    if (!trainIdInput.hasAttribute("value")) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing train";
        } else {
            modalBody.innerHTML = "Виберіть існуючий поїзд";
        }
        errorModal.show();
    }
});

trainNumberForDeleteFromScheduleInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, input.previousElementSibling.firstElementChild, datalist);
    if (input.value !== "") {
        deleteTrainFromScheduleButton.removeAttribute("disabled");
    } else {
        deleteTrainFromScheduleButton.setAttribute("disabled", "");
    }
});

deleteTrainFromScheduleButton.addEventListener("click", evt => {
    let trainIdInput = evt.currentTarget.previousElementSibling.previousElementSibling.firstElementChild;
    evt.preventDefault();
    if (!trainIdInput.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing train";
        } else {
            modalBody.innerHTML = "Виберіть існуючий поїзд";
        }
        errorModal.show();
        return;
    }
    confirmDeleteTrainFromScheduleModal.show();
});