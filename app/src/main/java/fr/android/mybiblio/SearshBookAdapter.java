package fr.android.mybiblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearshBookAdapter extends RecyclerView.Adapter<SearshBookAdapter.BookViewHolder> {
    private List<Book> bookList;

    public SearshBookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the new item_search_result layout for each book
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the current book
        Book book = bookList.get(position);

        // Bind the data to the views
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookCategory.setText(book.getCategory());
        holder.bookDescription.setText(book.getDescription());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // ViewHolder class to hold the views in item_search_result layout
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookCategory, bookDescription;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookCategory = itemView.findViewById(R.id.bookCategory);
            bookDescription = itemView.findViewById(R.id.bookDescription);
        }
    }
}
