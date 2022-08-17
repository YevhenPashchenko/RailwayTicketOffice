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
    let button = $('button[datatype="searchTrains"]')[0];
    let inputFromDatalist = $('#fromDatalist')[0];
    let inputsFrom = document.querySelectorAll('input[name="from"]');
    let inputToDatalist = $('#toDatalist')[0];
    let inputsTo = document.querySelectorAll('input[name="to"]');
    let modalBody = $('.errorModalBody')[0];
    let errorModal = new bootstrap.Modal($('#errorModal')[0], {
        keyboard: false,
    });
    let images = $('.changePasswordVisible');
    let uaLocale = {
        "format": "DD.MM.YYYY",
        "separator": "-",
        "applyLabel": "Готово",
        "cancelLabel": "Відміна",
        "fromLabel": "З",
        "toLabel": "До",
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

    $('#datePicker').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "minYear": minDate.split(".")[2],
        "maxYear": maxDate.split(".")[2],
        "locale": uaLocale,
        "startDate": startDate,
        "endDate": maxDate,
        "minDate": minDate,
        "maxDate": maxDate,
        "opens": "right",
        "linkedCalendars": false
    });

    function addDate() {
        let date = $('.drp-selected')[0].innerHTML.split("-")[0];
        if (date.length === 0) {
            date = new Date(startDate.split(".").reverse().join("-")).toLocaleDateString('ua-UK', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
            });
        }
        $('input[name="datePicker"]')[0].setAttribute("value", date);
        button.innerHTML = "Пошук поїздів на " + date;
    }

    addDate();

    $('.applyBtn')[0].addEventListener("click", () => {
        addDate();
    });

    function changeAttribute(inputValue, input) {
        if (inputValue === "") {
            input.removeAttribute("value");
        } else {
            let options = $('option[value=' + inputValue + ']');
            if (options[0] === undefined) {
                input.removeAttribute("value");
            } else {
                input.setAttribute("value", options[0].getAttribute('id'));
            }
        }
    }

    inputFromDatalist.addEventListener("change", e => {
        let inputValue = e.currentTarget.value;
        for (let i = 0; i < inputsFrom.length; i++) {
            changeAttribute(inputValue, inputsFrom[i]);
        }
    });

    inputToDatalist.addEventListener("change", e => {
        let inputValue = e.currentTarget.value;
        for (let i = 0; i < inputsTo.length; i++) {
            changeAttribute(inputValue, inputsTo[i]);
        }
    });

    button.addEventListener("click", ev => {
        if (!inputsFrom[0].hasAttribute("value")) {
            ev.preventDefault();
            modalBody.innerHTML = "Введіть пункт відправлення";
            errorModal.show();
            return;
        }
        if (!inputsTo[0].hasAttribute("value")) {
            ev.preventDefault();
            modalBody.innerHTML = "Введіть пункт призначення";
            errorModal.show();
        }
        if (inputsFrom[0].value === inputsTo[0].value) {
            ev.preventDefault();
            modalBody.innerHTML = "Станції відправлення та призначення співпадають";
            errorModal.show();
        }
    });

    $('.reverseRoute')[0].addEventListener("click", () => {
        let value = inputFromDatalist.value;
        inputFromDatalist.value = inputToDatalist.value;
        for (let i = 0; i < inputsFrom.length; i++) {
            changeAttribute(inputFromDatalist.value, inputsFrom[i]);
        }
        inputToDatalist.value = value;
        for (let i = 0; i < inputsTo.length; i++) {
            changeAttribute(inputToDatalist.value, inputsTo[i]);
        }
    });

    for (let i = 0; i < 2; i++) {
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

});