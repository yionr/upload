package cloud.yionr.controller;

import cloud.yionr.Exception.*;
import cloud.yionr.common.DateTool;
import cloud.yionr.common.ServerFileTool;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.DayOfWeek;

@Controller
public class UploadController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    @Autowired
    ServerFileTool serverFileTool;

    private Logger logger = Logger.getLogger(UploadController.class);

    @RequestMapping("/uploadHomework")
    public String UploadGroupByWeek(MultipartFile file, HttpServletRequest request, HttpServletResponse response, @RequestParam("fileName") String fileName) throws SysException, StudentNotFoundException, IdNotMatchException, NotInTimeException, FileAlreadyExsitsException {

//空指针报错是因为跳过reviseController了？
        logger.info("文件: " + fileName + " 开始上传至服务器:");

        if (!dateTool.isWorkingDay()) {
            logger.warn("当前时间禁止提交作业");
            throw new NotInTimeException("当前时间禁止提交作业");
        }


//        到这儿，肯定是学号+姓名的形式了，学号存在2位和11位的情况 11位两个学号是确定的，2位可以是随意两位
        String fileName_suf = fileName.split("\\.")[0];
//        根据fileName获取id
        String id = fileName_suf.split("\\D+$")[0];
//        获取name    1好像和split的特性有关，暂时不是很清楚
        String name = fileName_suf.split("^\\d+")[1];

        Student student = studentService.FindByName(name);
        if (!(student == null)) {
//            这里要分两种情况，学号2位和11位
//            11位不要标出来，主要应对的是'19吴伟'这种情况
            if ((name.equals("吴伟") || name.equals("胡凯伦")) && !student.getId().equals(id))
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            else if (id.length() == 2 && !student.getId().substring(9).equals(id)) {
                throw new IdNotMatchException("姓名学号不匹配，请重试！");
            } else {
                File CurrentWeekDir = new File(serverFileTool.properties.getProperty("homeWorkRoot") , dateTool.getWeek()+"");
                if (!CurrentWeekDir.exists())
                    CurrentWeekDir.mkdirs();
                //创建作业
                File homeWork = new File(CurrentWeekDir, fileName);
//                判断作业是否存在，将所有作业都提取出来，放到集合里面去，可以以全名的方式提取，也可以以前缀的方式提取
//                if (homeWork.exists())
//                    throw new SysException("服务器上已经存在此作业!");
//                通过文件前缀名，而不是之前的完整文件名来判断
                if (serverFileTool.getFileListWithoutSuf().contains(fileName.split("\\.")[0])){
                    logger.warn("服务器上已经存在此作业");
                    throw new FileAlreadyExsitsException("服务器上已经存在此作业");
                }

                try {
                    homeWork.createNewFile();
                    logger.info("服务器文件创建成功!");
                    file.transferTo(homeWork);
                    logger.info("服务器文件写入成功!");
//                    在这里创建Cookie,需要考虑一个问题，每次文件写入成功都要刷新cookie吗？
                    Cookie cookie = new Cookie("userInfo",id + "." + URLEncoder.encode(name,"utf-8"));
//                    设置cookie存活时间为一学期（半年）
                    cookie.setMaxAge(15768000);
                    response.addCookie(cookie);
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