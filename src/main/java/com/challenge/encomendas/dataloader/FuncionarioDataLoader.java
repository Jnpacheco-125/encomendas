package com.challenge.encomendas.dataloader;

import com.challenge.encomendas.encomendas.domain.entities.Funcionario;
import com.challenge.encomendas.encomendas.domain.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioDataLoader implements CommandLineRunner {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um funcionário, se não, cria um novo
        if (funcionarioRepository.findByEmail("teste@teste.com").isEmpty()) {
            // Criação do funcionário com a senha criptografada
            Funcionario funcionario = new Funcionario();
            funcionario.setEmail("teste@teste.com");
            funcionario.setNome("Funcionário Teste");
            funcionario.setSenha(passwordEncoder.encode("123456"));  // Senha criptografada

            // Salva o funcionário no banco de dados
            funcionarioRepository.save(funcionario);

            System.out.println("Funcionário de teste criado com sucesso.");
        } else {
            System.out.println("Funcionário de teste já existe no banco.");
        }
    }
}
