package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.annotation.ManagedBean;

@ManagedBean(name="clienteBean")
@RequestScoped
public class ClienteBean {

	public Integer id;
	public String nome;
	public String idade;
	public String contato;
	public String email;
	public ArrayList clienteList;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	Connection connection;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIdade() {
		return idade;
	}
	public void setIdade(String idade) {
		this.idade = idade;
	}
	public String getContato() {
		return contato;
	}
	public void setContato(String contato) {
		this.contato = contato;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	// Definir conexão SQL
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cliente","root","1234");
		} catch(Exception e){
			System.out.print(e);
		}
		return connection;
	}
		
	// Pegar todos os registros de Clientes
	public ArrayList clienteList() {
		try {
			clienteList = new ArrayList();
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from cliente");
			while(rs.next()) {
				ClienteBean cliente = new ClienteBean();
				cliente.setId(rs.getInt("id"));
				cliente.setNome(rs.getNString("nome"));
				cliente.setIdade(rs.getNString("idade"));
				cliente.setContato(rs.getNString("contato"));
				cliente.setEmail(rs.getNString("email"));
				clienteList.add(cliente);
			}
			connection.close();
		}catch(Exception e) {
			System.out.print(e);
		}
		return clienteList;
	}
	
	// Salvar registros de Clientes
	public String save() {
		int resultado = 0;
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("insert into cliente(id, nome, idade, contato, email) values(?,?,?,?,?)");
			stmt.setInt(1, id);
			stmt.setString(2, nome);
			stmt.setString(3, idade);
			stmt.setString(4, contato);
			stmt.setString(5, email);
			result = stmt.executeUpdate();
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		if(result != 0) {
			return "cadastroCliente.xhtml?faces-redirect-true";
		}else {
			return "cadastrarCliente.xhtml?faces.redirect-true";
		}
	}
	
	// Pegar registro de CLiente
	public String edit() {
		ClienteBean cli = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from cliente where id = " + (id));
			rs.next();
			cli = new ClienteBean();
			cli.setId(rs.getInt("id"));
			cli.setNome(rs.getString("nome"));
			cli.setIdade(rs.getString("idade"));
			cli.setContato(rs.getString("contato"));
			cli.setEmail(rs.getString("email"));
			sessionMap.put("editCliente", cli);
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "cadastrarCliente.xhtml?faces-redirect-true";
	}
	
	//Atualizar Registro de Cliente
	public String update(ClienteBean c) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("update cliente set nome = ?, idade = ?, contato = ?, email = ? where id = ?");
			stmt.setString(1, c.getNome());
			stmt.setString(2, c.getIdade());
			stmt.setString(3, c.getContato());
			stmt.setString(4, c.getEmail());
			stmt.setInt(5, c.getId());
			stmt.executeUpdate();
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "index.xhtml?faces-redirect-true";
	}
}
