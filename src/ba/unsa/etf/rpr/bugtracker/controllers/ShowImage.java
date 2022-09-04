package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowImage extends AbstractController implements Showable, Initializable {
    private String imageUrl = "";
    private Details parentController;
    public ImageView imageView;

    public ShowImage(Details parentController, String imageUrl) {
        this.parentController = parentController;
        this.imageUrl = imageUrl;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setImage(new Image(new File(imageUrl).toURI().toString()));
    }
}
