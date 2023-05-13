package com.emsi.dao.impl;

import com.emsi.dao.EmissionDao;
import com.emsi.entities.Emission;
import com.emsi.entities.Producer;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmissionDaoImp implements EmissionDao {

    private Connection conn= DB.getConnection();

    @Override
    public void insert(Emission emission) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("INSERT INTO emission (Titre,DateEmission,DureeEmission,Genre, ProducerId) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, emission.getTitre());
            ps.setDate(2, new java.sql.Date(emission.getDateEmission().getTime()));
            ps.setInt(3, emission.getDureeEmission());
            ps.setString(4, emission.getGenre());
            ps.setInt(5, emission.getProducer().getId());


            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);

                    emission.setId(id);
                }

                DB.closeResultSet(rs);
            } else {
                System.out.println("Aucune ligne renvoyée");
            }
        } catch (SQLException e) {
            System.err.println("problème d'insertion ");;
        } finally {
            DB.closeStatement(ps);
        }

    }

    @Override
    public void update(Emission emission) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "UPDATE emission SET Titre = ?, DateEmission = ?, DureeEmission = ?, Genre = ?, ProducerId = ? WHERE Id = ?");

            ps.setString(1, emission.getTitre());
            ps.setDate(2, new java.sql.Date(emission.getDateEmission().getTime()));
            ps.setInt(3, emission.getDureeEmission());
            ps.setString(4, emission.getGenre());
            ps.setInt(5, emission.getProducer().getId());
            ps.setInt(6, emission.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("problème de mise à jour d'emission");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM emission WHERE id = ?");

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("problème de suppression d'emission");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Emission findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT c.*, o.name AS ProducerName FROM emission c INNER JOIN Producer o ON c.ProducerId = o.Id WHERE c.id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                Producer producer = instantiateProducer(rs);
                Emission emission = instantiateEmission(rs, producer);

                return emission;
            }

            return null;
        } catch (SQLException e) {
            System.err.println("problème de requête pour trouver le producteur");
            return null;
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Emission> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT c.*, o.Name as ProducerName FROM emission c INNER JOIN Producer o ON c.ProducerId = o.Id ORDER BY c.Name");
            rs = ps.executeQuery();
            List<Emission> list = new ArrayList<>();
            Map<Integer, Producer> map = new HashMap<>();

            while (rs.next()) {
                Producer producer = map.get(rs.getInt("ProducerId"));

                if (producer == null) {
                    producer = instantiateProducer(rs);

                    map.put(rs.getInt("ProducerId"), producer);
                }

                Emission emission = instantiateEmission(rs, producer);

                list.add(emission);
            }

            return list;
        } catch (SQLException e) {
            System.err.println("problème de requête pour sélectionner les Emissions");
            return null;
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Emission> findByProducer(Producer producer) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT c.*, o.Name as ProducerName FROM emission c INNER JOIN Producer o ON c.ProducerId = o.Id WHERE c.ProducerId = ? ORDER BY c.Name");

            ps.setInt(1, producer.getId());

            rs = ps.executeQuery();
            List<Emission> list = new ArrayList<>();
            Map<Integer, Producer> map = new HashMap<>();

            while (rs.next()) {
                Producer own = map.get(rs.getInt("ProducerId"));

                if (own == null) {
                    own = instantiateProducer(rs);

                    map.put(rs.getInt("ProducerId"), own);
                }

                Emission emission = instantiateEmission(rs, own);

                list.add(emission);
            }

            return list;
        } catch (SQLException e) {
            System.err.println("problème de requête pour sélectionner les emissions d'un producteur donné");
            return null;
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    private Emission instantiateEmission(ResultSet rs, Producer producer) throws SQLException {
        Emission emission = new Emission();

        emission.setId(rs.getInt("Id"));
        emission.setTitre(rs.getString("Titre"));
        emission.setDateEmission(rs.getDate("Date Emission"));
        emission.setDureeEmission(rs.getInt("Durée Emission"));
        emission.setGenre(rs.getString("Genre"));
        emission.setProducer(producer);

        return emission;
    }

    private Producer instantiateProducer(ResultSet rs) throws SQLException {
        Producer producer = new Producer();

        producer.setId(rs.getInt("producerId"));
        producer.setName(rs.getString("Name"));
        producer.setCIN(rs.getString("Cin"));
        producer.setAddress(rs.getString("Adress"));
        producer.setPhoneNumber(rs.getInt("PhoneNumber"));
        return producer;
    }
}
