package com.example.seguridad6.controller;

import com.example.seguridad6.dto.JwtResponse;
import com.example.seguridad6.dto.LoginRequest;
import com.example.seguridad6.dto.SignUpRequest;
import com.example.seguridad6.entity.Rol;
import com.example.seguridad6.entity.Usuario;
import com.example.seguridad6.enums.RolNombre;
import com.example.seguridad6.repository.RolRepository;
import com.example.seguridad6.repository.UsuarioRepository;
import com.example.seguridad6.seguridad.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtProvider.generateToken(request.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    // 🔹 REGISTRO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: el nombre de usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // asignación de roles
        Set<Rol> rolesAsignar = new HashSet<>();

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Rol rolUser = rolRepository.findByNombre(RolNombre.ROLE_USER)
                    .orElseThrow();
            rolesAsignar.add(rolUser);
        } else {
            request.getRoles().forEach(r -> {
                if (r.equalsIgnoreCase("admin")) {
                    rolesAsignar.add(
                            rolRepository.findByNombre(RolNombre.ROLE_ADMIN)
                                    .orElseThrow()
                    );
                } else {
                    rolesAsignar.add(
                            rolRepository.findByNombre(RolNombre.ROLE_USER)
                                    .orElseThrow()
                    );
                }
            });
        }

        usuario.setRoles(rolesAsignar);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
