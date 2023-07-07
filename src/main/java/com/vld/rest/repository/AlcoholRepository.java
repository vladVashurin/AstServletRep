package com.vld.rest.repository;

import com.vld.rest.model.Alcohol;
import com.vld.rest.model.AlcoholType;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.vld.rest.meta.Constants.*;

@Slf4j
public class AlcoholRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Alcohol get(Long id) {
        String sql = """
                        select a.id as alcohol_id,
                               a.name as alcohol_name,
                               alt.id as type_id,
                               alt.strength as type_strength
                        from alcohol a
                        left join alcohol_type alt on a.alcohol_type_id = alt.id
                        where a.id = ?
                """;
        Alcohol alcohol = null;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long alcoholId = resultSet.getLong("alcohol_id");
                String alcoholName = resultSet.getString("alcohol_name");
                Long typeId = resultSet.getLong("type_id");
                Double typeStrength = resultSet.getDouble("type_strength");
                alcohol = new Alcohol(alcoholId, alcoholName, new AlcoholType(typeId, typeStrength));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (alcohol == null) {
            throw new RuntimeException("Alcohol with id + " + id + " not found");
        }
        return alcohol;
    }

    public List<Alcohol> getAll() {
        String sql = """
                        select a.id as alcohol_id,
                               a.name as alcohol_name,
                               alt.id as type_id,
                               alt.strength as type_strength
                        from alcohol a
                        left join alcohol_type alt on a.alcohol_type_id = alt.id
                """;

        List<Alcohol> alcohols = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long alcoholId = resultSet.getLong("alcohol_id");
                String alcoholName = resultSet.getString("alcohol_name");
                Long typeId = resultSet.getLong("type_id");
                Double typeStrength = resultSet.getDouble("type_strength");
                alcohols.add(new Alcohol(alcoholId, alcoholName, new AlcoholType(typeId, typeStrength)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alcohols;
    }

    public Alcohol create(Alcohol alcohol) {
        String sql = "insert into alcohol (name,alcohol_type_id) values (?, ?)";
        long alcoholId = 0;

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, alcohol.getName());
            preparedStatement.setLong(2, alcohol.getAlcoholType().getId());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                alcoholId = generatedKeys.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        alcohol.setId(alcoholId);
        return alcohol;
    }

    public Alcohol update(Alcohol alcohol) {
        String sql = "update alcohol set name = ?, alcohol_type_id = ? where id = ?";
        get(alcohol.getId());

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, alcohol.getName());
            preparedStatement.setLong(2, alcohol.getAlcoholType().getId());
            preparedStatement.setLong(3, alcohol.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alcohol;
    }

    public void delete(Long id) {
        String sql = "delete from alcohol where id = ?";
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
