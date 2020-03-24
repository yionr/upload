function roll(e) {
    let pad = e.parentNode;
    let offsetWidth = pad.offsetWidth;
    if (pad.className === 'tipPad'){
        //FIXME 只能暂时用这个代替一下，没有offsetRight，这样可能会有潜在的bug
        let offsetRight = pad.style.right;
        console.log(offsetRight);
        if (offsetRight != '-150px'){
            pad.style.right = - offsetWidth + "px";
            e.innerText = '展开';
        }
        else if (offsetRight == '-150px'){
            pad.style.right = 0 + "px";
            e.innerText = '收起';
        }
    }
    else{
        let offsetLeft = pad.offsetLeft;
        console.log(offsetLeft);
        if (offsetLeft === 0) {
            pad.style.left = -offsetWidth + "px";
            e.innerText = '展开';
        }
        else if (offsetLeft === -150){
            pad.style.left = 0 + "px";
            e.innerText = '收起';
        }
    }
}

//FIXME 公告板在第一次收起的时候会无视css transition 不知道如何解决

//TODO 这两个功能，都得加cookie才能用，主要有两种cookie 一个是展开情况 一个是checkbox的情况

//TODO 另外，后期如何给他们添加内容呢？tip可以很僵硬的直接在html加，但是公告的话，我觉得需要一个后台系统，能发布公告。后期再扩充内容 后台可以不直接暴露，比如必须指定一个url，只有我知道的，然后 java处理，给我返回一个网页，里面能新建公告，并发布，发布之后，提交的内容如何更新到前端呢？前段得有个js专门处理这个事情，每次打开网页的时候，都用ajax去看一个地方，如果这个思路的话，好像就用不到后台了，ajax去找公告，那java肯定就去找文件了，比如一个专门写公告的文件，里面每一行对应一个公告，java给他解析出来发给ajax，然后ajax根据返回的公告数组，每一条公告新建一个section即可