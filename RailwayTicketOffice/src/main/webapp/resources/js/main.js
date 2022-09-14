let images = document.querySelectorAll('.changePasswordVisible');
let inputFrom = document.querySelector('#fromDatalist');
let inputsFrom = document.querySelectorAll('input[name="from"]');
let inputTo = document.querySelector('#toDatalist');
let inputsTo = document.querySelectorAll('input[name="to"]');
let searchTrainsButton = document.querySelector('button[datatype="searchTrains"]');
let modalBody = document.querySelector('.errorModalBody');
let errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
    keyboard: false,
});
let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");

$(function() {
    let dateNow = Date.now();
    let minDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + new Date(dateNow).getFullYear();
    let startDate;
    let checkedDate = $('#checkedDate')[0];
    if (checkedDate !== undefined) {
        startDate = checkedDate.innerHTML;
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

    $('#departureDate').daterangepicker({
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
        let inputs = document.querySelectorAll('input[name="departureDate"]');
        let date = $('.drp-selected')[0].innerHTML.split("-")[0];
        if (date.length === 0) {
            date = new Date(startDate.split(".").reverse().join("-")).toLocaleDateString('uk-UA', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
            });
        }
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].id === 'departureDate') {
                continue;
            }
            inputs[i].setAttribute("value", date);
        }
        if (currentLocale === "en") {
            searchTrainsButton.innerHTML = "Search trains at " + date;
        } else {
            searchTrainsButton.innerHTML = "Пошук поїздів на " + date;
        }
    }

    addDate();

    $('.applyBtn')[0].addEventListener("click", () => {
        addDate();
    });

});

inputFrom.addEventListener("change", e => {
    let input = e.currentTarget;
    let datalist = input.getAttribute('list');
    for (let i = 0; i < inputsFrom.length; i++) {
        changeAttribute(input.value, inputsFrom[i], datalist);
    }
});

inputTo.addEventListener("change", e => {
    let input = e.currentTarget;
    let datalist = input.getAttribute('list');
    for (let i = 0; i < inputsTo.length; i++) {
        changeAttribute(input.value, inputsTo[i], datalist);
    }
});

searchTrainsButton.addEventListener("click", ev => {
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
    let datalist = document.querySelector('#stationsDatalistOptions').id;
    for (let i = 0; i < inputsFrom.length; i++) {
        changeAttribute(inputFrom.value, inputsFrom[i], datalist);
    }
    inputTo.value = value;
    for (let i = 0; i < inputsTo.length; i++) {
        changeAttribute(inputTo.value, inputsTo[i], datalist);
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

function changeAttribute(inputValue, input, datalist) {
    if (inputValue === "") {
        input.removeAttribute("value");
    } else {
        let option = document.querySelector('datalist[id="' + datalist +'"]').querySelector('option[value="' + inputValue + '"]');
        if (option === null) {
            input.removeAttribute("value");
        } else {
            input.setAttribute("value", option.getAttribute('id'));
        }
    }
}