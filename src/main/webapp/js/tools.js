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

function openUploadEntrance(){
    let uploadArea = document.getElementsByClassName("uploadArea")[0];
    uploadArea.style.height = '55%';
}

function openDeadLine() {
    //第一个数字
    let fn = document.getElementById('fn');
    //第一个刻度
    let fu = document.getElementById('fu');

    let kd = document.getElementById('kd');
    //第二个数字
    let sn = document.getElementById('sn');
    //第二个刻度
    let su = document.getElementById('su');

    let pad = document.getElementsByClassName('deadline')[0];

    let uploadArea = document.getElementsByClassName('uploadArea')[0];

    let tempkd = 0;

    kds = setInterval(function () {
        if (++tempkd > 1)
            tempkd = 0;
        kd.style.opacity = tempkd;
    },1000);

    let date = new Date();

    let lastDays = 5 - date.getDay();

    //修正1小时
    let lastHours = 11 - date.getHours();
    //尝试修正1分钟
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

    //设置 fn 和 sn 的初始数值

    //不能这么用interval，他不是从现在开始的，
    if (lastDays > 0){
        fu.innerText = '天';
        su.innerText = '时';
        fn.innerText = lastDays+"";
        sn.innerText = lastHours+"";

        setTimeout(function () {
            sn.innerText = parseInt(sn.innerText) - 1;
            let sns = setInterval(function () {
                sn.innerText = parseInt(sn.innerText) - 1;
                if (parseInt(sn.innerText) === -1){
                    if (parseInt(fn.innerText) > 0){
                        fn.innerText = parseInt(fn.innerText) - 1;
                        sn.innerText = '23';
                    }
                    else{
                        fn.innerText = '';
                        fu.innerText = '截';
                        sn.innerText = '止';
                        su.innerText = '';
                        pad.style.backgroundColor = 'gray';
                        clearInterval(kds);
                        kd.style.opacity = '0';
                        setTimeout(function () {
                            pad.style.display = 'none';
                            uploadArea.style.height = '0';
                        },2000);
                        clearInterval(sns);
                    }
                }
            },3600000)
        },lastMinuts*60*1000)
    }
    else if (lastHours > 0){
        fu.innerText = '时';
        su.innerText = '分';
        fn.innerText = lastHours+"";
        sn.innerText = lastMinuts+"";
        pad.style.backgroundColor = 'yellow';

        setTimeout(function () {
            sn.innerText = parseInt(sn.innerText) - 1;
            let sns = setInterval(function () {
                sn.innerText = parseInt(sn.innerText) - 1;
                if (parseInt(sn.innerText) === -1){
                    if (parseInt(fn.innerText) > 0){
                        fn.innerText = parseInt(fn.innerText) - 1;
                        sn.innerText = '59';
                    }
                    else{
                        fn.innerText = '';
                        fu.innerText = '截';
                        sn.innerText = '止';
                        su.innerText = '';
                        pad.style.backgroundColor = 'gray';
                        clearInterval(kds);
                        kd.style.opacity = '0';
                        setTimeout(function () {
                            pad.style.display = 'none';
                            uploadArea.style.height = '0';
                        },2000);
                        clearInterval(sns);
                    }
                }
            },60000)
        },lastSeconds*1000)

    }
    else{
        fu.innerText = '分';
        su.innerText = '秒';
        fn.innerText = lastMinuts+"";
        sn.innerText = lastSeconds+"";
        pad.style.backgroundColor = 'red';

        setTimeout(function () {
            let sns = setInterval(function () {
                sn.innerText = parseInt(sn.innerText) - 1;
                if (parseInt(sn.innerText) === -1) {
                    if(parseInt(fn.innerText) > 0){
                        fn.innerText = parseInt(fn.innerText) - 1;
                        sn.innerText = '59';
                    }
                    else{
                        fn.innerText = '';
                        fu.innerText = '截';
                        sn.innerText = '止';
                        su.innerText = '';
                        pad.style.backgroundColor = 'gray';
                        clearInterval(kds);
                        kd.style.opacity = '0';
                        setTimeout(function () {
                            pad.style.display = 'none';
                            uploadArea.style.height = '0';
                        },2000);
                        clearInterval(sns);
                    }
                }
            },1000);
        },lastMillSeconds);
    }

    let deadLine = document.getElementsByClassName("deadline")[0];
    deadLine.style.opacity = '1';
}
