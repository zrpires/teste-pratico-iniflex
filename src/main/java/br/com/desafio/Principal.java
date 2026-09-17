package br.com.desafio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {
    public static void main(String[] args) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

        // 3.1 - Inserir todos os funcionários na mesma ordem da tabela
        List<Funcionario> funcionarios = new ArrayList<>(Arrays.asList(
            new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
            new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
            new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
            new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
            new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
            new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
            new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
            new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));

        // 3.2 - Remover o funcionário "João"
        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));

        // 3.3 - Imprimir todos os funcionários com formatação de data e salário
        System.out.println("=== 3.3: Lista de Funcionários ===");
        for (Funcionario f : funcionarios) {
            System.out.println(String.format("Nome: %-8s | Data Nasc: %s | Salário: R$ %10s | Função: %s",
                f.getNome(),
                f.getDataNascimento().format(dateFormatter),
                decimalFormatter.format(f.getSalario()),
                f.getFuncao()));
        }

        // 3.4 - Atualizar salários com 10% de aumento
        for (Funcionario f : funcionarios) {
            BigDecimal novoSalario = f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novoSalario);
        }

        // 3.5 - Agrupar os funcionários por função em um Map
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao));

        // 3.6 - Imprimir os funcionários agrupados por função
        System.out.println("\n=== 3.6: Funcionários Agrupados por Função ===");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("[" + funcao + "]");
            lista.forEach(f -> System.out.println("  - " + f.getNome()));
        });

        // 3.8 - Imprimir aniversariantes dos meses 10 e 12
        System.out.println("\n=== 3.8: Aniversariantes de Outubro (10) e Dezembro (12) ===");
        
        List<Funcionario> aniversariantesMes10 = funcionarios.stream()
            .filter(f -> f.getDataNascimento().getMonthValue() == 10)
            .collect(Collectors.toList());

        List<Funcionario> aniversariantesMes12 = funcionarios.stream()
            .filter(f -> f.getDataNascimento().getMonthValue() == 12)
            .collect(Collectors.toList());

        System.out.println("Mês 10 (Outubro):");
        if (aniversariantesMes10.isEmpty()) {
            System.out.println("  Nenhum funcionário faz aniversário neste mês.");
        } else {
            aniversariantesMes10.forEach(f -> System.out.println("  - " + f.getNome() + " (" + f.getDataNascimento().format(dateFormatter) + ")"));
        }

        System.out.println("Mês 12 (Dezembro):");
        if (aniversariantesMes12.isEmpty()) {
            System.out.println("  Não existem aniversariantes atualmente no mês 12.");
        } else {
            aniversariantesMes12.forEach(f -> System.out.println("  - " + f.getNome() + " (" + f.getDataNascimento().format(dateFormatter) + ")"));
        }

        // 3.9 - Imprimir funcionário com maior idade (nome e idade)
        System.out.println("\n=== 3.9: Funcionário com Maior Idade ===");
        funcionarios.stream()
            .min(Comparator.comparing(Funcionario::getDataNascimento))
            .ifPresent(maisVelho -> {
                int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
                System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");
            });

        // 3.10 - Imprimir a lista por ordem alfabética
        System.out.println("\n=== 3.10: Funcionários em Ordem Alfabética ===");
        funcionarios.stream()
            .sorted(Comparator.comparing(Funcionario::getNome))
            .forEach(f -> System.out.println(f.getNome()));

        // 3.11 - Imprimir o total dos salários
        System.out.println("\n=== 3.11: Total dos Salários ===");
        BigDecimal totalSalarios = funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total: R$ " + decimalFormatter.format(totalSalarios));

        // 3.12 - Quantos salários mínimos ganha cada funcionário (base R$ 1212.00)
        System.out.println("\n=== 3.12: Salários Mínimos por Funcionário ===");
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        for (Funcionario f : funcionarios) {
            BigDecimal qtdSalarios = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + decimalFormatter.format(qtdSalarios) + " salários mínimos.");
        }
    }
}
