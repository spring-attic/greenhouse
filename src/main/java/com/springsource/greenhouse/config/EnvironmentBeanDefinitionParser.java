package com.springsource.greenhouse.config;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

class EnvironmentBeanDefinitionParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String environment = getEnvironment(parserContext);
		String beanName = element.getAttribute("id");
		List<Element> whenElements = DomUtils.getChildElementsByTagName(element, "when");
		for (Element whenElement : whenElements) {
			String value = whenElement.getAttribute("environment");
			if (value.equals(environment)) {
				parseAndRegisterBeanDefinition(beanName, whenElement, parserContext);
				return null;
			}
		}
		Element otherwiseElement = DomUtils.getChildElementByTagName(element, "otherwise");
		if (otherwiseElement == null) {
			Object source = parserContext.extractSource(element);
			parserContext.getReaderContext().error("No <bean/> matched in the current environment and no <otherwise/> <bean/> is defined", source);
		}
		parseAndRegisterBeanDefinition(beanName, otherwiseElement, parserContext);
		return null;
	}

	private String getEnvironment(ParserContext context) {
		return System.getProperty("environment");
	}

	private void parseAndRegisterBeanDefinition(String beanName, Element parentElement, ParserContext parserContext) {
		Element beanElement = DomUtils.getChildElementByTagName(parentElement, "bean");
		if (beanElement != null) {
			BeanDefinition beanDef = parserContext.getDelegate().parseBeanDefinitionElement(beanElement, beanName, null);
			BeanDefinitionHolder beanDefHolder = new BeanDefinitionHolder(beanDef, beanName);
			parserContext.getDelegate().decorateBeanDefinitionIfRequired(beanElement, beanDefHolder);
			BeanDefinitionReaderUtils.registerBeanDefinition(beanDefHolder, parserContext.getRegistry());
		} else {
			Element otherElement = DomUtils.getChildElements(parentElement).get(0);
			BeanDefinition containingBeanDef = new RootBeanDefinition();
			ParserContext nestedContext = new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), containingBeanDef);
			NamespaceHandler handler = parserContext.getReaderContext().getNamespaceHandlerResolver().resolve(otherElement.getNamespaceURI());
			BeanDefinition beanDef = handler.parse(otherElement, nestedContext);
			BeanDefinitionHolder beanDefHolder = new BeanDefinitionHolder(beanDef, beanName);
			BeanDefinitionReaderUtils.registerBeanDefinition(beanDefHolder, parserContext.getRegistry());			
		}
	}
	
}