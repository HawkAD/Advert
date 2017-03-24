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