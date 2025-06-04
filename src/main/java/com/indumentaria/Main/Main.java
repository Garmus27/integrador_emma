package com.indumentaria.Main;

import com.indumentaria.DAO.DomicilioDAO;
import com.indumentaria.DAO.PersonaDAO;
import com.indumentaria.Models.Domicilio;
import com.indumentaria.Models.Persona;
import com.indumentaria.services.DomicilioServiceImpl;
import com.indumentaria.services.GenericService;
import com.indumentaria.services.PersonaServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // creamos DomicilioDAO
        DomicilioDAO domicilioDAO = new DomicilioDAO();
        // creamos DomicilioService
        DomicilioServiceImpl domicilioGenericService = new DomicilioServiceImpl(domicilioDAO);
        //creamos PersonaDAO
        PersonaDAO personaDAO = new PersonaDAO(domicilioDAO);
        //creamos PersonaService
        PersonaServiceImpl personaService = new PersonaServiceImpl(personaDAO, domicilioGenericService);
        try {
            /* Instanciar y potencialmente insertar un Domicilio
            Domicilio domicilio = Domicilio.builder()
                    .calle("cochabamba")
                    .numero("573")
                    .build();

            domicilioGenericService.insertar(domicilio); // Usamos insertar simple para que el domicilio tenga un ID antes de pasarlo a la persona.
            //tiramos un mensaje por si falla, saber que aca es donde fallo
            System.out.println("el domicilio se guardo correctamente con ID: " + domicilio.getId());

            Persona persona = Persona.builder()
                    .nombre("Armando Esteban")
                    .apellido("Quito")
                    .dni("485261523")
                    .domicilio(domicilio)
                    .build();



            // Insertar Persona (usando insertTx para la integridad transaccional)
            personaService.insertar(persona);
            System.out.println("La persona se guardó correctamente con ID: " + persona.getId());

            //Creamos otro domicilio que no insertaremos con su servicio sino que lo insertaremos con persona
            Domicilio dom2 = Domicilio.builder()
                    .calle("Belgrano")
                    .numero("1731")
                    .build();

            // Insertamos una persona
            Persona persona2 = Persona.builder()
                    .nombre("Matias")
                    .apellido("Lampone")
                    .dni("52156987")
                    .domicilio(dom2)
                    .build();

            personaService.insertar(persona2);*/
            List<Persona> lista = personaService.getAll();
            lista.forEach(System.out::println);

            Persona persona = personaService.getById(17);
            System.out.println(persona);



        } catch (Exception e) {
            System.err.println("Error en la operación: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace completo para depuración
        }
    }
}