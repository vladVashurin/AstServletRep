package com.vld.rest.repository;

import com.vld.rest.model.Alcohol;
import com.vld.rest.model.AlcoholType;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.vld.rest.meta.Constants.*;

@Slf4j
public class MenuRepository {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Alcohol> getBarAlcohol(Long barId) {
        String sql = """
                        select a.id as alcohol_id,
                               a.name as alcohol_name,
                               alt.id as type_id,
                               alt.strength as type_strength
                        from alcohol a
                        left join alcohol_type alt on a.alcohol_type_id = alt.id
                        where a.id in (select alcohol_id 
                             from menu 
                             where bar_id = ?)
                """;
        List<Alcohol> alcohols = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, barId);
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


    public void addAlcoholToBar(Long barId, Long alcoholId) {
        String sql = "insert into menu (bar_id, alcohol_id) values (?, ?)";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, barId);
            preparedStatement.setLong(2, alcoholId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAlcoholFromBar(Long barId, Long alcoholId) {
        String sql = "delete from menu where bar_id = ? and alcohol_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, barId);
            preparedStatement.setLong(2, alcoholId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
