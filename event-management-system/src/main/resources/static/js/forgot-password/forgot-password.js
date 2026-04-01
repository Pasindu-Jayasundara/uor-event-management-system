let currentStep = 1;
let resendInterval = null;

function goToStep(step) {
    // Hide all panels
    document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
    document.getElementById('panel-' + step).classList.add('active');

    // Update step indicators
    for (let i = 1; i <= 3; i++) {
        const circle = document.getElementById('step-circle-' + i);
        const label = document.getElementById('step-label-' + i);
        circle.classList.remove('active', 'completed');
        label.classList.remove('active');

        if (i < step) {
            circle.classList.add('completed');
            circle.innerHTML = '<i class="bi bi-check"></i>';
        } else if (i === step) {
            circle.classList.add('active');
            circle.innerHTML = i;
            label.classList.add('active');
        } else {
            circle.innerHTML = i;
        }
    }

    // Update lines
    for (let i = 1; i <= 2; i++) {
        const line = document.getElementById('line-' + i);
        if (i < step) {
            line.classList.add('completed');
        } else {
            line.classList.remove('completed');
        }
    }

    currentStep = step;
}

function goToStep2() {
    const email = document.getElementById('email').value.trim();
    const emailError = document.getElementById('email-error');
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email || !emailRegex.test(email)) {
        emailError.classList.remove('d-none');
        document.getElementById('email').classList.add('is-invalid');
        return;
    }
    emailError.classList.add('d-none');
    document.getElementById('email').classList.remove('is-invalid');

    document.getElementById('display-email').textContent = email;
    goToStep(2);
    startResendTimer();
}

function goToStep3() {
    const inputs = document.querySelectorAll('.otp-input');
    let otp = '';
    inputs.forEach(inp => otp += inp.value);

    if (otp.length < 6) {
        document.getElementById('otp-error').classList.remove('d-none');
        return;
    }
    document.getElementById('otp-error').classList.add('d-none');
    goToStep(3);
}

function submitReset() {
    const newPwd = document.getElementById('new-pwd').value;
    const confirmPwd = document.getElementById('confirm-pwd').value;
    const matchError = document.getElementById('pwd-match-error');

    if (newPwd.length < 6) {
        document.getElementById('new-pwd').classList.add('is-invalid');
        return;
    }
    document.getElementById('new-pwd').classList.remove('is-invalid');

    if (newPwd !== confirmPwd) {
        matchError.classList.remove('d-none');
        document.getElementById('confirm-pwd').classList.add('is-invalid');
        return;
    }
    matchError.classList.add('d-none');
    document.getElementById('confirm-pwd').classList.remove('is-invalid');

    // Show success
    document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
    document.getElementById('panel-4').classList.add('active');

    // Update all steps to completed
    for (let i = 1; i <= 3; i++) {
        const circle = document.getElementById('step-circle-' + i);
        const label = document.getElementById('step-label-' + i);
        circle.classList.remove('active');
        circle.classList.add('completed');
        circle.innerHTML = '<i class="bi bi-check"></i>';
        label.classList.remove('active');
    }
    document.getElementById('line-1').classList.add('completed');
    document.getElementById('line-2').classList.add('completed');
}

function startResendTimer() {
    const btn = document.getElementById('resend-btn');
    const timerEl = document.getElementById('timer');
    let seconds = 60;

    btn.classList.add('disabled');
    timerEl.textContent = seconds;

    if (resendInterval) clearInterval(resendInterval);

    resendInterval = setInterval(() => {
        seconds--;
        timerEl.textContent = seconds;
        if (seconds <= 0) {
            clearInterval(resendInterval);
            btn.classList.remove('disabled');
            btn.innerHTML = 'Resend Code';
            btn.onclick = () => {
                btn.innerHTML = 'Resend in <span id="timer">60</span>s';
                startResendTimer();
            };
        }
    }, 1000);
}

function togglePassword(fieldId, iconEl) {
    const input = document.getElementById(fieldId);
    const icon = iconEl.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.replace('bi-eye', 'bi-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.replace('bi-eye-slash', 'bi-eye');
    }
}

function checkStrength(value) {
    const fill = document.getElementById('strength-fill');
    const text = document.getElementById('strength-text');
    let strength = 0;
    if (value.length >= 8) strength++;
    if (/[A-Z]/.test(value)) strength++;
    if (/[0-9]/.test(value)) strength++;
    if (/[^A-Za-z0-9]/.test(value)) strength++;

    const levels = [
        { width: '0%', color: '#dee2e6', label: '' },
        { width: '25%', color: '#dc3545', label: 'Weak' },
        { width: '50%', color: '#fd7e14', label: 'Fair' },
        { width: '75%', color: '#ffc107', label: 'Good' },
        { width: '100%', color: '#198754', label: 'Strong' },
    ];
    const level = value.length === 0 ? 0 : Math.max(1, strength);
    fill.style.width = levels[level].width;
    fill.style.backgroundColor = levels[level].color;
    text.textContent = levels[level].label;
    text.style.color = levels[level].color;
}

// OTP auto-advance
document.addEventListener('DOMContentLoaded', () => {
    const otpInputs = document.querySelectorAll('.otp-input');
    otpInputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            input.value = input.value.replace(/[^0-9]/g, '');
            if (input.value) {
                input.classList.add('filled');
                if (index < otpInputs.length - 1) otpInputs[index + 1].focus();
            } else {
                input.classList.remove('filled');
            }
        });
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !input.value && index > 0) {
                otpInputs[index - 1].focus();
                otpInputs[index - 1].classList.remove('filled');
            }
        });
    });
});
