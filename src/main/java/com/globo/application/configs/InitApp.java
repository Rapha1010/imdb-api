package com.globo.application.configs;

import com.globo.application.dtos.MovieDto;
import com.globo.application.dtos.UserDto;
import com.globo.application.enums.RoleName;
import com.globo.application.models.MovieModel;
import com.globo.application.models.RoleModel;
import com.globo.application.models.UserModel;
import com.globo.application.repositories.MovieRepository;
import com.globo.application.repositories.UserRepository;
import com.globo.application.services.MovieService;
import com.globo.application.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class InitApp implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {

        var admin = UserDto.builder()
                .email("admin@yahoo.com.br")
                .username("admin")
                .password("123456")
                .roles(Arrays.asList(RoleModel.builder().name(RoleName.ROLE_ADMINISTRATOR).build()))
                .phoneNumber("2199999999").build();

        var user = UserDto.builder()
                .email("user@yahoo.com.br")
                .username("user")
                .password("123456")
                .roles(Arrays.asList(RoleModel.builder().name(RoleName.ROLE_CUSTOMER).build()))
                .phoneNumber("2199999999").build();

        var disabledAdmin = UserDto.builder()
                .email("disabled_admin@yahoo.com.br")
                .username("disabled_admin")
                .password("123456")
                .disable(true)
                .roles(Arrays.asList(RoleModel.builder().name(RoleName.ROLE_ADMINISTRATOR).build()))
                .phoneNumber("2199999999").build();

        var disabledUser = UserDto.builder()
                .email("disabled_user@yahoo.com.br")
                .username("disabled_user")
                .password("123456")
                .disable(true)
                .roles(Arrays.asList(RoleModel.builder().name(RoleName.ROLE_CUSTOMER).build()))
                .phoneNumber("2199999999").build();

        if (!userRepository.existsByEmail(admin.getEmail())) userService.createUser(admin);
        if (!userRepository.existsByEmail(user.getEmail())) userService.createUser(user);
        if (!userRepository.existsByEmail(disabledAdmin.getEmail())) userService.createUser(disabledAdmin);
        if (!userRepository.existsByEmail(disabledUser.getEmail())) userService.createUser(disabledUser);

        var movie1 = MovieModel.builder().movieName("O Corvo").directorName("Alex Proyas").genres("Drama").storyLine("O Corvo\",\"As almas gêmeas Eric Draven e Shelly Webster são brutalmente assassinadas quando os demônios de seu passado sombrio os alcançam. Atravessando os mundos dos vivos e dos mortos, Draven retorna em busca de vingança sangrenta contra os assassinos.").build();
        var movie2 = MovieModel.builder().movieName("Oppenheimer").directorName("Christopher Nolan").genres("Terror").storyLine("O físico J. Robert Oppenheimer trabalha com uma equipe de cientistas durante o Projeto Manhattan, levando ao desenvolvimento da bomba atômica.").build();
        var movie3 = MovieModel.builder().movieName("Missão Impossível").directorName("Brian De Palma").genres("Ação").storyLine("Missão Impossível conta a história de Ethan Hunt (Tom Cruise), um agente americano que é suspeito de deslealdade. Ele deve descobrir e expor o verdadeiro espião sem a ajuda de sua organização. ").build();
        var movie4 = MovieModel.builder().movieName("Perdido em Marte").directorName("Ridley Scott").genres("Ficção Cientifica").storyLine("O astronauta Mark Watney é enviado a uma missão para Marte, mas após uma severa tempestade, ele é dado como morto, abandonado pelos colegas e acorda sozinho no planeta inóspito com escassos suprimentos e sem saber como reencontrar os companheiros ou retornar à Terra. Ele inicia então uma dura luta pela própria sobrevivência, utilizando de todo o seu conhecimento científico para fazer contato e retornar para casa.").build();
        var movie5 = MovieModel.builder().movieName("Interestelar").directorName("Christopher Nolan").genres("Ficção Cientifica").storyLine("As reservas naturais da Terra estão chegando ao fim e um grupo de astronautas recebe a missão de verificar possíveis planetas para receberem a população mundial, possibilitando a continuação da espécie. Cooper é chamado para liderar o grupo e aceita a missão sabendo que pode nunca mais ver os filhos. Ao lado de Brand, Jenkins e Doyle, ele seguirá em busca de um novo lar.").build();


        if (!movieRepository.existsByMovieName(movie1.getMovieName())) movieService.save(movie1);
        if (!movieRepository.existsByMovieName(movie2.getMovieName())) movieService.save(movie2);
        if (!movieRepository.existsByMovieName(movie3.getMovieName())) movieService.save(movie3);
        if (!movieRepository.existsByMovieName(movie4.getMovieName())) movieService.save(movie4);
        if (!movieRepository.existsByMovieName(movie5.getMovieName())) movieService.save(movie5);

    }
}
