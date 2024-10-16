package fr.android.mybiblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private OnBookClickListener onBookClickListener;

    public BookAdapter(List<Book> bookList, OnBookClickListener onBookClickListener) {
        this.bookList = bookList;
        this.onBookClickListener = onBookClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());  // Set the book title
        holder.authorTextView.setText(book.getAuthor()); // Set the book author

        //holder.itemView.setOnClickListener(v -> onBookClickListener.onBookClick(v, position));
        holder.moreOptionsButton.setOnClickListener(v -> onBookClickListener.onBookClick(v, position));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView;
        Button moreOptionsButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitle); // Bind title TextView
            authorTextView = itemView.findViewById(R.id.bookAuthor); // Bind author TextView
            //
            moreOptionsButton = itemView.findViewById(R.id.buttonMoreOptions);
        }
    }

    // Interface to handle book item clicks
    public interface OnBookClickListener {
        void onBookClick(View view, int position);
    }
}
