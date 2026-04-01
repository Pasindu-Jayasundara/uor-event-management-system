document.addEventListener("DOMContentLoaded", () => {

    loadRememberMe();

    const rememberCheckbox = document.getElementById("checkDefault");

    rememberCheckbox.addEventListener("change", () => {

        if (rememberCheckbox.checked) {

            let email = document.getElementById("email").value;
            let password = document.getElementById("pwd").value;

            const expiryDate = new Date().getTime() + (30 * 24 * 60 * 60 * 1000);

            localStorage.setItem("rememberMe", "true");
            localStorage.setItem("email", email);
            localStorage.setItem("password", password);
            localStorage.setItem("expiry", expiryDate);

        } else {
            clearRememberMe();
        }

    });

});

function loadRememberMe() {

    const remember = localStorage.getItem("rememberMe");
    const expiry = localStorage.getItem("expiry");

    if (remember === "true" && expiry) {

        const now = new Date().getTime();

        if (now > expiry) {
            clearRememberMe();
            return;
        }

        document.getElementById("checkDefault").checked = true;

        let email = localStorage.getItem("email");
        let password = localStorage.getItem("password");

        if (email) {
            document.getElementById("email").value = email;
        }

        if (password) {
            document.getElementById("pwd").value = password;
        }
    }
}

function clearRememberMe() {
    localStorage.removeItem("rememberMe");
    localStorage.removeItem("email");
    localStorage.removeItem("password");
    localStorage.removeItem("expiry");
}