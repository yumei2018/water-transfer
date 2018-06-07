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
import com.gei.io.xml.JAXBHelper;
import com.gei.io.xml.SecondClass;
import com.gei.reflection.ReflectionInfo;
import com.gei.testing.Trans;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.namespace.QName;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ymei
 */
public class xmlHelperTest {
  public xmlHelperTest() {
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
  
  @Test
  public void testTransSerialization(){
    System.out.println("Test Trans Serialization...");
    try{
      WtTransFacade wtf = new WtTransFacade();
      WtTransTrackFacade wttf = new WtTransTrackFacade();
      WtTrans trans = new WtTrans();
      WtTrans otherTrans = new WtTrans();
      trans = wtf.find(223);
      String xmlStr =  wttf.find(22).getTransXml();
      otherTrans = (WtTrans)
              JAXBHelper.parseXMLContext(xmlStr, WtTrans.class);
//      otherTrans = wtf.find(223);
//      System.out.println(trans.getCompareMap(otherTrans));    
      
      for(Map.Entry entry: trans.getCompareMap(otherTrans).entrySet()){
        String fieldName = (String) entry.getKey();
        System.out.println(Trans.transFieldMap.get(fieldName) + ": "+entry.getValue());
      }
      
//      String xmlStr = 
//                JAXBHelper.getXMLContent(QName.valueOf("WtTrans"),trans, true);
//      String xmlStr = trans.xmlSerialization();
//      System.out.println("Trans XML Content=\n" + xmlStr);
      
      // Create Track Record
//      WtTransTrack transTrack = new WtTransTrack();
//      transTrack.setWtTransId(trans.getWtTransId());
//      transTrack.setTransXml(xmlStr);
//      transTrack.setCreatedBy("Developer");
//      wttf.create(transTrack);
//      System.out.println(wttf.findAll()); 
      
//      WtTrans newTrans = (WtTrans)
//              JAXBHelper.parseXMLContext(xmlStr, WtTrans.class);
//      System.out.println("Trans Object=" + newTrans);
//      System.out.println("MajorRiverAttribute=" + newTrans.getMajorRiverAttribute());
//      System.out.println("Water Rights=" + newTrans.getWtWaterRightsCollection());
      
    }catch (Exception pExp) {
      fail("Test 'testTransSerialization' Failed.\n" + pExp.getMessage());
    }
  }
}
