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


function isWorkingDay() {
    let date = new Date();
    let weekDay = date.getDay();
    if (weekDay === 5)
        if (date.getHours() >= 12)
            return false;

    if (weekDay === 6 || weekDay === 7 || weekDay === 1)
        return false;
    return true;
}