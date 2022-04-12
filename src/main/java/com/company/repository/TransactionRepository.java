package com.company.repository;

import com.company.container.ComponentContainer;
import com.company.db.DBConnection;
import com.company.dto.CardDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.TerminalDTO;
import com.company.dto.TransactionDTO;
import com.company.enums.CardStatus;
import com.company.enums.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TransactionRepository {
    public TransactionDTO transactionReFill(TransactionDTO dto) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "insert into transaction_history(card_number,amount,type,transaction_date) " +
                    "values(?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dto.getCard_number());
            preparedStatement.setLong(2, dto.getAmount());
            preparedStatement.setString(3, dto.getType().name());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(dto.getTransaction_date()));

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                dto.setId(id);
            }
//            preparedStatement.clearParameters();
            preparedStatement.closeOnCompletion();

            sql = "update card\n" +
                    "set balance = balance + ?\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,dto.getAmount());
            preparedStatement.setString(2,dto.getCard_number());

            n = preparedStatement.executeUpdate();
            if (n == 0){
                return null;
            }
            return dto;
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

    public List<TransactionDTO> showTransactionList(ProfileDTO dto) {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "join profile_card on transaction_history.card_number = profile_card.card_number\n" +
                    "join profile on profile.phone = profile_card.phone\n" +
                    "where profile.phone = ?\n" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dto.getPhone());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                String terminal_code = resultSet.getString("terminal_code");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }

    public List<TransactionDTO> showTransactionList() {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                String terminal_code = resultSet.getString("terminal_code");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }

    public TransactionDTO transactionPayment(TransactionDTO dto) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "insert into transaction_history(card_number,amount,terminal_code,type,transaction_date) " +
                    "values(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dto.getCard_number());
            preparedStatement.setLong(2, dto.getAmount());
            preparedStatement.setString(3, dto.getTerminal_code());
            preparedStatement.setString(4, dto.getType().name());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(dto.getTransaction_date()));

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                dto.setId(id);
            }

            preparedStatement.closeOnCompletion();

            sql = "update card\n" +
                    "set balance = balance - ?\n" +
                    "where card_number = ?;" +
                    "update card\n" +
                    "set balance = balance + ?\n" +
                    "where card_number = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,dto.getAmount());
            preparedStatement.setString(2,dto.getCard_number());
            preparedStatement.setLong(3, ComponentContainer.TERMINAL_AMOUNT);
            preparedStatement.setString(4,ComponentContainer.COMPANY_CARD);

            n = preparedStatement.executeUpdate();

            if (n == 0){
                return null;
            }

            return dto;
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

    public List<TransactionDTO> getTransactionByDay(LocalDate date) {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "where transaction_date::date = ?\n" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(date));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                String terminal_code = resultSet.getString("terminal_code");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }


    public List<TransactionDTO> getTransactionBetweenDay(LocalDate from_day, LocalDate to_day) {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "where transaction_date::date between ? and ?" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(from_day));
            preparedStatement.setDate(2, Date.valueOf(to_day));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                String terminal_code = resultSet.getString("terminal_code");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }


    public List<TransactionDTO> getTransactionByTerminal(String terminal_code) {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "where terminal_code = ?" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, terminal_code);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }

    public List<TransactionDTO> getTransactionByCard(String card_number) {
        List<TransactionDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "select * from transaction_history\n" +
                    "where card_number = ?" +
                    "order by transaction_date desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, card_number);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String terminal_code = resultSet.getString("terminal_code");
                Long amount = resultSet.getLong("amount");
                String type = resultSet.getString("type");
                Timestamp transaction_date = resultSet.getTimestamp("transaction_date");

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(id);
                transactionDTO.setCard_number(card_number);
                transactionDTO.setTerminal_code(terminal_code);
                transactionDTO.setAmount(amount);
                transactionDTO.setType(TransactionType.valueOf(type));
                transactionDTO.setTransaction_date(transaction_date.toLocalDateTime());
                dtoList.add(transactionDTO);
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
        return dtoList;
    }
}
