package br.com.casacodigo.configuracao;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * O Spring ativa o gerenciamento de transações e já reconhece o TransactionManager.
 */
@EnableTransactionManagement
public class JPAConfiguracao {

	/*
	 * 
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		factoryBean.setJpaVendorAdapter(vendorAdapter);

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername("usuarioteste");
		dataSource.setPassword("teste123");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/casadocodigo");
		dataSource.setDriverClassName("org.postgresql.Driver");

		factoryBean.setDataSource(dataSource);

		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "update");

		factoryBean.setJpaProperties(props);

		factoryBean.setPackagesToScan("br.com.casacodigo.model"); // onde ficam nossas entidades

		return factoryBean;
	}

	/*
	 * Nossa operação com o banco de dados deve ser gerenciada com uma transação.
	 * 
	 * Primeiro precisaremos de um TransactionManager que conheça nosso
	 * EntityManager para que assim ele possa gerenciar as transações de nossas
	 * entidades.
	 */
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
