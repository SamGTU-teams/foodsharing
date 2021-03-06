package ru.rassafel.foodsharing.session.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.session.util.button.ButtonsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BotButtons {
    private List<BotButton> buttons = new ArrayList<>();

    public BotButtons() {
        buttons.add(new BotButton(ButtonsUtil.BACK_TO_MAIN_MENU));
    }

    public BotButtons(List<String> startWith) {
        addAll(startWith);
    }

    public BotButtons addButton(BotButton button) {
        buttons.add(button);
        return this;
    }

    public BotButtons addAll(List<String> textButtons) {
        buttons.addAll(textButtons.stream()
            .map(BotButton::new)
            .collect(Collectors.toList()));
        return this;
    }

    @Data
    public static class BotButton {
        public static BotButton GEO_BUTTON = new BotButton();
        private String text;
        private boolean geo;

        public BotButton(String text) {
            this.text = text;
        }

        private BotButton() {
            geo = true;
        }
    }
}
