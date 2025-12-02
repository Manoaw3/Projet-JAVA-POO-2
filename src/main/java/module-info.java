module com.example.cesibike {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.cesibike to javafx.fxml;

    exports Controller;
    exports Service;
    exports Model;

    opens Controller to javafx.fxml;

    exports com.example.cesibike;
}