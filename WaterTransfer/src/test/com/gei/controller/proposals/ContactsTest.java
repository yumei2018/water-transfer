package com.gei.controller.proposals;

import com.gei.controller.BaseControllerTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
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
public class ContactsTest extends BaseControllerTest {

  @Test
  @Parameters({
    "true",
    "true"
  })
  public void testProposalList(boolean valid) throws Exception {
    try {
      MockHttpServletRequestBuilder post = post("/proposal/proposal")
              .session(mockHttpSession);
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
