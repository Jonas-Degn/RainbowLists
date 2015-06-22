function loadItems() {
    $content = $(".main_area");
    newItemsData = new Array();
    $itemsContent = "";
    listID = getCurrentList();
    itemsData = window.JSInterface.loadItems(listID);

    if (itemsData.length > 0) {
        if (listID > 0) {
            $content.html('<div id="items" class="item_block_title">'+getCurrentListName()+'</div><div id="itemsContent"></div>');
        }
        else {
            $content.html('<div id="items" class="item_block_title">Items</div><div id="itemsContent"></div>');
        }
        $itemsContent = $("#itemsContent");
    }
    else {
        $content.html('<img src="images/hidden.gif" class="hiddenCool" /><img src="images/rainbow_item^^.png" class="noLists" alt="Add a new item by clicking the plus"/>');
        setTimeout(function() {
        $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
            $(".noLists").show("scale",{}, 400);
        });
        }, 500);
        return;
    }


    if (itemsData.length > 0) {
        itemsData = itemsData.split(";");
    }
    for(i = 0; i < itemsData.length; i++) {
            var dataFields = itemsData[i].split(",");
            newItemsData.push([parseInt(dataFields[0]), parseInt(dataFields[1]) ,dataFields[2], dataFields[3], parseInt(dataFields[4]=="true"?1:0)]);
    }
    newItemsData.sort(sortByFirstColumn);

    for(i = 0; i < newItemsData.length; i++) {
        var listID = newItemsData[i][0];
        var id = newItemsData[i][1]
        var name = newItemsData[i][2];
        var amount = newItemsData[i][3];
        var isChecked = newItemsData[i][4];

        if (isChecked == 0) {
            $itemsContent.append('<div id="'+id+'" class="item_block item_block_background">'+amount+' '+name+' <img src="images/checkbox_unchecked.png" alt="checkBox"></div>');
        }
        else {
            $itemsContent.append('<div id="'+id+'" class="item_block item_block_background crossedout">'+amount+' '+name+' <img src="images/checkbox_checked.png" alt="checkBox"></div>');
        }
    }

    setTimeout(function() {
        $(".item_block").on("tap", function(e) {

            if ($(e.target).find("img")[0]) {
                var id = e.target.id;
                var elem = $(e.target).find("img")[0];

                if (elem.src == "file:///android_asset/www/images/checkbox_unchecked.png") {
                    window.JSInterface.checkItem(id);
                    elem.src = "file:///android_asset/www/images/checkbox_checked.png";
                    $(e.target).toggleClass("crossedout");
                }
                else {
                    window.JSInterface.decheckItem(id);
                    elem.src = "file:///android_asset/www/images/checkbox_unchecked.png";
                    $(e.target).toggleClass("crossedout");
                }
            }
            else {
                var id = $(e.target).parent()[0].id;
                var elem = e.target;

                if (elem.src == "file:///android_asset/www/images/checkbox_unchecked.png") {
                    window.JSInterface.checkItem(id);
                    elem.src = "file:///android_asset/www/images/checkbox_checked.png";
                    $(e.target).parent().toggleClass("crossedout");
                }
                else {
                    window.JSInterface.decheckItem(id);
                    elem.src = "file:///android_asset/www/images/checkbox_unchecked.png";
                    $(e.target).parent().toggleClass("crossedout");
                }
            }
        });
        $(".item_block").on("swipeleft swiperight", function(e) {
            window.JSInterface.deleteItem(e.target.id);
            $(e.target).css({position: "relative"});
            if (e.swipestart.coords[0] > e.swipestop.coords[0]) {
                $(e.target).animate({right: "100%"}).hide('slow', function () {
                    updateItemDisplay();
                });
            }
            else {
                $(e.target).animate({left: "100%"}).hide('slow', function () {
                    updateItemDisplay();
                });
            }
        });
    }, 500);
}

function updateItemDisplay() {
    if ($("#itemsContent div:visible").length == 0) {
        $(".main_area").html('<img src="images/hidden.gif" class="hiddenCool" /><img src="images/rainbow_item^^.png" class="noLists" alt="Add a new item by clicking the plus"/>');
        setTimeout(function() {
            $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
                $(".noLists").show("scale",{}, 400);
            });
        }, 500);
    }
}

function prepareButtons2() {
    $("#leftButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Add a new product", "Input product as amount, type and name. e.g 1 kg Flour (no special characters):", "", "Add", "Cancel", "addProduct");}, 400);
    });
    $("#rightButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {
            window.JSInterface.inputDialog("Set amount", "Input amount and type. e.g 1 kg, 1 liter (no special characters):", "", "Continue", "Cancel", "scanProduct");
        });
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