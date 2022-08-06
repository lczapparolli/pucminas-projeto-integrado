package br.com.lczapparolli.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade para armazenar os dados das importações realizadas
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IMPORTACAO")
public class ImportacaoEntity {

    /**
     * Código da importação. Chave primária da tabela, gerada automaticamente.
     */
    @Id
    @Column(name = "importacao_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer importacaoId;

    /**
     * Layout correspondente à importação
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "layout_id")
    private LayoutEntity layout;

    /**
     * Situação do processo de importação
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "situacao_id")
    private SituacaoEntity situacao;

    /**
     * Nome do arquivo importado
     */
    @Column(name = "nome_arquivo", nullable = false, length = 500)
    private String nomeArquivo;

    /**
     * Momento em que a importação foi iniciada
     */
    @Column(name = "inicio_importacao", nullable = false)
    private LocalDateTime inicioImportacao;

    /**
     * Momento em que a importação foi encerrada, independente do sucesso
     */
    @Column(name = "fim_importacao")
    private LocalDateTime fimImportacao;

}
