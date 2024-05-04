package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Author;
import lombok.Data;
import lombok.Getter;

@Data
public class AuthorInformationResponse {
    Long id;
    String name;
    String option;
    String nationality;
    String description;
    String birthYear;
    String gender;


    public AuthorInformationResponse(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.option = author.getAuthorOption().toString();
        this.nationality = author.getNationality();
        this.description = author.getDescription();
        this.birthYear = author.getBirthYear();
        this.gender = "남성";


        switch (author.getAuthorOption().toString()) {
            case "WOMEN":
                gender = "여성";
                break;
            case "SECRET":
                gender = "미상";
                break;
        }
    }
}