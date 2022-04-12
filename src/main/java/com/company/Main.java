package com.company;

import com.company.container.ComponentContainer;
import com.company.controller.ProfileController;
import com.company.db.DBConnection;
import com.company.repository.ProfileCardRepository;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Setter
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        ProfileController profileController = (ProfileController) context.getBean("ProfileController");
        DBConnection.createTable();
        profileController.simpleMenu();
    }
}
