module ch.bbw.jh.helloworldfx {
	requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
	requires transitive javafx.fxml;

    opens ch.bbw.jh.test to javafx.fxml;
    exports ch.bbw.jh.test;
}