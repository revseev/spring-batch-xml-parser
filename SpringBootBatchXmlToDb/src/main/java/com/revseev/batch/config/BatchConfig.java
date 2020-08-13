package com.revseev.batch.config;

import com.revseev.batch.model.PersonType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private ItemProcessor<PersonType, PersonType> itemProcessor;

    @Autowired
    private StepExecutionListener stepExecutionListener;

    @Autowired
    private ParsingResourceProvider resourceProvider;

    @Bean
    public StaxEventItemReader<PersonType> reader() {
        StaxEventItemReader<PersonType> reader = new StaxEventItemReader<>();
        Resource resource = resourceProvider.provide();
        log.info("=========>>>> " + resource.getFilename());
        reader.setResource(resource);
        reader.setFragmentRootElementName("person");

        Map<String, String> aliasesMap = new HashMap<>();
        aliasesMap.put("person", "com.revseev.batch.model.PersonType");
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliasesMap);

        reader.setUnmarshaller(marshaller);
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<PersonType> writer() {
        JdbcBatchItemWriter<PersonType> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO person(person_id,first_name,last_name,email,age) VALUES(?,?,?,?,?)");
        //Another way:
        /*itemWriter.setItemSqlParameterSourceProvider(
          new BeanPropertyItemSqlParameterSourceProvider<>());
          itemWriter.setSql(
          "INSERT INTO singer (first_name, last_name, song) VALUES (:firstName, :lastName, :song)");*/
        writer.setItemPreparedStatementSetter(new PersonPreparedStatementSetter());
        return writer;
    }

    @Bean
    public Step step1(ItemReader<PersonType> reader,
                      ItemProcessor<PersonType, PersonType> processor,
                      ItemWriter<PersonType> writer) {
        return stepBuilderFactory.get("step1")
                .<PersonType, PersonType>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(stepExecutionListener)
                .build();
    }

    @Bean
    public Job exportPersonJob(@Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("importPersonJob2")
                                .incrementer(new RunIdIncrementer())
                                .flow(step1)
                                .end()
                                .build();
    }
}
