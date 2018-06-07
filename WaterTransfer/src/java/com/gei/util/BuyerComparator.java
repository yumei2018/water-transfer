package com.gei.util;

import com.gei.entities.WtAgency;
import com.gei.entities.WtBuyer;
import com.gei.facades.WtAgencyFacade;
import gov.ca.water.transfer.util.WebUtil;
import java.util.Comparator;

/**
 *
 * @author ymei
 */
public class BuyerComparator implements Comparator<WtBuyer> {

  @Override
  public int compare(WtBuyer b1, WtBuyer b2) {
    WtAgencyFacade waf = WebUtil.getFacade(WtAgencyFacade.class);
    Integer wtAgencyId_b1 = b1.getWtBuyerPK().getWtAgencyId();
    Integer wtAgencyId_b2 = b2.getWtBuyerPK().getWtAgencyId();
    WtAgency wtAgency_b1 = waf.find(wtAgencyId_b1);
    WtAgency wtAgency_b2 = waf.find(wtAgencyId_b2);

    return wtAgency_b1.getAgencyFullName().compareTo(wtAgency_b2.getAgencyFullName());
  }
}
