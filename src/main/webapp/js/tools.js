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
    let uploadArea = document.querySelector(".uploadArea");
    uploadArea.style.height = '55%';
}

class Time {
    constructor(padName) {
        this.padName = padName;
        this.pad = document.getElementsByClassName(padName)[0];
        this.fn = this.pad.getElementsByClassName('fn')[0];
        this.fu = this.pad.getElementsByClassName('fu')[0];
        this.kd = this.pad.getElementsByClassName('kd')[0];
        this.sn = this.pad.getElementsByClassName('sn')[0];
        this.su = this.pad.getElementsByClassName('su')[0];
        this.uploadArea = document.getElementsByClassName('uploadArea')[0];
    }
    setTime(fn,fu,sn,su){
        this.fn.innerText = fn;
        this.fu.innerText = fu;
        this.sn.innerText = sn;
        this.su.innerText = su;
    }
    setColor(color){
        this.pad.style.backgroundColor = color;
    }
    start(){
        this.pad.style.right = '20px';
        let e = this;
        setTimeout(function () {
            e.pad.style.right = '10px';
        },700);
    }

    flow(timeout1,timeout2,maxSn,kds,color){
        let e = this;
        setTimeout(function () {
            e.sn.innerText = parseInt(e.sn.innerText) - 1;
            if (parseInt(e.sn.innerText) === -1){
                if (parseInt(e.fn.innerText) > 0){
                    e.fn.innerText = parseInt(e.fn.innerText) - 1;
                    e.sn.innerText = maxSn;
                }
                else{
                    dropTime(e.padName,e,kds,sns,color);
                }
            }
            let sns = setInterval(function () {
                e.sn.innerText = parseInt(e.sn.innerText) - 1;
                if (parseInt(e.sn.innerText) === -1){
                    if (parseInt(e.fn.innerText) > 0){
                        e.fn.innerText = parseInt(e.fn.innerText) - 1;
                        e.sn.innerText = maxSn;
                    }
                    else{
                        dropTime(e.padName,e,kds,sns,color);
                    }
                }
            },timeout1);
        },timeout2)
    }

}

class LastTime{

    constructor(now,targetWeekDay,targetHour) {
        //传入2.8 倒计时不准确  2 - 5 = 3 小于0 则一周时间(到下一周了u)
        this.lastDays = targetWeekDay - now.getDay() < 0 ? targetWeekDay - now.getDay() + 7 : targetWeekDay - now.getDay();
        //修正1小时
        this.lastHours = targetHour - 1 - now.getHours();
        //修正1分钟
        this.lastMinuts = 59 - now.getMinutes();

        this.lastSeconds = 60 - now.getSeconds();

        this.lastMillSeconds = 1000 - now.getMilliseconds();
        if (this.lastHours < 0){
            this.lastDays--;
            this.lastHours += 24;
        }
        console.log(this.lastDays + " , " + this.lastHours + " , " + this.lastMinuts);
        //test
        // this.lastDays = this.lastHours = 0;
        // this.lastDays = this.lastHours = 0;
        // this.lastMinuts = 1;
        // this.lastSeconds = 0;

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
        timePart.kd.style.opacity = tempKd;
    },1000);

    let lastTime = new LastTime(new Date(),weekDay,hour);

    if (lastTime.lastDays > 0){
        timePart.setTime(lastTime.lastDays+"",'天',lastTime.lastHours+"",'时');
        timePart.setColor(color[0]);
        timePart.flow(3600000,lastTime.lastMinuts * 60 * 1000,'23',kds,color);
    }
    else if (lastTime.lastHours > 0){
        timePart.setTime(lastTime.lastHours+"",'时',lastTime.lastMinuts+"",'分');
        timePart.setColor(color[1]);
        timePart.flow(60000,lastTime.lastSeconds * 1000,'59',kds,color);
    }
    else{
        timePart.setTime(lastTime.lastMinuts+"",'分',lastTime.lastSeconds+"",'秒');
        timePart.setColor(color[2]);
        timePart.flow(1000,lastTime.lastMillSeconds,'59',kds,color);
    }
}

function dropTime(padName, timePart, kds, sns, color){

    //让中间的指针不再闪
    clearInterval(kds);
    //让中间指针隐藏
    timePart.kd.style.opacity = '0';
    //让时间计算停下
    clearInterval(sns);

    let targetTime;
    if (padName === 'deadline'){
        timePart.fn.innerText = '';
        timePart.fu.innerText = '截';
        timePart.sn.innerText = '止';
        timePart.su.innerText = '';
        targetTime = [0,2,8,'openline',false];
    }
    else if (padName === 'openline'){
        timePart.fn.innerText = '';
        timePart.fu.innerText = '开';
        timePart.sn.innerText = '放';
        timePart.su.innerText = '';
        targetTime = ['55%',5,12,'deadline',true];
    }
    //倒计时结束，设置背景色
    timePart.pad.style.backgroundColor = color[3];

    //关闭更新面板,这个的时间和下面的独立，自己控制
    if (targetTime[4])
        setTimeout(function () {
            openUpdateEntrance();
        },1000);
    else
        dropUpdateEntrance();

    //一秒钟之后收起这个倒计时
    setTimeout(function () {
        timePart.pad.style.right = '-200px';
    },1000);

    //两秒钟之后改变上传框高度
    setTimeout(function () {
        timePart.uploadArea.style.height = targetTime[0];
    },2000);

    //三秒钟之后开启另一个倒计时
    setTimeout(function (){
        openTime(targetTime[1],targetTime[2],targetTime[3],targetTime[4]);
    }, 3000);

}

function openUpdateEntrance() {
    if (isPC()){
        let announcementPad = document.querySelector('.announcementPad');

        setTimeout(function () {
            announcementPad.style.right = '-150px';
        },200);

        setTimeout(function () {
            let anInfo = document.querySelector('.announcementPad .after');
            anInfo.innerHTML = '收起';
            announcementPad.style.right = '0';
        },1400);

    }
}

function dropUpdateEntrance() {
    if (isPC()){
        let announcementPad = document.querySelector('.announcementPad');

        setTimeout(function () {
            let anInfo = document.querySelector('.after');
            anInfo.innerText = '展开';
            announcementPad.style.right = '-150px';
        },1400);

        setTimeout(function () {
            announcementPad.style.right = '-170px';
        },2600);
    }
}