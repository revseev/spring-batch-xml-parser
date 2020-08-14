package com.revseev.batch.listeners;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomItemWriteListener implements ItemWriteListener {

    @Override
    public void beforeWrite(List list) {
        list.forEach(System.out::println);
    }

    @Override
    public void afterWrite(List list) {
        list.forEach(System.out::println);
    }

    @Override
    public void onWriteError(Exception e, List list) {

    }
}
