package com.lgcns.svcp.prod.dataaccess.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import com.lgcns.svcp.prod.util.paging.PageResult;

@Repository
public class MyBatisCommonDao extends SqlSessionDaoSupport implements CommonDao {
	
	private static final int BATCH_SIZE = 1000;

	@Autowired
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public <T> T select(String queryId) {
		return getSqlSession().selectOne(queryId);
	}

	@Override
	public <T> T select(String queryId, Object parameter) {
		return getSqlSession().selectOne(queryId, parameter);
	}

	@Override
	public <E> List<E> selectList(String queryId) {
		return getSqlSession().selectList(queryId);
	}

	@Override
	public <E> List<E> selectList(String queryId, Object parameter) {
		return getSqlSession().selectList(queryId, parameter);
	}

	@Override
	public <E> PageResult<E> selectPagedList(String queryId, Object parameter) {
		if (!(parameter instanceof BasePaginationDto pagination)) {
			throw new IllegalArgumentException("Parameter must be an instance of BasePaginationDto");
		}

		PageHelper.startPage(pagination.getPage(), pagination.getSize());
		Map<String, String> sortMap = pagination.getSortMap();
		if (!sortMap.isEmpty()) {
			String orderBy = sortMap.entrySet().stream().filter(e -> e.getKey().matches("[a-zA-Z0-9_]+"))
					.map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining(", "));
			if (!orderBy.isEmpty()) {
				PageHelper.orderBy(orderBy);
			}
		}

		List<E> result = getSqlSession().selectList(queryId, parameter);
		PageInfo<E> pageInfo = new PageInfo<>(result);
		return new PageResult<>(result != null ? result : Collections.emptyList(), pagination.getPage(),
				pagination.getSize(), pageInfo.getTotal());
	}

	@Override
	public int insert(String queryId, Object parameter) {
		return getSqlSession().insert(queryId, parameter);
	}

	@Override
	public int update(String queryId) {
		return getSqlSession().update(queryId);
	}

	@Override
	public int update(String queryId, Object parameter) {
		return getSqlSession().update(queryId, parameter);
	}

	@Override
	public int delete(String queryId, Object parameter) {
		return getSqlSession().delete(queryId, parameter);
	}

	@Override
	public int batchInsert(String queryId, List<?> parameters) {
		return executeBatch(queryId, parameters, (sqlSession, param) -> sqlSession.insert(queryId, param));
	}

	@Override
	public int batchUpdate(String queryId, List<?> parameters) {
		return executeBatch(queryId, parameters, (sqlSession, param) -> sqlSession.update(queryId, param));
	}

	@Override
	public int batchDelete(String queryId, List<?> parameters) {
		return executeBatch(queryId, parameters, (sqlSession, param) -> sqlSession.delete(queryId, param));
	}

	private int executeBatch(String queryId, List<?> parameters, BiConsumer<SqlSession, Object> operation) {
		if (parameters == null || parameters.isEmpty()) {
			return 0;
		}

		try (SqlSession sqlSession = getSqlSessionFactory().openSession(ExecutorType.BATCH)) {
			int rowsAffected = 0;
			for (int i = 0; i < parameters.size(); i++) {
				operation.accept(sqlSession, parameters.get(i));
				if ((i + 1) % BATCH_SIZE == 0) {
					rowsAffected += countRowsAffected(sqlSession.flushStatements());
				}
			}
			rowsAffected += countRowsAffected(sqlSession.flushStatements());
			return rowsAffected;
		} catch (Exception e) {
			throw new RuntimeException("Batch operation failed for " + queryId, e);
		}
	}

	private int countRowsAffected(List<BatchResult> results) {
		int count = 0;
		for (BatchResult result : results) {
			for (int updateCount : result.getUpdateCounts()) {
				count += updateCount;
			}
		}
		return count;
	}
}