$(document).ready(function() {
    /* Go to specific element */
    $.fn.goTo = function() {
        $('html, body').animate({
            scrollTop: $(this).offset().top + 'px',
            scrollLeft: $(this).offset().left + 'px'
        }, 'fast');
        return this;
    }
});

/* Get URL parameter values */
function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function setCurrentList(id) {
    window.JSInterface.setCurrentList(id);
}

function getCurrentList() {
    return window.JSInterface.getCurrentList();
}

function loadPage(url) {
    $.mobile.loading('show');
    $("#newContent").load(url+" #content", function() {
        preparePage(url);
        window.JSInterface.setLocation(url.split(".html")[0]);
        $("#content").html($("#newContent #content").html());
        $("#content").trigger("create");
        finishPage(url);
        $("#newContent").html('');
        $.mobile.loading('hide');
    });
}

function preparePage(url) {
}

function finishPage(url) {
    if (url == "listOverview.html") {
        setTimeout(function() {
            loadLists();
            prepareButtons();
        }, 500);
    }
}

function sortByFirstColumn(a, b) {
    if (a[0] === b[0]) {
        return 0;
    }
    else {
        return (a[0] < b[0]) ? -1 : 1;
    }
}

function sortReverseByFirstColumn(a, b) {
    if (a[0] === b[0]) {
        return 0;
    }
    else {
        return (a[0] > b[0]) ? -1 : 1;
    }
}