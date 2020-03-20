package cloud.yionr.controller;

import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
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
        Student student = studentService.FindByName(name);
//        如果根据姓名找到了这个学生的话，进一步判断正确学号
        if (student != null){
            //如果学生输入的学号和正确的学号不一致，则返回正确学号，提醒用户
            if (id.length() == 11){
                if (!student.getId().equals(id))
                    return student.getId();
            }
            if (id.length() == 2){
                if (!student.getId().substring(9,11).equals(id))
                    return student.getId().substring(9,11);
            }
        }
//        如果姓名无法识别，则根据学号来查找
        // TODO 根据学号来找，同时进一步确认姓名是否正确...我觉得这个应该可以不用做了，因为无法做到完美识别姓名，做上这个的话可能会很尴尬
//        FIXME findbylastid无法适用于16级
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
