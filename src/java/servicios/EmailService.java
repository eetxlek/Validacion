/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author eetxa
 */
public class EmailService {

    private static String html;

    public static String getHtml() {
        return html;
    }

    public static synchronized void setHtml(String html) {
        EmailService.html = html;
    }

    private static Properties cargarPropiedades() {
   
        Properties props = new Properties();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("jakartamail.properties")) {
            if (input == null) {
                throw new RuntimeException("Archivo properties no encontrado");
            }
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Propiedades cargadas: " + props.keySet());
        return props;

    }

    private static void cargarHtml() {

    try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("activationEmail.html")) {
        if (input == null) {
            throw new RuntimeException("Archivo HTML no encontrado en el classpath");
        }
        setHtml(new String(input.readAllBytes(), StandardCharsets.UTF_8));
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public static void cambiarToken(String validationTokenBigInt) {
        String nuevaUrl = "http://localhost:8080/activar/api/servicios/validar?token=%%TOKEN%%";
        html = html.replace("http://localhost:8080/activar?token=%%TOKEN%%", nuevaUrl);
        //sustituye el token
        html = html.replace("%%TOKEN%%", String.valueOf(validationTokenBigInt));

    }

    public static boolean enviarCorreo(String token, String usuario) {
        //Cargar propiedades del archivo. Para configurar y validar el correo
        Properties props = cargarPropiedades();

        String host = props.getProperty("mail.smtp.host");
        String usuarioAuth = props.getProperty("mail.username");
        String contraseñaAuth = props.getProperty("mail.password");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioAuth, contraseñaAuth);
            }
        });

        cargarHtml();
        cambiarToken(token);

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(usuarioAuth));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuario));
            mensaje.setSubject("Activacion cuenta");
            cambiarToken(token);
            mensaje.setContent(html, "text/html; charset=utf-8");

            Transport.send(mensaje);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
