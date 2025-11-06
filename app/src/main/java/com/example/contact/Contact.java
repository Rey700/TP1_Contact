package com.example.contact;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Contact implements Serializable {
    private LinkedHashMap<String, String> attributs;

    public Contact(LinkedHashMap<String, String> attributs) {
        this.attributs = attributs;
    }
    public Contact(int num) {
        attributs = new LinkedHashMap<>();
        for(String champ:Carnet.champsPredefinis){
            attributs.put(champ,"");
        }
        attributs.put("num", String.valueOf(num));
        attributs.put("avatarId", "0");
    }
    public int getNum() {
        String val = attributs.get("num");
        if (val == null || val.trim().isEmpty()) {
            return -1;
        }
        return Integer.parseInt(val);
    }

    public int getAvatarId(){
        return Integer.parseInt(Objects.requireNonNull(attributs.get("avatarId")));
    }
    public void setAvatarId(int i){
        attributs.put("avatarId",Integer.toString(i));
    }
    public String getAttribut(String key){
        return attributs.get(key);
    }

    public LinkedHashMap<String, String> getAttributs(){
        return attributs;
    }

    public void setAttribut(String key,String value){
        attributs.put(key, value);
    }

    public void ajouterChampPersonalise(String champ){
        if(!attributs.containsKey(champ)){
            attributs.put(champ,"");
        }
    }

    public void updateChamps() {
        LinkedHashMap<String, String> nouveaux = new LinkedHashMap<>();
        for (String champ : Carnet.champsPredefinis) {
            // on met le nouvel attribut predef avant les personnalisÃ©s
            nouveaux.put(champ, attributs.getOrDefault(champ, ""));
        }
        for (String champ : attributs.keySet()) {
            if (!Carnet.champsPredefinis.contains(champ)) {
                nouveaux.put(champ, attributs.get(champ));
            }
        }
        attributs = nouveaux;
    }

    @Override
    public String toString() {
        String res = "";
        int i = 0;

        for (String key : attributs.keySet()) {
            res += key + "=" + attributs.get(key);
            if (i < attributs.size() - 1) {
                res += ";";
            }
            i++;
        }
        return res;
    }

    public static Contact fromString(String line) {
        if (line == null || line.isEmpty()) return null;
        String[] parts = line.split(";");
        LinkedHashMap<String, String> attributs = new LinkedHashMap<>();
        for (String paire : parts) {
            String[] couple = paire.split("=");
            if (couple.length == 2) {
                attributs.put(couple[0], couple[1]);
            }
        }
        Contact c = new Contact(attributs);
        c.updateChamps();
        return c;
    }
}