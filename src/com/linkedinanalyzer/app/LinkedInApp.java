/**
 * Classe principal para testar a implementação da Task 1
 */
public class LinkedInApp {
    
    public static void main(String[] args) {
        System.out.println("=== TASK 1: TESTE DO CONSTRUTOR DA ANÁLISE ===\n");
        
        // 1. Criar a rede social (grafo)
        Grafo redeSocial = criarRedeSocial();
        
        // 2. Exibir informações do grafo criado
        System.out.println("Rede social criada com " + redeSocial.getQuantidadeVertices() + " perfis:");
        System.out.println("Perfis: " + redeSocial.getVertices());
        System.out.println();
        
        // 3. Criar o LinkedInAnalyzer (TAREFA 1)
        System.out.println("--- Criando LinkedInAnalyzer ---");
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(redeSocial);
        System.out.println();
        
        // 4. Testar os métodos da Task 1
        testarMetodosTask1(analyzer);
        
        // 5. Testar validação de construtor
        testarValidacaoConstrutor();

        // 6. Testar o menorCaminho (Task 4)
        testarMenorCaminho(redeSocial);

        // 7. Testar as sugestões de conexões (Task 2)
        testarSugestaoConexoes(analyzer);

        // 8. Testar o Grau de Separação (Task 3)
        testarGrauSeparacao(analyzer);
    }

    
    /**
     * Cria a rede social de exemplo conforme o cenário sugerido
     */
    private static Grafo criarRedeSocial() {
        Grafo grafo = new Grafo();
        
        // Pessoas da Rede Principal
        String[] redePrincipal = {"Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda"};
        for (String pessoa : redePrincipal) {
            grafo.adicionarVertice(pessoa);
        }
        
        // Conexões e afinidades - Rede Principal
        grafo.adicionarAresta("Ana", "Bruno", 1);
        grafo.adicionarAresta("Ana", "Carlos", 2);
        grafo.adicionarAresta("Ana", "Daniela", 8);
        grafo.adicionarAresta("Bruno", "Eduardo", 1);
        grafo.adicionarAresta("Carlos", "Eduardo", 1);
        grafo.adicionarAresta("Daniela", "Fernanda", 5);
        grafo.adicionarAresta("Eduardo", "Fernanda", 1);
        
        // Grupo Isolado 1: Gabriel e Hugo
        grafo.adicionarVertice("Gabriel");
        grafo.adicionarVertice("Hugo");
        grafo.adicionarAresta("Gabriel", "Hugo", 1);
        
        // Grupo Isolado 2: Igor e Juliana
        grafo.adicionarVertice("Igor");
        grafo.adicionarVertice("Juliana");
        grafo.adicionarAresta("Igor", "Juliana", 1);
        
        return grafo;
    }
    
    /**
     * Testa os métodos disponíveis na Task 1
     */
    private static void testarMetodosTask1(LinkedInAnalyzer analyzer) {
        System.out.println("--- Testando métodos da Task 1 ---");
        
        // Teste 1: getRedeSocial()
        System.out.println("\n1. Testando getRedeSocial():");
        Grafo grafoRecuperado = analyzer.getRedeSocial();
        System.out.println("   Grafo recuperado com " + grafoRecuperado.getQuantidadeVertices() + " vértices");
        
        // Teste 2: perfilExiste()
        System.out.println("\n2. Testando perfilExiste():");
        String[] perfisTeste = {"Ana", "Carlos", "Maria"};
        for (String perfil : perfisTeste) {
            boolean existe = analyzer.perfilExiste(perfil);
            System.out.println("   Perfil '" + perfil + "' existe? " + (existe ? "✅ Sim" : "❌ Não"));
        }
        
        // Teste 3: getAllPerfis()
        System.out.println("\n3. Testando getAllPerfis():");
        System.out.println("   Todos os perfis da rede: " + analyzer.getAllPerfis());
        
        // Teste 4: toString()
        System.out.println("\n4. Testando toString():");
        System.out.println("   " + analyzer.toString());
        
        System.out.println("\n✅ Task 1 - Construtor da Análise implementado e testado com sucesso!");
    }
    
    /**
     * Testa a validação do construtor com parâmetros inválidos
     */
    private static void testarValidacaoConstrutor() {
        System.out.println("\n--- Testando validação do construtor ---");
        
        // Teste com grafo nulo (deve lançar exceção)
        try {
            System.out.println("   Tentando criar LinkedInAnalyzer com grafo nulo...");
            new LinkedInAnalyzer(null);
            System.out.println("   ❌ ERRO: Não lançou exceção!");
        } catch (IllegalArgumentException e) {
            System.out.println("   ✅ Sucesso: Exceção lançada corretamente: " + e.getMessage());
        }
        
        // Teste com grafo vazio
        try {
            System.out.println("   Tentando criar LinkedInAnalyzer com grafo vazio...");
            Grafo grafoVazio = new Grafo();
            LinkedInAnalyzer analyzerVazio = new LinkedInAnalyzer(grafoVazio);
            System.out.println("   ✅ Sucesso: Analisador criado com grafo vazio (0 perfis)");
            System.out.println("      " + analyzerVazio.toString());
        } catch (Exception e) {
            System.out.println("   ❌ ERRO inesperado: " + e.getMessage());
        }
        
        System.out.println("\n✅ Testes de validação concluídos!");
    }

    /**
     * Testa o método menorCaminho (Rota e Custo de Maior Afinidade)
     */
    private static void testarMenorCaminho(Grafo grafo) {
        System.out.println("\n=== TASK 4: TESTE DE ROTA E CUSTO DE MAIOR AFINIDADE ===\n");

        // Caso 1: caminho direto na rede principal (Ana -> Bruno)
        testarRota(grafo, "Ana", "Bruno");

        // Caso 2: caminho com múltiplos saltos (Ana -> Fernanda)
        testarRota(grafo, "Ana", "Fernanda");

        // Caso 3: mesmo vértice como origem e destino
        testarRota(grafo, "Ana", "Ana");

        // Caso 4: perfis inalcançáveis (grupos isolados diferentes)
        testarRota(grafo, "Ana", "Gabriel");

        // Caso 5: perfil que não existe no grafo
        testarRota(grafo, "Ana", "Maria");

        System.out.println("\n✅ Task 4 - Rota e Custo de Maior Afinidade testada!");
    }

    /**
     * Auxiliar: executa e exibe o resultado de uma busca de menor caminho
     */
    private static void testarRota(Grafo grafo, String origem, String destino) {
        System.out.println("--- Rota: " + origem + " -> " + destino + " ---");

        ResultadoCaminho resultado = grafo.menorCaminho(origem, destino);

        if (resultado.getCusto() == -1) {
            System.out.println("   ❌ Inalcançável (custo: -1, caminho: " + resultado.getCaminho() + ")");
        } else {
            System.out.println("   ✅ Caminho: " + resultado.getCaminho());
            System.out.println("   ✅ Custo total: " + resultado.getCusto());
        }
        System.out.println();
    }

    /**
     * Testa o método sugerirConexoes (Task 2)
     */
    private static void testarSugestaoConexoes(LinkedInAnalyzer analyzer) {
        System.out.println("\n=== TASK 2: TESTE DE SUGESTÃO DE CONEXÕES ===\n");
        
        String[] perfis = {"Ana", "Eduardo", "Fernanda", "Gabriel", "Maria"};
        for (String perfil : perfis) {
            System.out.println("--- Sugestões para " + perfil + " ---");
            java.util.List<sugereConexao> sugestoes = analyzer.sugerirConexoes(perfil);
            if (sugestoes.isEmpty()) {
                System.out.println("   Nenhuma sugestão encontrada.");
            } else {
                for (sugereConexao s : sugestoes) {
                    System.out.println("   -> " + s);
                }
            }
            System.out.println();
        }
        
        System.out.println("Task 2 - Sugestão de Conexões concluída!");
    }

    /**
     * Testa o método obterGrauSeparacao (Task 3)
     */
    private static void testarGrauSeparacao(LinkedInAnalyzer analyzer) {
        System.out.println("\n=== TASK 3: TESTE DE GRAU DE SEPARAÇÃO ===\n");

        // Caso 1: conexões diretas (1º grau) -> Ana <-> Bruno
        testarPassos(analyzer, "Ana", "Bruno");

        // Caso 2: amigo de amigo (2º grau) -> Ana <-> Eduardo (Ana -> Bruno/Carlos -> Eduardo)
        testarPassos(analyzer, "Ana", "Eduardo");

        // Caso 3: amigo de amigo de amigo (3º grau) -> Ana <-> Fernanda (Ana -> Daniela -> Fernanda [2 passos])
        // Nota: no menor caminho ponderado (Dijkstra) de Ana para Fernanda, o caminho é 3 passos,
        // mas em passos puros (unweighted/BFS), o menor é 2 passos (Ana -> Daniela -> Fernanda).
        testarPassos(analyzer, "Ana", "Fernanda");

        // Caso 4: mesmo perfil (grau 0) -> Ana <-> Ana
        testarPassos(analyzer, "Ana", "Ana");

        // Caso 5: perfis totalmente isolados -> Ana <-> Gabriel
        testarPassos(analyzer, "Ana", "Gabriel");

        // Caso 6: perfis isolados em outro grupo -> Gabriel <-> Hugo
        testarPassos(analyzer, "Gabriel", "Hugo");

        // Caso 7: perfil que não existe
        testarPassos(analyzer, "Ana", "Maria");

        System.out.println("\nTask 3 - Grau de Separação concluído!");
    }

    /**
     * Auxiliar: executa e exibe o resultado do grau de separação
     */
    private static void testarPassos(LinkedInAnalyzer analyzer, String origem, String destino) {
        int grau = analyzer.obterGrauSeparacao(origem, destino);
        System.out.println("Grau de separação de " + origem + " a " + destino + ": " + 
                           (grau == -1 ? "Sem conexão (-1)" : grau + " passo(s)"));
    }
}