import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

public class App {

    public static void main(String[] args) throws IOException {
        SeparadorDeImagem separador = new SeparadorDeImagem(); // Instanciando a classe SeparadorDeImagem
        BufferedImage imagem = carregarImagem(); // Chamando o método carregarImagem
        List<String> descricoes = separador.descrever(imagem); // Usando o método descrever da instância de SeparadorDeImagem

        for (String descricao : descricoes) {
            System.out.println(descricao);
        }
    }

    private static BufferedImage carregarImagem() throws IOException {
        try {
            return ImageIO.read(new URL("https://i.stack.imgur.com/69Wj9.jpg"));
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }
}
