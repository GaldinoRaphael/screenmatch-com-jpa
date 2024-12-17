package br.com.alura.screenmatch.service;


import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        String authKey = "c548da5d-1732-42d5-a652-fcd95e043689:fx";
        var translator = new Translator(authKey);
        TextResult result =
                null;
        try {
            result = translator.translateText(texto, "EN", "PT-BR");
        } catch (DeepLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.getText().trim();
    };
}