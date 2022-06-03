package com.dzco.vaccination;

import org.junit.Assert;
import org.junit.Test;
import com.dzco.vaccination.Models.User;

public class UserUnitTest {

    private final double delta = 0.0001;

    /*
    getAgeT() tests
     */

    @Test
    public void getAgeTPositiveTest1()
    {
        User user = new User();
        user.setAge(9);
        Assert.assertEquals(365, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest2()
    {
        User user = new User();
        user.setAge(10);
        Assert.assertEquals(305, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest3()
    {
        User user = new User();
        user.setAge(39);
        Assert.assertEquals(305, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest4()
    {
        User user = new User();
        user.setAge(40);
        Assert.assertEquals(275, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest5()
    {
        User user = new User();
        user.setAge(49);
        Assert.assertEquals(275, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest6()
    {
        User user = new User();
        user.setAge(50);
        Assert.assertEquals(212, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest7()
    {
        User user = new User();
        user.setAge(59);
        Assert.assertEquals(212, user.getAgeT());
    }

    @Test
    public void getAgeTPositiveTest8()
    {
        User user = new User();
        user.setAge(60);
        Assert.assertEquals(183, user.getAgeT());
    }

    @Test
    public void getAgeTNegativeTest1()
    {
        User user = new User();
        user.setAge(-32);
        Assert.assertEquals(0, user.getAgeT());
    }

    /*
    getLifePlaceKoef() tests
     */

    @Test
    public void getLifePlaceKoefPositiveTest1()
    {
        User user = new User();
        user.setLifePlace(User.LIFE_PLACE[0]);
        Assert.assertEquals(0.5, user.getPlaceKoef(), 0.0001);
    }
}
