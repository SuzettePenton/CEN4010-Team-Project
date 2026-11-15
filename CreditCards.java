package com.example.profile_management.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_cards")
@JsonPropertyOrder({ "username", "cardNumber", "name", "cardExpirationDate", "cardCVV" })
public class CreditCards {
         @Id 
         @Column (name = "Username")
         private String username;
         @Column(name = "Card_Number")
         private String  cardNumber;
         @Column (name = "User_Name")
            private String name;
         @Column (name = "Card_Expiration_Date")
            private String cardExpirationDate;
         @Column (name = "Card_CVV")
            private String cardCVV;

        public String getUsername() {
            return username;
    }
        public void setUsername(String username) {
            this.username = username;
    }
        public String getCardNumber() {
            return cardNumber;
    }
        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;   
    }
         public String name() {
              return name;
    }
          public void setCardUserName(String name) {
              this.name = name;
    }
          public String getCardExpirationDate() {
              return cardExpirationDate;
    }
          public void setCardExpirationDate(String cardExpirationDate) {
              this.cardExpirationDate = cardExpirationDate;
    }
          public String getCardCVV() {
              return cardCVV;
    }
           public void setCardCVV(String cardCVV) {
              this.cardCVV = cardCVV;
    }
}
