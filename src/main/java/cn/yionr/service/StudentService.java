package cn.yionr.service;

import cn.yionr.dao.StudentDao;
import cn.yionr.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentDao dao;

    public Student FindByName(String name){
        return dao.findByName(name);
    }
//    FindById 无论传入的是两位学号还是 11 位学号,都能查
    public Student FindById(String id){
        if (id.length() == 2)
            return dao.findByLastId("2017%" + id);
        else if (id.length() == 11)
            return dao.findById(id);
        else
            return null;
    }
    public boolean updateIP(Student student){
        return dao.updateIP(student);
    }
}
