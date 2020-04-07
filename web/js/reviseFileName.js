function reviseFileName(targetName_pre,targetName_suf) {
    //去除所有的空格
    targetName_pre = targetName_pre.replace(/ /g,"");

    let id,name;
    //打算加一种简单的 姓名+学号,目前所有姓名+学号的判断策略，没有一个扩展，必须是姓名+学号 不能有其他东西，学号2位 11位无所谓
    let regNumber = /\d/;
    //如果开头不是数字，结尾是数字的话，则判断为姓名+学号
    if(!regNumber.test(targetName_pre.substring(0,1)) && regNumber.test(targetName_pre.substring(targetName_pre.length - 1,targetName_pre.length))){

        id = targetName_pre.match(/\d+$/) + "";
        //相比\D+ , \D{2,3} 不存在任何优势
        name = targetName_pre.match(/^\D+/);
    }
    //除此之外的所有情况一律以下处理
    else{
        id = targetName_pre.match(/^\d+/) + "";
        //相比\D+ , \D{2,3} 不存在任何优势
        name = targetName_pre.match(/\D+$/);
    }

    let file = document.getElementById('file');

    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST","revise");
    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    //将原始文件名也发给服务器，方便以后增加内容
    xmlHttp.send("id=" + id + "&name=" + name + "&oFName=" + targetName_pre + targetName_suf);
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
            if (xmlHttp.responseText === 'false'){
                targetName_pre = userInput();
            }
            //当返回值以correctId开头，代表查找到了用户名，但是用户输入的id错了，给予提示
            else if (xmlHttp.responseText.startsWith("correctId")){
                let userId = xmlHttp.responseText.split(':')[1];
                if (confirm("用户: " + name + " 的学号为：" + userId +"\n而您输入的学号为: " + id + " ，是否纠正？")){
                    if (userId.startsWith("2016"))
                        targetName_pre = userId + name;
                    else
                        if (userId.length === 11)
                            targetName_pre = userId.substring(9,11) + name;
                        else
                            targetName_pre = userId + name;
                }
                else{
                    alert("本站不允许上传学号-姓名不对应的文件，请联系站长解决");
                    location.reload();
                    return;
                }
            }
            //当返回值以correctName开头，代表查找到了用户名，但是用户输入的id错了，给予提示
            else if (xmlHttp.responseText.startsWith("correctName")){
                let userName = xmlHttp.responseText.split(':')[1];
                if (confirm("学号为: " + id + " 的用户的姓名为: " + userName +"\n而您输入的姓名为: " + name + "，是否纠正？")){
                    if (id.startsWith("2017"))
                        targetName_pre = id.substring(9,11) + userName;
                    else if (id.length === 2 || id.startsWith("2016"))
                        targetName_pre = id + userName;
                    }
                else{
                    alert("本站不允许上传学号-姓名不对应的文件，请联系站长解决");
                    location.reload();
                    return;
                }
            }
            else if (xmlHttp.responseText.startsWith('cookie')){
                let userInfo = xmlHttp.responseText.split(':')[1];
                let userId = userInfo.split('.')[0];
                let username = userInfo.split('.')[1];
                if (confirm("无法识别文件名，请问你是: " + username + " 吗？")){
                    targetName_pre = userId + username;
                }
                else{
                //    用户不承认上次成功提交的数据为本人，这时让他自己填写
                    targetName_pre = userInput();
                }
            }
            //正常情况下这个应该永远也不会触发，非上传时间，uploadArea.display = none 不会看到上传框的，这样做是防止意外
            else if (xmlHttp.responseText === 'over'){
                file.value = '';
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

/**
 * 此方法封装用户手动输入学号和姓名（初步合法）步骤
 * @returns {string} 返回用户输入的学号+姓名
 */
function userInput(){
    alert("无法识别文件归属，请根据提示输入学号，姓名：");
    let fixId = "",fixName="";
    while(!(/^\d{2}$/.test(fixId)) && !(/^20160802019$/.test(fixId)) && !(/^20160802004$/.test(fixId)))
        fixId = prompt("请输入学号尾号（16级的请输入完整学号）:");
    while(!(/^\D{2,3}$/.test(fixName)))
        fixName = prompt("请输入姓名：");
    return fixId +fixName;
}
