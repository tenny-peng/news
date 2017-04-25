package com.news.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.news.dao.IRecordDao;
import com.news.pojo.Record;
import com.news.service.IRecordService;

@Service("recordService")
public class RecordServiceImpl implements IRecordService {

	@Resource
	private IRecordDao recordDao;

	@Override
	public int addRecord(Record record) {
		return recordDao.addRecord(record);
	}

	@Override
	public Record findByParam(Map<String, Object> param) {
		return recordDao.findByParam(param);
	}

	@Override
	public List<Record> findByUserId(Integer userId) {
		return recordDao.findByUserId(userId);
	}

	@Override
	public void updateRecord(Record record) {
		recordDao.updateRecord(record);
	}

}
