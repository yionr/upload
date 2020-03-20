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

/**
 * 根据一个基本日得到当前日期相对于基本日是第几周
 * @param startDate date.getTime()形式
 */
function getWeek(startDate){
    return Math.floor(((new Date().getTime() - startDate)/1000/3600/24 + 1) / 7 + 1);
}