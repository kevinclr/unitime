/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/
package org.unitime.timetable.tags;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.criterion.Order;
import org.unitime.commons.Debug;
import org.unitime.timetable.model.PreferenceLevel;
import org.unitime.timetable.model.dao.PreferenceLevelDAO;


/**
 * @author Tomas Muller
 */
public class PreferenceLegend extends TagSupport {
	private static final long serialVersionUID = -8994474921509928721L;
	private boolean iNotAvailable = false;
	private boolean iPrefs = true;
	private boolean iDpBackgrounds = false;
	private boolean iDpAssign = false;
	private boolean iDpOffered = false;
	private String iSeparator = "top";
	
	public String getSeparator() { return iSeparator; }
	public void setSeparator(String separator) { iSeparator = separator; }
	
	public void setNotAvailable(boolean notAvailable) { iNotAvailable = notAvailable; }
	public boolean isNotAvailable() { return iNotAvailable; }

    public void setDpBackgrounds(boolean dpBackgrounds) { iDpBackgrounds = dpBackgrounds; }
    public boolean isDpBackgrounds() { return iDpBackgrounds; }

    public void setPrefs(boolean prefs) { iPrefs = prefs; }
    public boolean isPrefs() { return iPrefs; }

    public void setDpOffered(boolean dpOffered) { iDpOffered = dpOffered; }
    public boolean isDpOffered() { return iDpOffered; }

    public void setDpAssign(boolean dpAssign) { iDpAssign = dpAssign; }
    public boolean isDpAssign() { return iDpAssign; }

    public int doEndTag() throws JspException {
		return EVAL_PAGE;
    }
    
    public int doStartTag() throws JspException {
    	
    	String border = "";
    	if (iSeparator!=null && iSeparator.length()>0)
    		border = "border-"+iSeparator+":1px dashed #9CB0CE";
    	if ("none".equals(iSeparator)) border = null;
        
        StringBuffer html = new StringBuffer(border==null?"":"<table width='100%' cellspacing='1' cellpadding='1' border='0' style='"+border+"'><tr><td align='center'>");
        html.append("<div class='unitime-ScrollTable'>");
        html.append("<table cellspacing='1' cellpadding='1' border='0'><tr>");
        
        Collection prefLevels = (Collection)pageContext.getRequest().getAttribute(PreferenceLevel.PREF_LEVEL_ATTR_NAME);
        if (prefLevels==null) {
        	PreferenceLevelDAO pdao = new PreferenceLevelDAO();
        	prefLevels = pdao.findAll(Order.asc("prefId"));
        }
        String imgFolder = ((HttpServletRequest)pageContext.getRequest()).getContextPath()+"/images/";
        if (isPrefs()) {
            for (Iterator i=prefLevels.iterator();i.hasNext();) {
                PreferenceLevel pl = (PreferenceLevel)i.next();
                html.append(
				"<td style='font-size: 80%;'>"+
				"<img border='0' align='absmiddle' src='"+imgFolder+"pref"+pl.getUniqueId()+".png'>"+
				"&nbsp;" + pl.getPrefName() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
				"</td>");
            }
        }
        if (isNotAvailable()) {
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle'src='"+imgFolder+"prefna.png'>"+
                    "&nbsp;Not Available&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
        }
        if (isDpOffered()) {
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-offered.png'>"+
                    "&nbsp;Classes Offered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-notoffered.png'>"+
                    "&nbsp;Classes Not Offered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
        }
        if (isDpBackgrounds()) {
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-start.png'>"+
                    "&nbsp;Start / End&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-exam.png'>"+
                    "&nbsp;Examination Start&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-holiday.png'>"+
                    "&nbsp;Holiday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-break.png'>"+
                    "&nbsp;Break&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
        }
        if (isDpAssign()) {
            html.append(
                    "<td style='font-size: 80%;'>"+
                    "<img border='0' align='absmiddle' src='"+imgFolder+"dp-assign.png'>"+
                    "&nbsp;Assignment&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                    "</td>");
        }
        html.append("</tr></table>");
        html.append("</div>");
        html.append(border==null?"":"</td></tr></table>");
        
        try {
            pageContext.getOut().print(html.toString());
        }
        catch (Exception e) {
            Debug.error("Could not display preference legend: " + e.getMessage());
        }

		return SKIP_BODY;
    }
}
