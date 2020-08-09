package com.revseev.batch.processor;

import com.revseev.batch.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component("itemProcessor")
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {
        String capitalizedFirstName = StringUtils.capitalize(person.getFirstName().toLowerCase());
        String capitalizedLastName = StringUtils.capitalize(person.getLastName().toLowerCase());
        // меняем поля в том же объекте, но могли бы создавать новый как в книге (но зачем?)
        person.setFirstName(capitalizedFirstName);
        person.setLastName(capitalizedLastName);
        log.info("Processing person #{}", person.getPersonId());
        return person;
    }
}
