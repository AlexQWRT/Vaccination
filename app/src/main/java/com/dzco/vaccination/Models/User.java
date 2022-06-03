package com.dzco.vaccination.Models;

import android.text.TextUtils;

import java.util.Arrays;

public class User {
    public static final String[] PROTECTIVE_MEASURES = {"YES", "SOMETIMES", "NO" };

    public static final String[] LIFE_PLACE = { "MEGAPOLIS", "CITY", "VILLAGE" };

    public static final String DEFAULT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/vaccinationdzco.appspot.com/o/Images%2Fdefault-profile-pic.jpeg?alt=media&token=67dd9011-a4d4-410a-b8a1-37e3c0a6cc3e";

    public static final Date MIN_DATE = new Date(1, 6, 2021);

    public static final String DEFAULT_EMAIL = "default@gmail.com";

    private static String name;
    private static boolean sex;//1 man
    private static int age;
    private static boolean chronicDiseases;
    private static boolean badHabits;
    private static String measure;
    private static String vaccine;
    private static Date first;
    private static Date second;//only for sputnikV
    private static String lifePlace;
    private static String email;
    private static String password;
    private static String imageURI = DEFAULT_IMAGE;
    private static final int MIN_VALUE=183;
    private static final int MAX_VALUE=365;

    public static final String[] VACCINES = {
            "Abdala",
            "SputnikV",
            "No"
    } ;

    public User()
    {
        //from firebase
    }

    public User(String name, boolean sex, int age, boolean chronicDiseases, boolean badHabits, String measure,
                String vaccine, Date first, Date second, String lifePlace, String email, String password, String imageURI)
    {
        User.name=name;
        User.sex=sex;
        User.age=age;
        User.chronicDiseases=chronicDiseases;
        User.badHabits=badHabits;
        User.measure= measure;
        User.vaccine=vaccine;
        User.first=first;
        User.second=second;
        User.lifePlace=lifePlace;
        User.email=email;
        User.password=password;
        User.imageURI=imageURI;
    }

    public User(User user) {
        User.name=user.getName();
        User.sex=user.isSex();
        User.age=user.getAge();
        User.chronicDiseases=user.isChronicDiseases();
        User.badHabits=user.isBadHabits();
        User.measure=user.getMeasure();
        User.vaccine=user.getVaccine();
        User.first=user.getFirst();
        User.second=user.getSecond();
        User.lifePlace=user.getLifePlace();
        User.email=user.getEmail();
        User.password=user.getPassword();
        User.imageURI=user.getImageURI();
    }

    static {
        User.name="none";
        User.sex=false;
        User.age=0;
        User.chronicDiseases =false;
        User.badHabits=false;
        User.measure= PROTECTIVE_MEASURES[0];
        User.vaccine=VACCINES[VACCINES.length-1];
        User.first=new Date(0, 0, 0);
        User.second=new Date(0, 0, 0);
        User.lifePlace=LIFE_PLACE[0];
        User.email="default@gmail.com";
        User.password="12345678";
        User.imageURI=DEFAULT_IMAGE;
    }

    public String getLifePlace() {
        return lifePlace;
    }

    public void setLifePlace(String lifePlace) {
        User.lifePlace = lifePlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        User.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        User.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        User.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        User.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        User.age = age;
    }

    public boolean isChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(boolean chronicDiseases) {
        User.chronicDiseases = chronicDiseases;
    }

    public boolean isBadHabits() {
        return badHabits;
    }

    public void setBadHabits(boolean badHabits) {
        User.badHabits = badHabits;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        User.measure = measure;
    }

    public String getVaccine() {
        return vaccine;
    }

    public int getVaccineIndex() {
        return Arrays.asList(VACCINES).indexOf(vaccine);
    }

    public void setVaccine(String vaccine) {
        User.vaccine = vaccine;
    }

    public Date getFirst() {
        return first;
    }

    public void setFirst(Date first) {
        User.first = first;
    }

    public Date getSecond() {
        return second;
    }

    public void setSecond(Date second) {
        User.second = second;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        User.imageURI = imageURI;
    }

    public int getAgeT()
    {
        if(age > 0 && age < 10)
        {
            return 365;
        }
        if(age >= 10 && age < 40)
        {
            return 305;
        }
        if(age >= 40 && age < 50)
        {
            return 275;
        }
        if(age >= 50 && age < 60)
        {
            return 212;
        }
        if(age >= 60)
        {
            return 183;
        }
        return 0;
    }

    public double getPlaceKoef()
    {
        if(TextUtils.equals(LIFE_PLACE[1], lifePlace)) //city
        {
            return 0.75;
        }
        if(TextUtils.equals(LIFE_PLACE[2], lifePlace)) //village
        {
            return 1;
        }
        if(TextUtils.equals(LIFE_PLACE[0], lifePlace)) //megapolis
        {
            return 0.5;
        }
        return 0;
    }

    public double getMeasuresKoef()
    {
        if(TextUtils.equals(PROTECTIVE_MEASURES[0], measure)) //yes
        {
            return 1;
        }
        if(TextUtils.equals(PROTECTIVE_MEASURES[2], measure)) //no
        {
            return 0.5;
        }
        if(TextUtils.equals(PROTECTIVE_MEASURES[1], measure)) //sometimes
        {
            return 0.75;
        }
        return 0;
    }

    public double getBadHabitsKoef()
    {
        if(badHabits)
        {
            return 0.95;
        }
        else
        {
            return 1.1;
        }
    }

    public double getSexKoef()
    {
        if(sex)
        {
            return 0.95;
        }
        else
        {
            return 1.1;
        }
    }

    public double getDesKoef()
    {
        if(chronicDiseases)
        {
            return 0.95;
        }
        else
        {
            return 1.1;
        }
    }

    public int getTime()
    {
        int t = (int) (getAgeT()*getBadHabitsKoef()*getSexKoef()* getPlaceKoef()*getDesKoef()*getMeasuresKoef());
        if(t<MIN_VALUE)
        {
            t=MIN_VALUE;
        }
        if(t>MAX_VALUE)
        {
            t=MAX_VALUE;
        }
        return t;
    }
}