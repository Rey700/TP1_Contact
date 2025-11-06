package com.example.contact;

import android.widget.Toast;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;


public class Carnet {
    private static Carnet instance; // instance partagÃ©e par toutes les classes (singleton)
    private ArrayList<Contact> contacts;
    private int currentId;
    private int currentIndex;
    static ArrayList<String> champsPredefinis;
    final int[] avatars = {
            R.drawable.client1,
            R.drawable.client2,
            R.drawable.client3,
            R.drawable.client4,
            R.drawable.client5,
            R.drawable.client6,
            R.drawable.client7,
    };
    private Carnet() {
        champsPredefinis = new ArrayList<>(Arrays.asList("num","nom","prenom","tel","adresse",
                "codePostal","email","metier","situation","avatarId"));
        contacts = new ArrayList<>();
        contacts.add(new Contact(0));
        currentId = 0;
        currentIndex = 0;
    }
    public static Carnet getInstance() {
        if (instance == null) {
            instance = new Carnet();
        }
        return instance;
    }

    public void ajouterChampPredefini(String champ){
        if(!champsPredefinis.contains(champ)){
            champsPredefinis.add(champ);
            for(Contact c:contacts){
                c.updateChamps();
            }
        }
    }

    public ArrayList<Contact> getContacts(){return this.contacts;}

    public int[] getAvatars(){
        return avatars;
    }

    public void moveAvatar(int i){
        if (i != -1 && i != 1) {
            throw new IllegalArgumentException("i doit valoir 1 ou -1");
        }
        Contact c = getCurrent();
        int id = c.getAvatarId() + i;
        c.setAvatarId(id);
        if(c.getAvatarId() < 0){
            c.setAvatarId(avatars.length -1);
        }
        if(c.getAvatarId() >= avatars.length){
            c.setAvatarId(0);
        }
    }

    public Contact newContact(){
        Contact c = new Contact(currentId);
        currentId++;
        addContact(c);
        return c;
    }
    public void addContact(Contact c) {
        contacts.add(c);
        currentIndex = contacts.size() - 1;
    }

    public void removeCurrent() {
        if (currentIndex >= 0 && currentIndex < contacts.size()) {
            contacts.remove(currentIndex);
            if (contacts.isEmpty()) {
                currentIndex = -1;
            } else if (currentIndex >= contacts.size()) {
                currentIndex = contacts.size() - 1;
            }
        }
    }

    public Contact getCurrent() {
        if (currentIndex >= 0 && currentIndex < contacts.size()) {
            return contacts.get(currentIndex);
        }
        else{
            return newContact();
        }

    }

    public Contact move(int i) {
        if (i != -1 && i != 1) {
            throw new IllegalArgumentException("move() n'accepte que -1 ou 1");
        }
        if (contacts.isEmpty()) return null;
        currentIndex += i;

        if (currentIndex < 0) {
            return last();
        } else if (currentIndex >= contacts.size()) {
            return first();
        }
        return getCurrent();
    }
    public Contact first() {
        if (!contacts.isEmpty()) {
            currentIndex = 0;
        }
        return getCurrent();
    }

    public Contact milieu() {
        if (!contacts.isEmpty()) {
            currentIndex = contacts.size() / 2; //le num et div sont entiers, le res le sera aussi
        }
        return getCurrent();
    }

    public Contact last() {
        if (!contacts.isEmpty()) {
            currentIndex = contacts.size() - 1;
        }
        return getCurrent();
    }

    public Contact goTo(int id) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getNum() == id) {
                currentIndex = i;
                return getCurrent();
            }
        }
        return null;
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Contact c : contacts) {
                writer.write(c.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String fileName) throws IOException {
        contacts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int maxId = -1;
            while ((line = reader.readLine()) != null) {
                Contact c = Contact.fromString(line);
                if (c != null) {
                    contacts.add(c);
                    if (c.getNum() > maxId) {
                        maxId = c.getNum();
                    }
                }
            }
            currentIndex = contacts.isEmpty() ? -1 : 0;
            currentId = maxId + 1;
        }
    }

    public Contact getContactByNum(int num){
        for(Contact c: contacts){
            if(c.getNum() == num){
                return c;
            }
        }
        return null;
    }

    public int size() {
        return contacts.size();
    }

    public int getIndexById(int id) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getNum() == id) {
                return i;
            }
        }
        return -1;
    }


}