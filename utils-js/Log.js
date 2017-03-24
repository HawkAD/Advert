/**
 * Created by lenovo on 2017/3/24.
 */
var L = (function(){
    if(!L){
        L = {};
    }
    L.e=function(tag,mes){
        var log ="";
        if(!mes){
            var i = mes;
            mes = tag;
            tag = i;
        }
        if(mes){
            log=log+mes;
        }else if(tag){
           log=tag+" ---> "+log;
        }else {
           log = "{}"
        }
        console.log(log);
    }
    return L;
})();

/*
var L = (function(L){
     //  function(）里的参数L与 var L 不是一个变量， 参数 L 是一个方法内自动生成一个新的变量
    if(!L){
        L = {};
    }
    L.e=function(tag,mes){
        console.log(" log e start")
        var log ="";
        if(mes){
            log=log+mes;
        }else if(tag){
            log=log+tag+" ---> ";
        }else {
            log = "{}"
        }
        console.log(log);
    }
    return L;
})(L);*/

/*
这种方式里， 函数里面访问的才是L变量；
var L= null;
(function(){
    if(!L){
        L = {};
    }
    L.e=function(tag,mes){
        console.log(" log e start")
        var log ="";
        if(mes){
            log=log+mes;
        }else if(tag){
            log=log+tag+" ---> ";
        }else {
            log = "{}"
        }
        console.log(log);
    }
    return L;
})();*/
