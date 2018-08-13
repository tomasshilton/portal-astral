package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Subject extends BaseModel{

    @Constraints.Required
    public String subjectName;

    @Constraints.Required
    public ArrayList<Student> students;

    public Subject(){
        subjectName = "";
        students = new ArrayList<>();
    }

    public Subject(String subjectName, ArrayList<Student> students){
        this.subjectName = subjectName;
        this.students = students;
    }
}
