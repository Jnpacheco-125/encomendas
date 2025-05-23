package com.challenge.encomendas.encomendas.usecase.cadastro;

import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.AtualizarMoradorDTO;
import com.challenge.encomendas.encomendas.adapters.controllers.dto.moradores.CadastroMoradorDTO;
import com.challenge.encomendas.encomendas.adapters.gateways.MoradorGateway;
import com.challenge.encomendas.encomendas.domain.entities.Morador;
import com.challenge.encomendas.encomendas.domain.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class MoradorServiceTest {
    @Mock
    private MoradorGateway moradorGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MoradorService moradorService;

    @Test
    public void deveCadastrarMoradorComSucesso() {
        CadastroMoradorDTO dto = new CadastroMoradorDTO(
                "João",
                "86999775533",
                "Apt 101",
                "joao@email.com",
                "12345",
                Role.ROLE_MORADOR // Esse parâmetro estava faltando antes
        );

        Morador morador = new Morador();
        morador.setNome(dto.nome());
        morador.setEmail(dto.email());
        morador.setTelefone(dto.telefone());
        morador.setApartamento(dto.apartamento());
        morador.setSenha("senhaCodificada");
        morador.adicionarRole(Role.ROLE_MORADOR);

        Mockito.when(passwordEncoder.encode(dto.senha())).thenReturn("senhaCodificada");
        Mockito.when(moradorGateway.save(Mockito.any(Morador.class))).thenReturn(morador);

        Morador resultado = moradorService.cadastrar(dto);

        Assertions.assertEquals("João", resultado.getNome());
        Assertions.assertEquals("joao@email.com", resultado.getEmail());
        Assertions.assertEquals("86999775533", resultado.getTelefone());
        Assertions.assertEquals("Apt 101", resultado.getApartamento());
        Assertions.assertEquals("senhaCodificada", resultado.getSenha());
        Assertions.assertTrue(resultado.getRoles().contains(Role.ROLE_MORADOR));
    }

    @Test
    public void deveBuscarMoradorPorId_ComSucesso() {
        Morador morador = new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>());

        Mockito.when(moradorGateway.findById(1L)).thenReturn(Optional.of(morador));

        Morador resultado = moradorService.buscarPorId(1L);

        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertEquals("Carlos", resultado.getNome());
        Assertions.assertEquals("carlos@email.com", resultado.getEmail());
    }

    @Test
    public void deveLancarExcecao_QuandoMoradorNaoExiste() {
        Mockito.when(moradorGateway.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.buscarPorId(99L);
        });
    }

    @Test
    public void deveBuscarMoradorPorEmail_ComSucesso() {
        Morador morador = new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>());

        Mockito.when(moradorGateway.findByEmail("carlos@email.com")).thenReturn(Optional.of(morador));

        Morador resultado = moradorService.buscarPorEmail("carlos@email.com");

        Assertions.assertEquals("Carlos", resultado.getNome());
        Assertions.assertEquals("carlos@email.com", resultado.getEmail());
    }

    @Test
    public void deveLancarExcecao_QuandoEmailNaoExiste() {
        Mockito.when(moradorGateway.findByEmail("naoExiste@email.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.buscarPorEmail("naoExiste@email.com");
        });
    }

    @Test
    public void deveBuscarMoradorPorTelefone_ComSucesso() {
        Morador morador = new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>());

        Mockito.when(moradorGateway.findByTelefone("86999775533")).thenReturn(Optional.of(morador));

        Morador resultado = moradorService.buscarPorTelefone("86999775533");

        Assertions.assertEquals("Carlos", resultado.getNome());
        Assertions.assertEquals("86999775533", resultado.getTelefone());
    }

    @Test
    public void deveLancarExcecao_QuandoTelefoneNaoExiste() {
        Mockito.when(moradorGateway.findByTelefone("0000000000")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.buscarPorTelefone("0000000000");
        });
    }

    @Test
    public void deveBuscarMoradorPorApartamento_ComSucesso() {
        Morador morador = new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>());

        Mockito.when(moradorGateway.findByApartamento("Apt 101")).thenReturn(Optional.of(morador));

        Morador resultado = moradorService.buscarPorApartamento("Apt 101");

        Assertions.assertEquals("Carlos", resultado.getNome());
        Assertions.assertEquals("Apt 101", resultado.getApartamento());
    }

    @Test
    public void deveLancarExcecao_QuandoApartamentoNaoExiste() {
        Mockito.when(moradorGateway.findByApartamento("Apt 999")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.buscarPorApartamento("Apt 999");
        });
    }
    @Test
    public void deveRetornarListaDeMoradores_QuandoExistiremMoradores() {
        List<Morador> listaMoradores = Arrays.asList(
                new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>()),
                new Morador(2L, "Maria", "86999887766", "Apt 202", "maria@email.com", "senha123", new HashSet<>())
        );

        Mockito.when(moradorGateway.findAll()).thenReturn(listaMoradores);

        List<Morador> resultado = moradorService.buscarTodos();

        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Carlos", resultado.get(0).getNome());
        Assertions.assertEquals("Maria", resultado.get(1).getNome());
    }

    @Test
    public void deveRetornarListaVazia_QuandoNaoExistiremMoradores() {
        Mockito.when(moradorGateway.findAll()).thenReturn(Collections.emptyList());

        List<Morador> resultado = moradorService.buscarTodos();

        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    public void deveAtualizarMorador_ComSucesso() {
        Morador moradorExistente = new Morador(1L, "Carlos", "86999775533", "Apt 101", "carlos@email.com", "senha123", new HashSet<>());
        AtualizarMoradorDTO dto = new AtualizarMoradorDTO("Carlos Silva", "87999887766", "Apt 202", "carlos.silva@email.com");

        Mockito.when(moradorGateway.findById(1L)).thenReturn(Optional.of(moradorExistente));
        Mockito.when(moradorGateway.save(Mockito.any(Morador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Morador resultado = moradorService.atualizar(1L, dto);

        Assertions.assertEquals("Carlos Silva", resultado.getNome());
        Assertions.assertEquals("87999887766", resultado.getTelefone());
        Assertions.assertEquals("Apt 202", resultado.getApartamento());
        Assertions.assertEquals("carlos.silva@email.com", resultado.getEmail());
    }

    @Test
    public void deveLancarExcecao_QuandoMoradorAtualizarNaoExiste() {
        AtualizarMoradorDTO dto = new AtualizarMoradorDTO("Novo Nome", "87999887766", "Apt 303", "novo@email.com");

        Mockito.when(moradorGateway.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.atualizar(99L, dto);
        });
    }

    @Test
    public void deveDeletarMorador_ComSucesso() {
        Long idMorador = 1L;

        Mockito.when(moradorGateway.findById(idMorador)).thenReturn(Optional.of(new Morador()));
        Mockito.doNothing().when(moradorGateway).deleteById(idMorador);

        moradorService.deletarMorador(idMorador);

        Mockito.verify(moradorGateway, Mockito.times(1)).deleteById(idMorador);
    }

    @Test
    public void deveLancarExcecao_QuandoMoradorDeletarNaoExiste() {
        Long idInexistente = 99L;

        Mockito.when(moradorGateway.findById(idInexistente)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            moradorService.deletarMorador(idInexistente);
        });
    }


}
