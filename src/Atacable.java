//Definimos un contrato que se deben cumplir en las clases que utilicen atacable
public interface Atacable {
    String atacar(String coordenada);

    //Aqui utilizamos Overloading, mismo metodo atacar con diferentes parametros
    String atacar(String coordenada, int potencia);
    boolean recibirImpacto();
}
