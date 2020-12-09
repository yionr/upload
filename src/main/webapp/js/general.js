function roll(e) {
    let pad = e.parentNode;
    let offsetWidth = pad.offsetWidth;
    if (pad.style.right !== '-150px'){
        pad.style.right = - offsetWidth + "px";
        e.innerText = '展开';
    }
    else if (pad.style.right === '-150px'){
        pad.style.right = 0 + "px";
        e.innerText = '收起';
    }
}
function getAjaxConnection(method,url,readyFunc){
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open(method,url);
    xmlHttp.send(null);
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
            readyFunc(xmlHttp);
        }
    }
}