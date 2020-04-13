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

    //    周日为0
    if (weekDay === 6 || weekDay === 0 || weekDay === 1)
        return false;
    if (weekDay === 2)
        if (date.getHours() < 8)
            return false;
    return true;
}
function isPC(){
    let userAgentInfo = navigator.userAgent;
    let Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
    let flag = true;
    for (let v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }
    }
    return flag;
}

function openUploadEntrance(){
    let uploadArea = document.getElementsByClassName("uploadArea")[0];
    uploadArea.style.height = '55%';
}

class Time {
    _padName;
    _pad;
    _fn;
    _fu;
    _kd;
    _sn;
    _su;
    _uploadArea;
    constructor(padName) {
        this._padName = padName;
        this._pad = document.getElementsByClassName(padName)[0];
        this._fn = this._pad.getElementsByClassName('fn')[0];
        this._fu = this._pad.getElementsByClassName('fu')[0];
        this._kd = this._pad.getElementsByClassName('kd')[0];
        this._sn = this._pad.getElementsByClassName('sn')[0];
        this._su = this._pad.getElementsByClassName('su')[0];
        this._uploadArea = document.getElementsByClassName('uploadArea')[0];
    }

}

function openTime(weekDay, hour, padName,changeColor) {

    let timePart = new Time(padName);

    //四档为: >1day , <1day , <1hour , deadline
    let color;
    if (changeColor){
        color = ['greenyellow','yellow','red','gray'];
    }
    else{
        color = ['yellow','yellow','yellow','greenyellow'];
    }

    //让中间的冒号一秒钟闪一次
    let tempKd = 0;
    let kds = setInterval(function () {
        if (++tempKd > 1)
            tempKd = 0;
        timePart._kd.style.opacity = tempKd;
    },1000);

    let date = new Date();

    let lastDays = weekDay - date.getDay();
    //修正1小时
    let lastHours = hour - 1 - date.getHours();
    //修正1分钟
    let lastMinuts = 59 - date.getMinutes();

    let lastSeconds = 60 - date.getSeconds();

    let lastMillSeconds = 1000 - date.getMilliseconds();

    //修正中午12点
    if (lastHours < 0){
        lastDays--;
        lastHours += 24;
    }

    //test
    // lastDays = lastHours = 0;
    // lastDays = lastHours = lastMinuts = 0;
    // lastSeconds = 10;

    //设置 fn 和 sn 的初始数值

    if (lastDays > 0){
        timePart._fu.innerText = '天';
        timePart._su.innerText = '时';
        timePart._fn.innerText = lastDays+"";
        timePart._sn.innerText = lastHours+"";
        timePart._pad.style.backgroundColor = color[0];
        setTimeout(function () {
            timePart._sn.innerText = parseInt(timePart._sn.innerText) - 1;
            let sns = setInterval(function () {
                timePart._sn.innerText = parseInt(timePart._sn.innerText) - 1;
                if (parseInt(timePart._sn.innerText) === -1){
                    if (parseInt(timePart._fn.innerText) > 0){
                        timePart._fn.innerText = parseInt(timePart._fn.innerText) - 1;
                        timePart._sn.innerText = '23';
                    }
                    else{
                        dropTime(padName,timePart,kds,sns,color);
                    }
                }
            },3600000)
        },lastMinuts*60*1000)
    }
    else if (lastHours > 0){
        timePart._fu.innerText = '时';
        timePart._su.innerText = '分';
        timePart._fn.innerText = lastHours+"";
        timePart._sn.innerText = lastMinuts+"";
        timePart._pad.style.backgroundColor = color[1];

        setTimeout(function () {
            timePart._sn.innerText = parseInt(timePart._sn.innerText) - 1;
            let sns = setInterval(function () {
                timePart._sn.innerText = parseInt(timePart._sn.innerText) - 1;
                if (parseInt(sn.innerText) === -1){
                    if (parseInt(timePart._fn.innerText) > 0){
                        timePart._fn.innerText = parseInt(timePart._fn.innerText) - 1;
                        timePart._sn.innerText = '59';
                    }
                    else{
                        dropTime(padName,timePart,kds,sns,color);
                    }
                }
            },60000)
        },lastSeconds*1000)

    }
    else{
        timePart._fu.innerText = '分';
        timePart._su.innerText = '秒';
        timePart._fn.innerText = lastMinuts+"";
        timePart._sn.innerText = lastSeconds+"";
        timePart._pad.style.backgroundColor = color[2];

        setTimeout(function () {
            let sns = setInterval(function () {
                timePart._sn.innerText = parseInt(timePart._sn.innerText) - 1;
                if (parseInt(timePart._sn.innerText) === -1) {
                    if(parseInt(timePart._fn.innerText) > 0){
                        timePart._fn.innerText = parseInt(timePart._fn.innerText) - 1;
                        timePart._sn.innerText = '59';
                    }
                    else{
                        dropTime(padName,timePart,kds,sns,color);
                    }
                }
            },1000);
        },lastMillSeconds);
    }
    timePart._pad.style.right = '20px';
    setTimeout(function () {
        timePart._pad.style.right = '10px';
    },700);
}

function dropTime(padName, timePart, kds, sns, color){

    //让中间的指针不再闪
    clearInterval(kds);
    //让中间指针隐藏
    timePart._kd.style.opacity = '0';
    //让时间计算停下
    clearInterval(sns);

    let targetTime;
    if (padName === 'deadline'){
        timePart._fn.innerText = '';
        timePart._fu.innerText = '截';
        timePart._sn.innerText = '止';
        timePart._su.innerText = '';
        targetTime = [0,2,8,'openline',false];
    }
    else if (padName === 'openline'){
        timePart._fn.innerText = '';
        timePart._fu.innerText = '开';
        timePart._sn.innerText = '放';
        timePart._su.innerText = '';
        targetTime = ['55%',5,12,'deadline',true];
    }
    //倒计时结束，设置背景色
    timePart._pad.style.backgroundColor = color[3];

    //关闭更新面板,这个的时间和下面的独立，自己控制
    if (targetTime[4])
        setTimeout(function () {
            openUpdateEntrance();
        },1000);
    else
        dropUpdateEntrance();

    //一秒钟之后收起这个倒计时
    setTimeout(function () {
        timePart._pad.style.right = '-190px';
    },1000);

    //两秒钟之后改变上传框高度
    setTimeout(function () {
        timePart._uploadArea.style.height = targetTime[0];
    },2000);

    //三秒钟之后开启另一个倒计时
    setTimeout(function (){
        openTime(targetTime[1],targetTime[2],targetTime[3],targetTime[4]);
    }, 3000);

}

function openUpdateEntrance() {
    if (isPC()){
        let announcementPad = document.getElementsByClassName('announcementPad')[0];

        setTimeout(function () {
            announcementPad.style.right = '-150px';
        },200);

        setTimeout(function () {
            let anInfo = announcementPad.getElementsByClassName('after')[0];
            anInfo.innerText = '收起';
            announcementPad.style.right = '0';
        },1400);

    }
}

function dropUpdateEntrance() {
    if (isPC()){
        let announcementPad = document.getElementsByClassName('announcementPad')[0];

        setTimeout(function () {
            let anInfo = announcementPad.getElementsByClassName('after')[0];
            anInfo.innerText = '展开';
            announcementPad.style.right = '-150px';
        },1400);

        setTimeout(function () {
            announcementPad.style.right = '-170px';
        },2600);
    }
}