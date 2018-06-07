package com.gei.controller;

import com.gei.context.UserContext;
import com.gei.entities.AppUser;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author clay
 */
@Controller
@RequestMapping("/authentication")
public class AuthenticationController extends BaseController implements Serializable {

  @Autowired
  ApplicationContext appContext;

  final static String SESSION_KEY_LOGGEDIN = "LOGGED_IN", SESSION_KEY_USER = "USER";

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public void login(
          @RequestParam("username") String username,
          @RequestParam("password") String rawpassword,
          HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      UserContext.getInstance().authenticate(username, rawpassword);
      jsonResponse.put("success", true);
    } catch (Exception e) {
      jsonResponse.put("success", false).put("error", e.getMessage());
    } finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

  public void loginTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String html = "<form method='POST' action='" + request.getContextPath() + "/authentication/login'>";
    html += "<label for='username'>Login ID</label><input type='text' id='username' name='username' /><br />";
    html += "<label for='password'>Password</label><input type='password' id='password' name='password' /><br />";
    html += "<input type='submit' value='Login' /><br />";
    html += "</form>";

    response.getWriter().write(html);
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    request.getSession().removeAttribute(SESSION_KEY_LOGGEDIN);
    request.getSession().removeAttribute(SESSION_KEY_USER);
    request.getSession().invalidate();
    response.sendRedirect(request.getContextPath());
  }

  public void initSession(AppUser user, HttpServletRequest request) {
    request.getSession().setAttribute(SESSION_KEY_LOGGEDIN, true);
    request.getSession().setAttribute(SESSION_KEY_USER, user);
    UserContext.getInstance().initSession(user);
  }

  public static boolean killSession(HttpServletRequest request) {
    request.getSession().removeAttribute(SESSION_KEY_LOGGEDIN);
    request.getSession().removeAttribute(SESSION_KEY_USER);
    return true;
  }

  public static boolean IsLoggedIn(HttpServletRequest request) {
    return request.getSession().getAttribute(SESSION_KEY_LOGGEDIN) != null
            && (boolean) request.getSession().getAttribute(SESSION_KEY_LOGGEDIN);
  }
}
