package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPersonalDataDto {
    private String firstName;
    private String lastName;
    private String telephone;
    private String company;
    private String address;
}
