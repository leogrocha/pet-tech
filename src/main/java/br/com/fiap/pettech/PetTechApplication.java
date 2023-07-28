package br.com.fiap.pettech;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

@SpringBootApplication
public class PetTechApplication {
    
//    public PetTechApplication() throws SQLException {
//
//        final String url = "jdbc:postgresql://localhost:5432/pettech";
//        final String user = "user";
//        final String password = "123456";
//
//        Connection connnection = DriverManager.getConnection(url, user, password);
//
//        System.out.println("Conexão efetuada com sucesso!");
//        connnection.close();
//    }

    public static void main(String[] args) {
        SpringApplication.run(PetTechApplication.class, args);
    }
    

}
