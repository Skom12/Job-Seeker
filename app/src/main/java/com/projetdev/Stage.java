package com.projetdev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stage  {
    public String subUserId,type,domaine,descrip,date,durée,contact,nomEnt,imageEnt,uniqueId;
   // public List<String> userEtudList ;
    public Stage() {
    }

    public Stage (String uniqueId,String subUserId,String nomEnt, String type, String domaine, String descrip,String date,String durée,String contact,String imageEnt) {
        this.subUserId = subUserId;
        this.type = type;
        this.domaine = domaine;
        this.descrip = descrip;
        this.date = date;
        this.contact=contact;
        this.durée=durée;
        this.nomEnt=nomEnt;
        this.imageEnt=imageEnt;
        this.uniqueId=uniqueId;
        //this.userEtudList=userEtudList;
    }

}
