package ru.rassafel.bot.session.mapper;

import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class OrikaMapper {

    @Autowired
    protected MapperFactory factory;

    @PostConstruct
    public abstract void configure();

}
