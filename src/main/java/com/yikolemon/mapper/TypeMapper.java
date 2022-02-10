package com.yikolemon.mapper;

import com.yikolemon.pojo.Type;
import com.yikolemon.queue.IndexType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeMapper {

    int saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    List<Type> getAllTypes();

    List<IndexType> getTypeTop(int size);

    List<IndexType> getAllIndexTypes();

    int updateType(Type type);

    int deleteType(Long id);
}
