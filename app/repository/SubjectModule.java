package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;
import io.ebean.Transaction;
import models.Student;
import models.Subject;
import play.db.ebean.EbeanConfig;
import play.libs.Json;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.ok;
import static play.mvc.Results.status;

public class SubjectModule implements IModule<Subject>{

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public SubjectModule(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Optional<Boolean>> update(String id, Subject entity) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Boolean> value = Optional.of(false);
            try {
                Subject savedSubject = ebeanServer.find(Subject.class).setId(id).findOne();
                if (savedSubject != null) {
                    savedSubject.subjectName = entity.subjectName;
                    savedSubject.students = entity.students;
                    savedSubject.update();
                    txn.commit();
                    value = Optional.of(true);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<Boolean>> delete(String id) {
        return supplyAsync(() -> {
            try {
                final Optional<Subject> computerOptional = Optional.ofNullable(ebeanServer.find(Subject.class).setId(id).findOne());
                computerOptional.ifPresent(Model::delete);
                return Optional.of(true);
            } catch (Exception e) {
                return Optional.of(false);
            }
        }, executionContext);
    }

    @Override
    public CompletionStage<String> insert(Subject entity) {
        return supplyAsync(() -> {
            entity.id = UUID.randomUUID().toString();
            ebeanServer.insert(entity);
            return entity.id;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<Subject>> get(String id) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Subject> value = Optional.empty();
            try {
                Subject savedSubject = ebeanServer.find(Subject.class).setId(id).findOne();
                if (savedSubject != null) {
                    value = Optional.of(savedSubject);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    public CompletionStage<Optional<Subject>> addStudentToSubject(Student student, String subjectID) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Subject> value = Optional.empty();
            try {
                Subject subject = ebeanServer.find(Subject.class).setId(subjectID).findOne();
                if (subject != null) {
                    subject.students.add(student);
                    update(subjectID, subject);
                    value = Optional.of(subject);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }
}
