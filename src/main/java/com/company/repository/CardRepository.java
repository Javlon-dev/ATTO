package com.company.repository;

import com.company.db.DBConnection;
import com.company.dto.CardDTO;
import com.company.dto.ProfileCardDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CardRepository {
    public CardDTO createCard(CardDTO cardDTO) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "insert into card(card_number,expiration_date,balance,status,created_date) " +
                    "values(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, cardDTO.getCard_number());
            preparedStatement.setDate(2, Date.valueOf(cardDTO.getExpiration_date()));
            preparedStatement.setLong(3, cardDTO.getBalance());
            preparedStatement.setString(4, cardDTO.getStatus().name());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(cardDTO.getCreated_date()));

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                cardDTO.setId(id);
            }
            return cardDTO;
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

    public List<CardDTO> getCardList() {
        List<CardDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from card");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                LocalDate expiration_date = resultSet.getDate("expiration_date").toLocalDate();
                Long balance = resultSet.getLong("balance");
                String status = resultSet.getString("status");
                Timestamp created_Date = resultSet.getTimestamp("created_date");

                CardDTO dto = new CardDTO();
                dto.setId(id);
                dto.setCard_number(card_number);
                dto.setExpiration_date(expiration_date);
                dto.setBalance(balance);
                dto.setStatus(CardStatus.valueOf(status));
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

    public CardDTO getCardByNumber(String card_number) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "select * from card\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, card_number);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                LocalDate expiration_date = resultSet.getDate("expiration_date").toLocalDate();
                Long balance = resultSet.getLong("balance");
                String status = resultSet.getString("status");
                Timestamp created_Date = resultSet.getTimestamp("created_date");

                CardDTO dto = new CardDTO();
                dto.setId(id);
                dto.setCard_number(card_number);
                dto.setExpiration_date(expiration_date);
                dto.setBalance(balance);
                dto.setStatus(CardStatus.valueOf(status));
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

    public CardDTO updateCard(String card_number, LocalDate expiration_date) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "update card set expiration_date = ?\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDate(1, Date.valueOf(expiration_date));
            preparedStatement.setString(2, card_number);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getCardByNumber(card_number);
//            preparedStatement.executeUpdate();
//
//            ResultSet set = preparedStatement.getGeneratedKeys();
//            int id = set.getInt(1);
//
//            preparedStatement.closeOnCompletion();
//
//            sql = "select * from card\n" +
//                    "where id = ?;";
//            preparedStatement = connection.prepareStatement(sql);
//
//            preparedStatement.setInt(1, id);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                Integer cardId = resultSet.getInt("id");
//                LocalDate date = resultSet.getDate("expiration_date").toLocalDate();
//                Long card_balance = resultSet.getLong("balance");
//                String status = resultSet.getString("status");
//                Timestamp created_Date = resultSet.getTimestamp("created_date");
//
//                CardDTO dto = new CardDTO();
//                dto.setId(cardId);
//                dto.setCard_number(card_number);
//                dto.setExpiration_date(date);
//                dto.setBalance(card_balance);
//                dto.setStatus(CardStatus.valueOf(status));
//                dto.setCreated_date(created_Date.toLocalDateTime());
//                return dto;
//            }
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

    public CardDTO changeCardStatus(String card_number, CardStatus status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection =  DBConnection.getConnection();

            String sql = "update card\n" +
                    "set status = ?" +
                    "where card_number = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, card_number);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getCardByNumber(card_number);
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

    public CardDTO cardRefill(String card_number, Long balance) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection =  DBConnection.getConnection();

            String sql = "update card\n" +
                    "set balance = balance + ?\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, balance);
            preparedStatement.setString(2, card_number);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            return getCardByNumber(card_number);
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

    public int deleteCard(String card_number) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "delete from card\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, card_number);

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
