function loadLists() {
    var $content = $(".main_area");
    var $shopping = $("#shopping");
    var $pantry = $("#pantry");
    var shoppingData = window.JSInterface.loadLists("shopping");
    var pantryData = window.JSInterface.loadLists("pantry");

    $content.html('<div id="shopping">Shopping lists</div><div id="pantry">Pantry lists</div>');

    if (shoppingData.length > 0) {
        shoppingData = shoppingData.split(";");
    }
    else {
        $shopping.after('<div>No shopping lists</div>');
    }

    if (pantryData.length > 0) {
        pantryData = pantryData.split(";");
    }
    else {
        $pantry.after('<div>No shopping lists</div>');
    }

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

function prepareButtons() {
    $(".left_button").on("tap", function() {
        window.JSInterface.messageDialog("You pressed something","You pressed LEFT button!!!");
    });
    $(".right_button").on("tap", function() {
        window.JSInterface.messageDialog("You pressed something","You pressed RIGHT button!!!");
    });

    $(".bot_curve").on("tap swipeup", function() {
        if (parseInt($(".bottom_piece").css("bottom").replace("px","")) > 0) {
            $(".bottom_piece").animate({bottom: "0"});
        }
        else {
            $(".bottom_piece").animate({bottom: "10em"});
        }
        $(document).on("tap swipedown", function(e) {
            if (!$(".left_button").is(e.target) && $(".left_button").has(e.target).length === 0 && !$(".right_button").is(e.target) && $(".right_button").has(e.target).length === 0) {
                $(".bottom_piece").animate({bottom: "0"});
                $(document).unbind("tap");
            }
        });
    });
}