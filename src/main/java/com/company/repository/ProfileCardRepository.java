package com.company.repository;

import com.company.db.DBConnection;
import com.company.dto.CardDTO;
import com.company.dto.ProfileCardDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ProfileCardRepository {
    public ProfileCardDTO addCardToProfile(ProfileCardDTO dto) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();
            String sql = "insert into profile_card(card_number,phone,added_date,visible_user) " +
                    "values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dto.getCard_number());
            preparedStatement.setString(2, dto.getPhone());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dto.getAdded_date()));
            preparedStatement.setBoolean(4, dto.getVisible_user());

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
                return null;
            }

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                dto.setId(id);
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

    public List<CardDTO> getProfileCardList(ProfileDTO dto) {
        List<CardDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "select * from card\n" +
                    "join profile_card on card.card_number = profile_card.card_number\n" +
                    "where phone = ?\n" +
                    "and visible_user = true";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dto.getPhone());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                LocalDate expiration_date = resultSet.getDate("expiration_date").toLocalDate();
                Long balance = resultSet.getLong("balance");
                String status = resultSet.getString("status");
                Timestamp created_Date = resultSet.getTimestamp("created_date");

                CardDTO cardDTO = new CardDTO();
                cardDTO.setId(id);
                cardDTO.setCard_number(card_number);
                cardDTO.setExpiration_date(expiration_date);
                cardDTO.setBalance(balance);
                cardDTO.setStatus(CardStatus.valueOf(status));
                cardDTO.setCreated_date(created_Date.toLocalDateTime());
                dtoList.add(cardDTO);
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

    public List<ProfileCardDTO> getProfileCardList() {
        List<ProfileCardDTO> dtoList = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "select * from profile_card\n" +
                    "order by added_date desc";
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String card_number = resultSet.getString("card_number");
                String phone = resultSet.getString("phone");
                LocalDateTime added_date = resultSet.getTimestamp("added_date").toLocalDateTime();
                Boolean visible_user = resultSet.getBoolean("visible_user");

                ProfileCardDTO profileCardDTO = new ProfileCardDTO();
                profileCardDTO.setId(id);
                profileCardDTO.setCard_number(card_number);
                profileCardDTO.setPhone(phone);
                profileCardDTO.setAdded_date(added_date);
                profileCardDTO.setVisible_user(visible_user);
                dtoList.add(profileCardDTO);
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

    public ProfileDTO changeCardStatus(ProfileDTO dto, String card_number, CardStatus status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DBConnection.getConnection();

            String sql = "update card\n" +
                    "set status = ?" +
                    "where card_number in (select card.card_number from card\n" +
                    "join profile_card on card.card_number = profile_card.card_number\n" +
                    "where phone = ?\n" +
                    "and profile_card.card_number = ?\n" +
                    "and visible_user = true)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, dto.getPhone());
            preparedStatement.setString(3, card_number);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
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

    public ProfileDTO deleteProfileCard(ProfileDTO dto, String card_number) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();

            String sql = "update profile_card\n" +
                    "set visible_user = false\n" +
                    "where card_number in (select card.card_number from card\n" +
                    "join profile_card on card.card_number = profile_card.card_number\n" +
                    "where phone = ?\n" +
                    "and profile_card.card_number = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dto.getPhone());
            preparedStatement.setString(2, card_number);

            int n = preparedStatement.executeUpdate();
            if (n == 0) {
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

    public ProfileCardDTO getProfileCardByNumber(ProfileDTO profileDTO, String card_number) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "select * from profile_card\n" +
                    "where card_number = ?\n" +
                    "and phone = ?\n" +
                    "and visible_user = true;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, card_number);
            preparedStatement.setString(2, profileDTO.getPhone());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");

                ProfileCardDTO dto = new ProfileCardDTO();
                dto.setId(id);
                dto.setCard_number(card_number);
                dto.setPhone(profileDTO.getPhone());
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


}
