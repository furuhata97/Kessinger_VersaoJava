package com.kessinger.kessinger.model;

import com.kessinger.kessinger.model.enums.Area;
import com.kessinger.kessinger.model.enums.FatorImpacto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Periodico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "periodico_id")
    private Integer id;

    @Column(name="periodico_issn")
    private String issn;

    @Column(name="periodico_titulo")
    private String titulo;

    @Column(name="periodico_qualis")
    private String qualis;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodico_fator")
    private FatorImpacto fator;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodico_area")
    private Area area;

    @Column(name = "periodico_upload")
    private String upload;

    @Column(name = "periodico_descricao")
    private String descricao;

    @OneToMany
    private List<Publicacao> publicacoes;

    @OneToOne
    @JoinColumn(name = "usuario_periodico")
    private Usuario user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public FatorImpacto getFator() {
        return fator;
    }

    public void setFator(FatorImpacto fator) {
        this.fator = fator;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getQualis() {
        return qualis;
    }

    public void setQualis(String qualis) {
        this.qualis = qualis;
    }

    public Usuario getUsuario() {
        return user;
    }

    public void setUsuario(Usuario usuario) {
        this.user = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Periodico periodico = (Periodico) o;

        return id != null ? id.equals(periodico.id) : periodico.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void addPublicacao(Publicacao publicacao) {
        if(publicacoes==null)
            publicacoes = new ArrayList<>();
        publicacoes.add(publicacao);
    }
}
