package org.springframework.web.mobile.tags;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;

import net.sourceforge.wurfl.core.handlers.AppleHandler;

public class AppleDevice extends SimpleTagSupport {
   
   public void doTag() throws JspException {
       
       PageContext pageContext = (PageContext) getJspContext();
       JspWriter out = pageContext.getOut();
       
       try {
           
    	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest(); 
    	   String userAgent = request.getHeader("User-Agent");
    	   
    	   if (new AppleHandler().canHandle(userAgent)) {
    		   
    		   JspFragment jspFragment = getJspBody();
    		   
    		   if (jspFragment != null) {
    			   
    			   StringWriter stringWriter = new StringWriter();
    			   jspFragment.invoke(stringWriter);
    			   out.print(stringWriter.getBuffer().toString());
    		   }
    	   }

       } catch (Exception e) {
           
       }       
   }   
}