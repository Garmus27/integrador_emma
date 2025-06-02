package com.indumentaria.Models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private Domicilio domicilio;


}