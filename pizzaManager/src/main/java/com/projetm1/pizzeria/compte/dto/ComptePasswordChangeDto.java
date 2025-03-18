package com.projetm1.pizzeria.compte.dto;

import lombok.Data;

@Data
public class ComptePasswordChangeDto {
    private String oldPassword;
    private String newPassword;
    private String comfirmPassword;
}
