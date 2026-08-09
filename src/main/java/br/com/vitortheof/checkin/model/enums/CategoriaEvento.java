package br.com.vitortheof.checkin.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriaEvento {
    CORPORATIVO("Corporativo"),
    ACADEMICO("Acadêmico"),
    SOCIAL("Social"),
    CULTURAL("Cultural"),
    ENTRETENIMENTO("Entretenimento"),
    ESPORTIVO("Esportivo"),
    RELIGIOSO("Religioso"),
    BENEFICENTE("Beneficente"),
    EDUCACIONAL("Educacional"),
    GOVERNAMENTAL("Governamental"),
    GASTRONOMICO("Gastronômico"),
    MODA("Moda"),
    TECNOLOGIA("Tecnologia"),
    FEIRA_NEGOCIOS("Feira de negócios"),
    SAUDE_BEM_ESTAR("Saúde e bem estar"),
    COMUNITARIO("Comunitário");

    private final String CategoriaEvento;

}
