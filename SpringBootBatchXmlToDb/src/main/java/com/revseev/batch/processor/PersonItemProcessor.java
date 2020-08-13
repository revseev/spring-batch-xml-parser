package com.revseev.batch.processor;

import com.revseev.batch.model.PersonType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component("itemProcessor")
public class PersonItemProcessor implements ItemProcessor<PersonType, PersonType> {

    @Override
    public PersonType process(PersonType personType) throws Exception {
        String capitalizedFirstName = StringUtils.capitalize(personType.getFirstName().toLowerCase());
        String capitalizedLastName = StringUtils.capitalize(personType.getLastName().toLowerCase());
        // меняем поля в том же объекте, но могли бы создавать новый как в книге (но зачем?)
        personType.setFirstName(capitalizedFirstName);
        personType.setLastName(capitalizedLastName);
        log.debug("Processing personType #{}", personType.getPersonId());
        return personType;
    }
}
