package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
@Document(collection = "commentaire")
public class Commentaire {
    @Id
    private String id;
    private String texte;
    private String photo;
    private String idCommande;

    public Commentaire() {
    }


    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
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

        Commentaire that = (Commentaire) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

