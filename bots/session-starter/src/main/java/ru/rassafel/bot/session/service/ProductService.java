package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    public List<String> getSimilarProducts(String product){
        ArrayList<String> objects = new ArrayList<>();
        objects.add("Молоко");
        objects.add("Кефир");
        objects.add("Яблоко");
        return objects;
    }

}
