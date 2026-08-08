package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.LoteRequest;
import br.com.vitortheof.checkin.dto.response.LoteResponse;
import br.com.vitortheof.checkin.model.Lote;
import org.springframework.stereotype.Component;

@Component
public class LoteMapper {

    public static LoteResponse toLoteResponse(Lote lote) {
        return LoteResponse.builder()
                .id(lote.getId())
                .numeroOrdem(lote.getNumeroOrdem())
                .preco(lote.getPreco())
                .qtdTotal(lote.getQtdTotal())
                .qtdVendida(lote.getQtdVendida())
                .qtdDisponivel(lote.getQtdTotal() - lote.getQtdVendida())
                .dataInicio(lote.getDataInicio())
                .dataFim(lote.getDataFim())
                .build();
    }

    public static Lote toLote(LoteRequest request) {
        return Lote.builder()
                .numeroOrdem(request.numeroOrdem())
                .preco(request.preco())
                .qtdTotal(request.qtdTotal())
                .qtdVendida(0)
                .dataInicio(request.dataInicio())
                .dataFim(request.dataFim())
                .build();
    }
}
