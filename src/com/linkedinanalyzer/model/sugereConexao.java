public class sugereConexao {
    private String nome;
    private int amigosEmComum;

    public sugereConexao(String nome, int amigosEmComum) {
        this.nome = nome;
        this.amigosEmComum = amigosEmComum;
    }
    public String getNome() {
        return nome;
    }
    public int getAmigosEmComum() {
        return amigosEmComum;
    }
    @Override
    public String toString() {
        return nome + " (amigos em comum: " + amigosEmComum + ")";
    }
}