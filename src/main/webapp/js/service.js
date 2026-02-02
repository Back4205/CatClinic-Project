function loadService(page = 1) {
    const name = document.getElementById("searchInput").value;
    const status = document.getElementById("statusFilter").value;
    const sortPrice = document.getElementById("sortPrice").value;
    const context = document.body.dataset.context;

    let url = context + "/SearchService?"
        + "nameService=" + encodeURIComponent(name)
        + "&status=" + status
        + "&sortPrice=" + sortPrice
        + "&page=" + page;

    fetch(url)
        .then(res => {
            if (!res.ok) throw new Error("Network error");
            return res.text();
        })
        .then(html => {
            document.getElementById("tableArea").innerHTML = html;
        })
        .catch(err => console.error(err));
}

function goPage(p) {
    loadService(p);
}
