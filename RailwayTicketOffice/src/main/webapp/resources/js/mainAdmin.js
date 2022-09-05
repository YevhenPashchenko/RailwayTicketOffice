mdtimepicker(document.querySelectorAll('.time'), {
    format: 'hh:mm',
    is24hour: true,
});

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
let carriageNumberForAddCarriageToTrainInput = document.querySelector('#carriageNumberForAddCarriageToTrain');
let carriageNumberForAddCarriageToTrainInputValue;
let carriageTypeForAddCarriageToTrainInput = document.querySelector('#carriageTypeForAddCarriageToTrain');
let addCarriageToTrainButton = document.querySelector('#addCarriageToTrain button');

let trainNumberForDeleteCarriageFromTrainInput = document.querySelector('#trainNumberForDeleteCarriageFromTrain');
let carriageNumberForDeleteCarriageFromTrainInput = document.querySelector('#carriageNumberForDeleteCarriageFromTrain');
let carriageNumberForDeleteCarriageFromTrainInputValue;
let carriageTypeForDeleteCarriageFromTrainInput = document.querySelector('#carriageTypeForDeleteCarriageFromTrain');
let deleteCarriageFromTrainButton= document.querySelector('#deleteCarriageFromTrain button');
let confirmDeleteCarriageFromTrainModal = document.querySelector('#confirmDeleteCarriageFromTrainModal');
let confirmDeleteCarriageFromTrainModalConfirmButton = confirmDeleteCarriageFromTrainModal.querySelector('button[type="submit"]');
confirmDeleteCarriageFromTrainModal = new bootstrap.Modal(confirmDeleteCarriageFromTrainModal, {
    keyboard: false,
});

let trainNumberForEditCarriageNumberInTrainInput = document.querySelector('#trainNumberForEditCarriageNumberInTrain');
let carriageNumberForEditCarriageNumberInTrainInput = document.querySelector('#carriageNumberForEditCarriageNumberInTrain');
let carriageNumberForEditCarriageNumberInTrainInputValue;
let newCarriageNumberForEditCarriageNumberInTrainInput = document.querySelector('#newCarriageNumberForEditCarriageNumberInTrain');
let newCarriageNumberForEditCarriageNumberInTrainInputValue;
let carriageTypeForEditCarriageNumberInTrainInput = document.querySelector('#carriageTypeForEditCarriageNumberInTrain');
let editCarriageNumberInTrainButton = document.querySelector('#editCarriageNumberInTrain button');

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

let adminInputs = [
    trainNumberForDeleteInput,
    trainNumberForEditRouteInput,
    carriageTypeForAddCarriageToTrainInput,
    carriageTypeForDeleteCarriageFromTrainInput,
    carriageTypeForEditCarriageNumberInTrainInput,
    carriageTypeForDeleteInput,
    carriageTypeForEditInput,
    stationNameForDeleteInput
];

adminInputs.map(input => {
    input.addEventListener("change", evt => {
        let input = evt.currentTarget;
        let idInput = input.previousElementSibling.firstElementChild;
        let datalist = input.getAttribute('list');
        changeAttribute(input.value, idInput, datalist);
    });
});

let carriageNumberIsInTrainInputs = {
    carriageNumberForDeleteCarriageFromTrainInput: [carriageNumberForDeleteCarriageFromTrainInput, carriageNumberForDeleteCarriageFromTrainInputValue, carriageTypeForDeleteCarriageFromTrainInput],
    oldCarriageNumberForEditCarriageNumberInTrainInput: [carriageNumberForEditCarriageNumberInTrainInput, carriageNumberForEditCarriageNumberInTrainInputValue, carriageTypeForEditCarriageNumberInTrainInput],
};

for (const carriageNumberIsInTrainInputsKey in carriageNumberIsInTrainInputs) {
    carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][0].addEventListener("change", evt => {
        let input = evt.currentTarget;
        let idInput = input.previousElementSibling.firstElementChild;
        if (!input.hasAttribute("disabled")) {
            let datalist = document.querySelector('datalist[id="' + input.getAttribute("list") + '"]');
            let options = datalist.querySelectorAll('option');
            let values = [];
            for (let i = 0; i < options.length; i++) {
                values[i] = options[i].value;
            }
            if (input.getAttribute("value") !== "" || input.value !== "") {
                if (!values.includes(input.value)) {
                    if (Math.min.apply(null, values) > input.value) {
                        input.setAttribute("value", Math.min.apply(null, values));
                        input.value = input.getAttribute("value");
                    } else if (Math.max.apply(null, values) < input.value) {
                        input.setAttribute("value", Math.max.apply(null, values));
                        input.value = input.getAttribute("value");
                    } else {
                        if (carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][1] < input.value) {
                            while (!values.includes(input.value)) {
                                input.value = +input.value + 1;
                            }
                            input.setAttribute("value", input.value);
                        } else {
                            while (!values.includes(input.value)) {
                                input.value = input.value - 1;
                            }
                            input.setAttribute("value", input.value);
                        }
                    }
                } else {
                    input.setAttribute("value", input.value);
                }
            } else {
                input.setAttribute("value", Math.min.apply(null, values));
                input.value = input.getAttribute("value");
            }
            carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][1] = input.getAttribute("value");
            options.forEach(option => {
                if (option.value === input.value) {
                    carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][2].setAttribute("value", option.getAttribute("data-type"));
                }
            });
            idInput.setAttribute("value", datalist.querySelector('option[value="' + input.value + '"]').id);
        } else {
            input.setAttribute("value", "");
            input.value = "";
            carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][2].removeAttribute("value");
            idInput.removeAttribute("value");
        }
        carriageNumberIsInTrainInputs[carriageNumberIsInTrainInputsKey][2].dispatchEvent(new Event("change"));
    });
}

let carriageNumberIsNotInTrainInputs = {
    carriageNumberForAddCarriageToTrainInput: [carriageNumberForAddCarriageToTrainInput, carriageNumberForAddCarriageToTrainInputValue],
    carriageNumberForEditCarriageNumberInTrainInput: [newCarriageNumberForEditCarriageNumberInTrainInput, newCarriageNumberForEditCarriageNumberInTrainInputValue],
};

for (const carriageNumberIsNotInTrainInputsKey in carriageNumberIsNotInTrainInputs) {
    carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][0].addEventListener("change", evt => {
        let input = evt.currentTarget;
        if (!input.hasAttribute("disabled")) {
            let form = input.parentElement;
            let trainId = form.querySelector('input[name="trainId"]');
            let options = document.querySelectorAll('datalist[id="train' + trainId.value + 'Carriages"] option');
            let values = [];
            for (let i = 0; i < options.length; i++) {
                values[i] = options[i].value;
            }
            if (input.getAttribute("value") !== "" || input.value !== "") {
                if (carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][1] < input.value) {
                    while (values.includes(input.value)) {
                        input.value = +input.value + 1;
                    }
                    input.setAttribute("value", input.value);
                } else {
                    while (values.includes(input.value)) {
                        input.value = input.value - 1;
                    }
                    if (input.value < 1) {
                        input.setAttribute("value", carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][1]);
                        input.value = carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][1];
                    } else {
                        input.setAttribute("value", input.value);
                    }
                }
                carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][1] = input.getAttribute("value");
            } else {
                input.setAttribute("value", "1");
                while (values.includes(input.getAttribute("value"))) {
                    input.setAttribute("value", +input.getAttribute("value") + 1);
                }
                input.value = input.getAttribute("value");
                carriageNumberIsNotInTrainInputs[carriageNumberIsNotInTrainInputsKey][1] = input.getAttribute("value");
            }
        } else {
            input.setAttribute("value", "");
            input.value = "";
        }
    });
}

let scheduleInputs = [trainNumberForAddToScheduleInput, trainNumberForDeleteFromScheduleInput];

scheduleInputs.map(input => {
    input.addEventListener("change", evt => {
        let input = evt.currentTarget;
        let idInput = input.previousElementSibling.firstElementChild;
        let datalist = input.getAttribute('list');
        let button = input.parentElement.querySelector('button');
        changeAttribute(input.value, idInput, datalist);
        if (idInput.value !== "") {
            button.removeAttribute("disabled");
        } else {
            button.setAttribute("disabled", "");
        }
    });
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
        }
    } else {
        trainNumberForEditInput.value = "";
        trainNumberForEditInput.setAttribute("disabled", "");
        departureTimeInput.value = "";
        departureTimeInput.setAttribute("disabled", "");
        for (let i = 0; i < editTrainCheckboxes.length; i++) {
            editTrainCheckboxes[i].setAttribute("hidden", "");
            if (editTrainCheckboxes[i].hasAttribute("checked")) {
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

trainNumberForAddCarriageToTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let idInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, idInput, datalist);
    if (idInput.value !== "") {
        carriageNumberForAddCarriageToTrainInput.removeAttribute("disabled");
        carriageTypeForAddCarriageToTrainInput.removeAttribute("disabled");
    } else {
        carriageNumberForAddCarriageToTrainInput.setAttribute("disabled", "");
        carriageTypeForAddCarriageToTrainInput.value = "";
        carriageTypeForAddCarriageToTrainInput.setAttribute("disabled", "");
    }
    carriageNumberForAddCarriageToTrainInput.dispatchEvent(new Event("change"));
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
        carriageNumberForDeleteCarriageFromTrainInput.setAttribute("disabled", "");
    }
    carriageNumberForDeleteCarriageFromTrainInput.dispatchEvent(new Event("change"));
});

deleteCarriageFromTrainButton.addEventListener("click", evt => {
    evt.preventDefault();
    let form = evt.currentTarget.parentElement;
    let trainIdInput = form.querySelector('input[name="trainId"]');
    let trainNumberInput = form.querySelector('input[name="trainNumber"]');
    let carriageIdInput = form.querySelector('input[name="carriageId"]');
    let carriageNumberInput = form.querySelector('input[name="carriageNumber"]');
    let typeIdInput = form.querySelector('input[name="typeId"]');
    let carriageTypeInput = form.querySelector('input[name="carriageType"]');
    if (trainIdInput.value === "" || trainNumberInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train number is not specified";
        } else {
            modalBody.innerHTML = "Номер поїзда не задано";
        }
        errorModal.show();
        return;
    }
    if (carriageIdInput.value === "" || carriageNumberInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "Номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "" || carriageTypeInput.value === "") {
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

confirmDeleteCarriageFromTrainModalConfirmButton.addEventListener("click", () => {
    carriageTypeForDeleteCarriageFromTrainInput.removeAttribute("disabled");
});

trainNumberForEditCarriageNumberInTrainInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let trainIdInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, trainIdInput, datalist);
    if (trainIdInput.value !== "") {
        carriageNumberForEditCarriageNumberInTrainInput.removeAttribute("disabled");
        carriageNumberForEditCarriageNumberInTrainInput.setAttribute("list", "train" + trainIdInput.value + "Carriages");
        newCarriageNumberForEditCarriageNumberInTrainInput.removeAttribute("disabled");
    } else {
        carriageNumberForEditCarriageNumberInTrainInput.removeAttribute("list");
        carriageNumberForEditCarriageNumberInTrainInput.setAttribute("disabled", "");
        newCarriageNumberForEditCarriageNumberInTrainInput.setAttribute("disabled", "");
    }
    carriageNumberForEditCarriageNumberInTrainInput.dispatchEvent(new Event("change"));
    newCarriageNumberForEditCarriageNumberInTrainInput.dispatchEvent(new Event("change"));
});

editCarriageNumberInTrainButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let trainIdInput = form.querySelector('input[name="trainId"]');
    let trainNumberInput = form.querySelector('input[name="trainNumber"]');
    let carriageIdInput = form.querySelector('input[name="carriageId"]');
    let carriageNumberInput = form.querySelector('input[name="carriageNumber"]');
    let newCarriageNumberInput = form.querySelector('input[name="newCarriageNumber"]');
    let typeIdInput = form.querySelector('input[name="typeId"]');
    let carriageTypeInput = form.querySelector('input[name="carriageType"]');
    if (trainIdInput.value === "" || trainNumberInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train number is not specified";
        } else {
            modalBody.innerHTML = "Номер поїзда не задано";
        }
        errorModal.show();
        return;
    }
    if (carriageIdInput.value === "" || carriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "Номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (newCarriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New carriage number is not specified";
        } else {
            modalBody.innerHTML = "Новий номер вагона не задано";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "" || carriageTypeInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "Тип вагона не задано";
        }
        errorModal.show();
        return;
    }
    carriageTypeInput.removeAttribute("disabled");
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