package io.qmeta.supplement.datafactory;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MockUser {

    private String name;
    private Date birthday;

    private List<String> stringList;

    //S/F 的枚举
    private StatusEnum statusEnum;

    private Map<String, String> map;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "MockUser{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", stringList=" + stringList +
                ", statusEnum=" + statusEnum +
                ", map=" + map +
                '}';
    }

    //Getter & Setter
}