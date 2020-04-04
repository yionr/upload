package cloud.yionr.controller;

import cloud.yionr.common.DateTool;
import cloud.yionr.common.Log4jUtils;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@RestController
public class ReviseController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    @Autowired
    Log4jUtils log4jUtils;

    @RequestMapping("revise")
    public String revise(String id, String name, String oFName, HttpServletRequest request){

        DayOfWeek weekDay = dateTool.getWeekDay();
        int timeOfHour = dateTool.getHour();

        if (weekDay == DayOfWeek.FRIDAY) {
            if (timeOfHour >= 12) {
                log4jUtils.getLogger().warn("用户ip: " + request.getRemoteAddr() + " 文件名:  " + oFName + "  试图在禁止上传的时间点上传文件，已拦截");
                return "over";
            }
        }
        if (weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY || weekDay == DayOfWeek.MONDAY){
            log4jUtils.getLogger().warn("用户ip: " + request.getRemoteAddr() + " 文件名:  " + oFName + "  试图在禁止上传的时间点上传文件，已拦截");
            return "over";
        }
        if (weekDay == DayOfWeek.TUESDAY)
            if (timeOfHour < 8){
                log4jUtils.getLogger().warn("用户ip: " + request.getRemoteAddr() + " 文件名:  " + oFName + "  试图在禁止上传的时间点上传文件，已拦截");
                return "over";
            }

        log4jUtils.getLogger().info("用户ip: " + request.getRemoteAddr() + " 提交文件,完整文件名称为: " + oFName + " 开始检测！");

        Student student = studentService.FindByName(name);
//        如果根据姓名找到了这个学生的话，进一步判断正确学号
        if (student != null){
            //如果学生输入的学号和正确的学号不一致，则返回正确学号，提醒用户
            if (id.length() == 11)
                if (!student.getId().equals(id)){
                    log4jUtils.getLogger().warn(name + "学号填写错误，提供纠正");
                    return "correctId:" + student.getId();
                }
            if (id.length() == 2)
                if (!student.getId().substring(9,11).equals(id)){
                    log4jUtils.getLogger().warn(name + "学号填写错误，提供纠正");
                    return "correctId:" + student.getId().substring(9,11);
                }
        }
//        如果姓名无法识别，则根据学号来查找
        else{
            if (id.length() == 11)
                student = studentService.FindById(id);
            else if (id.length() == 2)
                student = studentService.FindByLastId("2017%" + id);
        }
//        如果到这还没有找到的话，就说明，这个文件名，不是以学号开头，也不是以姓名结尾的，暂时无法处理
        if (student == null){
            log4jUtils.getLogger().warn("无法识别！请求用户手动填写表单");
            return "false";
        }
        else{
//        到这里，表示根据学号找到了学生,但是name错了
            if (!student.getName().equals(name)){
                log4jUtils.getLogger().warn(id + "姓名填写错误，提供纠正");
                return "correctName:" + student.getName();
            }
        }
//        到这里，代表格式完全正确
        log4jUtils.getLogger().info("格式完全正确，不需要纠正！");
//        如果是16级的学生
        if (student.getId().startsWith("2016"))
            return student.getId() + student.getName();
//        不是16级的话
        return student.getId().substring(9,11) + student.getName();
    }
}