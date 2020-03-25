window.onload = function () {
    //不属于提交作业的时间，不展开上传框
    if (isWorkingDay()){
        openUploadEntrance();
        openDeadLine();
    }

    //补充标题
    document.getElementById("week").innerText = getWeek(new Date(2020,2,4,8,0,0,0).getTime()) + "";

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

};