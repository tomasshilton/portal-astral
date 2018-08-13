package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Student;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repository.StudentModule;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class StudentController extends Controller {

    private final HttpExecutionContext executionContext;
    private final StudentModule studentModule;

    @Inject
    public StudentController (HttpExecutionContext executionContext, StudentModule studentModule) {
        this.executionContext = executionContext;
        this.studentModule = studentModule;
    }


    public CompletionStage<Result> saveStudent() {

        JsonNode json = request().body().asJson();
        Student realStudent = Json.fromJson(json, Student.class);
        return studentModule.insert(realStudent).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return status(201, data);
        }, executionContext.current());
    }

    public CompletionStage<Result> getStudent(String id) {

        return studentModule.get(id).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            if(data.isPresent()){
                Student student = data.get();
                return ok(Json.toJson(student));
            }else{
                return status(404, "Resource not found");
            }
        }, executionContext.current());
    }



}