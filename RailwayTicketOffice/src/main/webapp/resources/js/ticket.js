let currentLocale = document.querySelector('input[type="image"]').getAttribute("alt");
let cancelButtons = document.querySelectorAll('a[type="cancel"]');
let totalCost = document.querySelector('#totalCost');
let deleteButton = document.querySelector('.modal-footer').firstElementChild;
let cancelModal = new bootstrap.Modal(document.querySelector('#cancelModal'), {
    keyboard: false,
});
let buyTicketButton = document.querySelector('#buyTicket');

for (let i = 0; i < cancelButtons.length; i++) {
    cancelButtons[i].addEventListener("click", evt => {
        let button = evt.currentTarget;
        let container = button.parentElement.parentElement.parentElement;
        container.id = "forDelete";
        cancelModal.show();
    })
}

deleteButton.addEventListener("click", evt => {
    let deletingContainer = document.querySelector('#forDelete');
    deletingContainer.remove();
    getTotalCost();
    evt.currentTarget.nextElementSibling.click();
});

buyTicketButton.addEventListener("click", evt => {
    let inputs = document.querySelectorAll('input');
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].removeAttribute("disabled");
    }
});

function getTotalCost() {
    let sum = 0;
    let costInputs = document.querySelectorAll('input[name="cost"]');
    for (let i = 0; i < costInputs.length; i++) {
        sum += Number.parseFloat(costInputs[i].value);
    }
    let currentTotalCost = totalCost.innerHTML.split(" ");
    if (currentLocale === "en") {
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + sum.toLocaleString("en-EN", {minimumFractionDigits: 2}) + " UAH";
    } else {
        totalCost.innerHTML = currentTotalCost[0] + " " + currentTotalCost[1] + " " + sum.toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
    }
    if (sum === 0) {
        totalCost.nextElementSibling.querySelector('button').setAttribute("disabled", "");
    }
}

getTotalCost();

