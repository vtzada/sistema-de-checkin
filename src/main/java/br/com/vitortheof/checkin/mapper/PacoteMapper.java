package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.PacoteRequest;
import br.com.vitortheof.checkin.dto.response.PacoteResponse;
import br.com.vitortheof.checkin.model.Pacote;
import org.springframework.stereotype.Component;

@Component
public class PacoteMapper {

    public static PacoteResponse toPacoteResponse(Pacote pacote) {
        return PacoteResponse.builder()
                .nome(pacote.getNome())
                .limiteJogadores(pacote.getLimiteJogadores())
                .limiteVips(pacote.getLimiteVips())
                .limiteConvidados(pacote.getLimiteConvidados())
                .build();
    }

    public static Pacote toPacote(PacoteRequest request) {
        return Pacote.builder()
                .nome(request.nome())
                .limiteJogadores(request.limiteJogadores())
                .limiteVips(request.limiteVips())
                .limiteConvidados(request.limiteConvidados())
                .build();
    }
}
