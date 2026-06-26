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
     * Sobrescrita do método toString para facilitar a visualização
     * @return Representação textual do analisador
     */
    @Override
    public String toString() {
        return "LinkedInAnalyzer [redeSocial: " + redeSocial.getQuantidadeVertices() + " perfis]";
    }
}