package com.dzco.vaccination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dzco.vaccination.Models.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dzco.vaccination.Models.User;

import java.time.ZonedDateTime;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText nameField;

    private RadioButton maleRadiobutton;
    private RadioButton femaleRadiobutton;

    private TextInputEditText ageField;

    private RadioButton enableChronicRadiobutton;
    private RadioButton disableChronicRadiobutton;

    private RadioButton enableBadHabitsRadiobutton;
    private RadioButton disableBadHabitsRadiobutton;

    private RadioButton alwaysUsingMeasureRadiobutton;
    private RadioButton sometimesUsingMeasureRadiobutton;
    private RadioButton neverUsingMeasureRadiobutton;

    private Spinner vaccinesSpinner;

    private TextView dateOfFirstVaccinationField;
    private Button setDateOfFirstVaccinationButton;
    private TextView dateOfSecondVaccinationField;
    private Button setDateOfSecondVaccinationButton;
    private Date firstDate;
    private Date secondDate;

    private RadioButton megapolisRadiobutton;
    private RadioButton cityRadiobutton;
    private RadioButton villageRadiobutton;

    private TextInputEditText passwordField;
    private TextInputEditText passwordRepeatField;

    private RelativeLayout root;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;

    private InProgress InProgress;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance(); //создаём подключение к сервису авторизации
        db = FirebaseDatabase.getInstance(); //создаём подключение к БД
        users = db.getReference("Users"); //записываем ссылку на таблицу Users
        user = new User();

        root = findViewById(R.id.relative_edit_profile_activity);

        nameField = findViewById(R.id.name_edit_profile_field);

        maleRadiobutton = findViewById(R.id.male_edit_profile_radiobutton);
        femaleRadiobutton = findViewById(R.id.female_edit_profile_radiobutton);

        ageField = findViewById(R.id.age_edit_profile_field);

        enableChronicRadiobutton = findViewById(R.id.enabled_chronic_edit_profile_radiobutton);
        disableChronicRadiobutton = findViewById(R.id.disabled_chronic_edit_profile_radiobutton);

        enableBadHabitsRadiobutton = findViewById(R.id.enabled_bad_habits_edit_profile_radiobutton);
        disableBadHabitsRadiobutton = findViewById(R.id.disabled_bad_habits_edit_profile_radiobutton);

        alwaysUsingMeasureRadiobutton = findViewById(R.id.always_using_measure_edit_profile_radiobutton);
        sometimesUsingMeasureRadiobutton = findViewById(R.id.sometimes_using_measure_edit_profile_radiobutton);
        neverUsingMeasureRadiobutton = findViewById(R.id.never_using_measure_edit_profile_radiobutton);

        firstDate = new Date(user.getFirst());
        secondDate = new Date(user.getSecond());

        vaccinesSpinner = findViewById(R.id.vaccine_edit_profile_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, User.VACCINES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccinesSpinner.setAdapter(adapter);
        vaccinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                if (TextUtils.equals(item, User.VACCINES[User.VACCINES.length - 1])) {
                    dateOfFirstVaccinationField.setText(R.string.unaviable_label);
                    setDateOfFirstVaccinationButton.setEnabled(false);
                    setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    dateOfSecondVaccinationField.setText(R.string.unaviable_label);
                    setDateOfSecondVaccinationButton.setEnabled(false);
                    setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    return;
                }
                if (TextUtils.equals(item, User.VACCINES[User.VACCINES.length - 2])) {
                    if (user.getFirst().isNull()) {
                        dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
                    } else {
                        dateOfFirstVaccinationField.setText(user.getFirst().toString());
                    }
                    setDateOfFirstVaccinationButton.setEnabled(true);
                    setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    if (user.getFirst().isNull()) {
                        dateOfSecondVaccinationField.setText(R.string.choosing_second_date_label);
                    } else {
                        dateOfSecondVaccinationField.setText(user.getSecond().toString());
                    }
                    setDateOfSecondVaccinationButton.setEnabled(true);
                    setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    return;
                }
                if (user.getFirst().isNull()) {
                    dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
                } else {
                    dateOfFirstVaccinationField.setText(user.getFirst().toString());
                }
                setDateOfFirstVaccinationButton.setEnabled(true);
                setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
                dateOfSecondVaccinationField.setText(R.string.unaviable_label);
                setDateOfSecondVaccinationButton.setEnabled(false);
                setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        dateOfFirstVaccinationField = findViewById(R.id.first_vaccination_date_edit_profile_field);
        setDateOfFirstVaccinationButton = findViewById(R.id.first_vaccination_date_edit_profile_button);
        setDateOfFirstVaccinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedYear = firstDate.getYear();
                int selectedMonth = firstDate.getMonth() - 1;
                int selectedDayOfMonth = firstDate.getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                        firstDateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        dateOfSecondVaccinationField = findViewById(R.id.second_vaccination_date_edit_profile_field);
        setDateOfSecondVaccinationButton = findViewById(R.id.second_vaccination_date_edit_profile_button);
        setDateOfSecondVaccinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedYear = secondDate.getYear();
                int selectedMonth = secondDate.getMonth() - 1;
                int selectedDayOfMonth = secondDate.getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                        secondDateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        megapolisRadiobutton = findViewById(R.id.megapolis_edit_profile_radiobutton);
        cityRadiobutton = findViewById(R.id.city_edit_profile_radiobutton);
        villageRadiobutton = findViewById(R.id.village_edit_profile_radiobutton);

        passwordField = findViewById(R.id.password_edit_profile_field);
        passwordRepeatField = findViewById(R.id.repeat_password_edit_profile_field);

        nameField.setText(user.getName());
        maleRadiobutton.setChecked(user.isSex());
        femaleRadiobutton.setChecked(!user.isSex());
        ageField.setText(user.getAge() + "");
        enableChronicRadiobutton.setChecked(user.isChronicDiseases());
        disableChronicRadiobutton.setChecked(!user.isChronicDiseases());
        enableBadHabitsRadiobutton.setChecked(user.isBadHabits());
        disableBadHabitsRadiobutton.setChecked(!user.isBadHabits());
        if (TextUtils.equals(user.getMeasure(), User.PROTECTIVE_MEASURES[0])) {
            alwaysUsingMeasureRadiobutton.setChecked(true);
        }
        if (TextUtils.equals(user.getMeasure(), User.PROTECTIVE_MEASURES[1])) {
            sometimesUsingMeasureRadiobutton.setChecked(true);
        }
        if (TextUtils.equals(user.getMeasure(), User.PROTECTIVE_MEASURES[2])) {
            neverUsingMeasureRadiobutton.setChecked(true);
        }
        vaccinesSpinner.setSelection(user.getVaccineIndex());
        if (TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length - 1])) {
            dateOfFirstVaccinationField.setText(R.string.unaviable_label);
            setDateOfFirstVaccinationButton.setEnabled(false);
            setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
            dateOfSecondVaccinationField.setText(R.string.unaviable_label);
            setDateOfSecondVaccinationButton.setEnabled(false);
            setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
        } else if (TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length - 2])) {
            if (user.getFirst().isNull()) {
                dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
            } else {
                dateOfFirstVaccinationField.setText(user.getFirst().toString());
            }
            setDateOfFirstVaccinationButton.setEnabled(true);
            setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
            if (user.getFirst().isNull()) {
                dateOfSecondVaccinationField.setText(R.string.choosing_second_date_label);
            } else {
                dateOfSecondVaccinationField.setText(user.getSecond().toString());
            }
            setDateOfSecondVaccinationButton.setEnabled(true);
            setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
        } else {
            if (user.getFirst().isNull()) {
                dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
            } else {
                dateOfFirstVaccinationField.setText(user.getFirst().toString());
            }
            setDateOfFirstVaccinationButton.setEnabled(true);
            setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
            dateOfSecondVaccinationField.setText(R.string.unaviable_label);
            setDateOfSecondVaccinationButton.setEnabled(false);
            setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        firstDate = user.getFirst();
        secondDate = user.getSecond();
        if (TextUtils.equals(user.getLifePlace(), User.LIFE_PLACE[0])) {
            megapolisRadiobutton.setChecked(true);
        }
        if (TextUtils.equals(user.getLifePlace(), User.LIFE_PLACE[1])) {
            cityRadiobutton.setChecked(true);
        }
        if (TextUtils.equals(user.getLifePlace(), User.LIFE_PLACE[2])) {
            villageRadiobutton.setChecked(true);
        }
        passwordField.setText(user.getPassword());
        passwordRepeatField.setText(user.getPassword());

        Button acceptButton = findViewById(R.id.accept_edit_profile_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameField.getText().toString())) {
                    Snackbar.make(root, R.string.enter_name_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ageField.getText().toString())) {
                    Snackbar.make(root, R.string.enter_age_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(dateOfFirstVaccinationField.getText().toString(), getString(R.string.choosing_first_date_label))) {
                    Snackbar.make(root, R.string.enter_first_date_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(dateOfSecondVaccinationField.getText().toString(), getString(R.string.choosing_second_date_label))) {
                    Snackbar.make(root, R.string.enter_second_date_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (passwordField.getText().toString().length() < 8) {
                    Snackbar.make(root, R.string.enter_password_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(passwordField.getText().toString(), passwordRepeatField.getText().toString())) {
                    Snackbar.make(root, R.string.passwords_mismatch_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                InProgress.show();

                user.setName(nameField.getText().toString());
                user.setAge(Integer.parseInt(ageField.getText().toString()));
                user.setSex(maleRadiobutton.isChecked());
                user.setChronicDiseases(enableChronicRadiobutton.isChecked());
                user.setBadHabits(enableBadHabitsRadiobutton.isChecked());
                if (alwaysUsingMeasureRadiobutton.isChecked()) {
                    user.setMeasure(User.PROTECTIVE_MEASURES[0]);
                }
                if (sometimesUsingMeasureRadiobutton.isChecked()) {
                    user.setMeasure(User.PROTECTIVE_MEASURES[1]);
                }
                if (neverUsingMeasureRadiobutton.isChecked()) {
                    user.setMeasure(User.PROTECTIVE_MEASURES[2]);
                }
                user.setVaccine(vaccinesSpinner.getSelectedItem().toString());
                if (TextUtils.equals(dateOfFirstVaccinationField.getText().toString(), getString(R.string.unaviable_label))) {
                    user.setFirst(Date.NULL_DATE);
                } else {
                    user.setFirst(firstDate);
                }
                if (TextUtils.equals(dateOfSecondVaccinationField.getText().toString(), getString(R.string.unaviable_label))) {
                    user.setSecond(Date.NULL_DATE);
                } else {
                    user.setSecond(secondDate);
                }
                if (megapolisRadiobutton.isChecked()) {
                    user.setLifePlace(User.LIFE_PLACE[0]);
                }
                if (cityRadiobutton.isChecked()) {
                    user.setLifePlace(User.LIFE_PLACE[1]);
                }
                if (villageRadiobutton.isChecked()) {
                    user.setLifePlace(User.LIFE_PLACE[2]);
                }
                user.setPassword(passwordField.getText().toString());

                auth.getCurrentUser().updatePassword(user.getPassword());
                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        InProgress.hide();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        InProgress.hide();
                        Snackbar.make(root, R.string.failed_to_save_data_label, Snackbar.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                    }
                });
            }
        });
    }

    DatePickerDialog.OnDateSetListener firstDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            Date currentDate = new Date(ZonedDateTime.now().getDayOfMonth(), ZonedDateTime.now().getMonthValue(), ZonedDateTime.now().getYear());
            Date minDate = new Date(User.MIN_DATE);

            firstDate.setDay(dayOfMonth);
            firstDate.setMonth(monthOfYear+1);
            firstDate.setYear(year);
            if (firstDate.isSmallerThan(minDate) || firstDate.isBiggerThan(currentDate)) {
                firstDate.setCurrent();
                Snackbar.make(root, R.string.invalid_date_selected_label, Snackbar.LENGTH_SHORT).show();
            }
            dateOfFirstVaccinationField.setText(firstDate.toString());
        }
    };

    DatePickerDialog.OnDateSetListener secondDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            Date currentDate = new Date(ZonedDateTime.now().getDayOfMonth(), ZonedDateTime.now().getMonthValue(), ZonedDateTime.now().getYear());

            secondDate.setDay(dayOfMonth);
            secondDate.setMonth(monthOfYear+1);
            secondDate.setYear(year);
            if (secondDate.isSmallerThan(firstDate) || secondDate.isBiggerThan(currentDate)) {
                secondDate.setCurrent();
                Snackbar.make(root, R.string.invalid_date_selected_label, Snackbar.LENGTH_SHORT).show();
            }
            dateOfSecondVaccinationField.setText(secondDate.toString());
        }
    };

}