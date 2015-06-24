function loadLists() {
    var $content = $(".main_area");
    var shoppingData = window.JSInterface.loadLists("shopping");
    var pantryData = window.JSInterface.loadLists("pantry");
    var newShoppingData = new Array();
    var newPantryData = new Array();
    var $shoppingContent,
        $pantryContent;

    if (shoppingData.length > 0 || pantryData.length > 0) {
        if (shoppingData.length > 0) {
            $content.append('<div id="shopping" class="list_block_title">Shopping lists</div><div id="shoppingContent"></div>');
            $shoppingContent = $("#shoppingContent");
        }
        if (pantryData.length > 0) {
            $content.append('<div id="pantry" class="list_block_title">Pantry lists</div><div id="pantryContent"></div>');
            $pantryContent = $("#pantryContent");
        }
    }
    else {
        $content.html('<img src="images/rainbow^^.png" class="noLists" alt="Create a new list by clicking the plus"/>');
        setTimeout(function() {
            $(".noLists").show("scale",{}, 400);
        }, 250);
    }


    if (shoppingData.length > 0) {
        shoppingData = shoppingData.split(";");
    }
    for(i = 0; i < shoppingData.length; i++) {
            var dataFields = shoppingData[i].split(",");
            newShoppingData.push([parseInt(dataFields[0]), dataFields[1]]);
    }
    newShoppingData.sort(sortByFirstColumn);
    for(i = 0; i < newShoppingData.length; i++) {
        var id = newShoppingData[i][0];
        var name = newShoppingData[i][1];
        $shoppingContent.append('<div id="'+id+'" class="list_block list_block_background">'+name+'</div>');
    }

    if (pantryData.length > 0) {
        pantryData = pantryData.split(";");
    }
    for(i = 0; i < pantryData.length; i++) {
            var dataFields = pantryData[i].split(",");
            newPantryData.push([parseInt(dataFields[0]), dataFields[1]]);
    }
    newPantryData.sort(sortByFirstColumn);
    for(i = 0; i < newPantryData.length; i++) {
        var id = newPantryData[i][0];
        var name = newPantryData[i][1];
        $pantryContent.append('<div id="'+id+'" class="list_block list_block_background">'+name+'</div>');
    }

    setTimeout(function() {
        $(".list_block").on("tap", function(e) {
            $(e.target).toggleClass("activeList");
            var id = parseInt(e.target.id);
            setCurrentList(id);
            loadPage("itemOverview.html");
        });
        $(".list_block").on("swipeleft swiperight", function(e) {
            window.JSInterface.deleteList(e.target.id);
            $(e.target).css({position: "relative"});
            if (e.swipestart.coords[0] > e.swipestop.coords[0]) {
                $(e.target).animate({right: "100%"}).hide('slow', function () {
                    updateListDisplay();
                });
            }
            else {
                $(e.target).animate({left: "100%"}).hide('slow', function () {
                    updateListDisplay();
                });
            }
        });
    }, 500);
}

function updateListDisplay() {
    if ($("#shoppingContent div:visible").length == 0) {
        $("#shopping").hide('slow');
    };
    if ($("#pantryContent div:visible").length == 0) {
        $("#pantry").hide('slow');
    };
    if ($("#shoppingContent div:visible").length == 0 && $("#pantryContent div:visible").length == 0) {
        $(".main_area").html('<img src="images/hidden.gif" class="hiddenCool" /><img src="images/rainbow^^.png" class="noLists" alt="Create a new list by clicking the plus"/>');
        setTimeout(function() {
            $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
                $(".noLists").show("scale",{}, 400);
            });
        }, 500);
    }
}

function prepareButtons() {
    $("#leftButton1").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Create new shopping list", "Please name your new list (no special characters):", "", "Create", "Cancel", "newShoppingList")}, 400);
    });
    $("#rightButton1").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Create new pantry list", "Please name your new list (no special characters):", "", "Create", "Cancel", "newPantryList")}, 400);
    });

    $(".newList").on("tap", function() {
        window.JSInterface.setCurrentAction("showNewList");
        if (parseInt($(".bottom_piece").css("bottom").replace("px","")) < 20) {
            $(".newList").hide('fast', function() {$(".bottom_piece").animate({bottom: "2em"})});
        }
        else {
            $(".bottom_piece").animate({bottom: "-14em"}, 400, function() {$(".newList").hide('fast');});
        }
        $(document).on("tap", function(e) {
            if (!$(".left_button").is(e.target) && $(".left_button").has(e.target).length === 0 && !$(".right_button").is(e.target) && $(".right_button").has(e.target).length === 0) {
                $(".bottom_piece").animate({bottom: "-14em"},400,function() {
                    $(".newList").show('fast');
                });
                $(document).unbind("tap");
                window.JSInterface.setCurrentAction("");
            }
        });
    });
}