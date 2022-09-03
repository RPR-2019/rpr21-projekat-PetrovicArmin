package ba.unsa.etf.rpr.bugtracker.models;

import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class ActiveBug extends Bug implements Serializable {
    private SimpleObjectProperty<User> userWhoAsked = new SimpleObjectProperty<>();
    private SimpleObjectProperty<LocalDate> datePosted = new SimpleObjectProperty<>();

    public ActiveBug() {}

    public ActiveBug(SimpleIntegerProperty id, SimpleStringProperty title, SimpleStringProperty description, SimpleObjectProperty<Language> language, SimpleObjectProperty<Urgency> urgency, SimpleStringProperty keywords, SimpleStringProperty code, SimpleStringProperty imageUrl, SimpleObjectProperty<User> userWhoAsked, SimpleObjectProperty<LocalDate> datePosted) {
        super(id, title, description, language, urgency, keywords, code, imageUrl);
        this.userWhoAsked = userWhoAsked;
        this.datePosted = datePosted;
    }

    public ActiveBug(int id, String title, String description, Language language, Urgency urgency, String keywords, String code, String imageUrl, User whoAsked, LocalDate datePosted) {
        super(id, title, description, language, urgency, keywords, code, imageUrl);
        setUserWhoAsked(whoAsked);
        setDatePosted(datePosted);
    }

    public User getUserWhoAsked() {
        return userWhoAsked.get();
    }

    public SimpleObjectProperty<User> userWhoAskedProperty() {
        return userWhoAsked;
    }

    public void setUserWhoAsked(User userWhoAsked) {
        this.userWhoAsked.set(userWhoAsked);
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
}
