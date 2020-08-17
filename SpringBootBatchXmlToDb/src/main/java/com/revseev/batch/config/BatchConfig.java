package com.revseev.batch.config;

import com.revseev.batch.listeners.CustomItemWriteListener;
import com.revseev.batch.model.PARAMETRSType;
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
    private CustomItemWriteListener itemWriteListener;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private StepExecutionListener stepExecutionListener;

    @Autowired
    private ParsingResourceProvider resourceProvider;

    @Bean
    public StaxEventItemReader<PARAMETRSType> reader() {
        StaxEventItemReader<PARAMETRSType> reader = new StaxEventItemReader<>();
        Resource resource = resourceProvider.provide();
        log.info("=========>>>> " + resource.getFilename());
        reader.setResource(resource);
        reader.setFragmentRootElementName("PARAMETRS");

/*        Map<String, String> aliasesMap = new HashMap<>();
        aliasesMap.put("PARAMETRS", "com.revseev.batch.model.PARAMETRSType");
        aliasesMap.put("OBJCODE", "java.lang.String");
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliasesMap);
        marshaller.getXStream().ignoreUnknownElements();*/

        CustomUnmarshaller<PARAMETRSType> parametrsTypeCustomUnmarshaller = new CustomUnmarshaller<>(PARAMETRSType.class);

        reader.setUnmarshaller(parametrsTypeCustomUnmarshaller);
        return reader;
    }

    @Bean
    public ItemProcessor<PARAMETRSType, String> processor() {
        return item -> {
            System.out.println("In processor...");
            return item.getObjCode();
        };
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            System.out.println("In writer....");
            items.forEach(System.out::println);
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<PARAMETRSType, String >chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .listener(stepExecutionListener)
                .listener(itemWriteListener)
                .build();
    }

    @Bean
    public Job exportPersonJob(@Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("importPersonJob3")
                                .incrementer(new RunIdIncrementer())
                                .flow(step1)
                                .end()
                                .build();
    }
}
