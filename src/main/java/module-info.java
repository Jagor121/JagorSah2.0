module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}


