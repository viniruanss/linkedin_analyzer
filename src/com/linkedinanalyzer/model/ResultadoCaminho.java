import java.util.ArrayList;
import java.util.List;

public class ResultadoCaminho {
    private List<String> caminho;
    private int custo;

    public ResultadoCaminho (List<String> caminho, Integer custo){
        this.caminho = caminho;
        this.custo = custo;
    }

    public ResultadoCaminho (){
        this.caminho = new ArrayList<>();
        this.custo = -1;
    }

    public List<String> getCaminho(){
        return caminho;
    }

    public Integer getCusto(){
        return custo;
    }
}
