package prati.projeto.redeSocial.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prati.projeto.redeSocial.exception.RegraNegocioException;
import prati.projeto.redeSocial.modal.entity.Comentario;
import prati.projeto.redeSocial.modal.entity.ComentarioResposta;
import prati.projeto.redeSocial.modal.entity.Perfil;
import prati.projeto.redeSocial.repository.ComentarioRepository;
import prati.projeto.redeSocial.repository.ComentarioRespostaRepository;
import prati.projeto.redeSocial.repository.PerfilRepository;
import prati.projeto.redeSocial.rest.dto.RespostaDTO;
import prati.projeto.redeSocial.service.ComentarioRespostaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioRespostaServiceImpl implements ComentarioRespostaService {

    private final ComentarioRespostaRepository respostaRepository;
    private final ComentarioRepository comentarioRepository;
    private final PerfilRepository perfilRepository;

    @Override
    @Transactional
    public RespostaDTO adicionarResposta(Integer comentarioId, RespostaDTO respostaDTO) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RegraNegocioException("Comentário não encontrado"));

        Perfil perfil = perfilRepository.findById(respostaDTO.getPerfilId())
                .orElseThrow(() -> new RegraNegocioException("Perfil não encontrado"));

        ComentarioResposta resposta = new ComentarioResposta();
        resposta.setComentarioOriginal(comentario);
        resposta.setPerfil(perfil);
        resposta.setTexto(respostaDTO.getTexto());
        resposta.setDataResposta(LocalDateTime.now());

        resposta = respostaRepository.save(resposta);
        return convertToDTO(resposta);
    }

    @Override
    public List<RespostaDTO> listarRespostasPorComentario(Integer comentarioId) {
        List<ComentarioResposta> respostas = respostaRepository.findByComentarioOriginalId(comentarioId);
        return respostas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RespostaDTO atualizarResposta(Integer respostaId, RespostaDTO respostaDTO) {
        ComentarioResposta resposta = respostaRepository.findById(respostaId)
                .orElseThrow(() -> new RegraNegocioException("Resposta não encontrada"));

        resposta.setTexto(respostaDTO.getTexto());
        resposta.setDataResposta(LocalDateTime.now());

        resposta = respostaRepository.save(resposta);
        return convertToDTO(resposta);
    }

    @Override
    @Transactional
    public void deletarResposta(Integer respostaId) {
        respostaRepository.findById(respostaId)
                .orElseThrow(() -> new RegraNegocioException("Resposta não encontrada"));
        respostaRepository.deleteById(respostaId);
    }

    private RespostaDTO convertToDTO(ComentarioResposta resposta) {
        return new RespostaDTO(
                resposta.getId(),
                resposta.getPerfil().getId(),
                resposta.getTexto(),
                resposta.getDataResposta()
        );
    }
}
