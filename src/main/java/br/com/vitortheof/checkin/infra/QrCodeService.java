package br.com.vitortheof.checkin.infra;

import br.com.vitortheof.checkin.exception.BusinessException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeService {

    private static final int TAMANHO_PADRAO = 300;

    public byte[] gerarQrCodeBytes(String conteudo) {
        return gerarQrCodeBytes(conteudo, TAMANHO_PADRAO);
    }

    public byte[] gerarQrCodeBytes(String conteudo, int tamanho) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, tamanho, tamanho);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new BusinessException("Erro ao gerar QR Code: " + e.getMessage());
        }
    }
}