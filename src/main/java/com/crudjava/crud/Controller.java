package com.crudjava.crud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teste")
@CrossOrigin
public class Controller {
    final JdbcTemplate jdbcTemplate;

    public Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/listarTodos")
    public List<Usuario> listarTodos() {
        System.out.println("BBBBBBBBBBB");
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
        return jdbcTemplate.queryForObject("select id,nome, cpf, to_char(data_nascimento, 'yyyy-MM-DD') as data_nascimento, senha from usuario where id = ?", new Object[] {id}, (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setCpf(rs.getString("cpf"));
            u.setDataNascimento(LocalDate.parse(rs.getString("data_nascimento")));
            return u;
        });
    }

    @PostMapping("/novo")
    public String novo(@RequestBody Usuario usuario) throws JsonProcessingException {
        System.out.println("AAAAAAAAAAAAAAAAAAA");
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(usuario.generateSenha().getBytes("utf-8"));
            usuario.setSenha(String.format("%040x", new BigInteger(1, digest.digest())));
            String novo = jdbcTemplate.queryForObject("select criar_usuario(?::jsonb);", String.class, objectMapper.writeValueAsString(usuario));
            if(novo.equals("f")){
                return "ERRO";
            }
            return objectMapper.writeValueAsString(usuario);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "ERRO";
    }

    @PostMapping("/alterar")
    public String alterar(@RequestBody Usuario usuario) {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(usuario.generateSenha().getBytes("utf-8"));
            usuario.setSenha(String.format("%040x", new BigInteger(1, digest.digest())));
            String novo = jdbcTemplate.queryForObject("select alterar_usuario(?::jsonb);", String.class, objectMapper.writeValueAsString(usuario));
            if(novo.equals("f")){
                return "ERRO";
            }
            return objectMapper.writeValueAsString(usuario);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "ERRO";
    }

    @PostMapping("/remover")
    public String delete(@RequestBody String usuario) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        jdbcTemplate.queryForObject("select remover_usuario(?);", String.class, usuario);
        return "{\"message\":\"ok\"}";
    }
}
