package cloud.yionr.controller;

import cloud.yionr.Exception.SysException;
import org.apache.log4j.Logger;
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

    private Logger logger = Logger.getLogger(UploadController.class);

    @RequestMapping("/uploadHomework")
    public String UploadGroupByWeek(MultipartFile file, HttpServletRequest req, @RequestParam("fileName") String fileName) throws SysException {

        logger.info("try to submit a file>>> ip: " + req.getRemoteAddr() + ">>>fileName: " + fileName);

        //获取当前周（相对于开学）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = LocalDateTime.of(2020,3,1,0,0);
//        这里加上1 ： 原本是21-1 = 20 /7 = 2 导致21号归档到第三周 而22号归档到第四周，加上一偏移之后，周次正确
        int weekNum = (now.getDayOfYear() + 1 - baseDate.getDayOfYear())/7 + 1;
        //创建当前周的文件夹
//        File CurrentWeekDir = new File(req.getServletContext().getRealPath("/WEB-INF/homeWork/第" + weekNum + "周作业"));
        File CurrentWeekDir = new File("/root/homeWork/" + weekNum);
        if (!CurrentWeekDir.exists())
            CurrentWeekDir.mkdirs();
        //创建作业
        //FIXME 可能会出现前缀相同但是后缀不同的情况，但是这种情况即使提交了，也不会覆盖原文件，阻止用户二次提交相同文件的本意是防止被别的用户乱搞
        File homeWork = new File(CurrentWeekDir,fileName);
        //TODO 用户拥有纠错的机会，重新上传，遇到同名文件时，比较两个文件大小，提供文件修改日期并提醒用户是否替换
        if (homeWork.exists())
            throw new SysException("服务器上已经存在此作业!");
        try {
            homeWork.createNewFile();
            logger.info("serverFileCreated!");
            file.transferTo(homeWork);
            logger.info("serverFileTransferSuccess!");
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            throw new SysException("在服务器上创建文件时遇到未知错误，上传失败!");
        }

    }

}
