package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Model;
import io.ebean.Transaction;
import models.Student;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class StudentEntity implements IEntity<Student>{

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public StudentEntity(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Optional<Boolean>> update(String id, Student entity) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Boolean> value = Optional.of(false);
            try {
                Student savedStudent = ebeanServer.find(Student.class).setId(id).findOne();
                if (savedStudent != null) {
                    savedStudent.firstName = entity.firstName;
                    savedStudent.lastName = entity.lastName;
                    savedStudent.update();
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
                final Optional<Student> computerOptional = Optional.ofNullable(ebeanServer.find(Student.class).setId(id).findOne());
                computerOptional.ifPresent(Model::delete);
                return Optional.of(true);
            } catch (Exception e) {
                return Optional.of(false);
            }
        }, executionContext);
    }

    @Override
    public CompletionStage<String> insert(Student entity) {
        return supplyAsync(() -> {
            entity.id = UUID.randomUUID().toString();
            ebeanServer.insert(entity);
            return entity.id;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<Student>> get(String id) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Student> value = Optional.empty();
            try {
                Student savedStudent = ebeanServer.find(Student.class).setId(id).findOne();
                if (savedStudent != null) {
                    value = Optional.of(savedStudent);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }
}
