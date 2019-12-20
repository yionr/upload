package cloud.yionr.controller;

import cloud.yionr.Exception.SysException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class UploadController {
    @RequestMapping("/upload")
    public String Upload(MultipartFile file, HttpServletRequest request) throws SysException {
        File f = new File(request.getServletContext().getRealPath("/WEB-INF/Content"));
        File f1 = new File(f,request.getRemoteAddr().replace(":",""));
        if (!f1.exists()) {
            if (!f.exists())
                f.mkdirs();
            f1.mkdirs();
        }
        File f2 = new File(f1,file.getOriginalFilename());
        //当用户上传过了相同的文件时,之前的文件也保留,改名目前上传的文件
        if (f2.exists()){
            f2 = new File(f1,new Date().getTime() + file.getOriginalFilename());
        }

        try {
            f2.createNewFile();
            file.transferTo(f2);
        } catch (IOException e) {
            throw new SysException(e.getMessage());
        }

        return "uploadok";
    }


    //添加文件名校验功能，只能上传学号加姓名的文件，其余的一概拒绝，并显示格式错误的地方

}
