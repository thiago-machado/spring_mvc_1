package br.com.casacodigo.conf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceConfiguracaoTeste {

	@Bean
    @Profile("teste") // definindo profile da base de teste
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername("usuarioteste");
		dataSource.setPassword("teste123");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/casadocodigo_teste");
		dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
	
}
