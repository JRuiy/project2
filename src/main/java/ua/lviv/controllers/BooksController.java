package ua.lviv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.lviv.dao.PersonDAO;
import ua.lviv.models.Book;
import ua.lviv.models.Person;
import ua.lviv.services.BooksService;
import ua.lviv.services.PeopleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showBooks(Model model){
        model.addAttribute("books", booksService.showBooks());
        return "books/showBooks";
    }

    @GetMapping(params = {"page", "books_per_page"})
    public String showBooks(@RequestParam int page, @RequestParam("books_per_page") int booksPerPage, Model model){
        int currentPage = 1;
        Page<Book> pageOfBook = booksService.listOfBookByPage(page, booksPerPage);
        int totalPage = pageOfBook.getTotalPages();
        long totalElem = pageOfBook.getTotalElements();
        List<Book> listOfBook = pageOfBook.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("totalElem", totalElem);
        model.addAttribute("books", listOfBook);
        return "books/showBooks";
    }

    @GetMapping(params = "sort_by_year")
    public String showBooks(@RequestParam("sort_by_year") boolean sortByYear, Model model){
        if(sortByYear==true)
            model.addAttribute("books", booksService.listOfBookSorted());
        else
            return "redirect:/books";
        return "books/showBooks";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book){
        return "books/newBook";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/newBook";
        }
        booksService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person")Person person){
        model.addAttribute("book", booksService.showBook(id));

        Person owner = booksService.showOwner(id);
        if(owner!=null)
            model.addAttribute("owner", owner);
        else
            model.addAttribute("people", peopleService.showPeople());
        return "books/showBook";
    }
    @PatchMapping("/{id}/addUser")
    public String addBookToPerson(@PathVariable int id, @ModelAttribute("person") Person person){
        booksService.addBookToPerson(id, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release-book")
    public String deleteBookFromPerson(@PathVariable("id") int id){
        booksService.deleteBookFromPerson(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.showBook(id));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/editBook";
        }
        booksService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id){
        booksService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBook(@RequestParam("query") String query, Model model){
        model.addAttribute("books", booksService.searchBook(query));
        return "books/search";
    }
}
