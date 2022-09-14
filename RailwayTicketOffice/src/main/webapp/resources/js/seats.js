let tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
[...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");
let freeSeats = document.querySelectorAll('a[data-cost]');
let fieldSets = document.getElementsByTagName('fieldset');
let carriageNumber = document.querySelector('li.border');
let totalCost = document.querySelector('#totalCost');
let issueTicketsButton = document.querySelector('#issueTickets');
let modalBody = document.querySelector('.errorModalBody');
let errorModal = new bootstrap.Modal(document.querySelector('#errorModal'), {
    keyboard: false,
});

if (!fieldSets[0].hasAttribute("hidden")) {
    let sum = 0;
    for (let i = 0; i < fieldSets.length; i++) {
        if (fieldSets[i].querySelector('input[name="carriage"]').value === carriageNumber.firstElementChild.innerHTML) {
            for (let j = 0; j < freeSeats.length; j++) {
                if (freeSeats[j].innerHTML === fieldSets[i].querySelector('input[name="seat"]').value) {
                    freeSeats[j].classList.replace('bg-primary', 'bg-danger');
                }
            }
        }
    }
    for (let i = 0; i < fieldSets.length; i++) {
        sum += Number.parseFloat(fieldSets[i].querySelector('input[name="cost"]').value);
        fieldSets[i].querySelector('a').addEventListener("click", ev => {
            removeFieldset(ev);
        });
    }
    totalCost.removeAttribute("hidden");
    let currentTotalCost = totalCost.innerHTML.split(" ");
    if (currentLocale === "en") {
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + sum.toLocaleString("en-EN", {minimumFractionDigits: 2}) + " UAH";
    } else {
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + sum.toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
    }
}

for (let i = 0; i < freeSeats.length; i++) {
    freeSeats[i].style.cursor = 'pointer';
    freeSeats[i].addEventListener("click", evt => {
        let a = evt.currentTarget;
        let links = document.querySelectorAll('a');
        links = Array.from(links).filter(evt => (/carriageType/).test(evt.href));
        const style = getComputedStyle(a);
        if (style.backgroundColor === 'rgb(13, 110, 253)') {
            a.classList.replace('bg-primary', 'bg-danger');
            if (fieldSets[0].hasAttribute("hidden")) {
                if (currentLocale === "en") {
                    fieldSets[0].querySelector('legend').innerHTML = "Passenger " + 1;
                } else {
                    fieldSets[0].querySelector('legend').innerHTML = "Пасажир " + 1;
                }
                fieldSets[0].removeAttribute("hidden");
                fieldSets[0].querySelector('a').addEventListener("click", ev => {
                    removeFieldset(ev);
                });

                fieldSets[0].querySelector('input[name="carriage"]').setAttribute("value", carriageNumber.firstElementChild.innerHTML);
                fieldSets[0].querySelector('input[name="seat"]').setAttribute("value", a.innerHTML);
                fieldSets[0].querySelector('input[name="cost"]').setAttribute("value", a.getAttribute("data-cost").replace(",", "."));
            } else {
                let clone = fieldSets[fieldSets.length - 1].cloneNode(true);
                if (currentLocale === "en") {
                    clone.querySelector('legend').innerHTML = "Passenger " + (fieldSets.length + 1);
                } else {
                    clone.querySelector('legend').innerHTML = "Пасажир " + (fieldSets.length + 1);
                }
                clone.querySelector('a').addEventListener("click", ev => {
                    removeFieldset(ev);
                });
                clone.querySelector('input[name="carriage"]').setAttribute("value", carriageNumber.firstElementChild.innerHTML);
                clone.querySelector('input[name="seat"]').setAttribute("value", a.innerHTML);
                clone.querySelector('input[name="cost"]').setAttribute("value", a.getAttribute("data-cost").replace(",", "."));
                fieldSets[0].parentElement.insertAdjacentElement("beforeend", clone);
            }
            for (let j = 0; j < links.length; j++) {
                links[j].href = links[j].href + "&carriage=" + carriageNumber.firstElementChild.innerHTML + "&seat=" + a.innerHTML + "&cost=" + a.getAttribute("data-cost").replace(",", ".");
            }
            addCostToTotal(a.getAttribute("data-cost"));
        } else {
            a.classList.replace('bg-danger', 'bg-primary');
            if (fieldSets.length === 1) {
                fieldSets[0].setAttribute("hidden", "");
                fieldSets[0].querySelector('input[name="carriage"]').setAttribute("value", "");
                fieldSets[0].querySelector('input[name="seat"]').setAttribute("value", "");
                fieldSets[0].querySelector('input[name="cost"]').setAttribute("value", "");
            } else {
                for (let j = 0; j < fieldSets.length; j++) {
                    if (fieldSets[j].querySelector('input[name="seat"]').value === a.innerHTML) {
                        fieldSets[j].remove();
                    }
                }
                for (let j = 0; j < fieldSets.length; j++) {
                    if (currentLocale === "en") {
                        fieldSets[j].querySelector('legend').innerHTML = "Passenger " + (j + 1);
                    } else {
                        fieldSets[j].querySelector('legend').innerHTML = "Пасажир " + (j + 1);
                    }
                }
            }
            for (let j = 0; j < links.length; j++) {
                links[j].href = links[j].href.replaceAll("&carriage=" + carriageNumber.firstElementChild.innerHTML + "&seat=" + a.innerHTML + "&cost=" + a.getAttribute("data-cost").replace(",", "."), "");
            }
            minusCostFromTotal(a.getAttribute("data-cost"));
        }
    });
}

function addCostToTotal(cost) {
    cost = cost.replace(",", ".");
    let currentTotalCost = totalCost.innerHTML.split(" ");
    if (currentTotalCost[2] === "" || Number.parseFloat(currentTotalCost[2]) === 0) {
        totalCost.removeAttribute("hidden");
        if (currentLocale === "en") {
            totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + Number.parseFloat(cost).toLocaleString("en-EN", {minimumFractionDigits: 2}) + " UAH";
        } else {
            totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + Number.parseFloat(cost).toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
        }
    } else {
        if (currentLocale === "en") {
            currentTotalCost[2] = currentTotalCost[2].replace(",", "");
            totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + (Number.parseFloat(currentTotalCost[2]) + Number.parseFloat(cost)).toLocaleString("en-EN", {minimumFractionDigits: 2}) + " UAH";
        } else {
            currentTotalCost[2] = currentTotalCost[2].replace(/(&nbsp;)/, "");
            currentTotalCost[2] = currentTotalCost[2].replace(",", ".");
            totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + (Number.parseFloat(currentTotalCost[2]) + Number.parseFloat(cost)).toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
        }
    }
}

function minusCostFromTotal(cost) {
    cost = cost.replace(",", ".");
    let currentTotalCost = totalCost.innerHTML.split(" ");
    if (currentLocale === "en") {
        currentTotalCost[2] = currentTotalCost[2].replace(",", "");
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + (Number.parseFloat(currentTotalCost[2]) - Number.parseFloat(cost)).toLocaleString("en-EN", {minimumFractionDigits: 2}) + " UAH";
    } else {
        currentTotalCost[2] = currentTotalCost[2].replace(/(&nbsp;)/, "");
        currentTotalCost[2] = currentTotalCost[2].replace(",", ".");
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + (Number.parseFloat(currentTotalCost[2]) - Number.parseFloat(cost)).toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
    }
    if (Number.parseFloat(currentTotalCost[2]) - Number.parseFloat(cost) === 0) {
        totalCost.setAttribute("hidden", "");
    }
}

function removeFieldset(e) {
    let button = e.currentTarget;
    let fieldset = button.parentElement;
    let carriage = fieldset.querySelector('input[name="carriage"]').getAttribute("value");
    let seat = fieldset.querySelector('input[name="seat"]').getAttribute("value");
    let cost = fieldset.querySelector('input[name="cost"]').getAttribute("value");
    let links = document.querySelectorAll('a');
    links = Array.from(links).filter(evt => (/carriageType/).test(evt.href));
    for (let j = 0; j < links.length; j++) {
        links[j].href = links[j].href.replaceAll("&carriage=" + carriage + "&seat=" + seat + "&cost=" + cost, "");
    }
    minusCostFromTotal(cost);
    if (fieldSets.length === 1) {
        fieldset.setAttribute("hidden", "");
        fieldset.querySelector('input[name="carriage"]').setAttribute("value", "");
        fieldset.querySelector('input[name="seat"]').setAttribute("value", "");
        fieldset.querySelector('input[name="cost"]').setAttribute("value", "");
    } else {
        fieldset.remove();
    }
    if (carriage === carriageNumber.firstElementChild.innerHTML) {
        for (let i = 0; i < freeSeats.length; i++) {
            if (freeSeats[i].innerHTML === seat) {
                freeSeats[i].classList.replace('bg-danger', 'bg-primary');
            }
        }
    }
}

issueTicketsButton.addEventListener("click", evt => {
    if (fieldSets[0].hasAttribute("hidden")) {
        evt.preventDefault();
        if (currentLocale === "en") {
            modalBody.innerHTML = "No one seat selected";
        } else {
            modalBody.innerHTML = "Жодне місце не вибрано";
        }
        errorModal.show();
        return;
    }
    for (let i = 0; i < fieldSets.length; i++) {
        fieldSets[i].removeAttribute("disabled");
    }
});