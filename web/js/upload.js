window.onload = function () {

    //补充标题
    document.getElementById("week").innerText = getWeek(new Date(2020,2,1).getTime()) + "";

    //input file标签
    let file = document.getElementById("file");
    //input file 的状态（提示用户文件框是否有文件）
    let fileInfo = document.getElementById("fileInfo");
    let targetName = "";

    file.onchange = function () {
        //targetName        完整文件名
        //targetName_pre    文件名，不带后缀
        //targetName_suf    文件名后缀，带.
        targetName = file.value.split("\\")[file.value.split("\\").length - 1];
        let targetName_pre = targetName.split(".")[0];
        let targetName_suf = "." + targetName.split(".")[1];
        //不允许提交无后缀文件or文件夹
        if (targetName_suf === ".undefined"){
            alert("不支持提交文件夹或无后缀文件!请压缩后提交");
            fileInfo.innerText = "点击或直接拖拽文件至此";
            file.value = '';
            return false;
        }

        if (file.value.length === 0) {
            fileInfo.innerText = "点击或直接拖拽文件至此";
            return;
        }

        //只要不是文件夹or无后缀文件，都将文件名上传到服务器进行一次检测，防止学号姓名打错了的情况。
        reviseFileName(targetName_pre,targetName_suf);

    };


    file.ondragenter = function () {
        if (targetName !== "")
            fileInfo.innerText = "释放以更改即将上传的文件！";
        else
            fileInfo.innerText = "释放以添加上传文件！";
    };
    file.ondragleave = function () {
        if (targetName === "")
            fileInfo.innerText = "点击或直接拖拽文件至此";
        else {
            fileInfo.innerText = targetName + "\n即将被上传";
            // submit.style.display = "inline-block";
        }
    };

    //根据服务器存在的文件填充人员名单
    (function getStatus() {
        let xmlHttp = new XMLHttpRequest();
        xmlHttp.open('GET','show');
        xmlHttp.send(null);
        xmlHttp.onreadystatechange = function(){
            let showList;
            if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
                let plist = xmlHttp.responseText.substring(1, xmlHttp.responseText.length - 1);
                showList = plist.split(", ");
                for (let i = 0; i < showList.length; i++) {
                    if (showList[i].length < 15) {
                        let sn = parseInt(showList[i].substring(0, 2)) - 1;
                        let td = document.getElementsByTagName("td")[sn];
                        td.innerText = showList[i];
                        td.className = "green";
                    } else {
                        if (showList[i].startsWith("20160802019")) {
                            let td = document.getElementsByTagName("td")[30];
                            td.innerText = showList[i];
                            td.className = "green";
                        }
                        if (showList[i].startsWith("20160802004")) {
                            let td = document.getElementsByTagName("td")[31];
                            td.innerText = showList[i];
                            td.className = "green";
                        }
                    }

                }
            }
        }
    })();

};

//TODO 上传添加动画
//TODO 取消上传成功界面,文件拖到上传框触发动画,直接将文件上传,然后点亮(动画 就是 将上传框的文件移动到对应文件筐中去,然后点亮)
// 做动画成本有点高。。以后太闲的时候再说吧！