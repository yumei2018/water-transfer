/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.controller;

import static com.gei.controller.AuthenticationController.SESSION_KEY_USER;
import com.gei.entities.AppGroup;
import com.gei.entities.AppUser;
import com.gei.entities.WtContact;
import com.gei.facades.AppGroupFacade;
import com.gei.facades.WtContactFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController{
    @Autowired
    ApplicationContext appContext;
    final static String ROLE_APP_ACCOUNT = "APP_ACCT";

    public static boolean IsAppAccount(HttpServletRequest request){
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        Collection<AppGroup> groupCollection = user.getAppGroupCollection();
        for(AppGroup group : groupCollection){
            if(group.getName().equalsIgnoreCase(ROLE_APP_ACCOUNT)){
                return true;
            }
        }
        return false;
    }
     public void getUserGroup(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        Collection<AppGroup> agroup = agf.findAll();
        JSONArray agroupArray = new JSONArray();
        for(AppGroup grp: agroup)
        {
            JSONObject tmp = new JSONObject(grp.toMap());
            agroupArray.put(tmp);
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data",agroupArray);
        response.getWriter().write(jsonResponse.toString());
    }

    @RequestMapping("/edit/")
    public ModelAndView permission(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("adminApp/edit");
        WtContactFacade wtc = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        Collection<WtContact> wt = wtc.findAll();
        Collection<WtContact> userAcct = new ArrayList();
        for(WtContact contact:wt)
        {
            if(contact.getUser() != null)
            {
                userAcct.add(contact);
            }
        }
        mv.addObject("wtcontact",userAcct);
        mv.addObject("type","permission");
        return mv;
    }

    public void editPermission(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LoggedInCheck(request,response);
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        String wtContactId = request.getParameter("contactId");
        String groupId = request.getParameter("groupId");
        WtContact contact = wcf.find(Integer.parseInt(wtContactId));
        Collection<AppGroup> agCol = new ArrayList();
        contact.getUser().setAppGroupCollection(agCol);
        if(!groupId.isEmpty())
        {
            Collection<AppGroup> appGrp = agf.select("SELECT * FROM APP_GROUP WHERE GROUP_ID IN ("+groupId+")",com.gei.entities.AppGroup.class);
            for(AppGroup grp : appGrp)
            {
                agCol.add(grp);
            }
            contact.getUser().setAppGroupCollection(agCol);
        }
        wcf.edit(contact);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data",contact.getUser().toMap());
        response.getWriter().write(jsonResponse.toString());
    }

    public ModelAndView getPermissionInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("adminApp/template/AccountPermission");
        String wtContactId = request.getParameter("contactId");
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        WtContact contact = wcf.find(Integer.parseInt(wtContactId));
        Collection<AppGroup> appgrp = null;
        try{
            appgrp = contact.getUser().getAppGroupCollection();
        }catch(NullPointerException ex){}
        mv.addObject("AppGroup",appgrp);
        mv.addObject("AvailGrp",agf.findAll());
        return mv;
    }
}
