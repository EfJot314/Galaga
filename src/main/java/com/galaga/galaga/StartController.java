package com.galaga.galaga;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class StartController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onStartButtonClick() throws IOException {
        App.startGame();
    }
}