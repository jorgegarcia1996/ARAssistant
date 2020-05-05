package com.iescampanillas.arassistant.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.activity.HomeActivity;
import com.iescampanillas.arassistant.activity.ProfileActivity;

import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;

public class HomeFragment extends Fragment {

    private Button btnProfile;

    private Button btnTaskFragment;

    private Button btnReminderFragment;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        //btnProfile = homeView.findViewById(R.id.homeProfileButton);
        btnTaskFragment = homeView.findViewById(R.id.fragmentHomeTaskButton);

        btnTaskFragment.setOnClickListener(v -> {
            findNavController(v).navigate(R.id.home_to_task);
        });

        return homeView;
    }
}
