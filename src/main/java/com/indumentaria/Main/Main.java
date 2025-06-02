package com.indumentaria.Main;

import com.indumentaria.DAO.DomicilioDAO;
import com.indumentaria.DAO.PersonaDAO;
import com.indumentaria.Models.Domicilio;
import com.indumentaria.Models.Persona;
import com.indumentaria.services.DomicilioServiceImpl;
import com.indumentaria.services.GenericService;
import com.indumentaria.services.PersonaServiceImpl;

public class Main {
    public static void main(String[] args) {

        // crear DomicilioDAO
        DomicilioDAO domicilioDAO = new DomicilioDAO();

        // crear DomicilioService
        GenericService<Domicilio> domicilioGenericService = new DomicilioServiceImpl(domicilioDAO);

        try {
            // Instanciar y potencialmente insertar un Domicilio
            Domicilio domicilio = new Domicilio(
                    "miguel cane",
                    "123"
            );

            // Es mejor insertar el domicilio a través de su servicio, preferiblemente con Tx si la necesitas
            // Para este ejemplo, lo insertaremos con el método insertar normal primero para que tenga un ID.
            // Si quieres que el domicilio sea insertado dentro de la misma transacción que la persona,
            // entonces NO lo insertes aquí, sino deja que PersonaServiceImpl.insertarTx lo haga.
            // Pero si el domicilio ya existe en la DB, solo necesitas su ID.
            domicilioGenericService.insertar(domicilio); // Usamos insertar simple para que el domicilio tenga un ID antes de pasarlo a la persona.
            // Si quieres que el domicilio se gestione dentro de la transacción de la persona,
            // entonces esta línea NO debería ir, y el domicilio.setId(0) o un new Domicilio()
            // sin ID inicial debería usarse.
            System.out.println("el domicilio se guardo correctamente con ID: " + domicilio.getId());

            // Crear PersonaDAO y PersonaService
            PersonaDAO personaDAO = new PersonaDAO(domicilioDAO);
            GenericService<Persona> personaService = new PersonaServiceImpl(personaDAO, domicilioDAO);

            // Crear Persona con el Domicilio asociado
            Persona persona = new Persona();
            persona.setNombre("Juan");
            persona.setApellido("Perez");
            persona.setDni("12345678");
            persona.setDomicilio(domicilio); // Asignar el Domicilio (que ya tiene un ID si la línea anterior se ejecutó)

            // Insertar Persona (usando insertTx para la integridad transaccional)
            personaService.insertarTx(persona);
            System.out.println("La persona se guardó correctamente con ID: " + persona.getId());

        } catch (Exception e) {
            System.err.println("Error en la operación: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace completo para depuración
        }
    }
}