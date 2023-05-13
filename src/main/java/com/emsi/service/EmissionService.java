package com.emsi.service;

import com.emsi.dao.EmissionDao;
import com.emsi.dao.impl.EmissionDaoImp;
import com.emsi.entities.Emission;

import java.util.List;

public class EmissionService {
    private EmissionDao emissionDao = new EmissionDaoImp();

    public List<Emission> findAll() {
        return emissionDao.findAll();
    }

    public void save(Emission emission) {

        emissionDao.insert(emission);

    }
    public void update(Emission emission) {

        emissionDao.update(emission);

    }
    public void remove(Emission emission) {
        emissionDao.deleteById(emission.getId());
    }
}
