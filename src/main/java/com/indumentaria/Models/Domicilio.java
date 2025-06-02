package com.indumentaria.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Domicilio {
    private int id;
    private String calle;
    private String numero;

    public Domicilio(String calle, String numero) {
        this.calle = calle;
        this.numero = numero;
    }

    public Domicilio() {

    }
}