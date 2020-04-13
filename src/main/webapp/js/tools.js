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
    setTime(fn,fu,sn,su){
        this._fn.innerText = fn;
        this._fu.innerText = fu;
        this._sn.innerText = sn;
        this._su.innerText = su;
    }
    setColor(color){
        this._pad.style.backgroundColor = color;
    }
    start(){
        this._pad.style.right = '20px';
        let e = this;
        setTimeout(function () {
            e._pad.style.right = '10px';
        },700);
    }

    flow(timeout1,timeout2,maxSn,kds,color){
        let e = this;
        setTimeout(function () {
            e._sn.innerText = parseInt(e._sn.innerText) - 1;
            if (parseInt(e._sn.innerText) === -1){
                if (parseInt(e._fn.innerText) > 0){
                    e._fn.innerText = parseInt(e._fn.innerText) - 1;
                    e._sn.innerText = maxSn;
                }
                else{
                    dropTime(e._padName,e,kds,sns,color);
                }
            }
            let sns = setInterval(function () {
                e._sn.innerText = parseInt(e._sn.innerText) - 1;
                if (parseInt(e._sn.innerText) === -1){
                    if (parseInt(e._fn.innerText) > 0){
                        e._fn.innerText = parseInt(e._fn.innerText) - 1;
                        e._sn.innerText = maxSn;
                    }
                    else{
                        dropTime(e._padName,e,kds,sns,color);
                    }
                }
            },timeout1);
        },timeout2)
    }

}

class LastTime{
    _lastDays;
    _lastHours;
    _lastMinuts;
    _lastSeconds;
    _lastMillSeconds;

    constructor(now,targetWeekDay,targetHour) {

        this._lastDays = targetWeekDay - now.getDay();
        //修正1小时
        this._lastHours = targetHour - 1 - now.getHours();
        //修正1分钟
        this._lastMinuts = 59 - now.getMinutes();

        this._lastSeconds = 60 - now.getSeconds();

        this._lastMillSeconds = 1000 - now.getMilliseconds();
        if (this._lastHours < 0){
            this._lastDays--;
            this._lastHours += 24;
        }
        //test
        // this._lastDays = this._lastHours = 0;
        // this._lastDays = this._lastHours = 0;
        // this._lastMinuts = 1;
        // this._lastSeconds = 0;

    }
}

function openTime(weekDay, hour, padName,changeColor) {

    let timePart = new Time(padName);

    timePart.start();

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

    let lastTime = new LastTime(new Date(),weekDay,hour);

    if (lastTime._lastDays > 0){
        timePart.setTime(lastTime._lastDays+"",'天',lastTime._lastHours+"",'时');
        timePart.setColor(color[0]);
        timePart.flow(3600000,lastTime._lastMinuts * 60 * 1000,'23',kds,color);
    }
    else if (lastTime._lastHours > 0){
        timePart.setTime(lastTime._lastHours+"",'时',lastTime._lastMinuts+"",'分');
        timePart.setColor(color[1]);
        timePart.flow(60000,lastTime._lastSeconds * 1000,'59',kds,color);
    }
    else{
        timePart.setTime(lastTime._lastMinuts+"",'分',lastTime._lastSeconds+"",'秒');
        timePart.setColor(color[2]);
        timePart.flow(1000,lastTime._lastMillSeconds,'59',kds,color);
    }
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