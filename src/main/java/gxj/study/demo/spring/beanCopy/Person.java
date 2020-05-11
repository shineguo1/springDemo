package gxj.study.demo.spring.beanCopy;

import lombok.Getter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/3/30 14:33
 * @description
 */
public class Person {

    String name;

    String password;

    public void setUsername(String username){
        this.name = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString(){
        return "name:"+name+" pw:"+password;
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        Person p = new Person();
        p.setUsername("NAME");
        List<Person> persons = new ArrayList<>();
        for(int i=0;i<5;i++){
            p.setPassword("pw:"+i);
        }
        System.out.println("p:"+p);
        System.out.println("p:"+persons);
    }
}


