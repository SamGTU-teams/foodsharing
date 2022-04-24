package ru.rassafel.bot.session.model.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedUserSession {
    @Transient
    public static final EmbeddedUserSession EMPTY = new EmbeddedUserSession();

    private String sessionName = "";
    private Integer sessionStep = 0;
    private boolean sessionActive = false;

    public void incrementStep() {
        sessionStep += 1;
    }

    public void decrementStep() {
        sessionStep -= 1;
    }
}
