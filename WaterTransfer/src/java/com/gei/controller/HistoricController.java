/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.controller;

import com.gei.constants.Status;
import com.gei.context.LookupDataContext;
import static com.gei.controller.AuthenticationController.SESSION_KEY_USER;
import com.gei.entities.AppUser;
import com.gei.entities.WtAgency;
import com.gei.entities.WtBuyer;
import com.gei.entities.WtCity;
import com.gei.entities.WtState;
import com.gei.entities.WtStatusFlag;
import com.gei.entities.WtStatusTrack;
import com.gei.entities.WtTrans;
import com.gei.facades.WtStatusFlagFacade;
import com.gei.facades.WtStatusTrackFacade;
import com.gei.facades.WtTransFacade;
import com.gei.utils.ServletRequestUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/history")
public class HistoricController extends BaseController{
    @Autowired
    ApplicationContext appContext;

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        LoggedInCheckNoAjax(request,response);
        ModelAndView mv = new ModelAndView("history/index");

        return mv;
    }

    @RequestMapping("/proposalList")
    public void getProposalList(HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request,response);
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade f = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
//      WtTrans proposal = new WtTrans();
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      List<WtTrans> wts = null;

      // Proposal List return will depend on the user group and module type
      if(user.isManager()) {
        wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID="+Status.TCOMPLETE,com.gei.entities.WtTrans.class);
      } else {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      if (wts != null){
        JSONObject jsonResponse = new JSONObject(),tmpJson;
        jsonResponse.put("data",new JSONArray());
        WtBuyer buyer;
        WtAgency seller;
        String sellerNames,buyerNames,key,comma="";
        Iterator<WtAgency> sellersIt;
        Iterator<WtBuyer> buyersIt;
        for (WtTrans wt : wts){
          tmpJson = new JSONObject(wt.toMap());
          tmpJson.put("sellers","");
          tmpJson.put("buyers","");

          if (wt.getWtSellerCollection()!=null){
            sellersIt = wt.getWtSellerCollection().iterator();
            if (sellersIt != null){
              comma="";
              sellerNames="";
              key="sellers";
              while (sellersIt.hasNext()){
                seller = sellersIt.next();
                sellerNames += tmpJson.optString(key) + comma + seller.getAgencyCode();
                comma="/";
              }//end while
              tmpJson.put(key, sellerNames);
            }//end if
          }//end if

          if (wt.getWtBuyerCollection()!=null){
            buyersIt = wt.getWtBuyerCollection().iterator();
            if (buyersIt != null){
              comma="";
              buyerNames="";
              key="buyers";
              while (buyersIt.hasNext()){
                buyer = buyersIt.next();
                buyerNames += tmpJson.optString(key) + comma + buyer.getWtAgency().getAgencyCode();
                comma="/";
              }//end while
              tmpJson.put(key, buyerNames);
            }//end if
          }//end if

  //        if (wt.getWtSellerAgency()!=null){
  //            tmpJson.put("seller", wt.getWtSellerAgency().getAgencyCode());
  //        }//end if
          if (wt.getProTransQua() == null){
              tmpJson.put("proTransQua", "");
          }
          if (wt.getActTransQua() == null){
              tmpJson.put("actTransQua", "");
          }
          if (wt.getDwrProApprDate() == null){
              tmpJson.put("dwrProApprDate", "");
          } else {
              tmpJson.put("dwrProApprDate", df.format(wt.getDwrProApprDate()));
          }
          if (wt.getTransWinStart() == null){
              tmpJson.put("transWinStart", "");
          } else {
              tmpJson.put("transWinStart", df.format(wt.getTransWinStart()));
          }
          if (wt.getTransWinEnd() == null){
              tmpJson.put("transWinEnd", "");
          } else {
              tmpJson.put("transWinEnd", df.format(wt.getTransWinEnd()));
          }
          if (wt.getWtStatusFlag()== null){
              tmpJson.put("status", "");
          } else {
              tmpJson.put("status", wt.getWtStatusFlag().getStatusName());
          }
          jsonResponse.optJSONArray("data").put(tmpJson);
        }//end for
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/edit/{transId}")
    public ModelAndView edit(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException{
        LoggedInCheck(request,response);
        WtTransFacade f = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
//        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
//        WtCityFacade wcif = (WtCityFacade)appContext.getBean(WtCityFacade.class.getSimpleName());
//        WtStateFacade wsf = (WtStateFacade)appContext.getBean(WtStateFacade.class.getSimpleName());
//        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
//        wstf.set("wtTransId", transId);
//        List<WtStatusTrack> statusTrackList = wstf.findAll();
        List<WtStatusTrack> statusTrackList = wstf.select("SELECT WT.* FROM WT_STATUS_TRACK WT WHERE WT_TRANS_ID="+transId+" ORDER BY STATUS_DATE DESC",com.gei.entities.WtStatusTrack.class);

        ModelAndView mv = new ModelAndView("history/edit");
        WtTrans proposal = f.find(transId);
//        List<WtAgency> agencyList = waf.findAll();
//        List<WtCity> cityList = wcif.findAll();
//        List<WtState> stateList = wsf.findAll();
//        List<WtStatusFlag> statusList = wsff.findAll();
        
        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();//waf.select("SELECT * FROM WT_AGENCY ORDER BY AGENCY_FULL_NAME", com.gei.entities.WtAgency.class);
        List<WtCity> cityList = LookupDataContext.getInstance().getCityLookup();//wcif.findAll();
        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup();//wsf.findAll();
        List<WtStatusFlag> statusList = LookupDataContext.getInstance().getStatusFlagLookup();//wsff.findAll();

        if (proposal == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        mv.addObject("proposal", proposal);
        mv.addObject("agencyList", agencyList);
        mv.addObject("cityList", cityList);
        mv.addObject("stateList", stateList);
        mv.addObject("statusList", statusList);
        mv.addObject("statusTrackList", statusTrackList);
        return mv;
    }

    @RequestMapping("/getTransChart")
    public ModelAndView getTransChart(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("history/TransChart");
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.TCOMPLETE);

//        WtTrans trans = new WtTrans();
//        trans.setWtStatusFlag(statusFlag);
        List<WtTrans> transRecords = wtf.findAll();
        transRecords = wtf.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID="+Status.TCOMPLETE+" ORDER BY TRANS_YEAR",com.gei.entities.WtTrans.class);
        List<WtTrans> chartRecords = new ArrayList();
        Integer totalProTransQua = 0;
        Integer totalActTransQua = 0;
        Integer aveProTransQua = 0;
        Integer aveActTransQua = 0;

        if(transRecords == null || transRecords.isEmpty()){
            return mv;
        }
        for (WtTrans t : transRecords){
            Integer transYear = t.getTransYear();
            if (t.getProTransQua() != null){
//                totalProTransQua = totalProTransQua + t.getProTransQua();
            }
            if(t.getActTransQua() != null){
                totalActTransQua = totalActTransQua + t.getActTransQua();
            }

            if(chartRecords.isEmpty()){
                chartRecords.add(getNewTrans(t));
            } else {
                for (WtTrans c : chartRecords){
                    if (c.getTransYear().equals(t.getTransYear())){
                        transYear = null;
                        if(t.getProTransQua() != null){
                            c.setProTransQua(c.getProTransQua()+t.getProTransQua());
                        }
                        if(t.getActTransQua() != null){
                            c.setActTransQua(c.getActTransQua()+t.getActTransQua());
                        }
                    }
                }
                if (transYear != null){
                    chartRecords.add(getNewTrans(t));
                }
            }
        }

        Integer size = chartRecords.size();
        if (!size.equals(0)) {
            aveProTransQua = totalProTransQua/size;
            aveActTransQua = totalActTransQua/size;
        }

        mv.addObject("aveProTransQua", aveProTransQua);
        mv.addObject("aveActTransQua", aveActTransQua);
        mv.addObject("chartRecords", chartRecords);
        return mv;
    }

    private WtTrans getNewTrans(WtTrans t){
        WtTrans cr = new WtTrans();
        cr.setTransYear(t.getTransYear());
        if(t.getProTransQua() == null){
//            cr.setProTransQua(0);
        } else {
            cr.setProTransQua(t.getProTransQua());
        }
        if(t.getActTransQua() == null){
            cr.setActTransQua(0);
        } else {
            cr.setActTransQua(t.getActTransQua());
        }

        return cr;
    }
}
