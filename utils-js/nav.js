/**
 * Created by lenovo on 2017/3/24.
 */
function event(listName,rawColor,clickColor,hoverColor,callback){
    if(typeof hoverColor == 'function'){
        callback = hoverColor;
        hoverColor = false;
    }
    var eles = document.getElementsByName(listName);
    echoArray(eles,function(){
        var tag = this.id;

        if(tag){
            this.onclick = function () {
                echoArray(eles,function(){
                    this.style.color = rawColor
                })
                this.oldcolor =this.style.color=clickColor;
                if(callback){
                    callback.call(this);
                }

            }
        }
        if(hoverColor){
            this.onmouseover=function(){
                this.oldcolor=this.style.color;
                this.style.color =hoverColor
            }
            this.onmouseout=function(){
                this.style.color=this.oldcolor;
            }
        }
    })
    eles[0].style.color=clickColor;
}

function echoArray(array,callback){
    if(!array||!array instanceof Array){
        L.e("输入的参数没有数组")
        return;
    }
    for(var i = 0;i<array.length;i++){
        if(!callback){
            L.e("没有回调")
            return;
        }
        callback.call(array[i]);
    }
}


//获取元素相对于页面的偏移
function getOffset(ele){
    if(ele.getBoundingClientRect){
        return getOffsetRect(ele);
    }else{
        return getOffsetSum(ele);
    }
}

function getOffsetRect(ele){
    var box=ele.getBoundingClientRect();
    var body=document.body,
        docElem=document.documentElement;
    //获取页面的scrollTop,scrollLeft(兼容性写法)
    var scrollTop=window.pageYOffset||docElem.scrollTop||body.scrollTop,
        scrollLeft=window.pageXOffset||docElem.scrollLeft||body.scrollLeft;
    var clientTop=docElem.clientTop||body.clientTop,
        clientLeft=docElem.clientLeft||body.clientLeft;
    var top=box.top+scrollTop-clientTop,
        left=box.left+scrollLeft-clientLeft;
    return {
        //Math.round 兼容火狐浏览器bug
        top:Math.round(top),
        left:Math.round(left)
    }
}

function getOffsetSum(ele){
    var top= 0,left=0;
    while(ele){
        top+=ele.offsetTop;
        left+=ele.offsetLeft;
        ele=ele.offsetParent;
    }
    return {
        top:top,
        left:left
    }
}

function myParseInt(s) {
    var ret = parseInt(s);
    return (isNaN(ret) ? 0 : ret);
}

function moveEle(el,offset){
    if(!el||!offset){
        return
    }
   var parent =  document.body;
    var bodyWidth = parent.clientWidth+parent.scrollLeft;
    var bodyHight = parent.clientHeight+parent.scrollTop;
    var i = el.style;
    L.e(" el =="+i.top);
    el.style.top =(myParseInt(el.style.top) + offset) + "px";
    L.e(" el2 =="+el.style.top);
    if(parent.scrollLeft<=myParseInt(el.style.left)<= bodyWidth){
        el.style.left =(myParseInt(el.style.left) + offset) + "px";
    }
    if(parent.scrollTop<=myParseInt(el.style.top)<= bodyHight){

    }
}