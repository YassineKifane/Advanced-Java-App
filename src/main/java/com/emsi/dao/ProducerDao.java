package com.emsi.dao;

import com.emsi.entities.Producer;

import java.util.List;

public interface ProducerDao {
    void insert(Producer producer);

    void update(Producer producer);

    void deleteById(Integer id);

    Producer findById(Integer id);

    List<Producer> findAll();


}
