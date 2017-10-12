/*下拉菜单*/
function displaynavbar(obj){
  if($(obj).hasClass("open")){
    $(obj).removeClass("open");
    $("body").removeClass("big-page");
  }else{
    $(obj).addClass("open");
    $("body").addClass("big-page");
  }
}
