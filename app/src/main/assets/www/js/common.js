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
    if (url == "" || url == "listOverview.html") {
        loadLists();
    }
}

function finishPage(url) {
    if (url == "listOverview.html") {
        setTimeout(function() {
            prepareButtons();
        }, 500);
    }
}