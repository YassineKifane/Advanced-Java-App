package com.emsi.dao;

import com.emsi.entities.Emission;
import com.emsi.entities.Producer;

import java.util.List;

public interface EmissionDao {
    void insert(Emission emission);

    void update(Emission emission);

    void deleteById(Integer id);

    Emission findById(Integer id);
    List<Emission> findAll();

    List<Emission> findByProducer(Producer producer);

}
