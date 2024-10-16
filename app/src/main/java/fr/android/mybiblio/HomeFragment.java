package fr.android.mybiblio;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the CardViews
        CardView ProgrammingBook = view.findViewById(R.id.ProgrammingBook);
        CardView ElectronicBook = view.findViewById(R.id.ElectronicBook);
        CardView ManagementBook = view.findViewById(R.id.ManagementBook);
        CardView LiteratureBook = view.findViewById(R.id.LiteratureBook);
        CardView ChemistryBook = view.findViewById(R.id.ChemistryBook);
        CardView CyberSecBook = view.findViewById(R.id.CyberSecBook);
        CardView MathematicsBook = view.findViewById(R.id.MathematicsBook);
        CardView PhysicsBook = view.findViewById(R.id.PhysicsBook);

        // Set OnClickListener for each CardView
        ProgrammingBook.setOnClickListener(v -> openCategory("Programming"));
        ElectronicBook.setOnClickListener(v -> openCategory("Electronique"));
        ManagementBook.setOnClickListener(v -> openCategory("Management"));
        LiteratureBook.setOnClickListener(v -> openCategory("Litterature"));
        ChemistryBook.setOnClickListener(v -> openCategory("Chimie"));
        CyberSecBook.setOnClickListener(v -> openCategory("Cyber Security"));
        MathematicsBook.setOnClickListener(v->openCategory("Mathematiques"));
        PhysicsBook.setOnClickListener(v ->openCategory("Physique"));

        return view;
    }
    // Method to open a new activity with the selected category
    private void openCategory(String category) {
        Intent intent = new Intent(getActivity(), CategoryBooksActivity.class);
        intent.putExtra("category", category); // Pass the selected category to the new activity
        startActivity(intent);
    }
}