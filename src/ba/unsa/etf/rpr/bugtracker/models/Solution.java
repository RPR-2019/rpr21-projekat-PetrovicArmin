package ba.unsa.etf.rpr.bugtracker.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class Solution implements Serializable {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty description = new SimpleStringProperty();
    SimpleObjectProperty<LocalDate> datePosted = new SimpleObjectProperty<>();
    SimpleStringProperty code = new SimpleStringProperty();
    SimpleStringProperty imageUrl = new SimpleStringProperty();
    SimpleObjectProperty<User> userWhoSolved = new SimpleObjectProperty<>();

    public Solution(SimpleIntegerProperty id, SimpleStringProperty description, SimpleObjectProperty<LocalDate> datePosted, SimpleStringProperty code, SimpleStringProperty imageUrl, SimpleObjectProperty<User> userWhoSolved) {
        this.id = id;
        this.description = description;
        this.datePosted = datePosted;
        this.code = code;
        this.imageUrl = imageUrl;
        this.userWhoSolved = userWhoSolved;
    }

    public Solution(int id, String description, LocalDate datePosted, String code, String imageUrl, User userWhoSolved) {
        setId(id);
        setDescription(description);
        setDatePosted(datePosted);
        setCode(code);
        setImageUrl(imageUrl);
        setUserWhoSolved(userWhoSolved);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDate getDatePosted() {
        return datePosted.get();
    }

    public SimpleObjectProperty<LocalDate> datePostedProperty() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted.set(datePosted);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public SimpleStringProperty imageUrlProperty() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }

    public User getUserWhoSolved() {
        return userWhoSolved.get();
    }

    public SimpleObjectProperty<User> userWhoSolvedProperty() {
        return userWhoSolved;
    }

    public void setUserWhoSolved(User userWhoSolved) {
        this.userWhoSolved.set(userWhoSolved);
    }
}
