document.addEventListener('DOMContentLoaded',function () {

    //Grid and Table toggle
    const tableView = document.getElementById('tableView');
    const gridView  = document.getElementById('gridView');
    const btnTable = document.getElementById('btnTable');
    const btnGrid = document.getElementById('btnGrid');

    if(btnTable && btnGrid){
        btnTable.addEventListener('click',function () {
            tableView.style.display = 'block';
            gridView.style.display = 'none';
            btnTable.classList.add('active');
            btnGrid.classList.remove('active');
        });

        btnGrid.addEventListener('click',function () {
            tableView.style.display = 'none';
            gridView.style.display = 'block';
            btnTable.classList.remove('active');
            btnGrid.classList.add('active');
        });
    }

    //Search
    const searchInput = document.getElementById('searchInput');
    let searchTimer;

    if(searchInput){
        searchInput.addEventListener('keydown',function(e){
            if(e.key === 'Enter'){
                e.preventDefault();
                doSearch(this.value.trim())};
        });
        searchInput.addEventListener('input',function(e){
            clearTimeout(searchTimer);
            const val = this.value.trim();
            searchTimer = setTimeout(() => doSearch(val), 500);
        });
    }

    function doSearch(keyword) {
        const url = new URL(window.location.href);
        keyword ? url.searchParams.set('keyword', keyword) : url.searchParams.delete('search');
        url.searchParams.delete('category');
        window.location.href = url.toString();
    }
})