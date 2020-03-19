//用到这个函数的地方，那就是命名有问题的。
function reviseFileName(targetName_pre) {
    let id = targetName_pre.match(/^\d+/);
    let name = targetName_pre.match(/\D{2,3}$/);
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST","revise");
    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    //将学号和姓名发送给服务器，服务器可以直接处理数据，最终返回正确的文件名
    //FIXME 当用户输错了名字的时候，不过这种情况真的应该不多，暂时不考虑
    xmlHttp.send("id=" + id + "&name=" + name);
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.status === 200 && xmlHttp.readyState === 4) {
            console.log("return content is：" + xmlHttp.responseText);

            if (xmlHttp.responseText === 'false'){
            //    如果服务器返回false的话，代表服务器无法找到姓名对应的学号，此时给用户报错，我们班没有这个人！
                alert("不好意思，我们班没有这个人！有任何问题请联系站长");
                file.value = '';
                return false;
            }
            else{
            //    只要不是返回false，则将返回的内容直接作为文件名
                filename.value = xmlHttp.responseText;
                alert(filename.value);
                //本来是显示xx即将被上传，并且显示上传按钮，现在能否直接上传呢？
                // fileInfo.innerText = xmlHttp.responseText + "\n即将被上传";
                // submit.style.display = "inline-block";
                let form = document.getElementsByTagName("form")[0];
                console.log(form);
                form.submit();
            }
        }
    }
}