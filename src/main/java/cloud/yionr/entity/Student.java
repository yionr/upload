package cloud.yionr.entity;

//映射数据库中的每个用户,数据库里面保存每个name 以及对应的id
public class Student {
    private String name;
    private String id;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
