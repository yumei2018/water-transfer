package com.gei.controller.admin;

import com.gei.controller.BaseControllerTest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

;

/**
 *
 * @author rmarquez rmarquez@geiconsultants.com
 */
@RunWith(JUnitParamsRunner.class)
public class AdminControllerTest extends BaseControllerTest {

  /**
   *
   * @param wtContactId
   * @param regUserId
   * @param groupId
   * @param userId
   * @param username
   * @param password
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "468, 404, 2, 684, tester, testpassword, true",
    "468, -1, 2, 684, tester, testpassword, false",
    "468, 404, -1, 684, tester, testpassword, false",
    "0, 404, 2, 684, tester, testpassword, false",
    "468, 404, 2, -1, tester, testpassword, false",
    "468, 404, 2, -1, , , false",})
  public void testCreateAccount(
          Integer wtContactId, Integer regUserId,
          Integer groupId, Integer userId,
          String username, String password, boolean valid) throws Exception {
    try {
      JSONObject jsonData = new JSONObject()
              .put("WtContactId", wtContactId)
              .put("regUserId", regUserId)
              .put("groupId", groupId)
              .put("username", username)
              .put("userId", userId)
              .put("password", password);

      MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/admin/createAccount/")
              .param("jsondata", jsonData.toString())
              .session(mockHttpSession);
      ResultActions perform = this.mockMvc.perform(post);
      JSONObject jsonResult = new JSONObject(perform
              .andReturn()
              .getResponse()
              .getContentAsString());
      Assert.assertEquals(valid, jsonResult.getBoolean("success"));
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
   * @param userId
   * @param firstName
   * @param middleName
   * @param lastName
   * @param email
   * @param createdDate
   * @param updatedDate
   * @param isRegistered
   * @param agencyId
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "384, test, , 1, cduffy@geiconsultants.com, 04-DEC-2017 13:44:36, 04-DEC-2017 13:44:36,	1, 1, true"
  })
  public void testRegister(
          Integer userId, String firstName, String middleName,
          String lastName, String email, String createdDate,
          String updatedDate, Integer isRegistered, Integer agencyId,
          boolean valid) throws Exception {

    try {
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
      JSONObject jsonData = new JSONObject()
              .put("userId", userId)
              .put("firstName", firstName)
              .put("middleName", middleName)
              .put("lastName", lastName)
              .put("email", email)
              .put("createdDate", formatter.parse(createdDate))
              .put("updatedDate", formatter.parse(updatedDate))
              .put("isRegistered", isRegistered)
              .put("agencyId", agencyId);
      MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/admin/register/")
              .param("jsondata", jsonData.toString())
              .session(mockHttpSession);
      ResultActions perform = this.mockMvc.perform(post);
      MvcResult mvcResult = perform.andReturn();
      JSONObject jsonResult = new JSONObject(mvcResult
              .getResponse()
              .getContentAsString());
      Assert.assertEquals(valid, jsonResult.getBoolean("success"));
      Assert.assertEquals(0, jsonResult.getJSONObject("data").getInt("isRegistered"));
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
   * @param userName
   * @param userId
   * @param expResult
   * @param valid
   * @throws Exception
   */
  @Test
  @Parameters({
    "test1, 645, exist, true",
    "test1, -1, notExist, true",
    "test, -1, notExist, true"
  })
  public void testCheckUsername(String userName, Integer userId, String expResult,
          boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = MockMvcRequestBuilders
              .post("/admin/checkUsername/")
              .param("username", userName)
              .param("userId", String.valueOf(userId))
              .session(mockHttpSession);
      ResultActions perform = this.mockMvc.perform(post);
      MvcResult mvcResult = perform.andReturn();
      JSONObject jsonResult = new JSONObject(mvcResult
              .getResponse()
              .getContentAsString());
      Assert.assertEquals("matches expected result", expResult, jsonResult.getString("user"));

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
    "true",
    "true"
  })
  public void testProposalList(boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/admin/proposallist")
              .session(mockHttpSession);
      String result = mockMvc.perform(post)
              .andReturn()
              .getResponse()
              .getContentAsString();
      System.out.println("result = " + result);
      JSONObject obj = new JSONObject(result);
      boolean success = obj.getBoolean("success");
      if (!success) {
        System.out.println(obj.getString("error"));
        throw new RuntimeException(obj.getString("error"));
      }
      Assert.assertTrue("successful response", success);
      ArrayList arrayList = obj.getJSONArray("data").getArrayList();
      Assert.assertFalse(arrayList.isEmpty());
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
