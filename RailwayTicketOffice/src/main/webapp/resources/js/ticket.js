let ticketCost = document.querySelector("#ticketCost");
let totalCost = document.querySelector("#totalCost");
let select = document.querySelector("#ticketCounter");
let confirmButton = document.querySelector("#cancelModal").querySelector(".modal-footer").firstElementChild;
let currentPassenger;

select.addEventListener("change", evt => {
    let select = evt.currentTarget;
    let count = select.value;
    let passengers = document.querySelectorAll('div[datatype="passenger"]');
    if (count > passengers.length) {
        let difference = count - passengers.length
        for (let i = 0; i < difference; i++) {
            let passengerNumber = +passengers[passengers.length - 1].firstElementChild.innerHTML.split(" ")[1] + 1 + i;
            let clone = passengers[0].cloneNode(true);
            clone.firstElementChild.innerHTML = "Пасажир " + (passengerNumber);
            clone.querySelector('label[for="passengerSurname"]').setAttribute("for", "passengerSurname" + passengerNumber);
            let passengerSurnameInput = clone.querySelector('input[id="passengerSurname"]');
            passengerSurnameInput.setAttribute("id", "passengerSurname" + passengerNumber);
            passengerSurnameInput.setAttribute("value", "");
            clone.querySelector('label[for="passengerName"]').setAttribute("for", "passengerName" + passengerNumber);
            let passengerNameInput = clone.querySelector('input[id="passengerName"]');
            passengerNameInput.setAttribute("id", "passengerName" + passengerNumber);
            passengerNameInput.setAttribute("value", "");
            let html = "<div type=\"button\" class=\"col-1 offset-2 btn btn-sm btn-primary align-self-center\" data-bs-target=\"#cancelModal\" data-bs-toggle=\"modal\">Скасувати</div>";
            clone.lastElementChild.insertAdjacentHTML("beforeend", html);
            clone.lastElementChild.addEventListener("click", () => {
                currentPassenger = clone;
            });
            totalCost.parentElement.insertBefore(clone, totalCost);
        }
    } else {
        for (let i = passengers.length - 1; i >= count; i--) {
            passengers[i].remove();
        }
    }
    totalCost.innerHTML = "Загальна вартість: " + (ticketCost.innerHTML.replace(",", ".") * count).toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
});

confirmButton.addEventListener("click", () => {
    confirmButton.nextElementSibling.click();
    currentPassenger.remove();
    select.value = --select.value;
    totalCost.innerHTML = "Загальна вартість: " + (ticketCost.innerHTML.replace(",", ".") * select.value).toLocaleString("uk-UA", {minimumFractionDigits: 2}) + " грн.";
});