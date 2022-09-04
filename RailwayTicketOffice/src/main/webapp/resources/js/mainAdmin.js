let inputTrainNumberForDelete = document.querySelector('#trainNumberForDelete');
let deleteTrainButton = document.querySelector('#deleteTrainButton');
let confirmDeleteTrainModal = document.querySelector('#confirmDeleteTrainModal');
confirmDeleteTrainModal = new bootstrap.Modal(confirmDeleteTrainModal, {
    keyboard: false,
});
let inputTrainNumberForEdit = document.querySelector('#trainNumberForEdit');
let editTrainForm = document.querySelector('#editTrain');
let editTrainCheckboxes = editTrainForm.querySelectorAll('input[type="checkbox"]');
let editTrainButton = editTrainForm.querySelector('button');
let inputTrainNumberForEditRoute = document.querySelector('#trainNumberForEditRoute');
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

inputTrainNumberForDelete.addEventListener("change", e => {
    let inputValue = e.currentTarget.value;
    changeAttribute(inputValue, e.currentTarget.previousElementSibling.firstElementChild);
});

deleteTrainButton.addEventListener("click", e => {
    e.preventDefault();
    if (!inputTrainNumberForDelete.previousElementSibling.firstElementChild.hasAttribute("value")) {
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

for (let i = 0; i < editTrainCheckboxes.length; i++) {
    editTrainCheckboxes[i].addEventListener("click", (e) => {
        let inputTrainId = editTrainForm.querySelector('input[name="trainId"]');
        let checkbox = e.currentTarget;
        let input = checkbox.parentElement.parentElement.previousElementSibling;
        let inputId = input.getAttribute("id");
        let datalist = document.querySelector('datalist[id="' + inputId.substring(0, inputId.length - 7) + "Datalist" + '"]')
        if (checkbox.hasAttribute("checked")) {
            checkbox.removeAttribute("checked");
            input.value = datalist.querySelector('option[id="' +  inputTrainId.value + '"]').value;
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

editTrainButton.addEventListener("click", () => {
    let inputs = editTrainForm.querySelectorAll("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].removeAttribute("disabled");
    }
});

inputTrainNumberForEdit.addEventListener("change", evt => {
    let inputTrainNumber = evt.currentTarget;
    let inputSeats = document.querySelector('#trainSeatsForEdit');
    let inputDepartureTime = document.querySelector('#trainDepartureTimeForEdit');
    let inputTrainId = editTrainForm.querySelector('input[name="trainId"]');
    let trainNumberDatalist = document.querySelector('#trainNumberDatalist');
    let trainSeatsDatalist = document.querySelector('#trainSeatsDatalist');
    let trainDepartureTimeDatalist = document.querySelector('#trainDepartureTimeDatalist');
    let trainNumberOption = trainNumberDatalist.querySelector('option[value="' + inputTrainNumber.value + '"]');
    if (trainNumberOption !== null) {
        inputTrainId.value = trainNumberOption.id;
        inputTrainNumber.setAttribute("disabled", "");
        inputSeats.value = trainSeatsDatalist.querySelector('option[id="' +  inputTrainId.value + '"]').value;
        inputSeats.setAttribute("disabled", "");
        inputDepartureTime.value = trainDepartureTimeDatalist.querySelector('option[id="' +  inputTrainId.value + '"]').value;
        inputDepartureTime.setAttribute("disabled", "");
        for (let i = 0; i < editTrainCheckboxes.length; i++) {
            editTrainCheckboxes[i].removeAttribute("hidden");
            if (i === 0 && editTrainCheckboxes[i].hasAttribute("checked")) {
                editTrainCheckboxes[i].click();
                editTrainForm.querySelector('button').setAttribute("disabled", "");
            }
        }
    }
})

inputTrainNumberForEditRoute.addEventListener("change", evt => {
    let inputValue = evt.currentTarget.value;
    changeAttribute(inputValue, evt.currentTarget.previousElementSibling.firstElementChild);
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
    let inputValue = evt.currentTarget.value;
    changeAttribute(inputValue, evt.currentTarget.previousElementSibling.firstElementChild);
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
    changeAttribute(input.value, evt.currentTarget.previousElementSibling.firstElementChild);
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
    changeAttribute(input.value, input.previousElementSibling.firstElementChild);
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
    changeAttribute(input.value, input.previousElementSibling.firstElementChild);
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