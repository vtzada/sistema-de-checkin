package br.com.vitortheof.checkin.infra;

import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Ingresso;
import br.com.vitortheof.checkin.model.ItemPedido;
import br.com.vitortheof.checkin.model.Pedido;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Async
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static final String URL_BASE_CONFIRMACAO = "http://localhost:8080/patrocinador/confirmar";
    @Autowired
    private MailSender mailSender;

    public void enviarEmailConfirmacao(String destinatario, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("drakecrafts@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject("Confirme seu e-mail - Check-in");

            String linkConfirmacao = URL_BASE_CONFIRMACAO + "?token=" + token;

            Context context = new Context();
            context.setVariable("linkConfirmacao", linkConfirmacao);

            String htmlContent = templateEngine.process("email/confirmacao", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new BusinessException("Falha ao enviar e-mail de confirmação: " + e.getMessage());
        }
    }

    public void enviarEmailIngresso(Convidado convidado, Ingresso ingresso, byte[] qrCodeBytes) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("drakecrafts@gmail.com");

            helper.setTo(convidado.getEmail());
            helper.setSubject("Seu ingresso - " + convidado.getPatrocinador().getEvento().getNome());

            String localizador = ingresso.getCodigoQR().toString().substring(0, 8).toUpperCase();
            String linkMaps = "https://www.google.com/maps/search/?api=1&query="
                    + convidado.getPatrocinador().getEvento().getLocal().replace(" ", "+");

            Context context = new Context();
            context.setVariable("nomeConvidado", convidado.getNomeCompleto());
            context.setVariable("nomeEvento", convidado.getPatrocinador().getEvento().getNome());
            context.setVariable("dataEvento", convidado.getPatrocinador().getEvento().getData());
            context.setVariable("enderecoEvento", convidado.getPatrocinador().getEvento().getLocal());
            context.setVariable("linkMaps", linkMaps);
            context.setVariable("tipoIngresso", convidado.getTipoConvidado());
            context.setVariable("nomePatrocinador", convidado.getPatrocinador().getNome());
            context.setVariable("localizador", localizador);

            String htmlContent = templateEngine.process("email/ingresso-qrcode", context);

            helper.setText(htmlContent, true);

            helper.addInline("qrcode", new ByteArrayResource(qrCodeBytes), "image/png");

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new BusinessException("Falha ao enviar e-mail do ingresso: " + e.getMessage());
        }
    }

    public void enviarEmailIngressoCompra(EmailDadosIngressoCompra dados, byte[] qrCodeBytes) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("drakecrafts@gmail.com");
            helper.setTo(dados.emailComprador());
            helper.setSubject("Seu ingresso - " + dados.nomeEvento());

            String localizador = dados.codigoQR().substring(0, 8).toUpperCase();
            String linkMaps = "https://www.google.com/maps/search/?api=1&query="
                    + dados.enderecoEvento().replace(" ", "+");

            Context context = new Context();
            context.setVariable("nomeComprador", dados.nomeComprador());
            context.setVariable("nomeEvento", dados.nomeEvento());
            context.setVariable("dataEvento", dados.dataEvento());
            context.setVariable("enderecoEvento", dados.enderecoEvento());
            context.setVariable("linkMaps", linkMaps);
            context.setVariable("nomeTipoIngresso", dados.nomeTipoIngresso());
            context.setVariable("precoPago", dados.precoPago());
            context.setVariable("localizador", localizador);

            String htmlContent = templateEngine.process("email/ingresso-compra-qrcode", context);

            helper.setText(htmlContent, true);

            helper.addInline("qrcode", new ByteArrayResource(qrCodeBytes), "image/png");

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new BusinessException("Falha ao enviar e-mail do ingresso: " + e.getMessage());
        }
    }
}