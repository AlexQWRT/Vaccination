package com.dzco.myapplication;

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

import com.dzco.myapplication.Models.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dzco.myapplication.Models.User;

import java.time.ZonedDateTime;

public class RegistrationActivity extends AppCompatActivity {

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

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputEditText passwordRepeatField;

    private RelativeLayout root;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;

    private InProgress progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance(); //создаём подключение к сервису авторизации
        db = FirebaseDatabase.getInstance(); //создаём подключение к БД
        users = db.getReference("Users"); //записываем ссылку на таблицу Users

        root = (RelativeLayout) findViewById(R.id.relative_registration_activity);

        nameField = (TextInputEditText) findViewById(R.id.name_registration_field);

        maleRadiobutton = (RadioButton) findViewById(R.id.male_registration_radiobutton);
        maleRadiobutton.setChecked(true);
        femaleRadiobutton = (RadioButton) findViewById(R.id.female_registration_radiobutton);

        ageField = (TextInputEditText) findViewById(R.id.age_registration_field);

        enableChronicRadiobutton = (RadioButton) findViewById(R.id.enabled_chronic_registration_radiobutton);
        disableChronicRadiobutton = (RadioButton) findViewById(R.id.disabled_chronic_registration_radiobutton);
        disableChronicRadiobutton.setChecked(true);

        enableBadHabitsRadiobutton = (RadioButton) findViewById(R.id.enabled_bad_habits_registration_radiobutton);
        enableBadHabitsRadiobutton.setChecked(true);
        disableBadHabitsRadiobutton = (RadioButton) findViewById(R.id.disabled_bad_habits_registration_radiobutton);

        alwaysUsingMeasureRadiobutton = (RadioButton) findViewById(R.id.always_using_measure_registration_radiobutton);
        sometimesUsingMeasureRadiobutton = (RadioButton) findViewById(R.id.sometimes_using_measure_registration_radiobutton);
        sometimesUsingMeasureRadiobutton.setChecked(true);
        neverUsingMeasureRadiobutton = (RadioButton) findViewById(R.id.never_using_measure_registration_radiobutton);

        vaccinesSpinner = (Spinner) findViewById(R.id.vaccine_registration_spinner);
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
                    dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
                    setDateOfFirstVaccinationButton.setEnabled(true);
                    setDateOfFirstVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    dateOfSecondVaccinationField.setText(R.string.choosing_second_date_label);
                    setDateOfSecondVaccinationButton.setEnabled(true);
                    setDateOfSecondVaccinationButton.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    return;
                }
                dateOfFirstVaccinationField.setText(R.string.choosing_first_date_label);
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

        firstDate = new Date();
        firstDate.setCurrent();

        dateOfFirstVaccinationField = (TextView) findViewById(R.id.first_vaccination_date_registration_field);
        setDateOfFirstVaccinationButton = (Button) findViewById(R.id.first_vaccination_date_registration_button);
        setDateOfFirstVaccinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedYear = firstDate.getYear();
                int selectedMonth = firstDate.getMonth() - 1;
                int selectedDayOfMonth = firstDate.getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                        firstDateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        secondDate = new Date();
        secondDate.setCurrent();

        dateOfSecondVaccinationField = (TextView) findViewById(R.id.second_vaccination_date_registration_field);
        setDateOfSecondVaccinationButton = (Button) findViewById(R.id.second_vaccination_date_registration_button);
        setDateOfSecondVaccinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedYear = secondDate.getYear();
                int selectedMonth = secondDate.getMonth() - 1;
                int selectedDayOfMonth = secondDate.getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                        secondDateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        megapolisRadiobutton = (RadioButton) findViewById(R.id.megapolis_registration_radiobutton);
        cityRadiobutton = (RadioButton) findViewById(R.id.city_registration_radiobutton);
        cityRadiobutton.setChecked(true);
        villageRadiobutton = (RadioButton) findViewById(R.id.village_registration_radiobutton);

        emailField = (TextInputEditText) findViewById(R.id.email_registration_field);
        passwordField = (TextInputEditText) findViewById(R.id.password_registration_field);
        passwordRepeatField = (TextInputEditText) findViewById(R.id.repeat_password_registration_field);

        progress = new InProgress(RegistrationActivity.this);

        Button acceptButton = (Button) findViewById(R.id.accept_registration_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //что роизойдёт при нажатии на кнопку "Подтвердить"
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
                if (TextUtils.equals(dateOfSecondVaccinationField.getText().toString(), getString(R.string.choosing_first_date_label))) {
                    Snackbar.make(root, R.string.enter_second_date_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emailField.getText().toString())) {
                    Snackbar.make(root, R.string.enter_email_label, Snackbar.LENGTH_SHORT).show();
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

                progress.show();

                auth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
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
                                user.setFirst(firstDate);
                                user.setSecond(secondDate);
                                if (megapolisRadiobutton.isChecked()) {
                                    user.setLifePlace(User.LIFE_PLACE[0]);
                                }
                                if (cityRadiobutton.isChecked()) {
                                    user.setLifePlace(User.LIFE_PLACE[1]);
                                }
                                if (villageRadiobutton.isChecked()) {
                                    user.setLifePlace(User.LIFE_PLACE[2]);
                                }
                                user.setEmail(emailField.getText().toString());
                                user.setPassword(passwordField.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progress.hide();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progress.hide();
                                        Snackbar.make(root, R.string.failed_to_save_data_label, Snackbar.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.hide();
                        Snackbar.make(root, R.string.failed_to_save_data_label, Snackbar.LENGTH_SHORT).show();
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
            Date minDate = new Date(User.MIN_DATE);

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