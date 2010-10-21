package com.springsource.greenhouse.invite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.style.StylerUtils;


public class InviteeTest {
	
	@Test
	public void valueOf() {
		Invitee invitee = Invitee.valueOf("Keith Donald <keith.donald@springsource.com>");
		assertEquals("Keith", invitee.getFirstName());
		assertEquals("Donald", invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}
	
	@Test
	public void emailOnlyNoBrackets() {
		Invitee invitee = Invitee.valueOf("keith.donald@springsource.com");
		assertNull(invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}

	@Test
	public void emailOnlyBrackets() {
		Invitee invitee = Invitee.valueOf("<keith.donald@springsource.com>");
		assertNull(invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}

	@Test
	public void firstNameOnly() {
		Invitee invitee = Invitee.valueOf("Keith <keith.donald@springsource.com>");
		assertEquals("Keith", invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}
	
	@Test
	@Ignore
	public void extraWhitespace() {
		Invitee invitee = Invitee.valueOf("Keith        Donald    <keith.donald@springsource.com>");
		assertEquals("Keith", invitee.getFirstName());
		assertEquals("Donald", invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}
	
	@Test public void convertInvitees() throws Exception {
		GenericConversionService service = new GenericConversionService();
		ConversionServiceFactory.addDefaultConverters(service);
		List<Invitee> invitees = (List<Invitee>) service.convert("Venkat Subramaniam <venkats@agiledeveloper.com>, Guillaume LaForge <glaforge@vmware.com>, Tim Berglund <tlberglund@gmail.com>, Dave Klein <daveklein@usa.net>, Hamlet D'Arcy <hamletdrc@gmail.com>, Joshua Davis <webtech20@gmail.com>, Paul King <paulk@asert.com.au>, Kenneth Kousen <ken.kousen@kousenit.com>, Burt Beckwith <burt@burtbeckwith.com>, Graeme Rocher <grocher@vmware.com>, Matt Stine <matt.stine@gmail.com>, Jeff Brown <jbrown@vmware.com>, Colin Harrington <colin.harrington@gmail.com>, Peter Ledbrook <pledbrook@vmware.com>, Andres Almiray <aalmiray@gmail.com>, Peter Bell <peter@pbell.com>, Peter Niederwieser <pniederw@gmail.com>, James Williams <james.l.williams@gmail.com>, Hans Dockter <hans@gradle.biz>, Ken Sipe <kensipe@gmail.com>, Jean Barmash <jean.barmash@gmail.com>, Brian Sletten <brian@bosatsu.net>, Jay Zimmerman <jzimmerman@nofluffjuststuff.com>, Erik Weibust <erik@weibust.net>, Matthew McCullough <matthewm@ambientideas.com>", TypeDescriptor.valueOf(String.class), new TypeDescriptor(getClass().getField("invitees")));
		assertEquals(25, invitees.size());
		System.out.println(StylerUtils.style(invitees));
	}

	@Test public void convertInvitees2() throws Exception {
		String contents = getContents(new ClassPathResource("Attendees.txt", getClass()).getFile());
		System.out.println(contents);
		GenericConversionService service = new GenericConversionService();
		ConversionServiceFactory.addDefaultConverters(service);
		List<Invitee> invitees = (List<Invitee>) service.convert(contents, TypeDescriptor.valueOf(String.class), new TypeDescriptor(getClass().getField("invitees")));
		assertEquals(704, invitees.size());
		System.out.println(StylerUtils.style(invitees));
	}
	
	static public String getContents(File aFile) {
	    //...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        /*
	        * readLine is a bit quirky :
	        * it returns the content of a line MINUS the newline.
	        * it returns null only for the END of the stream.
	        * it returns an empty String if two newlines appear in a row.
	        */
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    
	    return contents.toString();
	  }


	public List<Invitee> invitees;

}