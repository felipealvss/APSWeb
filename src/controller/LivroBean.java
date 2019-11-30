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

@ManagedBean(name="livroBean")
@RequestScoped
public class LivroBean {

	public Integer id;
	public String nome;
	public String editora;
	public String ano;
	public String descricao;
	public ArrayList livroList;
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

	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	// Definir conexão SQL
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/livro","root","1234");
		} catch(Exception e){
			System.out.print(e);
		}
		return connection;	
	}
	
	// Pegar todos os registros dos Livros
	public ArrayList livroList() {
		try {
			livroList = new ArrayList();
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from livro");
			while(rs.next()) {
				LivroBean livro = new LivroBean();
				livro.setId(rs.getInt("id"));
				livro.setNome(rs.getNString("nome"));
				livro.setEditora(rs.getNString("editora"));
				livro.setAno(rs.getNString("ano"));
				livro.setDescricao(rs.getNString("descricao"));
				livroList.add(livro);
			}
			connection.close();
		}catch(Exception e) {
			System.out.print(e);
		}
		return livroList;
	}
	
	// Salvar registros dos Livros
	public String save() {
		int resultado = 0;
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("insert into livro(id, nome, editora, ano, descricao) values(?,?,?,?,?)");
			stmt.setInt(1, id);
			stmt.setString(2, nome);
			stmt.setString(3, editora);
			stmt.setString(4, ano);
			stmt.setString(5, descricao);
			result = stmt.executeUpdate();
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		if(result != 0) {
			return "cadastroLivro.xhtml?faces-redirect-true";
		}else {
			return "cadastrarLivro.xhtml?faces.redirect-true";
		}
	}
	
	// Pegar registro de Livro
	public String edit() {
		LivroBean liv = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from livro where id = " + (id));
			rs.next();
			liv = new LivroBean();
			liv.setId(rs.getInt("id"));
			liv.setNome(rs.getString("nome"));
			liv.setEditora(rs.getString("editora"));
			liv.setAno(rs.getString("ano"));
			liv.setDescricao(rs.getString("descricao"));
			sessionMap.put("editLivro", liv);
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "cadastrarLivro.xhtml?faces-redirect-true";
	}
	
	// Atualizar Registro de Livro
	public String update(LivroBean l) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("update cliente set nome = ?, editora= ?, ano = ?, descricao = ? where id = ?");
			stmt.setString(1, l.getNome());
			stmt.setString(2, l.getEditora());
			stmt.setString(3, l.getAno());
			stmt.setString(4, l.getDescricao());
			stmt.setInt(5, l.getId());
			stmt.executeUpdate();
			connection.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "index.xhtml?faces-redirect-true";
	}
}
