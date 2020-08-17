package com.revseev.batch.config;

import lombok.SneakyThrows;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.springframework.oxm.GenericUnmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.eclipse.persistence.jaxb.JAXBContextFactory.*;

public class CustomUnmarshaller<T> implements GenericUnmarshaller {

    private javax.xml.bind.Unmarshaller unmarshaller;

    private Class<T> clazz;

    @SneakyThrows
    public CustomUnmarshaller(Class<T> clazz) {
        this.clazz = clazz;
        this.unmarshaller = createContext(new Class[]{clazz}, null).createUnmarshaller();
    }


    @Override
    public boolean supports(Type type) {
        return true;
    }

    //todo сделать нормально
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object unmarshal(Source source) throws IOException, XmlMappingException {
        return unmarshaller.unmarshal(source);
    }
}
