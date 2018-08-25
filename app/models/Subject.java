package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject extends BaseModel{

    @Constraints.Required
    public String subjectName;

    @ManyToMany
    public List<Student> students;

    public Subject(){
        subjectName = "";
        students = new ArrayList<>();
    }

    public Subject(String subjectName, ArrayList<Student> students){
        this.subjectName = subjectName;
        this.students = students;
    }

}