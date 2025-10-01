package com.lgcns.svcp.prod.dataaccess;

import java.util.List;

import com.lgcns.svcp.prod.util.paging.PageResult;

public interface CommonDao {

    <T> T select(String queryId);
    <T> T select(String queryId, Object parameter);
    
    <E> List<E> selectList(String queryId);
    <E> List<E> selectList(String queryId, Object parameter);

    <E> PageResult<E> selectPagedList(String queryId, Object parameter);

    int insert(String queryId, Object parameter);

    int update(String queryId);
    int update(String queryId, Object parameter);

    int delete(String queryId, Object parameter);

    int batchInsert(String queryId, List<?> parameter);

    int batchUpdate(String queryId, List<?> parameter);

    int batchDelete(String queryId, List<?> parameter);
}
