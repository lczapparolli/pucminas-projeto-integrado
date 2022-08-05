package br.com.lczapparolli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade para armazenamento dos dados de layouts
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LAYOUT")
public class LayoutEntity {

    /**
     * Código do layout. Chave primária da tabela, gerada automaticamente.
     */
    @Id
    @Column(name = "layout_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer layoutId;

    /**
     * Identificação do layout, utilizada para separar os recursos
     */
    @Column(name = "identificacao", nullable = false, length = 50)
    private String identificacao;

    /**
     * Descrição do layout, legível para humanos
     */
    @Column(name = "descricao", nullable = false, length = 250)
    private String descricao;

    /**
     * Indicação se o layout está disponível para ser utilizado
     */
    @Column(name = "ativo", nullable = false)
    public Boolean ativo;

}
