/* ===== dashboard.js ===== */

document.addEventListener("DOMContentLoaded", function () {

    /* ── Tab switching ── */
    const tabBtns   = document.querySelectorAll(".tab-btn");
    const tabPanels = document.querySelectorAll(".tab-panel");

    tabBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            const target = btn.dataset.tab;

            // Buttons
            tabBtns.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            // Panels
            tabPanels.forEach(p => p.classList.remove("active"));
            const panel = document.getElementById("tab-" + target);
            if (panel) panel.classList.add("active");
        });
    });

    /* ── Navbar dropdown ── */
    const navUser = document.getElementById("navUser");
    if (navUser) {
        navUser.addEventListener("click", function (e) {
            e.stopPropagation();
            this.classList.toggle("open");
        });
        document.addEventListener("click", () => {
            navUser.classList.remove("open");
        });
    }

    /* ── Auto-open tab from URL hash ── */
    const hash = window.location.hash.replace("#", "");
    if (hash) {
        const targetBtn = document.querySelector(
            `.tab-btn[data-tab="${hash}"]`
        );
        if (targetBtn) targetBtn.click();
    }
});