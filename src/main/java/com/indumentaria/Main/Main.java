package com.indumentaria.Main;

import com.indumentaria.DAO.DomicilioDAO;
import com.indumentaria.DAO.GenericDAO;
import com.indumentaria.Models.Domicilio;
import com.indumentaria.services.DomicilioServiceImpl;
import com.indumentaria.services.GenericService;

public class Main {
    public static void main(String[] args) {

        // creo el DomiciloDAO
        GenericDAO<Domicilio> domicilioDAO= new DomicilioDAO();

        // creo el servicioDAO
        GenericService<Domicilio> domicilioGenericService = new DomicilioServiceImpl(domicilioDAO);

        try{
            Domicilio domicilio =  new Domicilio(
                    "miguel cane",
                    "123"
            );
            domicilioGenericService.insertar(domicilio);
            System.out.println("el domicilio se guardo corrrectamente");
        }catch (Exception e){
System.err.println("Error al guardar producto" + e.getMessage());
        }

    }
}