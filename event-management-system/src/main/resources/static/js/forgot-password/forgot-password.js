document.addEventListener('DOMContentLoaded', () => {

    // ── OTP box logic ──────────────────────────────────────────
    const otpInputs = document.querySelectorAll('.otp-input');
    const hiddenOtp = document.getElementById('otpInput');

    function syncHidden() {
        if (hiddenOtp) {
            hiddenOtp.value = [...otpInputs].map(i => i.value).join('');
        }
    }

    otpInputs.forEach((input, index) => {

        // Typing digit-by-digit
        input.addEventListener('input', () => {
            input.value = input.value.replace(/[^0-9]/g, '').slice(-1); // keep only last digit
            input.classList.toggle('filled', !!input.value);
            if (input.value && index < otpInputs.length - 1) {
                otpInputs[index + 1].focus();
            }
            syncHidden();
        });

        // Backspace to go back
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !input.value && index > 0) {
                otpInputs[index - 1].value = '';
                otpInputs[index - 1].classList.remove('filled');
                otpInputs[index - 1].focus();
                syncHidden();
            }
        });

        // Paste handling — distributes digits across boxes
        input.addEventListener('paste', (e) => {
            e.preventDefault();
            const pasted = e.clipboardData.getData('text').replace(/[^0-9]/g, '');
            if (!pasted) return;

            pasted.split('').forEach((digit, i) => {
                const target = otpInputs[index + i];
                if (target) {
                    target.value = digit;
                    target.classList.add('filled');
                }
            });

            // Focus the box after the last pasted digit (or last box)
            const nextFocus = Math.min(index + pasted.length, otpInputs.length - 1);
            otpInputs[nextFocus].focus();
            syncHidden();
        });
    });

    // Also sync before the form actually submits (safety net)
    const otpForm = document.getElementById('panel-2');
    if (otpForm) {
        otpForm.addEventListener('submit', (e) => {
            syncHidden();
            if (hiddenOtp && hiddenOtp.value.length !== 6) {
                e.preventDefault();
                // optionally show an error
            }
        });
    }

    // ── Password strength ──────────────────────────────────────
    const pwd = document.getElementById('new-pwd');
    if (pwd) {
        pwd.addEventListener('input', () => checkStrength(pwd.value));
    }
});

function checkStrength(val) {
    const fill = document.getElementById('strength-fill');
    const text = document.getElementById('strength-text');
    if (!fill || !text) return;

    let score = 0;
    if (val.length >= 8)           score++;
    if (/[A-Z]/.test(val))         score++;
    if (/[0-9]/.test(val))         score++;
    if (/[^A-Za-z0-9]/.test(val))  score++;

    const levels = ['Weak', 'Fair', 'Good', 'Strong'];
    const colors = ['#e53935', '#fb8c00', '#43a047', '#1e88e5'];
    const widths = ['25%', '50%', '75%', '100%'];

    if (val.length === 0) {
        fill.style.width = '0';
        text.innerText = '';
    } else {
        fill.style.width   = widths[score - 1]  || '25%';
        fill.style.background = colors[score - 1] || '#e53935';
        text.innerText     = levels[score - 1]  || 'Weak';
    }
}

function togglePassword(id, btn) {
    const input = document.getElementById(id);
    const icon  = btn.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.replace('bi-eye', 'bi-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.replace('bi-eye-slash', 'bi-eye');
    }
}