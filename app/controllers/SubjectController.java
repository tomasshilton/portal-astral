package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Student;
import models.Subject;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repository.StudentModule;
import repository.SubjectModule;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SubjectController extends Controller {

    private final HttpExecutionContext executionContext;
    private final SubjectModule subjectModule;
    private final StudentModule studentModule;

    @Inject
    public SubjectController (HttpExecutionContext executionContext, SubjectModule subjectModule, StudentModule studentModule) {
        this.executionContext = executionContext;
        this.subjectModule = subjectModule;
        this.studentModule = studentModule;
    }


    public CompletionStage<Result> saveSubject() {

        JsonNode json = request().body().asJson();
        Subject realSubject = Json.fromJson(json, Subject.class);
        return subjectModule.insert(realSubject).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return status(201, data);
        }, executionContext.current());
    }

    public CompletionStage<Result> getSubject(String id) {

        return subjectModule.get(id).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            if(data.isPresent()){
                Subject subject = data.get();
                return ok(Json.toJson(subject));
            }else{
                return status(404, "Resource not found");
            }
        }, executionContext.current());
    }

    public CompletionStage<Result> saveStudentToSubject(String subjectId){
        JsonNode json = request().body().asJson();
        Student student = Json.fromJson(json, Student.class);

        return subjectModule.addStudentToSubject(student, subjectId).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            if(data.isPresent()){
                return ok(Json.toJson(data));
            }else{
                return status(404, "Resource not found");
            }
        }, executionContext.current());
    }
}