package fr.android.mybiblio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Crud_Edit_Page extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText titleEditText, isbnEditText, authorEditText, categoryEditText, DescriptionOfBook;
    private Button saveButton;
    private int bookId = -1; // The ID of the book to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_edit_page);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        titleEditText = findViewById(R.id.TitleOfBook);
        isbnEditText = findViewById(R.id.ISBN);
        authorEditText = findViewById(R.id.Author);
        categoryEditText = findViewById(R.id.Book_Category); // Assuming it's category
        DescriptionOfBook = findViewById(R.id.DescriptionOfBook);

        saveButton = findViewById(R.id.SaveButton);

        // Get the book ID passed from the SettingsFragment
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BOOK_ID")) {
            bookId = intent.getIntExtra("BOOK_ID", -1);
            loadBookDetails(bookId);
        }

        // Set the click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookId != -1) {
                    updateBook(bookId);
                }
            }
        });
    }

    // Load book details into the input fields
    private void loadBookDetails(int bookId) {
        Cursor cursor = dbHelper.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            titleEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));
            isbnEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN)));
            authorEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR)));
            categoryEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY)));
            DescriptionOfBook.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)));
            cursor.close();
        }
    }

    // Update book in the database
    private void updateBook(int bookId) {
        String title = titleEditText.getText().toString();
        String isbn = isbnEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String description = DescriptionOfBook.getText().toString();

        if (title.isEmpty() || isbn.isEmpty() || author.isEmpty() || category.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.updateBook(bookId, isbn, title, author, category, description);
        if (result) {
            Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Send result back to SettingsFragment
            finish(); // Close the activity after updating
        } else {
            Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show();
        }
    }
}
