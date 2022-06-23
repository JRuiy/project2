package ua.lviv.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 200, message = "Full name should be between 2 and 200 characters")
    private String fullName;
    @Column(name = "year_of_born")
    @Min(value = 1900, message = "Year of born should be greater than 1900")
    private int yearOfBorn;

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    public Person() {
    }

    public Person(String fullName, int yearOfBorn, List<Book> books) {
        this.fullName = fullName;
        this.yearOfBorn = yearOfBorn;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBorn() {
        return yearOfBorn;
    }

    public void setYearOfBorn(int yearOfBorn) {
        this.yearOfBorn = yearOfBorn;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
