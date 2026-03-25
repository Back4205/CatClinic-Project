function submitForm() {
    document.getElementById('actionField').value = 'load';
    document.getElementById('bookingForm').submit();
}


function setSlotDate() {
    const r = document.querySelector('input[name="slotID"]:checked');
    if (r) document.getElementById('slotDate').value = r.getAttribute('data-slot-date');
}

function highlightSlot(radio) {
    document.querySelectorAll('.slot-pill').forEach(p => p.classList.remove('active-slot'));
    if (radio && radio.checked) {
        const lbl = radio.closest('.slot-pill');
        if (lbl) lbl.classList.add('active-slot');
    }
}

function selectSlot(labelEl, slotID, slotDate) {
    const radio = labelEl.querySelector('input[type="radio"]');
    if (radio) {
        radio.checked = true;
        highlightSlot(radio);
        setSlotDate();
        updateConfirmButton();
    }
}

function changePage(page) {
    const params = new URLSearchParams();
    for (const [k, v] of new FormData(document.getElementById('bookingForm')).entries()) {
        if (v && k !== 'action') params.append(k, v);
    }
    params.set('page', page);
    window.location.href = contextPath + '/Booking2?' + params.toString();
}

function updateEndDateMin() {
    const s = document.getElementById('startDate');
    const e = document.getElementById('endDate');
    if (!s || !e) return;
    e.min = s.value;
    if (e.value && e.value < s.value) e.value = s.value;
}

function filterFutureTime() {
    if (!isBoardingMode && !isParaclinicalMode) return;
    const d = document.getElementById('startDate');
    const t = document.getElementById('checkInTime');
    if (!d || !t || !d.value) return;

    const now   = new Date();
    const today = now.toISOString().slice(0, 10);
    const curH  = now.getHours();
    const curM  = now.getMinutes();

    Array.from(t.options).forEach(opt => {
        if (!opt.value) return;
        const h = parseInt(opt.value);
        opt.style.display = (d.value === today && (h < curH || (h === curH && curM > 0))) ? 'none' : 'block';
    });

    const sel = t.selectedOptions[0];
    if (sel && sel.style.display === 'none') t.value = '';
}

function calculatePrice() {
    const sel  = document.getElementById('serviceSelect');
    const area = document.getElementById('costSummaryArea');
    if (!sel || !sel.value) {
        if (area) area.style.display = 'none';
        return;
    }

    const price = parseFloat(sel.selectedOptions[0].dataset.price) || 0;
    let total   = price;

    if (isBoardingMode) {
        const s = document.getElementById('startDate')?.value;
        const e = document.getElementById('endDate')?.value;
        if (s && e) {
            const days = Math.ceil((new Date(e) - new Date(s)) / 86400000) + 1;
            if (days > 0) total = price * days;
        }
    }

    if (!area) return;
    area.style.display = 'block';
    document.getElementById('priceBaseDisplay').textContent = price.toLocaleString('vi-VN') + ' VND';
    document.getElementById('totalDisplay').textContent     = total.toLocaleString('vi-VN') + ' VND';
}

function updateConfirmButton() {
    const btn = document.getElementById('confirmBtn');
    if (!btn) return;

    let time = '', fDate = '';
    if (isBoardingMode || isParaclinicalMode) {
        const d = document.getElementById('startDate')?.value;
        const t = document.getElementById('checkInTime')?.value;
        if (d && t) {
            const p = d.split('-');
            fDate = p[2] + '/' + p[1] + '/' + p[0];
            time  = t.substring(0, 5);
        }
    } else if (needsVetMode) {
        const r = document.querySelector('input[name="slotID"]:checked');
        if (r) {
            fDate = r.dataset.date;
            time = r.dataset.time;
        }
    }

    if (time && fDate) {
        btn.innerHTML = '<i class="fa-solid fa-circle-check"></i> Confirm: ' + time + ' — ' + fDate;
    } else {
        btn.innerHTML = '<i class="fa-solid fa-calendar-check"></i> Booking Now';
    }
}


document.addEventListener('DOMContentLoaded', function () {
    calculatePrice();
    updateConfirmButton();
    filterFutureTime();
    if (isBoardingMode) updateEndDateMin();

    const checked = document.querySelector('input[name="slotID"]:checked');
    if (checked) {
        highlightSlot(checked);
        setSlotDate();
        updateConfirmButton();
    }

    const phone = document.getElementById('ownerPhone');
    if (phone) {
        phone.addEventListener('blur', function () {
            if (this.value.trim()) submitForm();
        });
    }
});