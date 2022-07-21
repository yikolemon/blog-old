package com.yikolemon.service;

import com.yikolemon.pojo.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeServiceImplTest {

    @Autowired
    private TypeServiceImpl typeService;

    @Test
    public void saveType() {
        Type type=new Type();
        type.setName("分类二");
        int i = typeService.saveType(type);
        System.out.println(i);
    }

    @Test
    public void getType() {
        Type type = typeService.getType(2);
        System.out.println(type);

    }

    @Test
    public void getAllTypes() {
        List<Type> allTypes = typeService.getAllTypes();
        for (Type type:
             allTypes) {
            System.out.println(type);
        }
    }

    @Test
    public void updateType() {
        Type type=new Type();
        type.setId((long) 2);
        type.setName("类型2");
        int i = typeService.updateType(type);
        System.out.println(i);
    }

    @Test
    public void deleteType() {
        int i = typeService.deleteType((long) 2);
        System.out.println(i);
    }

    @Test
    public void getTypeByName() {
        System.out.println(typeService.getTypeByName("66666"));
    }

    @Test
    public void getTypeTop(){
        System.out.println(typeService.getTypeTop(2));
    }

    @Test
    public void getAllIndexTypes(){
        System.out.println(typeService.getAllIndexTypes());
    }

}