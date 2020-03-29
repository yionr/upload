function reviseFileName(targetName_pre,targetName_suf) {
    //去除所有的空格
    targetName_pre = targetName_pre.replace(/ /g,"");
    let id = targetName_pre.match(/^\d+/);
    let name = targetName_pre.match(/\D{2,3}$/);

    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST","revise");
    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    //将原始文件名也发给服务器，方便以后增加内容
    xmlHttp.send("id=" + id + "&name=" + name + "&oFName=" + targetName_pre + targetName_suf);
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
            if (xmlHttp.responseText === 'false'){
                alert("无法识别文件归属，请根据提示输入学号，姓名：");
                let fixId = "",fixName="";
                while(!(/^\d{2}$/.test(fixId)) && !(/^20160802019$/.test(fixId)) && !(/^20160802004$/.test(fixId)))
                    fixId = prompt("请输入学号尾号（16级的请输入完整学号）:");
                while(!(/^\D{2,3}$/.test(fixName)))
                    fixName = prompt("请输入姓名：");
                targetName_pre = fixId + fixName;

            }
            //当返回值是11位or2位，代表查找到来用户名，但是用户输入的id错了，给予提示
            else if (xmlHttp.responseText.length === 11){
                if (confirm(name + "的学号为：" + xmlHttp.responseText +"\n而您输入的学号为：" + id + "，是否纠正？")){
                    if (xmlHttp.responseText.startsWith("2016"))
                        targetName_pre = xmlHttp.responseText + name;
                    else
                        targetName_pre = xmlHttp.responseText.substring(9,11) + name;
                }

                else{
                    alert("本站不允许上传学号-姓名不对应的文件，请联系站长解决");
                    location.reload();
                    return;
                }
            }
            else if (xmlHttp.responseText.length === 2){
                if (confirm(name + "的班内学号为：" + xmlHttp.responseText +"\n而您输入的学号为：" + id + "，是否纠正？"))
                    targetName_pre = xmlHttp.responseText + name;
                else{
                    alert("本站不允许上传学号-姓名不对应的文件，请联系站长解决");
                    location.reload();
                    return;
                }
            }
            //正常情况下这个应该永远也不会触发，非上传时间，uploadArea.display = none 不会看到上传框的，这样做是防止意外
            else if (xmlHttp.responseText === 'over'){
                alert("当前时间无法上传");
                return;
            }
            else{
                targetName_pre = xmlHttp.responseText;
            }
            //将文件上传到服务器时需要读取
            let filename = document.getElementById("fileName");
            filename.value = targetName_pre + targetName_suf;
            let form = document.getElementsByTagName("form")[0];
            form.submit();
        }
    }
}
