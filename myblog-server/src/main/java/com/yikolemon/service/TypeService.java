package com.yikolemon.service;
import com.yikolemon.pojo.Type;
import com.yikolemon.queue.IndexType;

import java.awt.print.Pageable;
import java.util.List;

public interface TypeService {

    int saveType(Type type);

    Type getType(long id);

    Type getTypeByName(String name);

    List<Type> getAllTypes();

    List<IndexType> getTypeTop(int size);

    List<IndexType> getAllIndexTypes();

    int updateType(Type type);

    int deleteType(Long id);

}
