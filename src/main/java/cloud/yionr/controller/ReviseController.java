package cloud.yionr.controller;

import cloud.yionr.common.DateTool;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@RestController
public class ReviseController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    private Logger logger = Logger.getLogger(ReviseController.class);

    @RequestMapping("revise")
    public String revise(String id,String name,String oFName){

        DayOfWeek weekDay = dateTool.getWeekDay();
        int timeOfHour = dateTool.getHour();

        if (weekDay == DayOfWeek.FRIDAY) {
            if (timeOfHour >= 12) {
                logger.info("this file submit request is not in time: " + oFName);
                return "over";
            }
        }
        if (weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY || weekDay == DayOfWeek.MONDAY){
            logger.info("this file submit request is not in time: " + oFName);
            return "over";
        }

        logger.info("file start revise,originFileName is: " + oFName);

        Student student = studentService.FindByName(name);
//        如果根据姓名找到了这个学生的话，进一步判断正确学号
        if (student != null){
            //如果学生输入的学号和正确的学号不一致，则返回正确学号，提醒用户
            if (id.length() == 11)
                if (!student.getId().equals(id))
                    return student.getId();
            if (id.length() == 2)
                if (!student.getId().substring(9,11).equals(id))
                    return student.getId().substring(9,11);
        }
//        如果姓名无法识别，则根据学号来查找
        else{
            if (id.length() == 11)
                student = studentService.FindById(id);
            else if (id.length() == 2)
                student = studentService.FindByLastId("2017%" + id);
        }
//        如果到这还没有找到的话，就说明，这个文件名，不是以学号开头，也不是以姓名结尾的，暂时无法处理
        if (student == null)
            return "false";

//        能到这里，表示找到了学生，并且它的学号没有填错
//        如果是16级的学生
        if (student.getId().startsWith("2016"))
            return student.getId() + student.getName();
//        不是16级的话
        return student.getId().substring(9,11) + student.getName();
    }
}