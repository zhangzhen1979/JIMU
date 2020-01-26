package com.thinkdifferent.MongoDBManager.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import net.sf.json.JSONObject;

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
	 * @param query 查询条件
	 * @param update 修改内容
	 * @return 修改后的数据对象
	 */
	public T findAndModify(Query query, Update update) {
		return mongoTemplate.findAndModify(query, update, this.getEntityClass());
	}

	/**
	 * 查询并修改指定集合（表）中的数据
	 * 
	 * @param collectionName 集合名（表名）
	 * @param query 查询条件
	 * @param update 修改内容
	 * @return 修改后的数据对象
	 */
	public T findAndModify(String collectionName, Query query, Update update) {
		return mongoTemplate.findAndModify(query, update, this.getEntityClass(), collectionName);
	}

	/**
	 * 按条件查询，并且删除记录
	 * 
	 * @param query 查询条件
	 * @return 删除的数据对象
	 */
	public T findAndRemove(Query query) {
		return mongoTemplate.findAndRemove(query, this.getEntityClass());
	}

	/**
	 * 按条件查询，操作指定集合（表），删除记录
	 * 
	 * @param collectionName 集合名（表名）
	 * @param query 查询条件
	 * @return 删除的数据对象
	 */
	public T findAndRemove(String collectionName, Query query) {
		return mongoTemplate.findAndRemove(query, this.getEntityClass(), collectionName);
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
	 * 更新多条数据。根据条件，更新指定集合（表）中的数据。
	 * 
	 * @param collectionName 集合（表）名
	 * @param query 查询条件
	 * @param update 更新对象
	 */
	public void updateMulti(String collectionName, Query query, Update update) {
		mongoTemplate.updateMulti(query, update, collectionName);
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
	 * 将对象保存到指定的集合（表）中
	 * 
	 * @param bean 数据对象
	 * @param collectionName 集合名（表名）
	 * @return 返回保存后的对象
	 */
	public JSONObject save(String collectionName, JSONObject data) {
		mongoTemplate.save(data, collectionName);
		return data;
	}

	/**	
	 * 通过ID获取记录
	 * 
	 * @param id MongoDB中的id，即"_id"字段的值。
	 * @return 查找到的对象
	 */
	public T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	/**
	 * 通过数据ID获取记录,并且指定了集合名(表的意思)
	 * 
	 * @param collectionName 集合名（表名）
	 * @param dataID 数据ID（自定义的ID，即"dataID"字段的值）
	 * @return JSONObject格式的数据
	 */
	public JSONObject findByDataId(String collectionName, String dataID) {
		Query query = new Query(Criteria.where("dataID").is(dataID));
		JSONObject joReturn=mongoTemplate.findOne(query, JSONObject.class, collectionName);
		return joReturn;
	}
	
	
}
