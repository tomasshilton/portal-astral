package repository;

import models.Student;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface IEntity<T> {

    CompletionStage<Optional<Boolean>> update(String id, T entity);

    CompletionStage<Optional<Boolean>> delete(String id);

    CompletionStage<String> insert(T entity);

    CompletionStage<Optional<Student>> get(String id);
}
