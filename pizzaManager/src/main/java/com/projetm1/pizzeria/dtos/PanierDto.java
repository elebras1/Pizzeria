package com.projetm1.pizzeria.dtos;

import java.util.Objects;
import java.util.Set;

public class PanierDto {
    private Long id;
    private Long idCommande;
    private Set<Long> idPizzas;

    public PanierDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Long idCommande) {
        this.idCommande = idCommande;
    }

    public Set<Long> getIdPizzas() {
        return idPizzas;
    }

    public void setIdPizzas(Set<Long> idPizzas) {
        this.idPizzas = idPizzas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PanierDto panierDto = (PanierDto) o;

        return Objects.equals(id, panierDto.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
