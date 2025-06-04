package com.indumentaria.Models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private Domicilio domicilio;



}