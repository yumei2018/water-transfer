package com.gei.controller.service;

import com.gei.context.ConnectionContext;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author clay
 */
@Controller
@RequestMapping("/service/test")
public class TestService {
  
  // <editor-fold defaultstate="collapsed" desc="Connection Test">
  @RequestMapping("/connection")
  public void connectionTest(HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      jsonResponse.put("data", conn.toString()).put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>
}
