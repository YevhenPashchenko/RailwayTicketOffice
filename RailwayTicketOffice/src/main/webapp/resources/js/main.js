$(function() {
    let dateNow = Date.now();
    let minDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + new Date(dateNow).getFullYear();
    let maxDate = new Date(dateNow).getDate() + "." + (new Date(dateNow).getMonth() + 1) + "." + (new Date(dateNow).getFullYear() + 1);
    let inputs = $('input[name="datePicker"]');
    let button = $('button[name="searchTrains"]')[0];
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

    $('.applyBtn')[0].addEventListener("click", ev => {
        addDate();
    });

    button.addEventListener("click", ev => {
        let array = ["from", "to"];
        for (const arrayKey in array) {
            let input = $('input[name=' + array[arrayKey] + ']')[0];
            let inputValue = input.value;
            input.value = $('option[value=' + inputValue + ']')[0].getAttribute('id');
        }
    });

    addDate();

});