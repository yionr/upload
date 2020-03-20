package cloud.yionr.controller;

import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviseController {
//    考虑不转发给客户端，直接在服务器内相互转发。
    @Autowired
    StudentService studentService;
    @RequestMapping("revise")
    public String revise(String id,String name){
        /*
        * 首先，根据提交的id 和name 来分组
        * 1. 完全没有任何有用信息的，比如asdassdiqod.xxx 多见于发图片
        * 2. 11位学号+姓名
        * 3. 11位学号+姓名+（）
        * 4. 2位学号+姓名
        * 情况好像是有点多。。这个没法预料啊，就用常见的，假设学号后面只有姓名的情况，其余一律让他们手动输入
        *
        *
        * */
        System.out.println(id + "," + name);
        Student student = studentService.FindByName(name);
        if (student == null)
            return "false";
//        如果学生输入的学号和正确的学号不一致，则返回正确学号，提醒用户
        if (id.length() == 11){
            if (!student.getId().equals(id))
                return student.getId();
        }
        if (id.length() == 2){
            if (!student.getId().substring(9,11).equals(id))
                return student.getId().substring(9,11);
        }
//        如果是16级的学生
        if (student.getId().startsWith("2016"))
            return student.getId() + student.getName();
//        不是16级的话
        return student.getId().substring(9,11) + student.getName();
    }
}
