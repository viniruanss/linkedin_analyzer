import java.util.*;
/**
 * Classe responsável por análises e recomendações na rede social
 * LinkedInAnalyzer - Task 1: Construtor da Análise
 */
public class LinkedInAnalyzer {
    
    // Referência ao grafo que representa a rede social
    private Grafo redeSocial;
    
    /**
     * CONSTRUTOR DA ANÁLISE
     * 
     * @param grafo Instância do grafo que representa a rede social
     * 
     * O que faz: Armazena a referência ao grafo para que as outras missões 
     * possam usá-la para realizar as análises.
     */
    public LinkedInAnalyzer(Grafo grafo) {
        // Validação: verifica se o grafo é nulo
        if (grafo == null) {
            throw new IllegalArgumentException("O grafo da rede social não pode ser nulo.");
        }
        
        // Armazena a referência do grafo
        this.redeSocial = grafo;
        
        // Log de inicialização (útil para debugging)
        System.out.println("LinkedInAnalyzer inicializado com sucesso!");
        System.out.println("Rede social carregada com " + grafo.getQuantidadeVertices() + " perfis.");
    }
    
    /**
     * Método getter para acessar o grafo da rede social
     * @return A instância do grafo armazenada
     */
    public Grafo getRedeSocial() {
        return this.redeSocial;
    }
    
    /**
     * Método utilitário para verificar se um perfil existe na rede
     * @param nome Nome do perfil a ser verificado
     * @return true se o perfil existe, false caso contrário
     */
    public boolean perfilExiste(String nome) {
        return redeSocial.contemVertice(nome);
    }
    
    /**
     * Método utilitário para obter todos os perfis da rede
     * @return Conjunto com todos os nomes dos perfis
     */
    public Set<String> getAllPerfis() {
        return redeSocial.getVertices();
    }

    /**
     * SUGESTÃO DE CONEXÕES (Amigos de 2º grau)
     *
     * @param nome Nome da pessoa para quem vamos sugerir conexões
     * @return Lista de sugestões ordenada por quantidade de amigos em comum (decrescente)
     */
    public List<sugereConexao> sugerirConexoes(String nome) {
        //se perfil nao existir: segue o mesmo padrão do menorCaminho (retorna "vazio", não lança exceção)
        if (!perfilExiste(nome)) {
            return new ArrayList<>();
        }

        //esse é o 1º grau: contatos diretos de "nome"
        Set<String> primeiroGrau = new HashSet<>();
        for (Aresta aresta : redeSocial.getVizinhos(nome)) {
            primeiroGrau.add(aresta.getDestino());
        }

        //já esse é o 2º grau: vizinhos dos vizinhos, onde contamos quantas vezes cada um aparece
        // (cada aparição = um amigo em comum diferente levando até ele)
        Map<String, Integer> contagem = new HashMap<>();
        for (String amigoDireto : primeiroGrau) {
            for (Aresta aresta : redeSocial.getVizinhos(amigoDireto)) {
                String candidato = aresta.getDestino();
                if (candidato.equals(nome)) continue;          //para nao sugerir a própria pessoa
                if (primeiroGrau.contains(candidato)) continue; //para nao sugerir quem já é 1º grau

                contagem.merge(candidato, 1, Integer::sum);
            }
        }

        //monta a lista de sugestões
        List<sugereConexao> sugestoes = new ArrayList<>();
        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            sugestoes.add(new sugereConexao(entrada.getKey(), entrada.getValue()));
        }

        //ordena por amigos em comum, decrescente
        sugestoes.sort((s1, s2) -> s2.getAmigosEmComum() - s1.getAmigosEmComum());

        return sugestoes;
    }

    /**
     * GRAU DE SEPARAÇÃO (Quantos "passos" de distância?)
     * 
     * @param origem Nome do perfil de origem
     * @param destino Nome do perfil de destino
     * @return O número de passos (arestas) entre origem e destino, ou -1 se não houver conexão ou perfil não existir
     */
    public int obterGrauSeparacao(String origem, String destino) {
        // Se algum dos perfis não existe na rede social, retorna -1
        if (!perfilExiste(origem) || !perfilExiste(destino)) {
            return -1;
        }

        // Se origem e destino são o mesmo perfil, o grau é 0
        if (origem.equals(destino)) {
            return 0;
        }

        // Fila para o BFS (Breadth-First Search) para encontrar o menor caminho em número de passos (não ponderado)
        Queue<String> fila = new LinkedList<>();
        Map<String, Integer> distancias = new HashMap<>();

        // Inicializa a busca a partir da origem
        fila.add(origem);
        distancias.put(origem, 0);

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            int distAtual = distancias.get(atual);

            // Se encontramos o destino, retornamos o grau de separação
            if (atual.equals(destino)) {
                return distAtual;
            }

            // Explora todas as conexões diretas (vizinhos) do perfil atual
            for (Aresta aresta : redeSocial.getVizinhos(atual)) {
                String vizinho = aresta.getDestino();
                if (!distancias.containsKey(vizinho)) {
                    distancias.put(vizinho, distAtual + 1);
                    fila.add(vizinho);
                }
            }
        }

        // Retorna -1 se os dois perfis forem totalmente isolados (sem conexão)
        return -1;
    }

    /**
     * MAPEAR GRUPOS ISOLADOS (Sub-redes / Componentes Conexos)
     *
     * O que faz: Varre toda a rede social e agrupa os perfis que estão
     * conectados entre si (direta ou indiretamente), mas isolados de outros grupos.
     *
     * @return Lista de conjuntos, onde cada conjunto representa um grupo de perfis
     *         que formam uma sub-rede isolada.
     */
    public List<Set<String>> mapearGruposIsolados() {
        Set<String> visitados = new HashSet<>();
        List<Set<String>> componentes = new ArrayList<>();

        // Percorre todos os perfis da rede social
        for (String perfil : getAllPerfis()) {
            // Se esse perfil já foi visitado, ele já pertence a um grupo conhecido
            if (visitados.contains(perfil)) continue;

            // Novo grupo: faz um BFS a partir desse perfil "semente"
            Set<String> grupoAtual = new HashSet<>();
            Queue<String> fila = new LinkedList<>();

            fila.add(perfil);
            visitados.add(perfil);

            while (!fila.isEmpty()) {
                String atual = fila.poll();
                grupoAtual.add(atual);

                // Explora todos os vizinhos diretos do perfil atual
                for (Aresta aresta : redeSocial.getVizinhos(atual)) {
                    String vizinho = aresta.getDestino();
                    if (!visitados.contains(vizinho)) {
                        visitados.add(vizinho);
                        fila.add(vizinho);
                    }
                }
            }

            // Grupo completo: adiciona na lista de componentes
            componentes.add(grupoAtual);
        }

        return componentes;
    }
    
    /**
     * Sobrescrita do método toString para facilitar a visualização
     * @return Representação textual do analisador
     */
    @Override
    public String toString() {
        return "LinkedInAnalyzer [redeSocial: " + redeSocial.getQuantidadeVertices() + " perfis]";
    }
}