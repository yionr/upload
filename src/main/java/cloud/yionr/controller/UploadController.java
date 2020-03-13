package cloud.yionr.controller;

import cloud.yionr.Exception.SysException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

//TODO 将文件根据第几次上传分类,实现所有用户所有作业归档,然后后期做一个可以查看历史上传的功能
@Controller
public class UploadController {
    @RequestMapping("/upload")
    public String UploadAndGroupByIP(MultipartFile file, HttpServletRequest request) throws SysException {
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

        return "success";
    }
    @RequestMapping("/uploadHomework")
    public String UploadGroupByTimes(MultipartFile file,HttpServletRequest req,@RequestParam("fileName") String fileName) throws SysException {

//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
//        String fileName = multipartRequest.getParameter("fileName");
        File f = new File(req.getServletContext().getRealPath("/WEB-INF/homeWork"));
        if (!f.exists())
            f.mkdirs();
        File file1 = new File(f,fileName);
        if (file1.exists())
            throw new SysException("服务器上已经存在同名文件!");
        try {
            file1.createNewFile();
            file.transferTo(file1);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            throw new SysException("上传失败!");
        }
    }

}
