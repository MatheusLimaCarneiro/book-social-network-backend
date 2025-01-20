package prati.projeto.redeSocial.modal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resenha")
public class Resenha {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_email", referencedColumnName = "email")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    private String titulo;
    private String autor;
    private LocalDateTime dataPublicacao;
    private LocalDateTime dataEdicao;
    private Double nota;
}
