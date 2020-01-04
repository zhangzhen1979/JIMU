package com.thinkdifferent.LogManager.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.thinkdifferent.LogManager.pojo.LogObject;

public abstract class MongoBaseDao<T> {

	protected static final Integer PAGE_SIZE = 10;
	/**
	 * spring mongodb 集成操作类
	 */
	@Autowired
	protected MongoTemplate mongoTemplate;

	/**
	 * 获取需要操作的实体类class
	 * 
	 * @return
	 */
	protected abstract Class<T> getEntityClass();

	/**
	 * 通过条件查询,查询分页结果
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @param collectionName collection名称
	 * @return
	 */
	public Pagination<T> getPage(int pageNo, int pageSize, Query query, String collectionName) {
		long totalCount = this.mongoTemplate.count(query, this.getEntityClass(), collectionName);
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<T> datas = this.find(query, collectionName);
		page.setDatas(datas);
		page.setTotalCount(totalCount);
		return page;
	}

	/**
	 * 通过条件查询实体(集合)
	 * 
	 * @param query
	 */
	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}
	
	/**
	 * 通过条件查询指定collection的实体(集合)
	 * 
	 * @param query
	 * @param collectionName collection名称
	 */
	public List<T> find(Query query, String collectionName) {
		return mongoTemplate.find(query, this.getEntityClass(), collectionName);
	}

	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param query
	 * @return
	 */
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}

	/**
	 * 查询出所有数据
	 * 
	 * @return
	 */
	public List<T> findAll() {
		return this.mongoTemplate.findAll(getEntityClass());
	}

	/**
	 * 查询并且修改记录
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public T findAndModify(Query query, Update update) {

		return this.mongoTemplate.findAndModify(query, update, this.getEntityClass());
	}

	/**
	 * 按条件查询,并且删除记录
	 * 
	 * @param query
	 * @return
	 */
	public T findAndRemove(Query query) {
		return this.mongoTemplate.findAndRemove(query, this.getEntityClass());
	}

	/**
	 * 通过条件查询更新数据
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public void updateFirst(Query query, Update update) {
		mongoTemplate.updateFirst(query, update, this.getEntityClass());
	}

	/**
	 * 保存一个对象到mongodb
	 * 
	 * @param bean
	 * @return
	 */
	public T save(T bean) {
		mongoTemplate.save(bean);
		return bean;
	}

	/**
	 * 通过ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	public T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 * 
	 * @param logID
	 * @param collectionName
	 *            集合名
	 * @return LogObject
	 */
	public LogObject findByLogId(String logID, String collectionName) {
		Query query = new Query(Criteria.where("logID").is(logID));
		LogObject lo=mongoTemplate.findOne(query, LogObject.class, collectionName);
		return lo;
	}
	
	
}
