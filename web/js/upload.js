window.onload = function () {
    //相对于开学，当前是第几周
    let week = Math.floor(((new Date().getTime() - new Date(2020,2,1).getTime())/1000/3600/24 + 1) / 7 + 1);
    document.getElementById("week").innerText = week;


    const file = document.getElementById("file");
    const fileInfo = document.getElementById("fileInfo");
    const submit = document.getElementsByClassName("submit")[0];
    let length;
    let targetName = "";
    let targetName_pre = "";
    let targetName_suf = "";
    let number = "";
    let name = "";
    file.onchange = function () {
        // console.log(file.value);
        length = file.value.split("\\").length;
        targetName = file.value.split("\\")[length - 1];
        targetName_pre = targetName.split(".")[0];
        targetName_suf = "." + targetName.split(".")[1];
        if (targetName_suf === ".undefined"){
            alert("不支持提交文件夹或无后缀文件!请压缩后提交");
            fileInfo.innerText = "点击或直接拖拽文件至此";
            file.value = '';
            return false;
        }
        // console.log(targetName);
        // console.log(targetName_pre);
        // console.log(targetName_suf);
        if (file.value.length === 0) {
            fileInfo.innerText = "点击或直接拖拽文件至此";
            submit.style.display = "none";
            return;
        }
        //校验上传的文件名,如果不正确的话,整改
        let reg = /^\d{2}.{2,3}$/; //格式必须为20170816109林鹏 学号必须是11位在前面 名字可以为两个字or三个字在后面
        let reg_number = /^\d{2}$/;
        let reg_name = /^.{2,3}$/;
        if (!reg.test(targetName_pre)){
            //如果不是这个格式的,看看是不是另外两个,也不是的话教育他们,是的话通过
            if (targetName_pre.startsWith("2016")){

            }
            //TODO 当文件被拖到上传框的时候，触发一次ajax，将文件名称拆分为 学号尾号部分 和 姓名部分 并传给服务器，请求查询数据库，从数据库返回信息，以名字为主要校验位，if名字无法匹配到，则提醒用户，手动输入学号姓名，if名字匹配到了，但是学号行尾号错了，返回给用户正确学号尾号以校对，当学号尾号正确，则直接触发上传，当不存在学号尾号，则自动为其添加学号
            else{
                alert("文件命名格式不正确,请根据接下来的引导输入信息");
                while(!reg_number.test(number))
                    number = prompt("请输入班内学号,是班内!懂?比如我是09:");
                while(!reg_name.test(name))
                    name = prompt("请输入姓名:");
                targetName_pre = number + name;
                targetName = targetName_pre + targetName_suf;
            }

        }
        document.getElementById("fileName").value = targetName;
        fileInfo.innerText = targetName + "\n即将被上传";
        submit.style.display = "inline-block";
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
            submit.style.display = "inline-block";
        }
    };

    function getStatus() {
        let xmlHttp = new XMLHttpRequest();
        xmlHttp.open('GET','show');
        xmlHttp.send(null);
        xmlHttp.onreadystatechange = function(){
            let showList;
            if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
                let plist = xmlHttp.responseText.substring(1, xmlHttp.responseText.length - 1);
                // console.log(plist);
                showList = plist.split(", ");
                // console.log(typeof showList);
                // console.log(showList);
                //    至此,能将已交的人名单列出,存入showlist数组,每一项为xxxx.xxxx
                //    然后做一个表格,点show的时候显示表格,把对应学号入位
                //    我们班一共多少人来着....29 就当30把 表格要扁一点,so 3x10
                for (let i = 0; i < showList.length; i++) {
                    if (showList[i].length < 15) {
                        let sn = parseInt(showList[i].substring(0, 2)) - 1;
                        let td = document.getElementsByTagName("td")[sn];
                        // console.log(sn);
                        // console.log(td);
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
    }

    getStatus();
};

//TODO 上传添加动画
//TODO 取消上传成功界面,文件拖到上传框触发动画,直接将文件上传,然后点亮(动画 就是 将上传框的文件移动到对应文件筐中去,然后点亮)