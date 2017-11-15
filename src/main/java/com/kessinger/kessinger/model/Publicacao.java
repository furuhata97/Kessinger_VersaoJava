package com.kessinger.kessinger.model;

import com.kessinger.kessinger.model.enums.Area;
import com.kessinger.kessinger.model.enums.Categoria;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "publicacao_id")
    private Integer id;

    @Column(name = "publicacao_nome")
    private String nome;

    @Column(name = "publicacao_autor")
    private String autor;

    @Temporal(TemporalType.DATE)
    @Column(name = "publicacao_ano")
    private Date ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "publicacao_area")
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(name = "publicacao_categoria")
    private Categoria categoria;

    @Column(name = "publicacao_upload")
    private String upload;

    @OneToOne
    @JoinColumn(name = "publicacao_periodico")
    private Periodico periodico;

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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Periodico getPeriodico() {
        return periodico;
    }

    public void setPeriodico(Periodico periodico) {
        this.periodico = periodico;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publicacao that = (Publicacao) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
