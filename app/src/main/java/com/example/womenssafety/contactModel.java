package com.example.womenssafety;

import android.widget.EditText;

public class contactModel {
String name;
String number;
contactModel(){

}
contactModel(String name, String number){
    this.name = name;
    this.number = number;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
