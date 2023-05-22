module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}


