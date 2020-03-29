package cloud.yionr.controller;

import cloud.yionr.Exception.IdNotMatchException;
import cloud.yionr.Exception.NotInTimeException;
import cloud.yionr.Exception.StudentNotFoundException;
import cloud.yionr.Exception.SysException;
import cloud.yionr.common.DateTool;
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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UploadController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    Properties properties;
    {
        properties = new Properties();
        try {
            properties.load(UploadController.class.getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Logger logger = Logger.getLogger(UploadController.class);

    @RequestMapping("/uploadHomework")
    public String UploadGroupByWeek(MultipartFile file, HttpServletRequest req, @RequestParam("fileName") String fileName) throws SysException, StudentNotFoundException, IdNotMatchException, NotInTimeException {


        logger.info("IP地址为: " + req.getRemoteAddr() + " 的用户即将开始上传文件，文件名是: " + fileName);

        DayOfWeek weekDay = dateTool.getWeekDay();
        int timeOfHour = dateTool.getHour();

        if (weekDay == DayOfWeek.FRIDAY) {
            if (timeOfHour >= 12) {
                logger.info("当前时间禁止提交作业");
                throw new NotInTimeException("当前时间禁止提交作业");
            }
        }
        if (weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY || weekDay == DayOfWeek.MONDAY){
            logger.info("当前时间禁止提交作业");
            throw new NotInTimeException("当前时间禁止提交作业");
        }
        if (weekDay == DayOfWeek.TUESDAY)
            if (timeOfHour < 8){
                logger.info("当前时间禁止提交作业");
                throw new NotInTimeException("当前时间禁止提交作业");
            }


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
//            11位不要标出来，主要应对的是'19吴伟'这种情况
            if ((name.equals("吴伟") || name.equals("胡凯伦")) && !student.getId().equals(id))
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            else if (id.length() == 2 && !student.getId().substring(9).equals(id)) {
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            } else {
                //创建当前周的文件夹
//                File CurrentWeekDir = new File("/root/homeWork/" + dateTool.getWeek());
                //测试环境下用下面地址
                File CurrentWeekDir = new File(properties.getProperty("homeWorkRoot") , dateTool.getWeek()+"");
                if (!CurrentWeekDir.exists())
                    CurrentWeekDir.mkdirs();
                //创建作业
                File homeWork = new File(CurrentWeekDir, fileName);
//                判断作业是否存在，将所有作业都提取出来，放到集合里面去，可以以全名的方式提取，也可以以前缀的方式提取
                if (homeWork.exists())
                    throw new SysException("服务器上已经存在此作业!");
                try {
                    homeWork.createNewFile();
                    logger.info("服务器文件创建成功!");
                    file.transferTo(homeWork);
                    logger.info("服务器文件写入成功!");
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