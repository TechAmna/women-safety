package com.example.womenssafety;

public class ContactModel {
String name;
String number;
ContactModel(){

}
ContactModel(String name, String number){
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
