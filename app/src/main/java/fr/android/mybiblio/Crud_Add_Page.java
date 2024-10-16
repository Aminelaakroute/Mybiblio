package fr.android.mybiblio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Crud_Add_Page extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText titleEditText, isbnEditText, authorEditText, categoryEditText, DescriptionOfBook ;
    private Button saveButton;
    private int bookId = -1; // Store the book ID for edit or delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_add_page);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // Link UI elements
        titleEditText = findViewById(R.id.TitleOfBook);
        isbnEditText = findViewById(R.id.ISBN);
        authorEditText = findViewById(R.id.Author);
        categoryEditText = findViewById(R.id.Book_Category); // Assuming category field here
        DescriptionOfBook = findViewById(R.id.DescriptionOfBook);

        saveButton = findViewById(R.id.SaveButton);


        // Check if we are editing an existing book
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BOOK_ID")) {
            bookId = intent.getIntExtra("BOOK_ID", -1);
            loadBookDetails(bookId);
        }

        // Save book (add new or update existing)
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookId == -1) {
                    addNewBook(); // Add a new book
                }
            }
        });


    }

    // Method to add a new book to the database
    private void addNewBook() {
        String title = titleEditText.getText().toString();
        String isbn = isbnEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String description = DescriptionOfBook.getText().toString();

        if (title.isEmpty() || isbn.isEmpty() || author.isEmpty() || category.isEmpty() ||description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = db.addBook(isbn, title, author, category, description);
        if (result) {
            Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Send result back to SettingsFragment
            finish(); // Close the activity after updating
        } else {
            Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load book details when editing
    @SuppressLint("Range")
    private void loadBookDetails(int bookId) {
        Cursor cursor = db.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            titleEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));
            isbnEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN)));
            authorEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR)));
            categoryEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY)));
            DescriptionOfBook.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)));
            cursor.close();
        }
    }


    // Clear form fields after operation
    private void clearForm() {
        titleEditText.setText("");
        isbnEditText.setText("");
        authorEditText.setText("");
        categoryEditText.setText("");
        DescriptionOfBook.setText("");
        bookId = -1; // Reset bookId
    }
}
