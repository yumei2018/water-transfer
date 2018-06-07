/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.testing;

import com.gei.entities.WtTrans;
import com.gei.entities.WtTransTrack;
import com.gei.facades.WtTransFacade;
import com.gei.facades.WtTransTrackFacade;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author ymei
 */
public class facadeJUnitTest {
    public facadeJUnitTest() {
    }
    @BeforeClass
        public static void setUpClass() {
    }

    @AfterClass
        public static void tearDownClass() {
    }

    @Before
        public void setUp() {
    }

    @After
        public void tearDown() {
    }
        
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    @Test
    public void hello() {
        String password = "Aa1!oooo";
        boolean match = (password != null) && (password.length() == 8)
                && (password.matches("[\\d]")) && (password.matches("/[a-z]/g"))
                && (password.matches("/[A-Z]/g")) && (password.matches("/[^\\w]/g"));
        System.out.println("len(8) => " + (password.length() == 8));
        System.out.println("digit => " + (password.matches(".*[\\d].*")));
        System.out.println("lcase => " + (password.matches(".*[a-z].*")));
        System.out.println("ucase => " + (password.matches(".*[A-Z].*")));
        System.out.println("special => " + (password.matches(".*[^\\w].*")));
        System.out.println(match);
    }    
    
    @Test
    public void testTransFacade() {
        WtTransFacade f = new WtTransFacade();
        WtTrans t = f.find(223);
        System.out.println("ProTransQua Start="+t.getProTransQua());
        t.setProTransQua(2000);
        f.edit(t);
        WtTrans t2 = f.find(223);
        System.out.println("ProTransQua End="+t2.getProTransQua());
    }
    
//    @Test
//    public void saveTransFacade() {
//        WtTransFacade f = new WtTransFacade();
//        WtTrans t = new WtTrans();
//        t.setTransYear(2016);
//        f.create(t);
//        System.out.println(t);
//    }
    
//    @Test
//    public void saveTransTrackFacade() {
//        WtTransTrackFacade wttf = new WtTransTrackFacade();
//        WtTransTrack t = new WtTransTrack();
//        t.setWtTransId(223);
//        wttf.create(t);
//        System.out.println(wttf.findAll());
//    }
}
