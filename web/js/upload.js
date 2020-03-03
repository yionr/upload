window.onload = function () {
    getList();
    const file = document.getElementById("file");
    const fileInfo = document.getElementsByTagName("span")[0];
    const submit = document.getElementsByClassName("submit")[0];
    const reset = document.getElementsByClassName("reset")[0];
    const show = document.getElementsByClassName("show")[0];
    let length;
    let targetName = "";
    let targetName_pre = "";
    let targetName_suf = "";
    let number = "";
    let name = "";
    let showList = [];
    file.onchange = function () {
        console.log(file.value);
        length = file.value.split("\\").length;
        targetName = file.value.split("\\")[length - 1];
        targetName_pre = targetName.split(".")[0];
        targetName_suf = "." + targetName.split(".")[1];
        if (targetName_suf === ".undefined"){
            alert("不支持提交文件夹!请压缩后提交");
            fileInfo.innerText = "点击或直接拖拽文件至此";
            return false;
        }
        console.log(targetName);
        console.log(targetName_pre);
        console.log(targetName_suf);
        if (file.value.length === 0) {
            fileInfo.innerText = "点击或直接拖拽文件至此";
            submit.style.display = "none";
            reset.style.display = "none";
            return;
        }
        //校验上传的文件名,如果不正确的话,整改
        var reg = /^\d{2}.{2,3}$/; //格式必须为20170816109林鹏 学号必须是11位在前面 名字可以为两个字or三个字在后面
        var reg_number = /^\d{2}$/;
        var reg_name = /^.{2,3}$/;
        if (!reg.test(targetName_pre)){
            //如果不是这个格式的,看看是不是另外两个,也不是的话教育他们,是的话通过
            if (targetName_pre.startsWith("2016")){

            }
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
        reset.style.display = "inline-block";
    }
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
            submit.style.display = "inline-block";
            reset.style.display = "inline-block";
        }
    }
    reset.onclick = function () {
        submit.style.display = "none";
        reset.style.display = "none";
        fileInfo.innerText = "点击或直接拖拽文件至此";
    }
    // show.onclick = function () {
        // show.style.display = 'none';
        // document.getElementsByTagName("table")[0].style.display = 'inline-block';

}
function getList() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open('GET','show');
    xmlHttp.send(null);
    xmlHttp.onreadystatechange = function(){
        if (xmlHttp.status === 200 && xmlHttp.readyState === 4){
            var plist = xmlHttp.responseText.substring(1,xmlHttp.responseText.length - 1 );
            console.log(plist);
            showList = plist.split(", ");
            console.log(typeof showList);
            console.log(showList);
            //    至此,能将已交的人名单列出,存入showlist数组,每一项为xxxx.xxxx
            //    然后做一个表格,点show的时候显示表格,把对应学号入位
            //    我们班一共多少人来着....29 就当30把 表格要扁一点,so 3x10
            for(var i =0;i< showList.length;i++){
                if (showList[i].length < 15){
                    var sn = parseInt(showList[i].substring(0,2)) - 1;
                    var td = document.getElementsByTagName("td")[sn];
                    td.innerText = showList[i];
                    td.className = "green";
                }
                else{
                    if (showList[i].startsWith("20160805019")){
                        var td = document.getElementsByTagName("td")[30];
                        td.innerText = showList[i];
                        td.className = "green";
                    }
                    if (showList[i].startsWith("20160802004")){
                        var td = document.getElementsByTagName("td")[31];
                        td.innerText = showList[i];
                        td.className = "green";
                    }
                }

            }
        }
    }
}