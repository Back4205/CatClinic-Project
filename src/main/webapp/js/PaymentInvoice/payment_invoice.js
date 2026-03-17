document.addEventListener('DOMContentLoaded', function() {
    const methodBtns = document.querySelectorAll('.method-btn');
    const hiddenInput = document.getElementById('selectedMethod');

    methodBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            methodBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            const value = this.getAttribute('data-value');
            hiddenInput.value = value;
        });
    });
});