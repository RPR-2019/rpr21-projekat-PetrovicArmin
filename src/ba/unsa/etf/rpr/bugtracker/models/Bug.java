package ba.unsa.etf.rpr.bugtracker.models;

import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

abstract public class Bug implements Serializable {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleObjectProperty<Language> language = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Urgency> urgency = new SimpleObjectProperty<>();
    private SimpleStringProperty keywords = new SimpleStringProperty();
    private SimpleStringProperty code = new SimpleStringProperty();

    private SimpleStringProperty imageUrl = new SimpleStringProperty();

    public Bug() {}

    abstract public User getUserWhoAsked();
    abstract public LocalDate getDatePosted();

    public Bug(SimpleIntegerProperty id, SimpleStringProperty title, SimpleStringProperty description, SimpleObjectProperty<Language> language, SimpleObjectProperty<Urgency> urgency, SimpleStringProperty keywords, SimpleStringProperty code, SimpleStringProperty imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.language = language;
        this.urgency = urgency;
        this.keywords = keywords;
        this.code = code;
        this.imageUrl = imageUrl;
    }

    public Bug(int id, String title, String description, Language language, Urgency urgency, String keywords, String code, String imageUrl) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLanguage(language);
        setUrgency(urgency);
        setKeywords(keywords);
        setCode(code);
        setImageUrl(imageUrl);
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

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
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

    public Language getLanguage() {
        return language.get();
    }

    public SimpleObjectProperty<Language> languageProperty() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language.set(language);
    }

    public Urgency getUrgency() {
        return urgency.get();
    }

    public SimpleObjectProperty<Urgency> urgencyProperty() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency.set(urgency);
    }

    public String getKeywords() {
        return keywords.get();
    }

    public SimpleStringProperty keywordsProperty() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords.set(keywords);
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
}
