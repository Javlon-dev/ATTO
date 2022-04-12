package com.company.repository;

import com.company.db.DBConnection;
import com.company.dto.CardDTO;
import com.company.dto.TerminalDTO;
import com.company.enums.CardStatus;
import com.company.enums.TerminalStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TerminalRepository {
    public TerminalDTO createTerminal(TerminalDTO terminalDTO) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "insert into terminal(code,address,status,created_date) " +
                    "values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, terminalDTO.getCode());
            preparedStatement.setString(2, terminalDTO.getAddress());
            preparedStatement.setString(3, terminalDTO.getStatus().name());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(terminalDTO.getCreated_date()));

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                terminalDTO.setId(id);
            }
            return terminalDTO;
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

    public List<TerminalDTO> getTerminalList() {
        List<TerminalDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from terminal");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String address = resultSet.getString("address");
                String status = resultSet.getString("status");
                Timestamp created_Date = resultSet.getTimestamp("created_date");

                TerminalDTO dto = new TerminalDTO();
                dto.setId(id);
                dto.setCode(code);
                dto.setAddress(address);
                dto.setStatus(TerminalStatus.valueOf(status));
                dto.setCreated_date(created_Date.toLocalDateTime());
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

    public TerminalDTO getTerminalByCode(String terminal_code) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "select * from terminal\n" +
                    "where code = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, terminal_code);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String address = resultSet.getString("address");
                String status = resultSet.getString("status");
                Timestamp created_Date = resultSet.getTimestamp("created_date");

                TerminalDTO dto = new TerminalDTO();
                dto.setId(id);
                dto.setCode(terminal_code);
                dto.setAddress(address);
                dto.setStatus(TerminalStatus.valueOf(status));
                dto.setCreated_date(created_Date.toLocalDateTime());
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

    public TerminalDTO changeCardStatus(String code, TerminalStatus status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "update terminal\n" +
                    "set status = ?" +
                    "where code = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, code);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getTerminalByCode(code);
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

    public TerminalDTO updateTerminal(String code, String address) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "update card set address = ?\n" +
                    "where code = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, address);
            preparedStatement.setString(2, code);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getTerminalByCode(code);

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

    public int deleteTerminal(String code) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "delete from terminal\n" +
                    "where code = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, code);

            return preparedStatement.executeUpdate();

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
        return 0;
    }


}
