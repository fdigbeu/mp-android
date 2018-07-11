package com.maliprestige.Model;

import java.util.ArrayList;
import java.util.Random;

public class Cryptage {
    // Methode permettant de crypter des données
    public String crypterData(String motdepasse){
        // On récupère dans un tableau, caractère par caractère
        char[] strSplit = motdepasse.toCharArray();
        // Inversion du mot à crypter et intercallage de caractères
        String strInverse = "";
        for(int i = strSplit.length-1; i >= 0; i--){
            // Récupération aléatoire des cinq premiers caractères
            String strRandom = this.shuffleCharValue();
            // Intercallage entre chaque caractère
            strInverse += ""+strSplit[i]+strRandom;
        }
        return this.shuffleCharValue()+strInverse;
    }

    public String decrypterData(String motdepasseCrypte){
        // On récupère dans un tableau, 6 caractères par 6 caractères
        String[] strSplit = this.stringSplit(motdepasseCrypte);
        String strOrdre = "";
        for(int i=strSplit.length-1; i >= 0; i--){
            if(strSplit[i].length()==6){
                char[] tabSplit = strSplit[i].toCharArray();
                strOrdre += ""+tabSplit[5];
            }
        }
        return strOrdre;
    }

    private String[] stringSplit(String chaine){
        ArrayList<String> arrayList = new ArrayList<>();
        // On récupère dans un tableau, caractère par caractère
        char[] strSplit = chaine.toCharArray();
        String strOrdre = "";
        for(int i = 1; i <= strSplit.length; i++){
            int j = i-1;
            strOrdre += strSplit[j];
            if(i%6 == 0 || i == strSplit.length){
                arrayList.add(strOrdre);
                strOrdre = "";
            }
        }
        // ArrayList to Array
        String[] stockArr = new String[arrayList.size()];
        stockArr = arrayList.toArray(stockArr);

        return stockArr;
    }

    private String shuffleCharValue() {
        // Lettre à intercaller
        String intercallage = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // On récupère dans un tableau, lettre par lettre
        char[] intercallageSplit = intercallage.toCharArray();
        // On récupère cinq caratctètes aléatoirement
        String strAleatoire = "";
        for (int i=0; i < 5; i++){
            Random random = new Random();
            int aleatoire = random.nextInt(intercallageSplit.length);
            strAleatoire += ""+intercallageSplit[aleatoire];
        }
        return strAleatoire;
    }
}
