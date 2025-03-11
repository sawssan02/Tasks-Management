package Presentation.models;
import Presentation.views.LoginView;
import Service.ServiceLogin;

import java.io.IOException;

public class LoginModel {
    private ServiceLogin serviceLogin;
    private LoginView view;
    public LoginModel() {
        this.serviceLogin = new ServiceLogin();
    }
    public boolean verifyEmail(String email) throws IOException {
        return serviceLogin.verifyEmail(email);
    }
    public void handleLogin(String email) {
        try {
            boolean isValid = verifyEmail(email);
            if (isValid) {
                view.updateStatus("Email is valid.");
            } else {
                view.updateStatus("Email is not valid.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            view.updateStatus("Error");
        }
    }
}