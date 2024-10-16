package fr.android.mybiblio;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int type; // To distinguish between the different use cases

    public static final int LOGIN_SIGNUP_TYPE = 0;
    public static final int MAIN_NAVIGATION_TYPE = 1;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int type) {
        super(fragmentManager, lifecycle);
        this.type = type;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (type == LOGIN_SIGNUP_TYPE) {
            switch (position) {
                case 0:
                    return new LoginTabFragment();
                case 1:
                    return new SignupTabFragment();
            }
        } else if (type == MAIN_NAVIGATION_TYPE) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new SearchFragment();
                case 2:
                    return new SettingsFragment();
                case 3:
                    return new ProfileFragment();
            }
        }
        return new Fragment(); // Default empty fragment, in case of an error
    }

    @Override
    public int getItemCount() {
        if (type == LOGIN_SIGNUP_TYPE) {
            return 2; // Number of tabs in the login/signup screen
        } else if (type == MAIN_NAVIGATION_TYPE) {
            return 4; // Number of pages in the main navigation
        }
        return 0; // Default case
    }
}
