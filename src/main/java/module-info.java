module com.galaga.galaga {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.galaga.galaga to javafx.fxml;
    exports com.galaga.galaga;
}