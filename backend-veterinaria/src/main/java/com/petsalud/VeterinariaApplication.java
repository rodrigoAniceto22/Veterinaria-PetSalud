package com.petsalud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VeterinariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(VeterinariaApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Veterinaria PetSalud - Sistema Iniciado");
        System.out.println("  Puerto: 8080");
        System.out.println("==============================================");
    }
}
