package ru.rassafel.foodsharing.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LuceneObject implements Serializable {
    private String id;
    private String body;
}
