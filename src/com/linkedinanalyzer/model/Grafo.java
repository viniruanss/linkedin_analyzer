import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.*;

/**
 * Classe que representa um Grafo Não-Direcionado e Ponderado
 */
public class Grafo {
    // Mapeia cada vértice (pessoa) para sua lista de arestas
    private Map<String, List<Aresta>> listaAdjacencia;
    
    // Conjunto para manter registro de todos os vértices
    private Set<String> vertices;
    
    /**
     * Construtor da classe Grafo
     */
    public Grafo() {
        this.listaAdjacencia = new HashMap<>();
        this.vertices = new HashSet<>();
    }
    
    /**
     * Adiciona um vértice ao grafo
     * @param nome Nome do vértice/pessoa
     */
    public void adicionarVertice(String nome) {
        if (!vertices.contains(nome)) {
            vertices.add(nome);
            listaAdjacencia.put(nome, new ArrayList<>());
        }
    }
    
    /**
     * Adiciona uma aresta ponderada entre dois vértices
     * @param origem Nome do vértice de origem
     * @param destino Nome do vértice de destino
     * @param peso Peso da aresta (afinidade)
     */
    public void adicionarAresta(String origem, String destino, int peso) {
        // Verifica se os vértices existem, se não, cria
        adicionarVertice(origem);
        adicionarVertice(destino);
        
        // Adiciona a aresta em ambas as direções (grafo não-direcionado)
        Aresta aresta1 = new Aresta(destino, peso);
        Aresta aresta2 = new Aresta(origem, peso);
        
        listaAdjacencia.get(origem).add(aresta1);
        listaAdjacencia.get(destino).add(aresta2);
    }
    
    /**
     * Retorna a lista de adjacência de um vértice
     * @param vertice Nome do vértice
     * @return Lista de arestas do vértice
     */
    public List<Aresta> getVizinhos(String vertice) {
        return listaAdjacencia.getOrDefault(vertice, new ArrayList<>());
    }
    
    /**
     * Verifica se um vértice existe no grafo
     * @param vertice Nome do vértice
     * @return true se o vértice existe, false caso contrário
     */
    public boolean contemVertice(String vertice) {
        return vertices.contains(vertice);
    }
    
    /**
     * Retorna todos os vértices do grafo
     * @return Conjunto com todos os vértices
     */
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }
    
    /**
     * Retorna o número de vértices no grafo
     * @return Quantidade de vértices
     */
    public int getQuantidadeVertices() {
        return vertices.size();
    }

    public void inicializarDjkstra(Map<String, Integer> distancia, String origem) {

        for (String vertice: vertices){
            distancia.put(vertice, Integer.MAX_VALUE);
        }
        distancia.put(origem, 0);
    }

    public void relaxarDjkstra(String atual, Aresta aresta, Map<String, Integer> distancia,
                               Map<String, String> pai, PriorityQueue<String> fila) {
        String vizinho = aresta.getDestino();
        int novaDistancia = distancia.get(atual) + aresta.getPeso();

        if (novaDistancia < distancia.get(vizinho)){
            distancia.put(vizinho, novaDistancia);
            pai.put(vizinho, atual);
            fila.add(vizinho);
        }
    }

    public ResultadoCaminho menorCaminho(String origem, String destino) {
        if (!contemVertice(origem) || !contemVertice(destino)) return new ResultadoCaminho();

        Map<String, Integer> distancia = new HashMap<>();
        Map<String, String> pai = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        PriorityQueue<String> fila = new PriorityQueue<>(
                Comparator.comparingInt(distancia::get)
        );

        inicializarDjkstra(distancia, origem);

        fila.add(origem);

        while (!fila.isEmpty()){
            String atual = fila.poll();

            if (visitados.contains(atual)) continue;
            visitados.add(atual);

            if (atual.equals(destino)) break;

            for (Aresta aresta: getVizinhos(atual)){
                relaxarDjkstra(atual, aresta, distancia, pai, fila);
            }
        }

        if (distancia.get(destino) == Integer.MAX_VALUE) return new ResultadoCaminho();

        List<String> caminho = new ArrayList<>();
        String passo = destino;

        while (passo != null){
            // método addFirst usado no java 21
            caminho.addFirst(passo);
            passo = pai.get(passo);
        }
        return new ResultadoCaminho(caminho, distancia.get(destino));
    }
}