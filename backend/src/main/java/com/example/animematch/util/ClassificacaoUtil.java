package com.example.animematch.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassificacaoUtil {
    
    // Classificações que contêm conteúdo inadequado
    private static final Set<String> CLASSIFICACOES_PROIBIDAS = new HashSet<>(
        Arrays.asList(
            "R+ - Mild Nudity",
            "Rx - Hentai",
            "R+",
            "Rx",
            "Hentai"
        )
    );
    
    /**
     * Verifica se uma classificação está na lista de proibidas
     */
    public static boolean ehClassificacaoProibida(String classificacao) {
        if (classificacao == null || classificacao.isEmpty()) {
            return false;
        }
        
        // Verifica se a classificação completa está na lista
        if (CLASSIFICACOES_PROIBIDAS.contains(classificacao)) {
            return true;
        }
        
        // Verifica se contém alguma das palavras-chave proibidas
        String classificacaoLower = classificacao.toLowerCase();
        for (String proibida : CLASSIFICACOES_PROIBIDAS) {
            if (classificacaoLower.contains(proibida.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Retorna lista de classificações permitidas para exibição no frontend
     */
    public static List<String> getClassificacoesPermitidas() {
        return Arrays.asList(
            "G - All Ages",
            "PG - Children",
            "PG-13 - Teens 13 or older",
            "R - 17+ (violence & profanity)"
        );
    }
}

