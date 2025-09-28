package com.example.animematch.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class Periodo {
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Periodo() {
    }

    public Periodo(LocalDate dataInicio, LocalDate dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodo periodo = (Periodo) o;
        return Objects.equals(dataInicio, periodo.dataInicio) && Objects.equals(dataFim, periodo.dataFim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataInicio, dataFim);
    }

    @Override
    public String toString() {
        return "Periodo{" +
                "dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                '}';
    }
}
