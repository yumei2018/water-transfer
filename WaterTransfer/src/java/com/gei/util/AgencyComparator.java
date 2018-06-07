/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.util;

import com.gei.entities.WtAgency;
import java.util.Comparator;

/**
 *
 * @author ymei
 */
public class AgencyComparator implements Comparator<WtAgency>{
    @Override
    public int compare(WtAgency a1, WtAgency a2)
    {
        return a1.getAgencyFullName().compareTo(a2.getAgencyFullName());
    }
}
