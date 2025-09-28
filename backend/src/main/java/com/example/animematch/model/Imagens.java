package com.example.animematch.model;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Imagens {

    private String urlImagemPequena;
    private String urlImagemMedia;
    private String urlImagemGrande;

    public Imagens() {
    }

    public Imagens(String urlImagemPequena, String urlImagemMedia, String urlImagemGrande) {
        this.urlImagemPequena = urlImagemPequena;
        this.urlImagemMedia = urlImagemMedia;
        this.urlImagemGrande = urlImagemGrande;
    }

    public String getUrlImagemPequena() {
        return urlImagemPequena;
    }

    public void setUrlImagemPequena(String urlImagemPequena) {
        this.urlImagemPequena = urlImagemPequena;
    }

    public String getUrlImagemMedia() {
        return urlImagemMedia;
    }

    public void setUrlImagemMedia(String urlImagemMedia) {
        this.urlImagemMedia = urlImagemMedia;
    }

    public String getUrlImagemGrande() {
        return urlImagemGrande;
    }

    public void setUrlImagemGrande(String urlImagemGrande) {
        this.urlImagemGrande = urlImagemGrande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imagens imagens = (Imagens) o;
        return Objects.equals(urlImagemPequena, imagens.urlImagemPequena) &&
               Objects.equals(urlImagemMedia, imagens.urlImagemMedia) &&
               Objects.equals(urlImagemGrande, imagens.urlImagemGrande);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlImagemPequena, urlImagemMedia, urlImagemGrande);
    }

    @Override
    public String toString() {
        return "Imagens{" +
                "urlImagemPequena='" + urlImagemPequena + '\'' +
                ", urlImagemMedia='" + urlImagemMedia + '\'' +
                ", urlImagemGrande='" + urlImagemGrande + '\'' +
                '}';
    }
}