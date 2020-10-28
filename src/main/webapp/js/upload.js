$(() =>{
        //工作日展开上传框和截止时间
        if (isWorkingDay()){
            openUploadEntrance();
            if (isPC()){
                openTime(5,12,'deadline',true);
                openUpdateEntrance();
            }
            else
                closeParts();
        }
//非工作日：打开倒计时 倒计时到了展开上传框   不过如果打开倒计时的话，位置呢？肯定会撞，另外如果不放倒计时，那么早上8点，应该也没人会看这个网页，那么做上传框好像没有太大用处，这个暂时不做，因为没有价值
        else{
            if (isPC())
                openTime(2,8,'openline',false);
            else
                closeParts();
        }


//补充标题
        $('#week').text(getWeek(new Date(2020,2,4,0,0,0,0).getTime()))
//input file标签
        let file = $('#file')
//input file 的状态（提示用户文件框是否有文件）
        let fileInfo = $('#fileInfo');
        let targetName = "";

        file.change(function () {
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

            //做动画在这里做
            let animation = $('#animation');
            let deg = 0;
            fileInfo.remove();
            animation.style.display = 'inline-block';
            setInterval(function () {
                //控制旋转的速度
                deg += 2;
                animation.style.transform = 'rotate(' + deg +'deg)';
            },1000/60);

            //只要不是文件夹or无后缀文件，都将文件名上传到服务器进行一次检测，防止学号姓名打错了的情况。
            reviseFileName(targetName_pre,targetName_suf);

        })

        file.ondragenter = function () {
            if (targetName !== "")
                fileInfo.innerText = "释放以更改即将上传的文件！";
            else
                fileInfo.innerText = "释放以添加上传文件！";
        }
        file.ondragleave = function () {
            if (targetName === "")
                fileInfo.innerText = "点击或直接拖拽文件至此";
            else {
                fileInfo.innerText = targetName + "\n即将被上传";
                // submit.style.display = "inline-block";
            }
        }
})


function closeParts() {
    let pcOnly = $('.pcOnly');
    for (let i = 0;i < pcOnly.length;i++){
        pcOnly[i].style.display = 'none';
    }
}
