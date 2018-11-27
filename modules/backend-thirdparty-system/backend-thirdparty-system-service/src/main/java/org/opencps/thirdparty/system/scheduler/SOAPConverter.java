package org.opencps.thirdparty.system.scheduler;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.opencps.thirdparty.system.nsw.model.Envelope;
import org.opencps.thirdparty.system.nsw.model.RequestPayload;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class SOAPConverter {
	public static String convertNSWRequest(RequestPayload request) {
		JAXBContext context;
		String xmlString = "";
		
		try {
			context = JAXBContext.newInstance(RequestPayload.class);
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	        StringWriter sw = new StringWriter();
	        m.marshal(request, sw);
	        xmlString = sw.toString();		
		} catch (JAXBException e) {
			_log.error(e);
		}
        return xmlString;
	}

	public static String convertNSWRequest(Envelope envelope) {
		JAXBContext context;
		String xmlString = "";
		
		try {
			context = JAXBContext.newInstance(Envelope.class);
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	        StringWriter sw = new StringWriter();
	        m.marshal(envelope, sw);
	        xmlString = sw.toString();		
		} catch (JAXBException e) {
			_log.error(e);
		}
        return xmlString;
	}
	
	public static String convertNSWRequestVT(org.opencps.thirdparty.system.nsw.vt.model.RequestPayload request) {
		JAXBContext context;
		String xmlString = "";
		
		try {
			context = JAXBContext.newInstance(org.opencps.thirdparty.system.nsw.vt.model.RequestPayload.class);
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	        StringWriter sw = new StringWriter();
	        m.marshal(request, sw);
	        xmlString = sw.toString();		
		} catch (JAXBException e) {
			_log.error(e);
		}
        return xmlString;
	}

	public static String convertNSWRequestVT(org.opencps.thirdparty.system.nsw.vt.model.Envelope envelope) {
		JAXBContext context;
		String xmlString = "";
		
		try {
			context = JAXBContext.newInstance(org.opencps.thirdparty.system.nsw.vt.model.Envelope.class);
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	        StringWriter sw = new StringWriter();
	        m.marshal(envelope, sw);
	        xmlString = sw.toString();		
		} catch (JAXBException e) {
			_log.error(e);
		}
        return xmlString;
	}
	
	private static final Log _log = LogFactoryUtil.getLog(SOAPConverter.class);
}
