/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package servicios;

import clase.PspUsers;
import controllerDao.PspUsersJpaController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * REST Web Service
 *
 * @author eetxa
 */
@Path("/servicios")
public class Servicios {

    @Context
    private UriInfo context;

    public Servicios() {

    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response post(@FormParam("nombre") String nombre,
            @FormParam("email") String email,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("confirmar-password") String confirmarPassword
    ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ApiRestPU");

        PspUsersJpaController dao = new PspUsersJpaController(emf);
        String html;
        Response response;
        Response.Status status;

        try {
            // Generación del token
            UUID uuid = UUID.randomUUID();
            BigInteger mostSigBits = BigInteger.valueOf(uuid.getMostSignificantBits());
            BigInteger leastSigBits = BigInteger.valueOf(uuid.getLeastSignificantBits());
            BigInteger token = mostSigBits.shiftLeft(64).or(leastSigBits).abs(); // Asegurar que sea positivo

            // Expiracion
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(2);
            ZoneId zoneId = ZoneId.systemDefault(); 
            Date date = Date.from(expiryDate.atZone(zoneId).toInstant());

            // Activacion a false
            boolean activo = false;
            //Encripta contraseña
            String hashContraseña = Encriptacion.encriptar(password);

            PspUsers user = new PspUsers(username, nombre, email, hashContraseña, activo, token, date);
            //INSERT
            dao.create(user);

            html = "<html><body>"
                    + "<h1>Formulario recibido</h1>"
                    + "<p>Nombre: " + nombre + "</p>"
                    + "<p>Email: " + email + "</p>"
                    + "<p>Username: " + username + "</p>"
                    + "<p>Usuario registrado exitosamente. Consulte su correo para validacion.</p>"
                    + "</body></html>";

            //ENVIO el mail con el token modificado.
            EmailService.enviarCorreo(token.toString(), email);  

            return Response.status(Response.Status.CREATED).entity(html).build();

        } catch (Exception ex) {
            ex.printStackTrace(); // Para depuración
            status = Response.Status.INTERNAL_SERVER_ERROR;

            // Generar HTML para el mensaje de error
            String htmlError = "<html><body>"
                    + "<h1>Error al procesar la solicitud</h1>"
                    + "<p>Detalles del error: " + ex.getMessage() + "</p>"
                    + "</body></html>";

            response = Response.status(status).entity(htmlError).build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @GET
    @Path("/validar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activarUsuario(@QueryParam("token") BigInteger token) {
   
        EntityManagerFactory emf = null;
        EntityManager em = null;
        Response response;
        Response.Status status;
        HashMap<String, String> mensaje = new HashMap();

        try {
            emf = Persistence.createEntityManagerFactory("ApiRestPU");
            em = emf.createEntityManager();
            PspUsersJpaController dao = new PspUsersJpaController(emf);

            // Verificar que el token no esté vacío
            if (token == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El token es necesario para la activación.")
                        .build();
            }

            // Buscar el usuario por el token en la base de datos.
            PspUsers usuario = dao.findPspUsertoken(token);

            // Si el usuario no existe o el token es inválido
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Token de activación no válido o expirado.")
                        .build();
            }
            // Verificar si el token ha expirado
            if (usuario.getActivationExpiryDate() != null) {
                Date fechaActual = new Date();
                if (usuario.getActivationExpiryDate().before(fechaActual)) {
                    return Response.status(Response.Status.GONE) // Código 410: "Gone" indica que un recurso ya no está disponible
                            .entity("El token de activación ha expirado.")
                            .build();
                }
            }
            // Activar al usuario
            em.getTransaction().begin(); // Iniciar la transacción
            usuario.setIsActive(true);   // Se activa el usuario
            em.merge(usuario);           // Guardar cambios
            em.getTransaction().commit(); // Confirmar la transacción

            return Response.status(Response.Status.OK)
                    .entity("Usuario activado correctamente.")
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            status = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la peticion" + ex.getMessage());
            response = Response.status(status).entity(mensaje).build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

}
