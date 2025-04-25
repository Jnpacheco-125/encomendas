package com.challenge.encomendas.dataloader;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.domain.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MoradorDataLoader implements CommandLineRunner {
    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um morador com o e-mail de teste, se não, cria um novo
        if (moradorRepository.findByEmail("morador.teste@email.com").isEmpty()) {
            // Criação do morador com a senha criptografada
            Morador morador = new Morador();
            morador.setNome("Morador Teste");
            morador.setEmail("morador.teste@email.com");
            morador.setTelefone("86999887766");
            morador.setApartamento("202B");
            morador.setSenha(passwordEncoder.encode("senha123")); // Senha criptografada

            // Salva o morador no banco de dados
            moradorRepository.save(morador);

            System.out.println("Morador de teste criado com sucesso.");
        } else {
            System.out.println("Morador de teste já existe no banco.");
        }
    }
}
