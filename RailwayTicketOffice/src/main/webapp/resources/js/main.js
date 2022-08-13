$(function() {
    let dateNow = Date.now();
    let minDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + new Date(dateNow).getFullYear();
    let maxDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + (new Date(dateNow).getFullYear() + 1);
    let inputs = $('input[name="datePicker"]');
    let button = $('button[datatype="searchTrains"]')[0];
    let inputFromDatalist = $('#fromDatalist')[0];
    let inputFrom = $('input[name="from"]')[0];
    let inputToDatalist = $('#toDatalist')[0];
    let inputTo = $('input[name="to"]')[0];
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

    inputs.daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "minYear": minDate.split(".")[2],
        "maxYear": maxDate.split(".")[2],
        "locale": uaLocale,
        "startDate": minDate,
        "endDate": maxDate,
        "minDate": minDate,
        "maxDate": maxDate,
        "opens": "right",
        "linkedCalendars": false
    });

    function addDate() {
        let date = $('.drp-selected')[0].innerHTML.split("-")[0];
        if (date.length === 0) {
            date = inputs[0].value;
        }
        button.innerHTML = "Пошук поїздів на " + date;
    }

    addDate();

    $('.applyBtn')[0].addEventListener("click", ev => {
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
        changeAttribute(inputValue, inputFrom);
    });

    inputToDatalist.addEventListener("change", e => {
        let inputValue = e.currentTarget.value;
        changeAttribute(inputValue, inputTo);
    });

    button.addEventListener("click", ev => {
        if (!inputFrom.hasAttribute("value")) {
            ev.preventDefault();
            alert("Введіть пункт відправлення");
            return;
        }
        if (!inputTo.hasAttribute("value")) {
            ev.preventDefault();
            alert("Введіть пункт призначення");
        }
        if (inputFrom.value === inputTo.value) {
            ev.preventDefault();
            alert("Станції відправлення та призначення співпадають");
        }
    });

    $('img[role="button"]')[0].addEventListener("click", e => {
        let value = inputFromDatalist.value;
        inputFromDatalist.value = inputToDatalist.value;
        changeAttribute(inputFromDatalist.value, inputFrom);
        inputToDatalist.value = value;
        changeAttribute(inputToDatalist.value, inputTo);
    });
});