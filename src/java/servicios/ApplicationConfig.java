/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import java.util.Set;


@jakarta.ws.rs.ApplicationPath("/api")
public class ApplicationConfig extends jakarta.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

 
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Servicios.class);
    }
    
}
