(function() {
    var dateNodes = document.querySelectorAll('#app-content article .date');

    for (var i = 0; i < dateNodes.length; i++) {
        var dateNode = dateNodes.item(i);
        var dateString = dateNode.innerHTML.replace("\[UTC\]", "");
        var date = new Date(Date.parse(dateString));

        var options = { weekday: "short", year: "numeric", month: "short", day: "numeric", hour12: false };
        dateNode.innerHTML = date.toLocaleString("en-US", options);
    }
})();
