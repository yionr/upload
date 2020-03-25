function roll(e) {
    let pad = e.parentNode;
    let offsetWidth = pad.offsetWidth;
    if (pad.className === 'tipPad'){
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

