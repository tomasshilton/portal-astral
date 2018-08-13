package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import models.Student;
import repository.StudentEntity;
import scala.concurrent.ExecutionContextExecutor;
import views.html.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class StudentController extends Controller {

    private final HttpExecutionContext executionContext;
    private final StudentEntity studentEntity;

    @Inject
    public StudentController (HttpExecutionContext executionContext, StudentEntity studentEntity) {
        this.executionContext = executionContext;
        this.studentEntity = studentEntity;
    }


    public CompletionStage<Result> saveStudent() {

        JsonNode json = request().body().asJson();
        Student realStudent = Json.fromJson(json, Student.class);
        return studentEntity.insert(realStudent).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            flash("success", "Computer " + realStudent.firstName + " " + realStudent.lastName + " has been created");
            return ok(data);
        }, executionContext.current());
    }

    public CompletionStage<Result> getStudent(String id) {

        return studentEntity.get(id).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            if(data.isPresent()){
                Student student = data.get();
                flash("success", "Computer " + student.firstName + " " + student.lastName + " has been created");
                return ok(Json.toJson(student));
            }else{
                return status(404, "Resource not found");
            }
        }, executionContext.current());
    }



}