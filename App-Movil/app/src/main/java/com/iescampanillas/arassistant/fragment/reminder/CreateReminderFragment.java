package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iescampanillas.arassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateReminderFragment extends Fragment {

    public CreateReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_reminder, container, false);
    }
}
