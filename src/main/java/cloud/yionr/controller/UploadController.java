package cloud.yionr.controller;

import cloud.yionr.Exception.SysException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

        //获取当前周（相对于开学）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = LocalDateTime.of(2020,3,1,0,0);
        int weekNum = (now.getDayOfYear() - baseDate.getDayOfYear())/7 + 1;
        //创建当前周的文件夹
        File CurrentWeekDir = new File(req.getServletContext().getRealPath("/WEB-INF/homeWork/第" + weekNum + "周作业"));
        if (!CurrentWeekDir.exists())
            CurrentWeekDir.mkdirs();
        //创建作业
        //FIXME 可能会出现前缀相同但是后缀不同的情况
        File homeWork = new File(CurrentWeekDir,fileName);
        System.out.println(homeWork.getAbsolutePath());
        //TODO 用户拥有纠错的机会，重新上传，遇到同名文件时，比较两个文件大小，提供文件修改日期并提醒用户是否替换
        if (homeWork.exists())
            throw new SysException("服务器上已经存在此作业!");
        try {
            homeWork.createNewFile();
            file.transferTo(homeWork);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            throw new SysException("在服务器上创建文件时遇到未知错误，上传失败!");
        }
    }

}
