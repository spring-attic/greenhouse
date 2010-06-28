package org.springframework.web.mobile;

import net.sourceforge.wurfl.core.DefaultDeviceProvider;
import net.sourceforge.wurfl.core.DefaultWURFLManager;
import net.sourceforge.wurfl.core.DefaultWURFLService;
import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.handlers.matchers.MatcherManager;
import net.sourceforge.wurfl.core.resource.DefaultWURFLModel;
import net.sourceforge.wurfl.core.resource.SpringXMLResource;
import net.sourceforge.wurfl.core.resource.WURFLResources;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

public class WURFLManagerFactoryBean implements FactoryBean<WURFLManager>, InitializingBean {

	private WURFLManager manager;
	
	public void afterPropertiesSet() throws Exception {
		this.manager = initWURFLManager();
	}

	// implementing FactoryBean
	
	public Class<?> getObjectType() {
		return WURFLManager.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public WURFLManager getObject() throws Exception {
		return manager;
	}
	
	protected WURFLManager initWURFLManager() {
		ClassPathResource wurflZip = new ClassPathResource("wurfl.zip", getClass());
		WURFLResources resources = new WURFLResources();
		resources.add(new SpringXMLResource(new ClassPathResource("web_browsers_patch.xml", getClass())));
		DefaultWURFLModel model = new DefaultWURFLModel(new SpringXMLResource(wurflZip), resources);
		MatcherManager matcherManager = new MatcherManager(model);
		DefaultDeviceProvider deviceProvider = new DefaultDeviceProvider(model);
		DefaultWURFLService service = new DefaultWURFLService(matcherManager, deviceProvider);
		return new DefaultWURFLManager(service);		
	}

}