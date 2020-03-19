package cloud.yionr.controller;

import cloud.yionr.dao.StudentDao;
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
        System.out.println(student);
        return student.getId().substring(9,11) + student.getName();
    }
}
