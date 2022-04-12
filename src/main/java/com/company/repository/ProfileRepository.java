package com.company.repository;

import com.company.db.DBConnection;
import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ProfileRepository {

    public ProfileDTO registrationProfile(ProfileDTO profileDTO) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "insert into profile(name,surname,phone,password,created_date,status,role) " +
                    "values(?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, profileDTO.getName());
            preparedStatement.setString(2, profileDTO.getSurname());
            preparedStatement.setString(3, profileDTO.getPhone());
            preparedStatement.setString(4, profileDTO.getPassword());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(profileDTO.getCreated_date()));
            preparedStatement.setString(6, profileDTO.getStatus().name());
            preparedStatement.setString(7, profileDTO.getRole().name());

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                profileDTO.setId(id);
            }
            return profileDTO;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return null;
    }

    public List<ProfileDTO> getProfileList() {
        List<ProfileDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from profile");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                Timestamp created_Date = resultSet.getTimestamp("created_date");
                String status = resultSet.getString("status");
                String role = resultSet.getString("role");

                ProfileDTO dto = new ProfileDTO();
                dto.setId(id);
                dto.setName(name);
                dto.setSurname(surname);
                dto.setPhone(phone);
                dto.setPassword(password);
                dto.setCreated_date(created_Date.toLocalDateTime());
                dto.setStatus(ProfileStatus.valueOf(status));
                dto.setRole(ProfileRole.valueOf(role));
                dtoList.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return dtoList;
    }

    public ProfileDTO loginProfile(String phone, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "select * from profile\n" +
                    "where phone = ? and password = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                Timestamp created_Date = resultSet.getTimestamp("created_date");
                String status = resultSet.getString("status");
                String role = resultSet.getString("role");

                ProfileDTO dto = new ProfileDTO();
                dto.setId(id);
                dto.setName(name);
                dto.setSurname(surname);
                dto.setPhone(phone);
                dto.setPassword(password);
                dto.setCreated_date(created_Date.toLocalDateTime());
                dto.setStatus(ProfileStatus.valueOf(status));
                dto.setRole(ProfileRole.valueOf(role));
                return dto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return null;
    }

    public ProfileDTO getProfileByPhone(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "select * from profile\n" +
                    "where phone = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, phone);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String password = resultSet.getString("password");
                Timestamp created_Date = resultSet.getTimestamp("created_date");
                String status = resultSet.getString("status");
                String role = resultSet.getString("role");

                ProfileDTO dto = new ProfileDTO();
                dto.setId(id);
                dto.setName(name);
                dto.setSurname(surname);
                dto.setPhone(phone);
                dto.setPassword(password);
                dto.setCreated_date(created_Date.toLocalDateTime());
                dto.setStatus(ProfileStatus.valueOf(status));
                dto.setRole(ProfileRole.valueOf(role));
                return dto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return null;
    }

    public ProfileDTO changeProfileStatus(String phone, ProfileStatus status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection =  DBConnection.getConnection();

            String sql = "update profile\n" +
                    "set status = ?" +
                    "where phone = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, phone);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getProfileByPhone(phone);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return null;
    }
}
