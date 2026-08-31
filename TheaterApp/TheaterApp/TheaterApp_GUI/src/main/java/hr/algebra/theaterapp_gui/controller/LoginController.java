package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.User;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.repository.UserRepository;
import hr.algebra.theaterapp_gui.TheaterApp;
import hr.algebra.theaterapp_gui.session.UserSession;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import hr.algebra.theaterapp_gui.util.SceneUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    private final UserRepository userRepository = RepositoryFactory.getUserRepository();
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            Optional<User> authenticatedUser = userRepository.authenticate(username, password);

            if (authenticatedUser.isPresent()) {
                Stage stage = (Stage) loginButton
                        .getScene()
                        .getWindow();

                UserSession.login(authenticatedUser.get());

                SceneUtil.loadScene(TheaterApp.class.getResource("/fxml/main.fxml"), stage, "Theater Management App");

                return;
            }

            AlertUtil.showError("Login failed", "Invalid username or password.");

            passwordField.clear();
            passwordField.requestFocus();

        } catch (SQLException e) {
            AlertUtil.showError("Login error", "Unable to connect to the database.");

            e.printStackTrace();
        }
    }
}