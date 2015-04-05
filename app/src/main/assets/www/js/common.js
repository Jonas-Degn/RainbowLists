$(document).bind('mobileinit',function(){
    /* Disable loading thing for now */
    $.mobile.loadingMessage = false;
})
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