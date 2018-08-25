package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
