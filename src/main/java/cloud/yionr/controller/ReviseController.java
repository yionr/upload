package cloud.yionr.controller;

import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviseController {
    @Autowired
    StudentService studentService;
    @RequestMapping("revise")
    public String revise(String id,String name){
        System.out.println(id + "," + name);
        Student student = studentService.FindByName(name);
        if (student == null)
            return "false";
//        如果是16级的学生
        if (student.getId().startsWith("2016"))
            return student.getId() + student.getName();
//        不是16级的话
        return student.getId().substring(9,11) + student.getName();
    }
}
