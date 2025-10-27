/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author tasniafarinifa
 */
public class UserAuthentication {
        // giant class that handles registration, login, password reset, 2FA, session storage, email
    private Map<String, String> userPasswords = new HashMap<>();
    private Map<String, String> sessions = new HashMap<>();
    private Map<String, String> resetTokens = new HashMap<>();
    private Map<String, String> twoFactorSecrets = new HashMap<>();

    public void register(Customer user, String password) {
        // password stored in plain text (security issue) - also violates DIP because callers expect concrete class
        userPasswords.put(user.getEmail(), password);
        // send welcome email directly
        sendWelcomeEmail(user.getEmail());
    }

    public boolean login(String email, String password) {
        String actual = userPasswords.get(email);
        if (actual != null && actual.equals(password)) {
            String sessionId = "SID-" + UUID.randomUUID().toString();
            sessions.put(sessionId, email);
            return true;
        }
        return false;
    }

    public String requestPasswordReset(String email) {
        String token = UUID.randomUUID().toString();
        resetTokens.put(email, token);
        sendResetEmail(email, token);
        return token;
    }

    public boolean verifyTwoFactor(String email, String code) {
        String secret = twoFactorSecrets.get(email);
        if (secret == null) return false;
        // naive check
        return code.equals(secret);
    }

    private void sendWelcomeEmail(String email) {
        System.out.println("Sending welcome email to " + email);
    }

    private void sendResetEmail(String email, String token) {
        System.out.println("Sending reset token " + token + " to " + email);
    }
    
}
