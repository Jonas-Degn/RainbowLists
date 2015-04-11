$(document).on("pagecreate", function(event,ui) {
    $(".bot_curve").on("tap", function() {
        if (parseInt($(".bottom_piece").css("buttom").replace("px","")) > 0) {
            $(".bottom_piece").animate({bottom: "0"});
        }
        else {
            $(".bottom_piece").animate({bottom: "4em"});
        }
    });
});


function loadLists() {
    var $content = $(".main_area");
    var $shopping = $("#shopping");
    var $pantry = $("#pantry");
    var shoppingData = window.JSInterface.loadLists("shopping").split(";");
    var pantryData = window.JSInterface.loadLists("pantry").split(";");

    $content.html('<div id="shopping">Shopping lists</div><div id="pantry">Pantry lists</div>');
    for(i = 0; i < shoppingData.length-1; i++) {
        var dataFields = data.split(",");
        var id = dataFields[0];
        var name = dataFields[1];
        $shopping.after('<div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }
    for(i = 0; i < pantryData.length-1; i++) {
        var dataFields = data.split(",");
        var id = dataFields[0];
        var name = dataFields[1];
        $pantry.after('<div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }
}