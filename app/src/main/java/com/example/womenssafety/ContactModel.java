package com.example.womenssafety;

public class ContactModel {
    String id;
String name;
String number;
ContactModel(){

}
ContactModel(String name, String number){
    this.name = name;
    this.number = number;
    this.id=id;
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
