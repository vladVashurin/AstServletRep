package com.vld.rest.repository;

import com.vld.rest.model.Bar;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.vld.rest.meta.Constants.*;

@Slf4j
public class BarRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bar get(Long id) {
        String sql = "select * from bar where id = ?";
        Bar bar = null;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long barId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                bar = new Bar(barId, name, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (bar == null) {
            throw new RuntimeException("Bar with id + " + id + " not found");
        }
        return bar;
    }


    public List<Bar> getAll() {
        String sql = "select * from bar";
        List<Bar> bars = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long barId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                bars.add(new Bar(barId, name, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bars;
    }

    public Bar create(Bar bar) {
        String sql = "insert into bar (name,address) values (?, ?)";
        long barId = 0;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bar.getName());
            preparedStatement.setString(2, bar.getAddress());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                barId = generatedKeys.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        bar.setId(barId);
        return bar;
    }

    public Bar update(Bar bar) {
        String sql = "update bar set name = ?, address = ? where id = ?";
        get(bar.getId());

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bar.getName());
            preparedStatement.setString(2, bar.getAddress());
            preparedStatement.setLong(3, bar.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bar;
    }

    public void delete(Long id) {
        String sql = "delete from bar where id = ?";
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
