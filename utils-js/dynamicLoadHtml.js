/**
 * Created by lenovo on 2017/3/24.
 */
function WP(id,document){
    this.id=id;
    this.ele = document.getElementById(id);
}
function _(id){
    console.log("$ fang fa ")
    return new WP(id,this.document);
 }

WP.prototype.load = function(url,Fun){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    var ele = this.ele;
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            ele.innerHTML=xmlhttp.responseText;
            if(Fun){
                Fun.call(ele);
            }
        }
    }
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
}
