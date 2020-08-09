package com.infotech.batch.config;

import com.infotech.batch.model.Person;
import com.infotech.batch.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public StaxEventItemReader<Person> reader() {
        StaxEventItemReader<Person> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("persons.xml"));
        reader.setFragmentRootElementName("person");

        Map<String, String> aliasesMap = new HashMap<>();
        aliasesMap.put("person", "com.infotech.batch.model.Person");
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliasesMap);

        reader.setUnmarshaller(marshaller);
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO person(person_id,first_name,last_name,email,age) VALUES(?,?,?,?,?)");
        writer.setItemPreparedStatementSetter(new PersonPreparedStatementSetter());
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job exportPersonJob() {
        return jobBuilderFactory.get("importPersonJob")
                                .incrementer(new RunIdIncrementer())
                                .flow(step1())
                                .end()
                                .build();
    }
}
