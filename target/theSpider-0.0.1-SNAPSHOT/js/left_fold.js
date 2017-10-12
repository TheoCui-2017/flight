/*折叠*/
jQuery.Huifold = function(obj,obj_c,speed,obj_type,Event){
  if(obj_type == 2){
    $(obj+":first").find("b").html("-");
    $(obj_c+":first").show();
  }
  $(obj).on(Event,function(){
    if($(this).next().is(":visible")){
      if(obj_type == 2){
        return false;
      }else{
        $(this).next().slideUp(speed).end().removeClass("selected");
        if($(this).find("b")){
          $(this).find("b").html("+");
        }
      }
    }
    else{
      if(obj_type == 3){
        $(this).next().slideDown(speed).end().addClass("selected");
        if($(this).find("b")){
          $(this).find("b").html("-");
        }
      }else{
        $(obj_c).slideUp(speed);
        $(obj).removeClass("selected");
        if($(this).find("b")){
          $(obj).find("b").html("+");
        }
        $(this).next().slideDown(speed).end().addClass("selected");
        if($(this).find("b")){
          $(this).find("b").html("-");
        }
      }
    }
  });
}
/*左侧菜单*/
$.Huifold(".menu_dropdown dl dt", ".menu_dropdown dl dd", "fast", 1, "click");
