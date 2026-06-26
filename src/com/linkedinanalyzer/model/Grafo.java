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
    
    /**
     * Classe interna para representar uma aresta
     */
    public static class Aresta {
        private String destino;
        private int peso;
        
        public Aresta(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
        
        public String getDestino() {
            return destino;
        }
        
        public int getPeso() {
            return peso;
        }
        
        @Override
        public String toString() {
            return destino + " (peso: " + peso + ")";
        }
    }
}