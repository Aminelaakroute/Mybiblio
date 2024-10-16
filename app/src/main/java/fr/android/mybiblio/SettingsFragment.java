package fr.android.mybiblio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize the database helper and RecyclerView
        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, new BookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(View view, int position) {
                showPopupMenu(view, position);
            }
        });
        recyclerView.setAdapter(bookAdapter);

        // Load books from the database
        loadBooksFromDatabase();

        // Add book button click listener
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the add book activity
                Intent intent = new Intent(getActivity(), Crud_Add_Page.class);
                startActivityForResult(intent, REQUEST_CODE_ADD); // Use startActivityForResult
            }
        });

        return view;
    }

    // Load books from the database and refresh RecyclerView
    private void loadBooksFromDatabase() {
        Cursor cursor = dbHelper.getAllBooks();
        bookList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_BOOK_ID));
                String isbn = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ISBN));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

                // Add the book to the list
                Book book = new Book(id, isbn, title, author, category, description);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged(); // Refresh the RecyclerView after loading data
    }

    // Show popup menu for edit and delete
    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.book_options_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_edit) {
                editBook(position);
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                deleteBook(position);
                return true;
            } else {
                return false;
            }
        });
        popup.show();
    }

    // Edit a book
    private void editBook(int position) {
        Book book = bookList.get(position);
        Intent intent = new Intent(getActivity(), Crud_Edit_Page.class);
        intent.putExtra("BOOK_ID", book.getBook_Id());
        startActivityForResult(intent, REQUEST_CODE_EDIT); // Use startActivityForResult
    }

    // Delete a book
    //private void deleteBook(int position) {
    //    Book book = bookList.get(position);
    //    dbHelper.deleteBook(book.getBook_Id());
    //    bookList.remove(position);
    //    bookAdapter.notifyItemRemoved(position);
    //}
    private void deleteBook(int position) {
        // Vérifier si la position est valide
        if (position >= 0 && position < bookList.size()) {
            // Obtenir le livre à supprimer
            Book book = bookList.get(position);

            // Supprimer le livre de la base de données
            boolean isDeleted = dbHelper.deleteBook(book.getBook_Id());

            if (isDeleted) {
                // Supprimer le livre de la liste
                bookList.remove(position);

                // Notifier l'adaptateur du retrait de l'élément
                bookAdapter.notifyItemRemoved(position);
                bookAdapter.notifyItemRangeChanged(position, bookList.size()); // Repositionner les items restants

                // Afficher un message de succès
                Toast.makeText(getContext(), "Book deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Afficher un message d'erreur
                Toast.makeText(getContext(), "Failed to delete book", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Position invalide, afficher un message d'erreur
            Toast.makeText(getContext(), "Invalid book selected for deletion", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result from add/edit activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT) {
                // Reload books after an add or edit operation
                loadBooksFromDatabase();
            }
        }
    }
}