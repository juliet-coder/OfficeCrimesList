package com.example.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        UUID crimeId = (UUID) getIntent().getSerializableExtra("crime_id");
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context context, UUID crimeId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("crime_id",crimeId);
        return intent;
    }
}
