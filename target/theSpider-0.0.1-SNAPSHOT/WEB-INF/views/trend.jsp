<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="show_Line_Chart"></div>

<script type="text/javascript">
  $('document').ready(function(){
	  console.log("====");
	var lineNames_all = []; //总航司
	var dataset_all = {}; //总数据
    var dataset=[];//数据集
    var lines=[]; //保存折线图对象
    var xMarks=[]; //日期数组
    var lineNames=[]; //保存系列名称
    var lineColor=["#F00","#9c00ff","#0F0","#00ffea","#FF00EA","#1800ff","#2fa8e0","#00d8ff","#00991d","#bad82c","#fff000","#ff7200"];
    var w=1200;
    var h=500;
    var padding=80;
    var currentLineNum=0;

    //用一个变量存储标题和副标题的高度，如果没有标题什么的，就为0
    var head_height=padding;
    var title="${departure}"+" 至 "+"${arrival}"+" 价格趋势";
    var subTitle="${start_date}" + " 至 " + "${end_date}";

    //用一个变量计算底部的高度，如果不是多系列，就为0
    var foot_height=padding;

    //模拟数据
    getData();

    //判断是否多维数组，如果不是，则转为多维数组，这些处理是为了处理外部传递的参数设置的，现在数据标准，没什么用
    if(!(dataset[0] instanceof Array))
    {
      var tempArr=[];
      tempArr.push(dataset);
      dataset=tempArr;
    }

    //保存数组长度，也就是系列的个数
    currentLineNum=dataset.length;

    //图例的预留位置
    foot_height+=25;

    //定义画布
    var svg=d3.select("#show_Line_Chart")
    .append("svg")
    .attr("width",w)
    .attr("height",h);

    //添加背景
    svg.append("g")
    .append("rect")
    .attr("x",0)
    .attr("y",0)
    .attr("width",w)
    .attr("height",h)
    .style("fill","#FFF")
    .style("stroke-width",2)
    .style("stroke","#F7F7F7");

    //添加标题
    if(title!="")
    {
      svg.append("g")
      .append("text")
      .text(title)
      .attr("class","title")
      .attr("x",w/2)
      .attr("y",head_height);

      head_height+=30;
    }

    //添加副标题
    if(subTitle!="")
    {
      svg.append("g")
      .append("text")
      .text(subTitle)
      .attr("class","subTitle")
      .attr("x",w/2)
      .attr("y",head_height);

      head_height+=20;
    }

    maxdata=getMaxdata(dataset);

    //横坐标轴比例尺
    var xScale = d3.scale.linear()
    .domain([0,dataset[0].length-1])
    .range([padding,w-padding]);

    //纵坐标轴比例尺
    var yScale = d3.scale.linear()
    .domain([0,maxdata])
    .range([h-foot_height,head_height]);

    //定义横轴网格线
    var xInner = d3.svg.axis()
    .scale(xScale)
    .tickSize(-(h-head_height-foot_height),0,0)
    .tickFormat("")
    .orient("bottom")
    .ticks(dataset[0].length);

    //添加横轴网格线
    var xInnerBar=svg.append("g")
    .attr("class","inner_line")
    .attr("transform", "translate(0," + (h - padding) + ")")
    .call(xInner);

    //定义纵轴网格线
    var yInner = d3.svg.axis()
    .scale(yScale)
    .tickSize(-(w-padding*2),0,0)
    .tickFormat("")
    .orient("left")
    .ticks(10);

    //添加纵轴网格线
    var yInnerBar=svg.append("g")
    .attr("class", "inner_line")
    .attr("transform", "translate("+padding+",0)")
    .call(yInner);

    //定义横轴
    var xAxis = d3.svg.axis()
    .scale(xScale)
    .orient("bottom")
    .ticks(dataset[0].length);

    //添加横坐标轴
    var xBar=svg.append("g")
    .attr("class","axis")
    .attr("transform", "translate(0," + (h - foot_height) + ")")
    .call(xAxis);
    if(xMarks.length==1){
        var xBar =d3.select(".axis").append("text")
        .attr("y","9")
        .attr("x","0")
        .attr("dy",".71em")
        .text(xMarks[0]);
      }

    //通过编号获取对应的横轴标签
    xBar.selectAll("text")
    .text(function(d){return xMarks[d];});

    //定义纵轴
    var yAxis = d3.svg.axis()
    .scale(yScale)
    .orient("left")
    .ticks(10);

    //添加纵轴
    var yBar=svg.append("g")
    .attr("class", "axis")
    .attr("transform", "translate("+padding+",0)")
    .call(yAxis);

    //添加图例
    var legend=svg.append("g");

    addLegend();

    //添加折线
    lines=[];
    for(i=0;i<currentLineNum;i++)
    {
      var newLine=new CrystalLineObject();
      newLine.init(i);
      lines.push(newLine);
    }


    // 显示下拉框
    var html = '<ul class="airline tempclass" id="ul_FlightCarrier0">';
    for(var k in lineNames_all){
    	var airline = lineNames_all[k];
    	var content = '<li id="li_FlightCarrier0" carriercode="'+airline+'"> <label class="fl width110">';
    	content += '<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="carrier" value="'+airline+'"><span id="sp_TCarrierName">'+airline+'</span>';
    	content += '</label></li>';
    	html += content;
    }
    html += '</ul>';
    $("#carrier_list").html(html);
    $("#carrier_lists").show();
    //定义折线类
    function CrystalLineObject()
    {
      this.group=null;
      this.path=null;
      this.oldData=[];

      this.init=function(id)
      {
        var arr=dataset[id];
        this.group=svg.append("g");

        var line = d3.svg.line()
        .x(function(d,i){return xScale(i);})
        .y(function(d){return yScale(d);});

        //添加折线
        this.path=this.group.append("path")
        .attr("d",line(arr))
        .style("fill","none")
        .style("stroke-width",1)
        .style("stroke",lineColor[id])
        .style("stroke-opacity",0.9);

        //添加系列的小圆点
        this.group.selectAll("circle")
        .data(arr)
        .enter()
        .append("circle")
        .attr("cx", function(d,i) {
          return xScale(i);
        })
        .attr("cy", function(d) {
          return yScale(d);
        })
        .attr("r",5)
        .attr("fill",lineColor[id])
        .on("mouseover",function(d,i){
            /*console.log(d3.select(this).attr('cx'));//圆点的cx值
            console.log(d3.select(this).attr('cy'));//圆点的cy值
            //console.log(d3.select(this).data());//圆点元素的数组
            console.log(d3.select(this).data()[0]);//圆点元素的x数组值
            console.log(xMarks[i]);//圆点元素的y数组值*/
            //定义弹出层坐标数据提示
            var thisTop = $(this).offset().top+3;
            var thisRight = $('body').width()-$(this).offset().left;
            if(thisRight<180){
              var thisLeft = $(this).offset().left-217;
            }else{
              var thisLeft = $(this).offset().left+3;
            }
            var thiscolor = d3.select(this).attr("fill");
            var index = 0;
            for(var c=0;c<lineColor.length;c++){
              if(lineColor[c] == thiscolor){
            	index = c;
                break;
              }
            }
            var popDivLayout = "<div class='pop_tip' style='top:"+thisTop+"px;left:" +thisLeft+"px;'>航司：" +lineNames[index]+"<br/>价钱："+d3.select(this).data()[0]+"元"+"<br/>日期：" +xMarks[i]+"</div>";
            $("body").prepend(popDivLayout);
          })
          .on("mouseout",function(d,i){
              $(document).find('.pop_tip').remove();
          });
        this.oldData=arr;


      };

      //动画初始化方法
      this.movieBegin=function(id)
      {
        var arr=dataset[i];
        //补足/删除路径
        var olddata=this.oldData;
        var line= d3.svg.line()
        .x(function(d,i){if(i>=olddata.length) return w-padding; else return xScale(i);})
        .y(function(d,i){if(i>=olddata.length) return h-foot_height; else return yScale(olddata[i]);});

        //路径初始化
        this.path.attr("d",line(arr));

        //截断旧数据
        var tempData=olddata.slice(0,arr.length);
        var circle=this.group.selectAll("circle").data(tempData);

        //删除多余的圆点
        circle.exit().remove();

        //圆点初始化，添加圆点,多出来的到右侧底部
        this.group.selectAll("circle")
        .data(arr)
        .enter()
        .append("circle")
        .attr("cx", function(d,i){
        if(i>=olddata.length) return w-padding; else return xScale(i);
        })
        .attr("cy",function(d,i){
          if(i>=olddata.length) return h-foot_height; else return yScale(d);
        })
        .attr("r",5)
        .attr("fill",lineColor[id]);

        this.oldData=arr;
      };

      //重绘加动画效果
      this.reDraw=function(id,_duration)
      {
        var arr=dataset[i];
        var line = d3.svg.line()
        .x(function(d,i){return xScale(i);})
        .y(function(d){return yScale(d);});

        //路径动画
        this.path.transition().duration(_duration).attr("d",line(arr));

        //圆点动画
        this.group.selectAll("circle")
        .transition()
        .duration(_duration)
        .attr("cx", function(d,i) {
          return xScale(i);
        })
        .attr("cy", function(d) {
          return yScale(d);
        })
      };

      //从画布删除折线
      this.remove=function()
      {
        this.group.remove();
      };
    }

    //添加图例
    function addLegend()
    {
   	
      // enter
      var textGroup=legend.selectAll("text")
      .data(lineNames);
      
      //
      legend.selectAll("text")
      .data(lineNames)
      .enter()
      .append("text")
      .text(function(d){return d;})
      .attr("class","legend")
      .attr("x", function(d,i) {return i*100;})
      .attr("y",0)
      .attr("fill",function(d,i){ return lineColor[i];});
      
      // update
      textGroup.html(function(d,i){ return d; });
	  // remove
      textGroup.exit().remove();

      var rectGroup=legend.selectAll("rect")
      .data(lineNames);

      legend.selectAll("rect")
      .data(lineNames)
      .enter()
      .append("rect")
      .attr("x", function(d,i) {return i*100-20;})
      .attr("y",-10)
      .attr("width",12)
      .attr("height",12)
      .attr("fill",function(d,i){ return lineColor[i];});
      
      rectGroup.html(function(d,i){ return d; });
      
      rectGroup.exit().remove();

      legend.attr("transform","translate("+((w-lineNames.length*100)/2)+","+(h-10)+")");
    }

    //产生随机数据
    function getData()
    {
    	oldData=dataset;
    	dataset=[];
    	xMarks=[];//日期数组
    	lineNames=[];
    	var flightInfo = ${ flightLegPrices };
    	var airlines_str = "${ airlines }";
    	var airlines = airlines_str.split(",");
    	// 最多显示前8个航司
    	for(var index in airlines){
    		if(lineNames.length < 8){
    			lineNames.push(airlines[index]);
    		}
    		lineNames_all.push(airlines[index]);
    	}
    	for(var s in lineNames){
    		var tempArr = [];
    		for(var k in flightInfo){
    			if(xMarks.indexOf(k) == -1){
    				xMarks.push(k);
    			}
    			var flightIndex = flightInfo[k];
    			for(var m in flightIndex){
    				var airline = lineNames[s];
    				if(flightIndex[m][airline]){
    					tempArr.push(flightIndex[m][airline]);
    				}
    			}
    		}
    		dataset.push(tempArr);
    	}
    	
    	for(var s in lineNames_all){
    		var tempArr = [];
    		for(var k in flightInfo){
    			var flightIndex = flightInfo[k];
    			for(var m in flightIndex){
    				var airline = lineNames_all[s];
    				if(flightIndex[m][airline]){
    					tempArr.push(flightIndex[m][airline]);
    				}
    			}
    		}
    		dataset_all[lineNames_all[s]] = tempArr;
    	}
    }

    //取得多维数组最大值
    function getMaxdata(arr)
    {
      maxdata=0;
      for(i=0;i<arr.length;i++)
      {
        maxdata=d3.max([maxdata,d3.max(arr[i])]);
      }
      return maxdata;
    }
    
    
    $("input:checkbox[name='carrier']").on('click',function(){
    	var airlines = [];
    	console.log(airlines);
    	$("input:checkbox[name='carrier']:checked").each(function() {
    		if(airlines.length < 11){
    			airlines.push($(this).val());
    		}
		});
    	console.log(airlines);
    	if(airlines.length){
    		drawChart(airlines);
    	}
    });
    
  //重新作图
    function drawChart(airlines)
    {
      var _duration=1000;

      lineNames = [];
      lineNames = airlines;
      dataset = [];

      for(var k in lineNames){
    	  var airline = lineNames[k];
    	  dataset.push(dataset_all[airline]);
      }
      addLegend();
      //设置线条动画起始位置
      var lineObject=new CrystalLineObject();

      for(i=0;i<dataset.length;i++)
      {
        if(i<currentLineNum)
        {
          //对已有的线条做动画
          lineObject=lines[i];
          lineObject.movieBegin(i);
        }
        else
        {
          //如果现有线条不够，就加上一些
          var newLine=new CrystalLineObject();
          newLine.init(i);
          lines.push(newLine);
        }
      }

      //删除多余的线条，如果有的话
      if(dataset.length<currentLineNum)
      {
        for(i=dataset.length;i<currentLineNum;i++)
        {
          lineObject=lines[i];
          lineObject.remove();
        }
        lines.splice(dataset.length,currentLineNum-dataset.length);
      }

      maxdata=getMaxdata(dataset);
      newLength=dataset[0].length;

      //横轴数据动画
      xScale.domain([0,newLength-1]);
      xAxis.scale(xScale).ticks(newLength);
      xBar.transition().duration(_duration).call(xAxis);
      xBar.selectAll("text").text(function(d){return xMarks[d];});
      xInner.scale(xScale).ticks(newLength);
      xInnerBar.transition().duration(_duration).call(xInner);

      //纵轴数据动画
      yScale.domain([0,maxdata]);
      yBar.transition().duration(_duration).call(yAxis);
      yInnerBar.transition().duration(_duration).call(yInner);

      //开始线条动画
      for(i=0;i<lines.length;i++)
      {
        lineObject=lines[i];
        lineObject.reDraw(i,_duration);
      }

      currentLineNum=dataset.length;
      dataLength=newLength;
    }
    
  });
</script>