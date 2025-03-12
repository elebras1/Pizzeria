package com.projetm1.pizzeria.dtos;

import java.util.Objects;

public class PizzaDto {
    private Long id;
    private String nom;
    private String description;
    private String photo;

    public PizzaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PizzaDto pizzaDto = (PizzaDto) o;

        if (!Objects.equals(id, pizzaDto.id)) return false;
        if (!Objects.equals(nom, pizzaDto.nom)) return false;
        if (!Objects.equals(description, pizzaDto.description))
            return false;
        return Objects.equals(photo, pizzaDto.photo);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
