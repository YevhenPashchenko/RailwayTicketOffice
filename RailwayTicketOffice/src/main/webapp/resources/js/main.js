let button = document.querySelector('button[datatype="searchTrains"]');
let inputFrom = document.querySelector('#fromDatalist');
let inputsFrom = document.querySelectorAll('input[name="from"]');
let inputTo = document.querySelector('#toDatalist');
let inputsTo = document.querySelectorAll('input[name="to"]');
let modalBody = document.querySelector('.errorModalBody');
let errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
    keyboard: false,
});
let images = document.querySelectorAll('.changePasswordVisible');
let registrationFormButton = document.querySelector('button[form="registrationForm"]');
let editUserForm = document.querySelector('#editUserForm');
let editUserFormButton = document.querySelector('button[form="editUserForm"]');
let inputTrainNumberForDelete = document.querySelector('#trainNumberForDelete');
let deleteTrainButton = document.querySelector('#deleteTrainButton');
let confirmDeleteTrainModal = document.querySelector('#confirmDeleteTrainModal');
if (confirmDeleteTrainModal !== null) {
    confirmDeleteTrainModal = new bootstrap.Modal(confirmDeleteTrainModal, {
        keyboard: false,
    });
}
let inputTrainNumberForEdit = document.querySelector('#trainNumberForEdit');
let editTrainForm = document.querySelector('#editTrain');
let editTrainCheckboxes = null;
if (editTrainForm != null) {
    editTrainCheckboxes = editTrainForm.querySelectorAll('input[type="checkbox"]');
}
let inputTrainNumberForEditRoute = document.querySelector('#trainNumberForEditRoute');
let addStationButton = document.querySelector('#addStation button');
let stationNameForDeleteInput = document.querySelector('#stationNameForDelete');
let deleteStationButton = document.querySelector('#deleteStation button');
let confirmDeleteStationModal = document.querySelector('#confirmDeleteStationModal');
if (confirmDeleteStationModal != null) {
    confirmDeleteStationModal = new bootstrap.Modal(confirmDeleteStationModal, {
        keyboard: false,
    });
}
let oldStationNameForEditInput = document.querySelector('#oldStationNameForEdit');
let newStationNameForEditInput = document.querySelector('#newStationNameForEdit');
let editStationButton = document.querySelector('#editStation button');
let trainNumberForAddToScheduleInput = document.querySelector('#trainNumberForAddToSchedule');
let addTrainToScheduleButton = document.querySelector('#addTrainToSchedule button');
let trainNumberForDeleteFromScheduleInput = document.querySelector('#trainNumberForDeleteFromSchedule');
let deleteTrainFromScheduleButton = document.querySelector('#deleteTrainFromSchedule button');
let confirmDeleteTrainFromScheduleModal = document.querySelector('#confirmDeleteTrainFromScheduleModal');
if (confirmDeleteTrainFromScheduleModal != null) {
    confirmDeleteTrainFromScheduleModal = new bootstrap.Modal(confirmDeleteTrainFromScheduleModal, {
        keyboard: false,
    });
}
let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");

$(function() {
    let dateNow = Date.now();
    let minDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + new Date(dateNow).getFullYear();
    let startDate;
    let checkedDate = $('#checkedDate')[0];
    if (checkedDate !== undefined) {
        startDate = checkedDate.innerHTML.split("-").reverse().join(".");
    } else {
        startDate = minDate;
    }
    let maxDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + (new Date(dateNow).getFullYear() + 1);
    let ukLocale = {
        "format": "DD.MM.YYYY",
        "separator": "-",
        "applyLabel": "Готово",
        "cancelLabel": "Відміна",
        "daysOfWeek": [
            "Нд",
            "Пн",
            "Вт",
            "Ср",
            "Чт",
            "Пт",
            "Сб"
        ],
        "monthNames": [
            "Січень",
            "Лютий",
            "Березень",
            "Квітень",
            "Травень",
            "Червень",
            "Липень",
            "Серпень",
            "Вересень",
            "Жовтень",
            "Листопад",
            "Грудень"
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

    $('#datePicker').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "minYear": minDate.split(".")[2],
        "maxYear": maxDate.split(".")[2],
        "locale": locale,
        "startDate": startDate,
        "endDate": maxDate,
        "minDate": minDate,
        "maxDate": maxDate,
        "opens": "right",
        "linkedCalendars": false
    });

    function addDate() {
        let inputs = document.querySelectorAll('input[name="datePicker"]');
        let date = $('.drp-selected')[0].innerHTML.split("-")[0];
        if (date.length === 0) {
            date = new Date(startDate.split(".").reverse().join("-")).toLocaleDateString('uk-UA', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
            });
        }
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].id === 'datePicker') {
                continue;
            }
            inputs[i].setAttribute("value", date);
        }
        if (currentLocale === "en") {
            button.innerHTML = "Search trains at " + date;
        } else {
            button.innerHTML = "Пошук поїздів на " + date;
        }
    }

    addDate();

    $('.applyBtn')[0].addEventListener("click", () => {
        addDate();
    });

});

inputFrom.addEventListener("change", e => {
    let inputValue = e.currentTarget.value;
    for (let i = 0; i < inputsFrom.length; i++) {
        changeAttribute(inputValue, inputsFrom[i]);
    }
});

inputTo.addEventListener("change", e => {
    let inputValue = e.currentTarget.value;
    for (let i = 0; i < inputsTo.length; i++) {
        changeAttribute(inputValue, inputsTo[i]);
    }
});

button.addEventListener("click", ev => {
    if (!inputsFrom[0].hasAttribute("value")) {
        ev.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter your destination";
        } else {
            modalBody.innerHTML = "Введіть пункт відправлення";
        }
        errorModal.show();
        return;
    }
    if (!inputsTo[0].hasAttribute("value")) {
        ev.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Enter your destination";
        } else {
            modalBody.innerHTML = "Введіть пункт відправлення";
        }
        errorModal.show();
    }
    if (inputsFrom[0].value === inputsTo[0].value) {
        ev.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "Departure and destination stations match";
        } else {
            modalBody.innerHTML = "Станції відправлення та призначення співпадають";
        }
        errorModal.show();
    }
});

document.querySelector('.reverseRoute').addEventListener("click", () => {
    let value = inputFrom.value;
    inputFrom.value = inputTo.value;
    for (let i = 0; i < inputsFrom.length; i++) {
        changeAttribute(inputFrom.value, inputsFrom[i]);
    }
    inputTo.value = value;
    for (let i = 0; i < inputsTo.length; i++) {
        changeAttribute(inputTo.value, inputsTo[i]);
    }
});

for (let i = 0; i < images.length; i++) {
    images[i].addEventListener('click', (ev) => {
        let el = ev.currentTarget;
        if (el.getAttribute('alt') === "Show password") {
            el.nextElementSibling.classList.remove('visually-hidden');
            el.parentElement.parentElement.querySelector('input[type="password"]').setAttribute('type', 'text');
            el.classList.add('visually-hidden');
        } else {
            el.previousElementSibling.classList.remove('visually-hidden');
            el.parentElement.parentElement.querySelector('input[type="text"]').setAttribute('type', 'password');
            el.classList.add('visually-hidden');
        }
    });
}

if (registrationFormButton !== null) {
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
}

if (editUserFormButton !== null) {
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
    });
}

if (editUserForm !== null) {
    let editUserCheckboxes = editUserForm.querySelectorAll('.form-check-input');
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
}

function changeAttribute(inputValue, input) {
    if (inputValue === "") {
        input.removeAttribute("value");
    } else {
        let option = document.querySelector('option[value="' + inputValue + '"]');
        if (option === undefined) {
            input.removeAttribute("value");
        } else {
            input.setAttribute("value", option.getAttribute('id'));
        }
    }
}

if (inputTrainNumberForDelete !== null) {
    inputTrainNumberForDelete.addEventListener("change", e => {
        let inputValue = e.currentTarget.value;
        changeAttribute(inputValue, e.currentTarget.previousElementSibling.firstElementChild);
    });
}

if (deleteTrainButton !== null) {
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
}

if (editTrainCheckboxes !== null) {
    let button = editTrainForm.querySelector('button');
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
    button.addEventListener("click", () => {
        let inputs = editTrainForm.querySelectorAll("input");
        for (let i = 0; i < inputs.length; i++) {
            inputs[i].removeAttribute("disabled");
        }
    });
}

if (inputTrainNumberForEdit !== null) {
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
}

if (inputTrainNumberForEditRoute !== null) {
    inputTrainNumberForEditRoute.addEventListener("change", evt => {
        let inputValue = evt.currentTarget.value;
        changeAttribute(inputValue, evt.currentTarget.previousElementSibling.firstElementChild);
    });
}

if (addStationButton != null) {
    addStationButton.addEventListener("click", evt => {
        if (evt.currentTarget.previousElementSibling.value === "") {
            evt.preventDefault();
            if (currentLocale === "en") {
                modalBody.innerHTML = "Station name cannot be empty";
            } else {
                modalBody.innerHTML = "Ім'я станції не може бути пустим";
            }
            errorModal.show();
        }
    });
}

if (stationNameForDeleteInput != null) {
    stationNameForDeleteInput.addEventListener("change", evt => {
        let inputValue = evt.currentTarget.value;
        changeAttribute(inputValue, evt.currentTarget.previousElementSibling.firstElementChild);
    });
}

if (deleteStationButton != null) {
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
}

if (oldStationNameForEditInput != null) {
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
}

if (trainNumberForAddToScheduleInput != null) {
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
}

if (trainNumberForDeleteFromScheduleInput != null) {
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
}