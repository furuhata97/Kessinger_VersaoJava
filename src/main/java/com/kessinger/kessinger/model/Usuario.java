package com.kessinger.kessinger.model;

import com.kessinger.kessinger.model.enums.Paises;
import com.kessinger.kessinger.model.enums.Sexo;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usuario_id")
    private Integer id;

    @Column(unique = true, name = "usuario_username")
    private String username;

    @Column(name = "usuario_password")
    private String password;

    @Column(name = "usuario_nome")
    private String nome;

    @Column(name = "usuario_sobrenome")
    private String sobrenome;

    @Column(name = "usuario_endereco")
    private String endereco;

    @Column(name = "usuario_numero")
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_sexo")
    private Sexo sexo;

    @Temporal(TemporalType.DATE)
    @Column(name = "usuario_nascimento")
    private Date nascimento;

    @CPF
    @Column(unique = true, name = "usuario_cpf")
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_pais")
    private Paises pais;

    @Column(name = "usuario_cidade")
    private String cidade;

    @Column(name = "usuario_email")
    private String email;

    @Column(name = "usuario_telefone")
    private String telefone;

    @Column(name = "foto_perfil")
    private String foto;

    @Column(name = "usuario_bio")
    private String bio;

    @OneToMany
    private List<Periodico> periodicos;

    @OneToMany
    private List<Publicacao> publicacoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Paises getPais() {
        return pais;
    }

    public void setPais(Paises pais) {
        this.pais = pais;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBio() {
        return bio;
    }

    public List<Periodico> getPeriodico() {
        return periodicos;
    }

    public void setPeriodico(List<Periodico> periodico) {
        this.periodicos = periodico;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return id != null ? id.equals(usuario.id) : usuario.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void addPeriodico(Periodico periodico) {
        if(this.periodicos==null)
            this.periodicos = new ArrayList<>();
        this.periodicos.add(periodico);
    }

    public void addPublicacao(Publicacao publicacao) {
        if(this.publicacoes==null)
            this.publicacoes = new ArrayList<>();
        this.publicacoes.add(publicacao);
    }
}

