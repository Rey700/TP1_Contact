package com.example.contact;

public class Contact{
    private int id;
    private string nom;
    private string prenom;
    private string telephone;
    private string adresse;
    private string codePostal;
    private string email;
    private string metier;
    private string situation;
    private string image;

    public Contact(int id, string nom, string prenom, string telephone, string adresse, string
                   codePostal, string email, string metier, string situation, string image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.email = email;
        this.metier = metier;
        this.situation = situation;
        this.image = image;
    }

    public int getId() {return id;}
    public string getNom() {return nom;}
    public string getPrenom() {return prenom;}
    public string getTelephone() {return telephone;}
    public string getAdresse() {return adresse;}
    public string getCodePostal() {return codePostal;}
    public string getEmail() {return email;}
    public string getMetier() {return metier;}
    public string getSituation() {return situation;}
    public string getImage() {return image;}
}