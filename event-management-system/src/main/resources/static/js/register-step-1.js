document.addEventListener('DOMContentLoaded', () => {

    passwordMatch();
    nextBtnBasedOnAccountType();

});

function passwordMatch() {

    const pwd1 = document.getElementById('pwd1');
    const pwd2 = document.getElementById('pwd2');

    pwd1.addEventListener('input', ()=>{
        checkPasswordMatch(pwd1,pwd2)
    });
    pwd2.addEventListener('input', ()=>{
        checkPasswordMatch(pwd1,pwd2)
    });

}

function checkPasswordMatch(pwd1, pwd2) {

    const val1 = pwd1.value;
    const val2 = pwd2.value;

    const message = document.getElementById('passwordMessage');

    if (!val1 && !val2) {
        message.textContent = "";
        return;
    }

    console.log(val1,val2);

    if (val1 === val2) {
        message.textContent = "Password Match";
        message.className = "text-success fs-6";

    } else {
        message.textContent = "Password Do Not Match";
        message.className = "text-danger fs-6";
    }
}

function nextBtnBasedOnAccountType() {

    const accountTypeSelect = document.getElementById('accountType');

    const continueBtn = document.getElementById('continueBtn');
    const registerBtn = document.getElementById('registerBtn');

    accountTypeSelect.addEventListener('change', function () {

        if (accountTypeSelect.value == "1") {
            registerBtn.style.display = "none";
            continueBtn.style.display = "block";
        } else if (accountTypeSelect.value == "2") {
            continueBtn.style.display = "none";
            registerBtn.style.display = "block";
        }
    });
}