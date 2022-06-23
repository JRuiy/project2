package ua.lviv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.models.Person;
import ua.lviv.repositories.BooksRepository;
import ua.lviv.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> showPeople(){
        return peopleRepository.findAll();
    }

    public void createPerson(Person person){
        peopleRepository.save(person);
    }

    public void deletePerson(int id){
        peopleRepository.deleteById(id);
    }

    public Person showPerson(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> showPerson(String fullName){
        return peopleRepository.findByFullName(fullName);
    }

    public void editPerson(int id, Person personWithUpdate){
        personWithUpdate.setId(id);
        peopleRepository.save(personWithUpdate);
    }


}
