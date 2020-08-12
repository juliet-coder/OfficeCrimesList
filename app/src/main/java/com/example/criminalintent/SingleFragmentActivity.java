package com.example.criminalintent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager(); //запускаем фрагмент внутри активности
        Fragment fragment;  // fm.findFragmentById(R.id.fragment_container);

        fragment = createFragment();
        fm.beginTransaction()  // транзакция - добавление, изменение, удаление фрагментов (у нас - добавление)
                .add(R.id.fragment_container, fragment) // добавляющий метод, в него предается идентификатор фрагмента и его содержимое
                .commit(); //зафиксировали фрагмент на активности, закрепить результат

    }
}
