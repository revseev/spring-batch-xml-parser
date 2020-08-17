package com.revseev.batch.config;

import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.IOException;

public class XMLHelper {
    private Jaxb2Marshaller marshaller;

    /**
     * Unmarshal a given source
     */
    public Object load(Source source) throws XmlMappingException, IOException {
        return marshaller.unmarshal(source);
    }

    /**
     * Marshal a given Object to a Result
     */
    public void save(Object obj, Result result) throws XmlMappingException, IOException {
        marshaller.marshal(obj, result);
    }

    /**
     * This method is used by Spring to inject an instance of Jaxb2Marshaller
     */
    public void setMarshaller(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }
}
