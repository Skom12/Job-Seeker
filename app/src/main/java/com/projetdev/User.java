package com.projetdev;

public class User {
    public String nom,prenom,pseudo,mail,domain,adresse,image,cv,uid;

    public User() {
    }

    public User(String uid,String nom, String prenom, String pseudo, String mail,String domain,String adresse,String image,String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.domain = domain;
        this.adresse=adresse;
        this.image=image;
        this.cv=cv;
        this.uid=uid;
    }
}
