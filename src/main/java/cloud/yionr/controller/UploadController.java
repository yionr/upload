package cloud.yionr.controller;

import cloud.yionr.Exception.IdNotMatchException;
import cloud.yionr.Exception.StudentNotFoundException;
import cloud.yionr.Exception.SysException;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO 做一个可以查看历史上传的功能
@Controller
public class UploadController {
    @Autowired
    StudentService studentService;

    private Logger logger = Logger.getLogger(UploadController.class);

    @RequestMapping("/uploadHomework")
    public String UploadGroupByWeek(MultipartFile file, HttpServletRequest req, @RequestParam("fileName") String fileName) throws SysException, StudentNotFoundException, IdNotMatchException {

        logger.info("try to submit a file>>> ip: " + req.getRemoteAddr() + ">>>fileName: " + fileName);
//        到这儿，肯定是学号+姓名的形式了，学号存在2位和11位的情况 11位两个学号是确定的，2位可以是随意两位
        String fileName_suf = fileName.split("\\.")[0];
//        根据fileName获取id
        String id = fileName_suf.split("\\D+$")[0];
//        获取name    1好像和split的特性有关，暂时不是很清楚
        String name = fileName_suf.split("^\\d+")[1];

        Student student = studentService.FindByName(name);
        logger.info("fileName_suf: " + fileName_suf + " id: " + id + " name: " + name + "   searched: " + student);
        if (!(student == null)) {
//            这里要分两种情况，学号2位和11位
            if ((name.equals("吴伟") || name.equals("胡凯伦")) && !student.getId().equals(id))
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            else if (!student.getId().substring(9).equals(id)) {
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            } else {
                //获取当前周（相对于开学）

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime baseDate = LocalDateTime.of(2020, 3, 1, 0, 0);
//              这里加上1 ： 原本是21-1 = 20 /7 = 2 导致21号归档到第三周 而22号归档到第四周，加上一偏移之后，周次正确
                int weekNum = (now.getDayOfYear() + 1 - baseDate.getDayOfYear()) / 7 + 1;
                //创建当前周的文件夹
//                File CurrentWeekDir = new File("/root/homeWork/" + weekNum);
                //测试环境下用下面地址
                File CurrentWeekDir = new File("/Users/Yionr/homeWork/" + weekNum);
                if (!CurrentWeekDir.exists())
                    CurrentWeekDir.mkdirs();
                //创建作业
                //FIXME 可能会出现前缀相同但是后缀不同的情况，但是这种情况即使提交了，也不会覆盖原文件，阻止用户二次提交相同文件的本意是防止被别的用户乱搞
                File homeWork = new File(CurrentWeekDir, fileName);
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
        else{
            throw new StudentNotFoundException("数据库中无"+ name + "的记录！");
        }
    }

}