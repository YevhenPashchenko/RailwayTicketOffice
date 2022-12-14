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

let stationNameForEditInput = document.querySelector('#stationNameForEdit');
let newStationNameUAForEditInput = document.querySelector('#newStationNameUAForEdit');
let newStationNameENForEditInput = document.querySelector('#newStationNameENForEdit');
let editStationButton = document.querySelector('#editStation button');

let trainNumberForAddToScheduleInput = document.querySelector('#trainNumberForAddToSchedule');
let addTrainToScheduleButton = document.querySelector('#addTrainToSchedule button');

let trainNumberForDeleteFromScheduleInput = document.querySelector('#trainNumberForDeleteFromSchedule');
let deleteTrainFromScheduleButton = document.querySelector('#deleteTrainFromSchedule button');
let confirmDeleteTrainFromScheduleModal = document.querySelector('#confirmDeleteTrainFromScheduleModal');
confirmDeleteTrainFromScheduleModal = new bootstrap.Modal(confirmDeleteTrainFromScheduleModal, {
    keyboard: false,
});

let trainNumberForSwitchAutoAdditionToScheduleInput = document.querySelector('#trainNumberForSwitchAutoAdditionToSchedule');
let switchAutoAdditionToScheduleButton = document.querySelector('#switchAutoAdditionTrainToSchedule button');

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

let scheduleInputs = [trainNumberForAddToScheduleInput, trainNumberForDeleteFromScheduleInput, trainNumberForSwitchAutoAdditionToScheduleInput];

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

let scheduleButtons = [addTrainToScheduleButton, switchAutoAdditionToScheduleButton];

scheduleButtons.map(button => {
    button.addEventListener("click", evt => {
        let form = evt.currentTarget.parentElement;
        let trainIdInput = form.querySelector('input[name="trainId"]');
        if (!trainIdInput.hasAttribute("value")) {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Select an existing train";
            } else {
                modalBody.innerHTML = "???????????????? ???????????????? ??????????";
            }
            errorModal.show();
        }
    });
});

deleteTrainButton.addEventListener("click", e => {
    e.preventDefault();
    if (!trainNumberForDeleteInput.previousElementSibling.firstElementChild.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing train";
        } else {
            modalBody.innerHTML = "???????????????? ???????????????? ??????????";
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
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (trainNumberForEdit.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New train number is not specified";
        } else {
            modalBody.innerHTML = "?????????? ?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (trainDepartureTimeForEdit.length !== 2) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Train departure time is not specified";
        } else {
            modalBody.innerHTML = "?????? ???????????????????????? ???????????? ???? ????????????";
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
                modalBody.innerHTML = "?????? ???????????????????????? ???????????? ???? ????????????";
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
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (carriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
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
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (carriageIdInput.value === "" || carriageNumberInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "" || carriageTypeInput.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
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
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (carriageIdInput.value === "" || carriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage number is not specified";
        } else {
            modalBody.innerHTML = "?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (newCarriageNumberInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New carriage number is not specified";
        } else {
            modalBody.innerHTML = "?????????? ?????????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (typeIdInput.value === "" || carriageTypeInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
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
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (maxSeats.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Maximum number seats this carriage type is not specified";
        } else {
            modalBody.innerHTML = "?????????????????????? ?????????????????? ?????????? ???????????? ?????????? ???????? ???? ????????????";
        }
        errorModal.show();
    }
});

deleteCarriageTypeButton.addEventListener("click", evt => {
    evt.preventDefault();
    let form = evt.currentTarget.parentElement;
    let typeIdInput = form.querySelector('input[name="typeId"]');
    let carriageType = form.querySelector('input[name="carriageType"]');
    if (typeIdInput.value === "" || carriageType.value === "") {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Carriage type is not specified";
        } else {
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
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
            modalBody.innerHTML = "?????? ???????????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (newCarriageType.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "New carriage type is not specified";
        } else {
            modalBody.innerHTML = "???????? ?????????? ?????????????? ?????????? ???????? ???? ????????????";
        }
        errorModal.show();
        return;
    }
    if (carriageType.value === newCarriageType.value) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Old carriage type and new carriage type are match";
        } else {
            modalBody.innerHTML = "???????? ???? ?????????? ?????????? ?????????????? ?????????? ???????? ????????????????";
        }
        errorModal.show();
    }
});

addStationButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let stationNameUAInput = form.querySelector('input[name="stationNameUA"]');
    let stationNameENInput = form.querySelector('input[name="stationNameEN"]');
    if (stationNameENInput.value === "" || stationNameUAInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Station name cannot be empty";
        } else {
            modalBody.innerHTML = "????'?? ?????????????? ???? ???????? ???????? ????????????";
        }
        errorModal.show();
    }
});

deleteStationButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let stationIdInput = form.querySelector('input[name="stationId"]');
    evt.preventDefault();
    if (!stationIdInput.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing station";
        } else {
            modalBody.innerHTML = "???????????????? ?????????????? ??????????????";
        }
        errorModal.show();
        return;
    }
    confirmDeleteStationModal.show();
});

stationNameForEditInput.addEventListener("change", evt => {
    let input = evt.currentTarget;
    let idInput = input.previousElementSibling.firstElementChild;
    let datalist = input.getAttribute('list');
    changeAttribute(input.value, idInput, datalist);
    if (idInput.value !== "") {
        newStationNameUAForEditInput.removeAttribute("disabled");
        newStationNameENForEditInput.removeAttribute("disabled");
        editStationButton.removeAttribute("disabled");
    } else {
        newStationNameUAForEditInput.setAttribute("disabled", "");
        newStationNameENForEditInput.setAttribute("disabled", "");
        editStationButton.setAttribute("disabled", "");
    }
});

editStationButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let idInput = form.querySelector('input[name="stationId"]');
    if (!idInput.hasAttribute("value")) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing station";
        } else {
            modalBody.innerHTML = "???????????????? ?????????????? ??????????????";
        }
        errorModal.show();
        return;
    }
    if (newStationNameUAForEditInput.value === "" || newStationNameENForEditInput.value === "") {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Station name cannot be empty";
        } else {
            modalBody.innerHTML = "????'?? ?????????????? ???? ???????? ???????? ????????????";
        }
        errorModal.show();
    }
});

$(function() {
    let dateNow = Date.now();
    let minDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + new Date(dateNow).getFullYear();
    let maxDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + (new Date(dateNow).getFullYear() + 1);
    let ukLocale = {
        "format": "DD.MM.YYYY",
        "separator": "-",
        "applyLabel": "????????????",
        "cancelLabel": "??????????????",
        "daysOfWeek": [
            "????",
            "????",
            "????",
            "????",
            "????",
            "????",
            "????"
        ],
        "monthNames": [
            "????????????",
            "??????????",
            "????????????????",
            "??????????????",
            "??????????????",
            "??????????????",
            "????????????",
            "??????????????",
            "????????????????",
            "??????????????",
            "????????????????",
            "??????????????"
        ],
        "firstDay": 1
    };
    let enLocale = {
        "format": "DD.MM.YYYY",
        "separator": "-",
        "applyLabel": "Ok",
        "cancelLabel": "Cancel",
        "daysOfWeek": [
            "Su",
            "Mo",
            "Tu",
            "We",
            "Th",
            "Fr",
            "Sa"
        ],
        "monthNames": [
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        ],
        "firstDay": 0
    };

    let locale;

    if (currentLocale === "en") {
        locale = enLocale;
    } else {
        locale = ukLocale;
    }

    $('#departureDateForDeleteFromSchedule').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "minYear": minDate.split(".")[2],
        "maxYear": maxDate.split(".")[2],
        "locale": locale,
        "startDate": minDate,
        "endDate": maxDate,
        "minDate": minDate,
        "maxDate": maxDate,
        "opens": "right",
        "linkedCalendars": false
    });
});

deleteTrainFromScheduleButton.addEventListener("click", evt => {
    let form = evt.currentTarget.parentElement;
    let trainIdInput = form.querySelector('input[name="trainId"]');
    evt.preventDefault();
    if (!trainIdInput.hasAttribute("value")) {
        if (currentLocale === "en") {
            modalBody.innerHTML = "Select an existing train";
        } else {
            modalBody.innerHTML = "???????????????? ???????????????? ??????????";
        }
        errorModal.show();
        return;
    }
    confirmDeleteTrainFromScheduleModal.show();
});