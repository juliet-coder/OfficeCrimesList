package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime crime;
    private EditText titleField;
    private CheckBox solvedCheckBox;
    private Button setDateBtn;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable("crime_id",crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) { // создает фрагмент и выводит на экран
        super.onCreate(savedInstanceState); //
        UUID crimeId = (UUID) getArguments().getSerializable("crime_id");
        crime = CrimeLab.get(getActivity()).getCrime(crimeId); // когда будет создан фрагмент, сюда запишется информации о преступлении

    }

    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(crime);
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // заполняем представление фрагментов
        // вызываем метод инфлэйт LayoutInflater и передаем туда идентификтор ресурса макета, параметр родителя представления,
        // и нужно ли включать заполнение представления в родителе, false т. к. представление будет добавлено в ходе выполнения активности
        View  v = inflater.inflate(R.layout.fragment_crime, container, false);

        setDateBtn = (Button) v.findViewById(R.id.set_date);
        setDateBtn.setText(crime.getDate().toString());
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,0);
                dialog.show(manager,"DIALOG_DATE");
            }
        });
        solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked(crime.isSolved());
        titleField = (EditText) v.findViewById(R.id.crime_title); // находим элемент по идентификатору и передаем в переменную
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {  // интерфейс, реализующий методы, отвечающие на изменение текста
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // метод реагирует на изменение текста
                crime.setTitle(s.toString()); // в объект crime записывается заголовок
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intentData){
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == 0) {
            Date date = (Date) intentData.getSerializableExtra("extra_date");
            crime.setDate(date);
            setDateBtn.setText(crime.getDate().toString());
        }
    }
}

