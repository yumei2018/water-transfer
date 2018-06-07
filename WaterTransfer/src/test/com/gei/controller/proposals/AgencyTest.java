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
public class AgencyTest extends BaseControllerTest {

  @Test
  @Parameters({
    "Big Water Agency, BWA, true",
    "Zone 7, , true",
    ", , true",
  })
  public void testAddNewAgency(String agencyFullName, String expAgencyCode,
          boolean valid) throws Exception {

    try {
      MockHttpServletRequestBuilder post = post("/proposal/addNewAgency/")
              .param("agencyFullName", agencyFullName)
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      String agencyCode;
      if (!result.isEmpty()) {
        JSONObject obj = new JSONObject(result);
        agencyCode = obj.getString("agencyCode");
      } else {
        agencyCode = "";
      }
      Assert.assertEquals("agency code matches", expAgencyCode, agencyCode);
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
    "182, AG, true, true",
    "182, Fake, true, true",
    "-1, Fake, false, true",
  })
  public void testEditAgency(Integer agencyId, String newAgencyType,
          boolean success, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/editAgency/")
              .param("wtAgencyId", String.valueOf(agencyId))
              .param("agencyType", newAgencyType)
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject jsonResult = new JSONObject(result);
      Assert.assertEquals(success, jsonResult.getBoolean("success"));
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
    "182, true",
  })
  public void testRemoveAgency(Integer agencyId, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/removeAgency")
              .session(mockHttpSession);
      //??????
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
    "1685, null, 1, true, true",
    "1685, DWR, 1, true, true",
    "1685, DWR, -1, true, true",
    "-1, , , false, true",
  })
  public void testSaveAgencyApproval(Integer transId, String fieldName,
          String fieldValue, boolean expSuccess, boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/saveagencyapproval")
              .param("transId", String.valueOf(transId))
              .param("fieldName", String.valueOf(fieldName))
              .param("fieldValue", String.valueOf(fieldValue))
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      JSONObject jsonResult = new JSONObject(result);
      Assert.assertEquals(expSuccess, jsonResult.getBoolean("success"));
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
