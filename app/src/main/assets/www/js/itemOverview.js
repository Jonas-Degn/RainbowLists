function loadItems() {
    var $content = $(".main_area");
    var itemsData = window.JSInterface.loadItems();
    var newItemsData = new Array();
    var $itemsContent;
    var listID = getCurrentList();

    if (itemsData.length > 0) {
        $content.append('<div id="items" class="item_block_title">Items</div><div id="itemsContent"></div>');
        $itemsContent = $("#itemsContent");
    }
    else {
        $content.html('<img src="images/hidden.gif" class="hiddenCool" /><img src="images/rainbow_item^^.png" class="noLists" alt="Add a new item by clicking the plus"/>');
        setTimeout(function() {
        $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
            $(".noLists").show("scale",{}, 400);
        });
        }, 500);
    }


    if (itemsData.length > 0) {
        itemsData = itemsData.split(";");
    }
    for(i = 0; i < itemsData.length; i++) {
            var dataFields = itemsData[i].split(",");
            if (listID > 0) {
                newItemsData.push([parseInt(dataFields[0]), parseInt(dataFields[1]) ,dataFields[2], dataFields[3], parseInt(dataFields[4])]);
            }
            else {
                newItemsData.push([parseInt(dataFields[0]), dataFields[1] ,dataFields[2], parseInt(dataFields[3])]);
            }
    }
    newItemsData.sort(sortByFirstColumn);

    if (listID > 0) {

    }
    else {
        for(i = 0; i < newItemsData.length; i++) {
            var id = newItemsData[i][0];
            var name = newItemsData[i][1];
            var amount = newItemsData[i][2];
            var isChecked = newItemsData[i][3];

            if (isChecked == 0) {
                $itemsContent.append('<div id="'+id+'" class="item_block item_block_background">'+amount+' '+name+'<img class="unchecked" src="images/checkbox_unchecked.png"></div>');
            }
            else {
                $itemsContent.append('<div id="'+id+'" class="item_block item_block_background">'+amount+' '+name+'<img class="checked" src="images/checkbox_checked.png"></div>');
            }
        }
    }


    $(".list").on("tap", function(e) {
        $(e.target).toggleClass("toggledItem");
        // Save toggle in database + model
    });
    $(".list").on("swipeleft swiperight", function(e) {
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



    setTimeout(function() {
        $(".hiddenCool").animate({left: "-100%"}, 1000, function() {
            $(".noLists").show("scale",{}, 400);
        });
    }, 500);
}

function updateItemDisplay() {
    if ($("#itemsContent div:visible").length == 0) {
        $(".main_area").html('<img src="images/rainbow_item^^.png" class="noLists" alt="Add a new item by clicking the plus"/>');
    }
}

function prepareButtons2() {
    $("#leftButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {window.JSInterface.inputDialog("Add a new product", "Input product as amount, type and name. e.g 1 kg Flour (no special chars):", "", "Add", "Cancel", "addProduct");}, 400);
    });
    $("#rightButton2").on("tap", function() {
        $(".bottom_piece").animate({bottom: "-14em"});
        $(".bot_curve").unbind("tap");
        $(document).unbind("tap");
        setTimeout(function() {
            window.JSInterface.inputDialog("Set amount", "Input amount and type. e.g 1 kg, 1 liter (no special chars):", "", "Continue", "Cancel", "scanProduct");
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