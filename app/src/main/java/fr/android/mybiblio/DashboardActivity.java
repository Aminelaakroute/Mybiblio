package fr.android.mybiblio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DashboardActivity extends AppCompatActivity {
        private ViewPager2 viewPager2;
        private BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_view);

            viewPager2 = findViewById(R.id.viewPager2);
            bottomNavigationView = findViewById(R.id.bottomNavigation);

            ViewPagerAdapter mainNavigationAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), ViewPagerAdapter.MAIN_NAVIGATION_TYPE);
            viewPager2.setAdapter(mainNavigationAdapter);

            // Listener for BottomNavigationView
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.bottom_home) {
                    viewPager2.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.bottom_search) {
                    viewPager2.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.bottom_settings) {
                    viewPager2.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    viewPager2.setCurrentItem(3);
                    return true;
                }

                return false;
            });

            // Sync ViewPager2 with BottomNavigationView
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            bottomNavigationView.setSelectedItemId(R.id.bottom_home);
                            break;
                        case 1:
                            bottomNavigationView.setSelectedItemId(R.id.bottom_search);
                            break;
                        case 2:
                            bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
                            break;
                        case 3:
                            bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
                            break;
                    }
                }
            });
        }
}
