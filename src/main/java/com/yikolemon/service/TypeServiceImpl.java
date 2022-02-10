package com.yikolemon.service;

import com.yikolemon.mapper.TypeMapper;
import com.yikolemon.mapper.UserMapper;
import com.yikolemon.pojo.Type;
import com.yikolemon.queue.IndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {


    @Autowired
    private TypeMapper typeMapper;

    @Override
    @Transactional
    public int saveType(Type type) {
        return typeMapper.saveType(type);
    }

    @Transactional
    @Override
    public Type getType(long id) {
        return typeMapper.getType(id);
    }

    @Override
    @Transactional
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    @Transactional
    @Override
    public List<Type> getAllTypes() {
        return typeMapper.getAllTypes();
    }

    @Override
    public List<IndexType> getTypeTop(int size) {
        return typeMapper.getTypeTop(size);
    }

    @Override
    public List<IndexType> getAllIndexTypes() {
        return typeMapper.getAllIndexTypes();
    }

    @Transactional
    @Override
    public int updateType(Type type) {
        return typeMapper.updateType(type);
    }

    @Transactional
    @Override
    public int deleteType(Long id) {
        return typeMapper.deleteType(id);
    }
}
