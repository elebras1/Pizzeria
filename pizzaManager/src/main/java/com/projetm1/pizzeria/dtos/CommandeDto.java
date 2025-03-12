package com.projetm1.pizzeria.dtos;

import java.util.List;
import java.util.Objects;

public class CommandeDto {
    private Long id;
    private Long idCompte;
    private List<PanierDto> paniers;

    public CommandeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public List<PanierDto> getPaniers() {
        return paniers;
    }

    public void setPaniers(List<PanierDto> paniers) {
        this.paniers = paniers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandeDto that = (CommandeDto) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
