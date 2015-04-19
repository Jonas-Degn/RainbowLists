function loadItems() {
    var $content = $(".main_area");

    $content.html('<div id="itemContent"></div><img src="images/hidden.gif" class="hiddenCool" /><img src="images/rainbow_item^^.png" class="noLists" alt="Create a new list by clicking the plus"/>');
    setTimeout(function() {
    $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
        $(".noLists").show("scale",{}, 400);
    });
    }, 500);
}

function updateItemDisplay() {
    if ($("#itemContent div:visible").length == 0) {
        $(".main_area").html('<img src="images/rainbow_item^^.png" class="noLists" alt="Add a new item by clicking the plus"/>');
    }
}

function prepareButtons2() {
    $("#leftButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Create new shopping list", "Please name your new list (no special chars):", "", "Create", "Cancel", "newShoppingList")}, 400);
    });
    $("#rightButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Create new pantry list", "Please name your new list (no special chars):", "", "Create", "Cancel", "newPantryList")}, 400);
    });

    $(".newItem").on("tap", function() {
        window.JSInterface.setCurrentAction("showNewItem");
        if (parseInt($(".bottom_piece").css("bottom").replace("px","")) < 20) {
            $(".newItem").hide('fast', function() {$(".bottom_piece").animate({bottom: "2em"})});
        }
        else {
            $(".bottom_piece").animate({bottom: "-14em"}, 400, function() {$(".newItem").hide('fast');});
        }
        $(document).on("tap", function(e) {
            if (!$(".left_button").is(e.target) && $(".left_button").has(e.target).length === 0 && !$(".right_button").is(e.target) && $(".right_button").has(e.target).length === 0) {
                $(".bottom_piece").animate({bottom: "-14em"},400,function() {
                    $(".newItem").show('fast');
                });
                $(document).unbind("tap");
                window.JSInterface.setCurrentAction("");
            }
        });
    });
}