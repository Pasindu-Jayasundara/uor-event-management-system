document.addEventListener("DOMContentLoaded", function () {

    const currentPath = window.location.pathname;

    document.querySelectorAll(".nav-link-custom").forEach(link => {
        const linkPath = new URL(link.href).pathname;

        // remove existing selected class
        link.classList.remove("selected");

        // exact match OR startsWith (for sub routes)
        if (currentPath === linkPath || currentPath.startsWith(linkPath)) {
            link.classList.add("selected");
        }
    });

});