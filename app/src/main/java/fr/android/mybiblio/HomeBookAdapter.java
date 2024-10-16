package fr.android.mybiblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.BookViewHolder> {
    private List<Book> bookList;

    public HomeBookAdapter(List<Book> bookList, BookAdapter.OnBookClickListener onBookClickListener) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_home, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());  // Set the book title
        holder.authorTextView.setText(book.getAuthor()); // Set the book author
        holder.isbnTextView.setText(book.getIsbn());
        holder.CategorieTextView.setText(book.getCategory());
        holder.DescriptionTextView.setText(book.getDescription());

        // Set book cover image (you can load actual images from a URL or resource)
        holder.bookCoverImageView.setImageResource(R.drawable.baseline_library_books_24); // Placeholder for now
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, isbnTextView, CategorieTextView, DescriptionTextView;
        ImageView bookCoverImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitle); // Bind title TextView
            authorTextView = itemView.findViewById(R.id.bookAuthor); // Bind author TextView
            bookCoverImageView = itemView.findViewById(R.id.bookCoverImage); // Bind book cover image
            isbnTextView = itemView.findViewById(R.id.bookISBN);
            CategorieTextView   = itemView.findViewById(R.id.bookCategories);
            DescriptionTextView = itemView.findViewById(R.id.bookDescription);

        }
    }
}
