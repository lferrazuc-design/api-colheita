package br.com.luana.colheita.config;

import br.com.luana.colheita.model.Frente;
import br.com.luana.colheita.model.StatusFrente;
import br.com.luana.colheita.repository.FrenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Carrega frentes FICTÍCIAS no banco quando a aplicação sobe,
 * para quem baixar o projeto já ver a API funcionando com dados.
 *
 * Só insere as que ainda não existem (pelo código), então pode
 * reiniciar a aplicação à vontade sem duplicar nada.
 *
 * Para desligar: app.dados-exemplo=false no application.properties.
 */
@Component
@ConditionalOnProperty(name = "app.dados-exemplo", havingValue = "true")
public class DadosExemplo implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DadosExemplo.class);

    private final FrenteRepository repository;

    public DadosExemplo(FrenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<Frente> exemplos = List.of(
                frente("F-01", "Fornecedor Alfa Ltda", 820.0, StatusFrente.ATIVA),
                frente("F-02", "Fornecedor Beta Ltda", 640.0, StatusFrente.ATIVA),
                frente("F-03", "Fornecedor Gama Ltda", 910.0, StatusFrente.ATIVA),
                frente("F-04", "Fornecedor Delta Ltda", 550.0, StatusFrente.PARADA),
                frente("F-05", "Fornecedor Épsilon Ltda", 780.0, StatusFrente.ATIVA),
                frente("F-06", "Fornecedor Zeta Ltda", 700.0, StatusFrente.ATIVA),
                frente("F-07", "Fornecedor Alfa Ltda", 480.0, StatusFrente.ENCERRADA),
                frente("F-08", "Fornecedor Beta Ltda", 860.0, StatusFrente.ATIVA),
                frente("F-09", "Fornecedor Eta Ltda", 600.0, StatusFrente.PARADA),
                frente("F-10", "Fornecedor Teta Ltda", 950.0, StatusFrente.ATIVA)
        );

        List<Frente> novas = exemplos.stream()
                .filter(f -> !repository.existsByCodigo(f.getCodigo()))
                .toList();

        repository.saveAll(novas);
        log.info("Dados de exemplo: {} frentes fictícias inseridas", novas.size());
    }

    private static Frente frente(String codigo, String fornecedor, double toneladasDia, StatusFrente status) {
        Frente f = new Frente(codigo, fornecedor, toneladasDia);
        f.setStatus(status);
        return f;
    }
}
