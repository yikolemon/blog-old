package com.yikolemon.service;

import com.github.pagehelper.PageHelper;
import com.yikolemon.mapper.TypeMapper;
import com.yikolemon.mapper.UserMapper;
import com.yikolemon.pojo.Type;
import com.yikolemon.queue.IndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "types")
public class TypeServiceImpl implements TypeService {


    @Resource
    private TypeMapper typeMapper;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int saveType(Type type) {
        return typeMapper.saveType(type);
    }

    @Transactional
    @Override
    @Cacheable(key = "'getType'+#id")
    public Type getType(long id) {
        return typeMapper.getType(id);
    }

    @Override
    @Transactional
    @Cacheable(key = "'getTypeByName'+#name")
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    @Transactional
    @Override
    @Cacheable(key = "'getAllTypes'")
    public List<Type> getAllTypes() {
        return typeMapper.getAllTypes();
    }

    @Override
    @Cacheable(key = "'getTypeTop'+#size")
    public List<IndexType> getTypeTop(int size) {
        PageHelper.clearPage();
        return typeMapper.getTypeTop(size);
    }

    @Override
    @Cacheable(key = "'getAllIndexTypes'")
    public List<IndexType> getAllIndexTypes() {
        return typeMapper.getAllIndexTypes();
    }

    @Transactional
    @Override
    @CacheEvict(allEntries = true)
    public int updateType(Type type) {
        return typeMapper.updateType(type);
    }

    @Transactional
    @Override
    @CacheEvict(allEntries = true)
    public int deleteType(Long id) {
        return typeMapper.deleteType(id);
    }
}
