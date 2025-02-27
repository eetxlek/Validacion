/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author eetxa
 */
public class Encriptacion {

    static String encriptar(String password) {
        String encriptado = "";
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-1");
            m.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hashPassword = m.digest();
            encriptado = toHexadecimal(hashPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encriptado.substring(0, 20);
    }

    static String toHexadecimal(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02X", b)); 
        }
        return hex.toString();
    }

}
