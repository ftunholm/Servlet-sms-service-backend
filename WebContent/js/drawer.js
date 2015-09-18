/**
 * Created by martingabrielsson on 2015-05-30.
 */
$(document).ready(function() {
    $("#hamburger_trigger").click(function(){
        $("#hamburger" ).toggleClass("active");
        if($("#navigation_drawer").css("left") != 0+'px'){
            $("#navigation_drawer").animate({left:'0'},350);
        }else{
            $("#navigation_drawer").animate({left:'-20em'},350);
        }


    });
});