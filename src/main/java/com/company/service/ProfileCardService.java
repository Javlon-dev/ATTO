package com.company.service;

import com.company.container.ComponentContainer;
import com.company.dto.CardDTO;
import com.company.dto.ProfileCardDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.TransactionDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileStatus;
import com.company.enums.TransactionType;
import com.company.repository.ProfileCardRepository;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Setter
public class ProfileCardService {

    private ProfileCardRepository profileCardRepository;
    private CardService cardService;

    public void addCardToProfile(ProfileDTO profileDTO, String card_number) {
        if (cardService.getCardByNumber(card_number) == null) {
            return;
        }
        ProfileCardDTO dto = new ProfileCardDTO();
        dto.setPhone(profileDTO.getPhone());
        dto.setCard_number(card_number);
        dto.setAdded_date(LocalDateTime.now());
        dto.setVisible_user(Boolean.TRUE);
        ProfileCardDTO profileCardDTO = profileCardRepository.addCardToProfile(dto);
        Optional<ProfileCardDTO> optionalProfileCardDTO = Optional.ofNullable(profileCardDTO);
        if (optionalProfileCardDTO.isPresent()) {
            System.out.println(profileCardDTO);
            System.out.println("Successfully added!");
        } else
            System.out.println("Mazgi nitoku!, try again");
    }

    public void showProfileCardList(ProfileDTO profileDTO) {
        List<CardDTO> list = profileCardRepository.getProfileCardList(profileDTO);
        if (list.isEmpty()) {
            System.out.println("You have not cards, Mazgiable");
            return;
        }
        for (CardDTO cardDTO : list) {
            System.out.println(cardDTO);
        }
    }

    public void showProfileCardList() {
        List<ProfileCardDTO> list = profileCardRepository.getProfileCardList();
        for (ProfileCardDTO profileCardDTO : list) {
            System.out.println(profileCardDTO);
        }
    }

    public void changeCardStatus(ProfileDTO dto, String card_number, CardStatus status) {
        CardDTO cardDTO = cardService.getCardByNumber(card_number);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isPresent()) {
            showProfileCardList(profileCardRepository.changeCardStatus(dto, cardDTO.getCard_number(), status));
        } else System.out.println("Wrong card number! Mazgi");
    }

    public void deleteProfileCard(ProfileDTO dto, String card_number) {
        CardDTO cardDTO = cardService.getCardByNumber(card_number);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isPresent()) {
            showProfileCardList(profileCardRepository.deleteProfileCard(dto, cardDTO.getCard_number()));
        } else System.out.println("Wrong card number! Mazgi");
    }

}
