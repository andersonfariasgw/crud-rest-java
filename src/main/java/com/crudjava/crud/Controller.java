package com.crudjava.crud;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teste")
public class Controller {
    final JdbcTemplate jdbcTemplate;

    public Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/listarTodos")
    public List<Usuario> listarTodos() {
        Map<String, Object> mapa = new HashMap<>();

        return jdbcTemplate.query("select * from usuario", (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setCpf(rs.getString("cpf"));
            return u;
        });
    }

    @GetMapping("/carregar/{id}")
    public Usuario carregar(@PathVariable("id") int id) {
        return jdbcTemplate.queryForObject("select * from usuario where id = ?", new Object[] {id}, (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setCpf(rs.getString("cpf"));
            return u;
        });
    }

    @PostMapping("/novo")
    public String novo(@RequestBody String usuario) {
        return jdbcTemplate.queryForObject("select criar_usuario(?::jsonb);", String.class, usuario);

    }

    @PostMapping("/alterar")
    public String alterar(@RequestBody String usuario) {
        return jdbcTemplate.queryForObject("select alterar_usuario(?::jsonb);", String.class, usuario);
    }

    @PostMapping("/remover")
    public String delete(@RequestBody String usuario) {
        return jdbcTemplate.queryForObject("select remover_usuario(?::jsonb);", String.class, usuario);

    }
}
