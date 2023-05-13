package com.emsi.service;

import com.emsi.dao.ProducerDao;
import com.emsi.dao.impl.ProducerDaoImp;

import com.emsi.entities.Producer;


import java.util.List;

public class ProducerService {
    private ProducerDao producerDao =new ProducerDaoImp();

    public List<Producer> findAll() {
        return producerDao.findAll();
    }

    public void save(Producer producer) {

        producerDao.insert(producer);

    }
    public void update(Producer producer) {
        producerDao.update(producer);
    }
    public void remove(Producer producer) {
        producerDao.deleteById(producer.getId());
    }


}
