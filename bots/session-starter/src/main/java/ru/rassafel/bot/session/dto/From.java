package ru.rassafel.bot.session.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class From {

    private String username;
    private String phone;
    private Long id;
}
