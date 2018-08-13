package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;

@Entity
public class Student extends BaseModel{

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    public Student(){
        firstName = "";
        lastName = "";
    }

    public Student(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
