
function highlightSlot(radioInput) {

    document.querySelectorAll('.slot').forEach(slot => {
        slot.classList.remove('active-slot');
    });


    if (radioInput && radioInput.checked) {
        const slotLabel = radioInput.closest('.slot');
        if (slotLabel) {
            slotLabel.classList.add('active-slot');
        }
    }
}


function submitForm() {
    document.body.classList.add('submitting');
    document.getElementById('bookingForm').submit();
}

function updateEndDateMin() {
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');

    if (!startDate || !endDate) return;

    endDate.min = startDate.value;

    if (endDate.value && endDate.value < startDate.value) {
        endDate.value = startDate.value;
    }
}


function filterFutureTime() {
    if (!isBoardingMode && !isCheckupMode) return;

    const dateInput = document.getElementById('startDate');
    const timeSelect = document.getElementById('checkInTime');

    if (!dateInput || !timeSelect) return;

    const selectedDate = dateInput.value;
    if (!selectedDate) return;

    const now = new Date();
    const today =
        now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0');

    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();

    Array.from(timeSelect.options).forEach(option => {
        if (!option.value) return;

        const hour = parseInt(option.value.split(':')[0]);

        if (selectedDate === today) {
            if (hour < currentHour || (hour === currentHour && currentMinute > 0)) {
                option.style.display = 'none';
            } else {
                option.style.display = 'block';
            }
        } else {
            option.style.display = 'block';
        }
    });

    const selected = timeSelect.selectedOptions[0];
    if (selected && selected.style.display === 'none') {
        timeSelect.value = "";
    }
}


function calculatePrice() {
    const sel = document.getElementById('serviceSelect');
    const area = document.getElementById('costSummaryArea');

    if (!sel || !sel.value) {
        if (area) area.style.display = 'none';
        return;
    }

    const price = parseFloat(sel.selectedOptions[0].dataset.price) || 0;
    let total = price;

    if (isBoardingMode) {
        const start = document.getElementById('startDate')?.value;
        const end = document.getElementById('endDate')?.value;

        if (start && end) {
            const startDate = new Date(start);
            const endDate = new Date(end);
            const days = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;

            if (days > 0) total = price * days;
        }
    }

    if (!area) return;

    area.style.display = 'block';
    document.getElementById('priceBaseDisplay').textContent = price.toLocaleString('vi-VN') + " VND";
    document.getElementById('totalDisplay').textContent = total.toLocaleString('vi-VN') + " VND";
    document.getElementById('depositDisplay').textContent = (total * 0.2).toLocaleString('vi-VN') + " VND";
}


function setSlotDate() {
    const checkedSlot = document.querySelector('input[name="slotID"]:checked');

    if (checkedSlot) {
        const slotDate = checkedSlot.dataset.slotDate;
        const vetID = checkedSlot.dataset.vetId;

        document.getElementById('slotDate').value = slotDate;
        document.getElementById('vetID').value = vetID;

        console.log(' Slot Date set to:', slotDate);
        console.log(' Vet ID set to:', vetID);
    }
}


function updateConfirmButton() {
    const btn = document.getElementById('confirmBtn');
    if (!btn) return;

    let time = "";
    let formattedDate = "";

    if (isBoardingMode || isCheckupMode) {
        const date = document.getElementById('startDate')?.value;
        const timeSelect = document.getElementById('checkInTime')?.value;

        if (date && timeSelect) {
            const parts = date.split("-");
            formattedDate = parts[2] + "/" + parts[1] + "/" + parts[0];
            time = timeSelect.substring(0, 5);
        }
    } else if (needsVetMode) {
        const sel = document.querySelector('input[name="slotID"]:checked');

        if (sel) {
            formattedDate = sel.dataset.date;
            time = sel.dataset.time;
        }
    }

    if (time && formattedDate) {
        btn.innerHTML = '<i class="bi bi-check-circle-fill"></i> Confirm: ' + time + ' - ' + formattedDate;
        btn.classList.add('ready');
    } else {
        btn.innerHTML = 'Confirm Appointment';
        btn.classList.remove('ready');
    }
}


document.addEventListener('DOMContentLoaded', function () {
    calculatePrice();
    updateConfirmButton();
    filterFutureTime();

    if (isBoardingMode) {
        updateEndDateMin();
    }

    document.querySelectorAll('.slot').forEach(el => {
        el.addEventListener('click', function () {
            const radio = this.querySelector('input');

            if (radio) {
                radio.checked = true;

                highlightSlot(radio);

                setSlotDate();
                updateConfirmButton();
            }
        });
    });

    const checkedSlot = document.querySelector('input[name="slotID"]:checked');

    if (checkedSlot) {
        highlightSlot(checkedSlot);
        setSlotDate();
    }
});