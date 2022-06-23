package ua.lviv.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.lviv.dao.PersonDAO;
import ua.lviv.models.Person;
import ua.lviv.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> insertPerson = peopleService.showPerson(person.getFullName());
        if(insertPerson.isPresent() && insertPerson.get().getId()!=person.getId()){
            errors.rejectValue("fullName", "", "This name is already taken");
        }
    }
}
