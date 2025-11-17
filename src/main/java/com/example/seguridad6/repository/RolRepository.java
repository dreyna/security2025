package com.example.seguridad6.repository;

import com.example.seguridad6.entity.Rol;
import com.example.seguridad6.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(RolNombre nombre);
}