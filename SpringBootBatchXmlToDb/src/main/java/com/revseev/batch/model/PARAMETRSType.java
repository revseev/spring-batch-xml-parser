package com.revseev.batch.model;

import lombok.Data;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.lang.reflect.Field;

@Data
@XmlRootElement(name = "PARAMETRS")
@XmlAccessorType(XmlAccessType.FIELD)
public class PARAMETRSType {

    @XmlAttribute(name = "OBJCODE")
    private String objCode;

    @XmlPath("attr/name/text()")
    private String name;
}
