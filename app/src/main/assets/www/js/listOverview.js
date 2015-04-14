function loadLists() {
    var $content = $(".main_area");
    $content.html('<div id="shopping" class="list_block">Shopping lists</div><div id="pantry" class="list_block">Pantry lists</div>');

    // SHOPPING
    var $shopping = $("#shopping");
    var shoppingData = window.JSInterface.loadLists("shopping");
    var newShoppingData = new Array();
    if (shoppingData.length > 0) {
        shoppingData = shoppingData.split(";");
    }
    else {
        $shopping.after('<div class="list_block">No shopping lists</div>');
    }

    for(i = 0; i < shoppingData.length; i++) {
            var dataFields = shoppingData[i].split(",");
            newShoppingData.push([parseInt(dataFields[0]), dataFields[1]]);
    }
    newShoppingData.sort(sortByFirstColumn);
    for(i = 0; i < newShoppingData.length; i++) {
        var id = newShoppingData[i][0];
        var name = newShoppingData[i][1];
        $shopping.after('<div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }

    // PANTRY
    var $pantry = $("#pantry");
    var pantryData = window.JSInterface.loadLists("pantry");
    var newPantryData = new Array();
    if (pantryData.length > 0) {
        pantryData = pantryData.split(";");
    }
    else {
        $pantry.after('<div class="list_block">No pantry lists</div>');
    }
    for(i = 0; i < pantryData.length; i++) {
            var dataFields = pantryData[i].split(",");
            newPantryData.push([parseInt(dataFields[0]), dataFields[1]]);
    }
    newPantryData.sort(sortByFirstColumn);
    for(i = 0; i < newPantryData.length; i++) {
        var id = newPantryData[i][0];
        var name = newPantryData[i][1];
        $pantry.after('<div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }
}

function prepareButtons() {
    $(".left_button").on("tap", function() {
        window.JSInterface.inputDialog("Create new shopping list", "Please name your new list:", "", "Create", "Cancel", "newShoppingList");
    });
    $(".right_button").on("tap", function() {
        window.JSInterface.inputDialog("Create new pantry list", "Please name your new list:", "", "Create", "Cancel", "newPantryList");
    });

    $(".bot_curve").on("tap", function() {
        if (parseInt($(".bottom_piece").css("bottom").replace("px","")) > 0) {
            $(".bottom_piece").animate({bottom: "0"});
        }
        else {
            $(".bottom_piece").animate({bottom: "10em"});
        }
        $(document).on("tap", function(e) {
            if (!$(".left_button").is(e.target) && $(".left_button").has(e.target).length === 0 && !$(".right_button").is(e.target) && $(".right_button").has(e.target).length === 0) {
                $(".bottom_piece").animate({bottom: "0"});
                $(document).unbind("tap");
            }
        });
    });
}