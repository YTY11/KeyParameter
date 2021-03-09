console.log($(".td1").find("a").eq(1).attr("class"));


for (var i = 1; i < 55; i++){
    let tdLength = $(".td" + i).find("a").length;
    for(var j = 0; j < tdLength; j++){
        if($(".td"+i).find("a").eq(j).attr("class") != 'none'){
            $(".td"+i).addClass($(".td"+i).find("a").eq(j).attr("class"));
            break;
        }
    }
}
