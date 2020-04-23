package cloud.yionr.controller;

import cloud.yionr.Exception.*;
import cloud.yionr.common.DateTool;
import cloud.yionr.common.ServerFileTool;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/uploadHomework")
    public String UploadGroupByWeek(MultipartFile file, HttpServletRequest request, HttpServletResponse response, @RequestParam("fileName") String fileName) throws SysException, StudentNotFoundException, IdNotMatchException, NotInTimeException, PermissionDeniedException, SqlQueryException {

        logger.info("文件: " + fileName + " 开始上传至服务器:");

        if (!dateTool.isWorkingDay()) {
            logger.warn("当前时间禁止提交作业");
            logger.info("-----------------FAIL----------------");
            throw new NotInTimeException("当前时间禁止提交作业");
        }


//        到这儿，肯定是学号+姓名的形式了，学号存在2位和11位的情况 11位两个学号是确定的，2位可以是随意两位
//        之所以有这个二次校验,是因为,用户可以通过修改js,跳过reviseController直接来到这
        String fileName_suf = fileName.split("\\.")[0];
//        根据fileName获取id
        String id = fileName_suf.split("\\D+$")[0];
//        获取name    1好像和split的特性有关，暂时不是很清楚
        String name = fileName_suf.split("^\\d+")[1];
        logger.info("切分出学号为: " + id + " 姓名为: " + name +" 接下来根据姓名比对数据库数据,二次确认命名正确" );
        Student student = studentService.FindByName(name);
        logger.info("查找到的结果为: " + student);

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

//                通过文件前缀名，而不是之前的完整文件名来判断
                if (serverFileTool.getFileListWithoutSuf().contains(fileName.split("\\.")[0])){
                    logger.warn("服务器上已经存在此作业");
//                    判断ip
                    String lastIP = studentService.FindById(id).getLastIP();
                    logger.info("ip is: " + lastIP);
//                    这个在本地无法测试，因为本地是带端口的，但是作为beta功能，可以直接发布
                    if (request.getRemoteAddr().equals(lastIP)){
                        logger.info("此次提交ip与该用户上次提交ip相同，判定为用户自己提交的，即将覆盖原文件:");
                        try {
//                            删除根据学号姓名前缀查到的第一个文件,而不是当前名字的文件,这样能解决用户前后上传不同名文件,虽然能成功,但是结果服务器存了两个文件,原来那个文件没有删掉
//                            这样其实从某个角度,还是可能会有bug,如果返回的是所有这个前缀的文件名列表,然后所有删除才是最保险的.以后再说吧
                            serverFileTool.getFileByPre(fileName_suf).delete();
                            logger.info("原文件删除成功");
                            homeWork.createNewFile();
                            file.transferTo(homeWork);
                            logger.info("新文件写入成功");
                            logger.info("---------------SUCCESS---------------");
                            return "success";
                        }
                        catch (Exception e){
                            logger.info("-----------------FAIL----------------");
                            throw new SysException("在服务器上更新文件时遇到未知错误，更新失败!");
                        }
                    }
                    else if (lastIP == null){
                        logger.info("-----------------FAIL----------------");
                        throw new SqlQueryException("查询上次提交IP失败，故无法确定权限，请重试!");
                    }
                    else{
                        logger.warn("更新文件时的ip和提交时的ip不同，没有权限更新文件!");
                        logger.info("-----------------FAIL----------------");
                        throw new PermissionDeniedException("您没有权限更新文件!");
                    }
                }

                try {
                    homeWork.createNewFile();
                    logger.info("服务器文件创建成功!");
                    file.transferTo(homeWork);
                    logger.info("服务器文件写入成功!");

//                    将ip记录到数据库
                    Student student1 = new Student();

                    if (id.length() == 2)
                        student1.setId("201708161" + id);
                    else if (id.length() == 11)
                        student1.setId(id);

                    student1.setLastIP(request.getRemoteAddr());
                    logger.info("即将更新学号: " + student1.getId() + " 用户的ip为: " + student1.getLastIP());
                    try {
                        studentService.updateIP(student1);
                        logger.info("更新id: " + id + "的IP成功！");
                        logger.info("---------------SUCCESS---------------");
                    } catch (Exception e) {
                        logger.warn("更新IP失败。");
                        logger.info("-----------------FAIL----------------");
                    }

//                    附加cookie
                    Cookie cookie = new Cookie("userInfo",id + "." + URLEncoder.encode(name,"utf-8"));
//                    设置cookie存活时间为一学期（半年）
                    cookie.setMaxAge(15768000);
                    response.addCookie(cookie);
                    logger.info("cookie附加成功");
                    logger.info("---------------SUCCESS---------------");
                    return "success";
                } catch (IOException e) {
                    logger.info("-----------------FAIL----------------");
                    throw new SysException("在服务器上创建文件时遇到未知错误，上传失败!");
                }
            }
        }
        else{
            logger.info("-----------------FAIL----------------");
            throw new StudentNotFoundException("数据库中无"+ name + "的记录！");
        }
    }

}