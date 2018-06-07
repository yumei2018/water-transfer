package com.gei.io.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import com.gei.reflection.ReflectionInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kprins
 */
public class XmlHelperTest {
  
  public XmlHelperTest() {
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

  @Test
  public void testJaxbHelper() {
    System.out.println("Test 'JaxbHelper Test'");
    try {
      SimpleObject xmlObj1 = new SimpleObject("Koos");
      xmlObj1.setLength(10); 
      SubObject subObj = new SubObject();
      subObj.setName("Prins");
      subObj.setLength(4);
      subObj.setValue(100.9999);
      xmlObj1.setSubObject(subObj);
      SimpleObject xmlObj2 = new SimpleObject("Attie");
      xmlObj2.setLength(10); 
      xmlObj2.setSubObject(subObj);
      xmlObj2.setValue(-999.99);
      
      SecondClass secObj1 = new SecondClass("Koos", "Folsom");
      secObj1.setLength(100);
      secObj1.setValue(20.3333333333333333333333);
      secObj1.setSubObject(subObj);
      SecondClass secObj2 = new SecondClass("Attie", "Lutzville");
      secObj2.setLength(-20);
      secObj2.setValue((1.0d/3.0d));
      secObj2.setSubObject(subObj);
      
      List<SimpleObject> pList1 = new ArrayList<>();
      pList1.add(xmlObj1);
      pList1.add(xmlObj2);
      
      List<SecondClass> p2classList1 = new ArrayList<>();
      p2classList1.add(secObj1);
      p2classList1.add(secObj2);

      List<Serializable> pMapList = new ArrayList<>();
      pMapList.add(xmlObj1);
      pMapList.add(xmlObj2);
      
      List<String> pList2 = new ArrayList<>();
      pList2.add("Koos");
      pList2.add("Prins");
       
      JaxbListWrapper<SimpleObject> xmlList1 = new JaxbListWrapper<>(pList1);
      JaxbListWrapper<String> xmlList2 = new JaxbListWrapper<>(pList2);
      SimpleList simplelList = new SimpleList(pList1);
      SecondClassList secondList = new SecondClassList(p2classList1);

//      String xmlStr = xmlBuilder.getXMLContent(xmlList1, elemClass);
      System.out.println("------------------- Start of Sub Test -------------------\n");
      String xmlStr = 
                JAXBHelper.getXMLContent(QName.valueOf("Seconds"),p2classList1, true);
      System.out.println("SimpleObject XML Content=\n" + xmlStr);      
      if (xmlStr != null) {
        Class<ArrayList<SecondClass>> xmlClass 
                          = ReflectionInfo.castAsSpecificGenericClass(ArrayList.class);
        ArrayList<SecondClass> outList = (ArrayList<SecondClass>) 
                       JAXBHelper.parseXMLContext(xmlStr, xmlClass, SecondClass.class);
        if (outList == null) {
          throw new Exception("parsing failed.");
        }
        for (SimpleObject pObj : outList) {
          System.out.println("SecondClass Content=\n" + pObj.toString());
        }
        System.out.println("------------------- End of Sub Test -------------------\n");
      }
      
      
      System.out.println("------------------- Start of Sub Test -------------------\n");
      xmlStr = JAXBHelper.getXMLContent(null,p2classList1, true);
      System.out.println("SimpleObject XML Content=\n" + xmlStr);
      if (xmlStr != null) {
        Class<ArrayList<SecondClass>> xmlClass 
                          = ReflectionInfo.castAsSpecificGenericClass(ArrayList.class);
        List<SecondClass> outList = (ArrayList<SecondClass>) 
                       JAXBHelper.parseXMLContext(xmlStr, xmlClass, SecondClass.class);
        if (outList == null) {
          throw new Exception("parsing failed.");
        }
        for (SimpleObject pObj : outList) {
          System.out.println("SecondClass Content=\n" + pObj.toString());
        }
        System.out.println("------------------- End of Sub Test -------------------\n");
      }
      
      System.out.println("------------------- Start of Sub Test -------------------\n");
      xmlStr = JAXBHelper.getXMLContent(null,secObj1, true);
      System.out.println("SimpleObject XML Content=\n" + xmlStr);
      if (xmlStr != null) {
        SecondClass outObj = JAXBHelper.parseXMLContext(xmlStr, SecondClass.class);
        if (outObj == null) {
          throw new Exception("parsing failed.");
        }
        System.out.println("SecondClass Content=\n" + outObj.toString());
        System.out.println("------------------- End of Sub Test -------------------\n");
      }
      System.out.println("Test 'JaxbHelper Test' Succeeded");
    } catch (Exception pExp) {
      fail("Test 'JaxbHelper Test' Failed.\n" + pExp.getMessage());
    }
  }

  @Test
  public void testJaxbMapWrapper() {
    System.out.println("Test 'JaxbMapWrapper Test'");
    try {
      SubObject subObj = new SubObject();
      subObj.setName("Prins");
      subObj.setLength(4);
      subObj.setValue(100.9999);
      
      SecondClass secObj1 = new SecondClass("Koos", "Folsom");
      secObj1.setLength(100);
      secObj1.setValue(20.3333333333333333333333);
      secObj1.setSubObject(subObj);
      SecondClass secObj2 = new SecondClass("Attie", "Lutzville");
      secObj2.setLength(-20);
      secObj2.setValue((1.0d/3.0d));
      secObj2.setSubObject(subObj);
      
      HashMap<String,SecondClass> inputMap = new HashMap<>();
      inputMap.put("object1", secObj1);
      inputMap.put("object2", secObj2);
             
//      String xmlStr = xmlBuilder.getXMLContent(xmlList1, elemClass);
      System.out.println("------------------- Start of Sub Test -------------------\n");
      String xmlStr = 
                JAXBHelper.getXMLContent(QName.valueOf("Seconds"),inputMap, true);
      System.out.println("SimpleObject XML Content=\n" + xmlStr);      
      if (xmlStr != null) {
        Class<? extends HashMap<String,SecondClass>> xmlClass 
                                 = ReflectionInfo.castAsGenericClass(HashMap.class);
        HashMap<String,SecondClass> map = (HashMap<String,SecondClass>)
                JAXBHelper.parseXMLContext(xmlStr, xmlClass, String.class, 
                SecondClass.class);
        if (map == null) {
          throw new Exception("parsing failed.");
        }
        for (Map.Entry entry : map.entrySet()) {
          System.out.println("Map Key=" + entry.getKey());
          System.out.println("Map Value=" + entry.getValue() );
          System.out.println("---------------------------\n" );
        }
        System.out.println("------------------- End of Sub Test -------------------\n");
      }
      System.out.println("Test 'JaxbMapWrapper Test' Succeeded");
    } catch (Exception pExp) {
      fail("Test 'JaxbMapWrapper Test' Failed.\n" + pExp.getMessage());
    }
  }
  
  @Test
  public void testHashMapSerialization() { 
    System.out.println("Test 'HashMapSerialization'");
    try {
      TestMapOwner mapOwer = new TestMapOwner("My Test MapOwner");
      
      SubObject subObj = new SubObject();
      subObj.setName("Prins");
      subObj.setLength(4);
      subObj.setValue(100.9999);
      
      SimpleObject simple = new SimpleObject("My House");
      simple.setLength(1000);
      simple.setValue(30.22);
      simple.setSubObject(subObj);
      mapOwer.setObjInst(simple);
      
      SecondClass secObj1 = new SecondClass("Koos", "Folsom");
      secObj1.setLength(100);
      secObj1.setValue(20.3333333333333333333333);
      secObj1.setSubObject(subObj);
      SecondClass secObj2 = new SecondClass("Attie", "Lutzville");
      secObj2.setLength(-20);
      secObj2.setValue((1.0d/3.0d));
      secObj2.setSubObject(subObj);
      
      mapOwer.put("Child1", secObj1);
      mapOwer.put("Child2", secObj2);
      
      String xmlStr = 
                JAXBHelper.getXMLContent(QName.valueOf("Seconds"),mapOwer, true);
      System.out.println("SimpleObject XML Content=\n" + xmlStr);  
      
      if (xmlStr != null) {
        Class<? extends HashMap<String,SecondClass>> xmlClass 
                                 = ReflectionInfo.castAsGenericClass(HashMap.class);
        TestMapOwner outOwner = JAXBHelper.parseXMLContext(xmlStr, TestMapOwner.class);
        if (outOwner == null) {
          throw new Exception("parsing failed.");
        }
        System.out.println(outOwner.toString() );
        System.out.println("------------------- End of Sub Test -------------------\n");
      }
      System.out.println("Test 'HashMapSerialization' Succeeded");
    } catch (Exception pExp) {
      fail("Test 'HashMapSerialization' Failed.\n" + pExp.getMessage());
    }
  }
}
