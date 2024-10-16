package fr.android.mybiblio;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private HomeBookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_books_activity);

        // Get the selected category from the intent
        String category = getIntent().getStringExtra("category");

        // Set the title for the selected category
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText("Books in " + category);

        // Initialize RecyclerView and DatabaseHelper
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        // Load and display books for the selected category
        loadBooksByCategory(category);
    }

    private void loadBooksByCategory(String category) {
        bookList = new ArrayList<>();
        Cursor cursor = dbHelper.getBooksByCategory(category);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BOOK_ID));
                String isbn = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                String bookCategory = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
                String Description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

                // Create a new Book object and add it to the list
                Book book = new Book(id, isbn, title, author, bookCategory, Description);
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        // Set up the adapter
        bookAdapter = new HomeBookAdapter(bookList, null); // No click listener needed for display only
        recyclerViewBooks.setAdapter(bookAdapter);
    }
}
