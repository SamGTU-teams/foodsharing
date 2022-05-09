package ru.rassafel.bot.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class From {

    private String username;
    private String phone;
    private Long id;
}
