package fr.android.mybiblio;

public class Book {

    private int Book_Id;
    private String Title;
    private String Author;
    private String isbn;
    private String category;
    private String description;


    public Book(int id, String isbn, String title, String author, String category, String description) {
        this.Book_Id = id;
        this.isbn = isbn;
        this.Title = title;
        this.Author = author;
        this.category = category;
        this.description = description;
    }

    public int getBook_Id() {
        return Book_Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
