package ua.lviv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.lviv.dao.BookDAO;
import ua.lviv.models.Person;
import ua.lviv.services.BooksService;
import ua.lviv.services.PeopleService;
import ua.lviv.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, BookDAO bookDAO, BooksService booksService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model){
        model.addAttribute("people", peopleService.showPeople());
        return "people/showPeople";
    }

    @GetMapping("/new")
    public String newPeople(@ModelAttribute("person")Person person){
        return "people/newPerson";
    }

    @PostMapping("/new")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            return "people/newPerson";
        }
        peopleService.createPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.showPerson(id));
        model.addAttribute("books", booksService.showBooksAtPerson(id));
        return "people/showPerson";

    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.deletePerson(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.showPerson(id));
        return "people/editPerson";
    }

    @PatchMapping ("/{id}")
    public String updatePerson(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            return "people/editPerson";
        }
        peopleService.editPerson(id, person);
        return "redirect:/people";
    }
}
