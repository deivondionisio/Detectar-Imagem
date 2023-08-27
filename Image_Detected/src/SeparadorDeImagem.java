import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class SeparadorDeImagem {

    private static final int LIMIAR = 232;

    public static final int VERMELHO = 0xFF_FF_00_00;
    public static final int VERDE = 0xFF_00_FF_00;
    public static final int AZUL = 0xFF_00_00_FF;
    public static final int BRANCO = 0xFF_FF_FF_FF;
    public static final int PRETO = 0xFF_00_00_00;
    public static final int AMARELO = 0xFF_FF_FF_00;
    public static final int CIANO = 0xFF_00_FF_FF;
    public static final int LIMA = 0xFF_80_FF_00;
    public static final int MAGENTA = 0xFF_FF_00_FF;
    public static final int ROSA = 0xFF_FF_00_80;
    public static final int VIOLETA = 0xFF_C0_80_FF;
    public static final int MARROM = 0xFF_80_40_00;
    public static final int LARANJA = 0xFF_80_40_00;
    public static final int CINZA = 0xFF_80_80_80;

    public static final Map<Integer, String> CORES = new HashMap<>(20);

    private static int vermelho(int cor) {
        return (cor & 0xFF0000) >>> 16;
    }

    private static int verde(int cor) {
        return (cor & 0xFF00) >>> 8;
    }

    private static int azul(int cor) {
        return cor & 0xFF;
    }

    private static int reduzirCor(int cor) {
        int r1 = vermelho(cor);
        int g1 = verde(cor);
        int b1 = azul(cor);
        int melhorCor = 0;
        int melhorDistancia = Integer.MAX_VALUE;
        for (int talvez : CORES.keySet()) {
            int r2 = vermelho(talvez);
            int g2 = verde(talvez);
            int b2 = azul(talvez);
            int r3 = Math.abs(r1 - r2);
            int g3 = Math.abs(g1 - g2);
            int b3 = Math.abs(b1 - b2);
            int distancia = r3 * r3 * 3 + g3 * g3 * 6 + b3 * b3;
            if (distancia < melhorDistancia) {
                melhorDistancia = distancia;
                melhorCor = talvez;
            }
        }
        return melhorCor;
    }

    private static BufferedImage reduzirCores(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int cor = reduzirCor(input.getRGB(x, y));
                bi.setRGB(x, y, cor);
            }
        }
        return bi;
    }

    private static Map<Integer, Integer> histogramaDeCores(BufferedImage imagem) {
        Map<Integer, Integer> histograma = new HashMap<>(50);
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                int cor = imagem.getRGB(x, y);
                histograma.merge(cor, 1, (a, b) -> a + b);
            }
        }
        return histograma;
    }

    private static int corMaisFrequente(Map<Integer, Integer> histograma) {
        return histograma.entrySet()
                .stream()
                .reduce((a, b) -> a.getValue() > b.getValue() ? a : b)
                .get().getKey();
    }

    private static BufferedImage pretoEBranco(BufferedImage input) {
        BufferedImage bi = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int cor = input.getRGB(x, y);
                int substituta = vermelho(cor) > LIMIAR && verde(cor) > LIMIAR && azul(cor) > LIMIAR ? BRANCO : PRETO;
                bi.setRGB(x, y, substituta);
            }
        }
        return bi;
    }

    private static void floodFill(int x, int y, BufferedImage input, int novaCor) {
        int corAntiga = input.getRGB(x, y);
        if (novaCor == corAntiga) return;
        Queue<Coordinate2D> c = new ArrayDeque<>(200);
        c.add(new Coordinate2D(input, x, y));
        while (!c.isEmpty()) {
            Coordinate2D p = c.poll();
            if (input.getRGB(p.x, p.y) != corAntiga) continue;
            input.setRGB(p.x, p.y, novaCor);
            c.addAll(Arrays.asList(p.plusX(), p.plusY(), p.minusX(), p.minusY()).stream().filter(e -> e != null).collect(Collectors.toList()));
        }
    }

    public List<String> descrever(BufferedImage input) {
        List<String> descs = new ArrayList<>();
        // O restante do método descrever aqui...
        return descs;
    }

    private static boolean retangulo(BufferedImage imagem) {
        floodFill(imagem.getWidth() / 2, imagem.getHeight() / 2, imagem, PRETO);
        Map<Integer, Integer> histograma = histogramaDeCores(imagem);
        int preto = histograma.get(PRETO);
        return preto > imagem.getWidth() * imagem.getHeight() * 0.99;
    }

    private static boolean elipse(BufferedImage imagem) {
        int cx = imagem.getWidth() / 2;
        int cy = imagem.getHeight() / 2;
        floodFill(cx, cy, imagem, PRETO);
        int certoDentro = 0, certoFora = 0, erradoDentro = 0, erradoFora = 0;
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                double a = (x - cx) / (double) cx;
                double b = (y - cy) / (double) cy;
                boolean dentro = a * a + b * b <= 1;
                boolean preto = imagem.getRGB(x, y) == PRETO;
                if (preto && dentro) {
                    certoDentro++;
                } else if (preto && !dentro) {
                    erradoFora++;
                } else if (!preto && dentro) {
                    erradoDentro++;
                } else {
                    certoFora++;
                }
            }
        }
        return certoDentro + erradoFora < certoFora + erradoDentro;
    }

    private static final class Coordinate2D {
        final int x;
        final int y;
        final BufferedImage image;

        Coordinate2D(BufferedImage image, int x, int y) {
            this.x = x;
            this.y = y;
            this.image = image;
        }

        Coordinate2D plusX() {
            return x + 1 < image.getWidth() ? new Coordinate2D(image, x + 1, y) : null;
        }

        Coordinate2D plusY() {
            return y + 1 < image.getHeight() ? new Coordinate2D(image, x, y + 1) : null;
        }

        Coordinate2D minusX() {
            return x - 1 >= 0 ? new Coordinate2D(image, x - 1, y) : null;
        }

        Coordinate2D minusY() {
            return y - 1 >= 0 ? new Coordinate2D(image, x, y - 1) : null;
        }
    }
}
