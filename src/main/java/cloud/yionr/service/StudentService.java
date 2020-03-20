package cloud.yionr.service;

import cloud.yionr.dao.StudentDao;
import cloud.yionr.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentDao dao;

    public Student FindByName(String name){
        return dao.findByName(name);
    }
    public Student FindById(String id){
        return dao.findById(id);
    }
    public Student FindByLastId(String lastId){
        return dao.findByLastId(lastId);
    }
}
