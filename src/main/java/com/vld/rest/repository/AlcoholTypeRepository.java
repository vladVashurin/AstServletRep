package com.vld.rest.repository;

import com.vld.rest.model.AlcoholType;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.vld.rest.meta.Constants.*;

@Slf4j
public class AlcoholTypeRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public AlcoholType get(Long id) {
        String sql = "select * from alcohol_type where id = ?";
        AlcoholType alcoholType = null;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long alcoholTypeId = resultSet.getLong("id");
                Double strength = resultSet.getDouble("strength");
                alcoholType = new AlcoholType(alcoholTypeId, strength);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (alcoholType == null) {
            throw new RuntimeException("AlcoholType with id + " + id + " not found");
        }
        return alcoholType;
    }


    public List<AlcoholType> getAll() {
        String sql = "select * from alcohol_type";
        List<AlcoholType> alcoholTypes = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long alcoholTypeId = resultSet.getLong("id");
                Double strength = resultSet.getDouble("strength");
                alcoholTypes.add(new AlcoholType(alcoholTypeId, strength));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alcoholTypes;
    }

    public AlcoholType create(AlcoholType alcoholType) {
        String sql = "insert into alcohol_type (strength) values (?)";
        long alcoholTypeId = 0;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, alcoholType.getStrength());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                alcoholTypeId = generatedKeys.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        alcoholType.setId(alcoholTypeId);
        return alcoholType;
    }

    public AlcoholType update(AlcoholType alcoholType) {
        String sql = "update alcohol_type set strength = ? where id = ?";
        get(alcoholType.getId());

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, alcoholType.getStrength());
            preparedStatement.setLong(2, alcoholType.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alcoholType;
    }

    public void delete(Long id) {
        String sql = "delete from alcohol_type where id = ?";
        get(id);

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
