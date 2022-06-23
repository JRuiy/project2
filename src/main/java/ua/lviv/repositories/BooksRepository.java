package ua.lviv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lviv.models.Book;
import ua.lviv.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByPerson(Person person);

    List<Book> findByTitleStartingWith(String title);
}
