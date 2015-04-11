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
        $shopping.append(' <div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }
    for(i = 0; i < pantryData.length-1; i++) {
        var dataFields = data.split(",");
        var id = dataFields[0];
        var name = dataFields[1];
        $pantry.append(' <div id="'+id+'" class="list_block">'+name+' <img class="threeDots" src="images/3_dots_list.png"><img class="listLine" src="images/list_line.png"></div>');
    }
}