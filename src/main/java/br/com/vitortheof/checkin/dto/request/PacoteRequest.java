package br.com.vitortheof.checkin.dto.request;

public record PacoteRequest(String nome, int limiteJogadores, int limiteVips, int limiteConvidados) {
}
