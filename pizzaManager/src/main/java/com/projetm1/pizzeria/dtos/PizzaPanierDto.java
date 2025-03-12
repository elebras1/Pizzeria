package com.projetm1.pizzeria.dtos;

import java.util.List;
import java.util.Objects;

public class PizzaPanierDto {
    private Long id;
    private PizzaDto pizza;
    private List<IngredientDto> ingredients;
    private Long idPanier;

    public PizzaPanierDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PizzaDto getPizza() {
        return pizza;
    }

    public void setPizza(PizzaDto pizza) {
        this.pizza = pizza;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public Long getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(Long idPanier) {
        this.idPanier = idPanier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PizzaPanierDto that = (PizzaPanierDto) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
