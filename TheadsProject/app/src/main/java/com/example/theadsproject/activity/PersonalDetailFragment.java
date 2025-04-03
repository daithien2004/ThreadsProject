package com.example.theadsproject.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;
import com.example.theadsproject.activityAccount.TabPersonalDetailFragment;


public class PersonalDetailFragment extends Fragment {

    public PersonalDetailFragment() {
        super(R.layout.fragment_personal_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new TabPersonalDetailFragment()) // Thay thế với TabPersonalDetailFragment
                    .commit();
        }

    }
}
