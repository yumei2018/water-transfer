package com.gei.controller;

import com.gei.context.ConnectionContext;
import com.gei.context.UserContext;
import com.gei.entities.AppUser;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */
@Controller
public class IndexController extends BaseController {

  //<editor-fold defaultstate="collapsed" desc="Autowired Properties">
  @Autowired
  ApplicationContext appContext;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Index Page">
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = null;
    Boolean networkFlag = false;
    try {
      if (UserContext.getInstance().isLoggedIn()) {
        AppUser user = UserContext.getInstance().getUser();
        if (user != null && user.getNeedPasswordReset() == 1) {
          mv = new ModelAndView("adminApp/reset");
        }
        else {
          mv = new ModelAndView("index");
        }
      }
      else {
        mv = new ModelAndView("login");
        try {
          Connection conn = ConnectionContext.getConnection("WtDataSource");
        } catch (Exception ex) {
          networkFlag = true;          
        }        
      }
    }
    catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    } finally {
      mv.addObject("networkFlag", networkFlag);
      return mv;
    }
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Error Page">
  @RequestMapping("/error/{errorCode}")
  public String error(@PathVariable("errorCode") Integer errorCode
          , Exception ex,ModelMap map,HttpServletRequest request,HttpServletResponse response) {
    String error = (String) request.getAttribute("javax.servlet.error.message");
    String errorTitle = null;
    switch (errorCode){
      case HttpServletResponse.SC_NOT_FOUND:
      case HttpServletResponse.SC_BAD_REQUEST:
        error=StringUtils.isEmpty(error) ? "The requested page does not exists!" : error;
        errorTitle="Page not found";
        break;
      case HttpServletResponse.SC_FORBIDDEN:// 403
      case HttpServletResponse.SC_UNAUTHORIZED:
        errorTitle="Unauthorized Access";
        error=StringUtils.isEmpty(error) ? "You are not authorized to access the page!" : error;
        break;
      default:// HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        errorTitle = "Internal Server Error";
    }
    map.addAttribute("errorTitle",errorTitle);
    map.addAttribute("error", error);
    
    return "error";
  }
  //</editor-fold>
}
