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
 * Entidade para mapeamento das possíveis situações das importações
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SITUACAO")
public class SituacaoEntity {

    /**
     * Código da situação. Chave primária da tabela, gerada automaticamente.
     */
    @Id
    @Column(name = "situacao_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer situacaoId;

    /**
     * Descrição da situação
     */
    @Column(name = "descricao", nullable = false, length = 250)
    private String descricao;

}
