package com.news.dao;

import java.util.List;
import java.util.Map;

import com.news.pojo.Record;

public interface IRecordDao {
	public int addRecord(Record record);

	public Record findByParam(Map<String, Object> param);

	public List<Record> findByUserId(Integer userId);

	public void updateRecord(Record record);
}
