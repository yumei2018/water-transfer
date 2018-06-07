package com.gei.controller;

import com.gei.util.WebUtil;
import javax.servlet.ServletContext;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author rmarquez rmarquez@geiconsultants.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
  "/conf/Controllers-context.xml", "/conf/applicationContext.xml", "/conf/wt.beans.xml"
})
@WebAppConfiguration
public class BaseControllerTest {

  public static MockMvc mockMvc;
  public MockHttpSession mockHttpSession;

  @Autowired
  public AdminController adminControllerMock;
  @Autowired
  public AuthenticationController authControllerMock;
  @Autowired
  public ProposalController proposalControllerMock;
  @Autowired
  public WebApplicationContext appContext;

  private final Boolean doLogin;

  public BaseControllerTest() {
    this.doLogin = true;
  }

  @Before
  public void setup() throws Exception {
    TestContextManager contextMngr = new TestContextManager(BaseControllerTest.class);
    contextMngr.prepareTestInstance(this);
    if (this.doLogin && adminControllerMock != null) {
      if (mockMvc == null) {
        mockMvc = MockMvcBuilders
              .standaloneSetup(adminControllerMock,
                      authControllerMock,
                      proposalControllerMock,
                      appContext)
              .build();
      }
      ServletContext servletContext = appContext.getServletContext();
      this.mockHttpSession = new MockHttpSession(servletContext);
      gov.ca.water.transfer.util.WebUtil.setAppContext(this.appContext);
      WebUtil.setAppContext(appContext);

      String json = mockMvc.perform(post("/authentication/login")
              .param("username", "seller")
              .param("password", "seller")
              .session(this.mockHttpSession))
              .andReturn()
              .getResponse()
              .getContentAsString();

      JSONObject obj = new JSONObject(json);
      boolean loggedIn = obj.getBoolean("success");
      if (!loggedIn) {
        throw new RuntimeException("Not logged in. " + obj.getString("error"));
      }
    }
  }
}
