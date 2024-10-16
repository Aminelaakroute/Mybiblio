package fr.android.mybiblio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile1, container, false);

        // Récupérer le bouton de logout
        Button logoutButton = view.findViewById(R.id.buttonlgout);

        // Définir un onClickListener pour le bouton de logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter ici la logique pour le logout
                performLogout();
            }
        });

        return view;
    }

    // Fonction pour le logout
    private void performLogout() {
        // Supprimer ou invalider le token d'authentification
        // Exemple : Effacer le token stocké dans SharedPreferences ou Local Storage
        SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Efface toutes les données stockées
        editor.apply();

        // Afficher un message indiquant que l'utilisateur est déconnecté
        Toast.makeText(getActivity(), "Logout successful!", Toast.LENGTH_SHORT).show();

        // Rediriger l'utilisateur vers l'écran de login ou d'accueil
        Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
        startActivity(intent);

        // Optionnel : Terminer l'activité actuelle si vous souhaitez empêcher l'utilisateur de revenir en arrière
        getActivity().finish();
    }
}
