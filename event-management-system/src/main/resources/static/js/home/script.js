

    const input = document.getElementById("searchInput");
    const suggestions = document.getElementById("suggestions");

    let currentIndex = -1;

    input.addEventListener("keyup", function (e) {
    let keyword = input.value;

    if (keyword.length < 2) {
    suggestions.innerHTML = "";
    return;
}

    fetch("/api/search?keyword=" + keyword)
    .then(res => res.json())
    .then(data => {
    suggestions.innerHTML = "";
    currentIndex = -1;

    data.forEach((event, index) => {
    let div = document.createElement("div");
    div.classList.add("suggest-item");

    // Highlight match
    let regex = new RegExp(`(${keyword})`, "gi");
    let title = event.title.replace(regex, "<b>$1</b>");

    div.innerHTML = `
                  ${title}<br>
                  <small>${event.eventDate}</small>
                `;

    div.onclick = () => {
    input.value = event.title;
    suggestions.innerHTML = "";
};

    suggestions.appendChild(div);
});
});

    // Keyboard navigation
    let items = document.querySelectorAll(".suggest-item");

    if (e.key === "ArrowDown") {
    currentIndex++;
    highlight(items);
} else if (e.key === "ArrowUp") {
    currentIndex--;
    highlight(items);
} else if (e.key === "Enter") {
    if (currentIndex > -1 && items[currentIndex]) {
    items[currentIndex].click();
}
}
});

    function highlight(items) {
    items.forEach(item => item.classList.remove("active"));

    if (currentIndex >= items.length) currentIndex = 0;
    if (currentIndex < 0) currentIndex = items.length - 1;

    if (items[currentIndex]) {
    items[currentIndex].classList.add("active");
}
}
