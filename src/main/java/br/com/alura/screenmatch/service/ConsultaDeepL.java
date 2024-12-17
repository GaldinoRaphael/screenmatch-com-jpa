package br.com.alura.screenmatch.service;


import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

public class ConsultaDeepL {
    public static String obterTraducao(String texto) {
        var translator = new Translator(System.getenv("DEEP_L_KEY"));
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