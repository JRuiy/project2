package ua.lviv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lviv.models.Book;
import ua.lviv.models.Person;
import ua.lviv.repositories.BooksRepository;
import ua.lviv.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> showBooks(){
        return booksRepository.findAll();
    }


    public Page<Book> listOfBookByPage(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return booksRepository.findAll(pageable);
    }

    public List<Book> listOfBookSorted(){
        return booksRepository.findAll(Sort.by("year"));
    }

    @Transactional
    public void createBook(Book book){
        booksRepository.save(book);
    }

    public Book showBook(int id){
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBook(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void updateBook(int id, Book book){
        book.setId(id);
        booksRepository.save(book);
    }

    public List<Book> showBooksAtPerson(int id) {
        List<Book> list = booksRepository.findBooksByPerson(peopleRepository.findById(id).orElse(null));
        System.out.println(list);
        for (Book book: list) {
            Date currentDate = new Date();
            long tenDays = 864000000;
            if((currentDate.getTime() - book.getTakenAt().getTime())>tenDays)
                book.setOverdue(true);
            else
                book.setOverdue(false);
        }
        return list;
    }

    public Person showOwner(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElseThrow(null).getPerson();
    }

    @Transactional
    public void addBookToPerson(int id, Person person){
        Optional<Book> book = booksRepository.findById(id);
        book.get().setPerson(person);
        book.get().setTakenAt(new Date());
        booksRepository.save(book.get());
    }

    @Transactional
    public void deleteBookFromPerson(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.get().setPerson(null);
        book.get().setTakenAt(null);
        booksRepository.save(book.get());
    }

    public List<Book> searchBook(String title){
        return booksRepository.findByTitleStartingWith(title);
    }
}
