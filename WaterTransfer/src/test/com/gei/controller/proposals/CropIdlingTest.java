package com.gei.controller.proposals;

import com.gei.controller.BaseControllerTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 *
 * @author rmarquez rmarquez@geiconsultants.com
 */
@RunWith(JUnitParamsRunner.class)
public class CropIdlingTest extends BaseControllerTest {

  /**
   *
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "165, true, true",
    "-1, true, false"
  })
  public void testRemoveCiCropType(Integer wtCiCroptypeId,
          boolean expSuccess, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/removecicroptype")
              .param("wtCiCroptypeId", String.valueOf(wtCiCroptypeId))
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertEquals("successful response", expSuccess, success);
    } catch (Exception ex) {
      if (valid) {
        throw ex;
      } else {
        return;
      }
    }
    Assert.assertTrue(valid);
  }

  /**
   *
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "1, 1, false, false",})
  public void testSaveCiCropType(
          String wtCiCroptypeId, String wtCropIdlingId, boolean expSuccess, boolean valid)
          throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/savecicroptype")
              .param("wtCiCroptypeId", wtCiCroptypeId)
              .param("wtCropIdlingId", wtCropIdlingId)
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertEquals("successful response", expSuccess, success);
    } catch (Exception ex) {
      if (valid) {
        throw ex;
      } else {
        return;
      }
    }
    Assert.assertTrue(valid);
  }

  /**
   *
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "-1, , , 0.0, false, false"
  })
  public void testSaveCiMonthly(Integer cropIdlingId,
          String fieldName, String fieldValue, Double twTotalVal,
          boolean expSuccess, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/savecimonthly")
              .param("cropIdlingId", String.valueOf(cropIdlingId))
              .param("fieldName", fieldName)
              .param("fieldValue", fieldValue)
              .param("twTotalVal", String.valueOf(twTotalVal))
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertEquals("successful response", expSuccess, success);
    } catch (Exception ex) {
      if (valid) {
        throw ex;
      } else {
        return;
      }
    }
    Assert.assertTrue(valid);

  }

  /**
   *
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "-1, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, false, false",})
  public void testSaveCropIdling(Integer cropIdlingId,
          String waterTransQuaCI, String totalTransferAcr,
          String currentFsAgency, String isResReleaseCI,
          String mayEtaw, String juneEtaw,
          String julyEtaw, String augustEtaw,
          String septemberEtaw, String mayTw,
          String juneTw, String julyTw,
          String augustTw, String septemberTw,
          boolean expSuccess, boolean valid) throws Exception {
    try {
      JSONObject jsondata = new JSONObject();
      jsondata.put("waterTransQuaCI", waterTransQuaCI); // 1
      jsondata.put("totalTransferAcr", totalTransferAcr); // 2
      jsondata.put("currentFsAgency", currentFsAgency); // 3
      jsondata.put("isResReleaseCI", isResReleaseCI); // 4
      jsondata.put("mayEtaw", mayEtaw); // 5
      jsondata.put("juneEtaw", juneEtaw); // 6
      jsondata.put("julyEtaw", julyEtaw); // 7
      jsondata.put("augustEtaw", augustEtaw); // 8
      jsondata.put("septemberEtaw", septemberEtaw); // 9
      jsondata.put("mayTw", mayTw); // 10
      jsondata.put("juneTw", juneTw); // 11
      jsondata.put("julyTw", julyTw); // 12
      jsondata.put("augustTw", augustTw); // 13
      jsondata.put("septemberTw", septemberTw); // 14
      MockHttpServletRequestBuilder post = post("/proposal/savecropidling")
              .param("cropIdlingId", String.valueOf(cropIdlingId))
              .param("jsondata", jsondata.toString())
              .session(mockHttpSession);

      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertEquals("successful response", expSuccess, success);
    } catch (Exception ex) {
      if (valid) {
        throw ex;
      } else {
        return;
      }
    }
    Assert.assertTrue(valid);
  }

  /**
   *
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "-1, name, , , false, false"
  })
  public void testSaveCropIdlingField(
          Integer cropIdlingId, String fieldName,
          String fieldValue, String actualAmount,
          boolean expSuccess, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/savecifield")
              .param("cropIdlingId", String.valueOf(cropIdlingId))
              .param("fieldName", fieldName)
              .param("fieldValue", fieldValue)
              .param("actualAmount", actualAmount)
              .session(this.mockHttpSession);
      String result = this.mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertEquals("successful response", expSuccess, success);
    } catch (Exception ex) {
      if (valid) {
        throw ex;
      } else {
        return;
      }
    }
    Assert.assertTrue(valid);
  }

}
