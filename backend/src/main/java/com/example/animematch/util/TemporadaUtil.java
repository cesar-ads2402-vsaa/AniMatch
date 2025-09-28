package com.example.animematch.util;

public class TemporadaUtil {

    public static String getTemporadaAtual() {
        int mes = java.time.LocalDate.now().getMonthValue();

        if (mes >= 1 && mes <= 3) {
            return "winter";
        } else if (mes >= 4 && mes <= 6) {
            return "spring";
        } else if (mes >= 7 && mes <= 9) {
            return "summer";
        } else {
            return "fall";
        }
    }

    public static int getAnoAtual() {
        return java.time.LocalDate.now().getYear();
    }
}
