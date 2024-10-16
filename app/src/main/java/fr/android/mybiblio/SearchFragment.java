package fr.android.mybiblio;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearshBookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the UI elements
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the database helper
        dbHelper = new DatabaseHelper(getContext());

        // Initialize the book list and adapter
        bookList = new ArrayList<>();
        bookAdapter = new SearshBookAdapter(bookList); // Initialize adapter with empty list
        recyclerView.setAdapter(bookAdapter); // Set the adapter to RecyclerView

        // Load all books initially
        loadAllBooks();

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchBooks(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    loadAllBooks(); // Reset to show all books if search is cleared
                } else {
                    searchBooks(newText); // Perform search as the user types
                }
                return false;
            }
        });

        return view;
    }

    // Load all books from the database
    private void loadAllBooks() {
        Cursor cursor = dbHelper.getAllBooks();
        bookList.clear(); // Clear the list before loading new data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BOOK_ID));
                String isbn = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

                Book book = new Book(id, isbn, title, author, category, description);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }

    // Search books based on query
    private void searchBooks(String query) {
        Cursor cursor = dbHelper.searchBooks(query);
        bookList.clear(); // Clear the list before loading search results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BOOK_ID));
                String isbn = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

                Book book = new Book(id, isbn, title, author, category, description);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }
}