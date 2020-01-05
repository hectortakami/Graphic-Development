package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by netocervantes on 24/10/17.
 */

public class Personaje extends Objeto{


    protected EstadoMovimiento estadoMovimiento;
    protected EstadoSalto estadoSalto;



    private float time;
    protected float DX = 3f; //16
    protected float vx = 200f;
    protected float DY = 32;
    private float yOriginal;
    private float alturaSalto;



    public Personaje(Texture textura, float x, float y){ }

    public Personaje(){
    }

    public void move(TiledMap mapa){
        DX = vx* Gdx.graphics.getDeltaTime();
        float newX = sprite.getX();
        if (getEstadoMovimiento() == EstadoMovimiento.DERECHA) {
            if (revisarPisoCaminar(mapa)) {
                newX += DX;
                if (newX < ChatCityLevel.ANCHO - sprite.getWidth()) {
                    sprite.setX(newX);
                } else{
                    setEstadoMovimiento(EstadoMovimiento.IZQUIERDA);
                    vx = - vx;
                }
            } else {
                setEstadoMovimiento(EstadoMovimiento.IZQUIERDA);
                vx = - vx;
            }
        }
        if(getEstadoMovimiento() == EstadoMovimiento.IZQUIERDA){
            if (revisarPisoCaminar(mapa)) {
                newX += DX;
                if (newX > 0 - sprite.getWidth() / 3) {
                    sprite.setX(newX);
                } else{
                    setEstadoMovimiento(EstadoMovimiento.DERECHA);
                    vx = -vx;
                }
            }
            else{
                setEstadoMovimiento(EstadoMovimiento.DERECHA);
                vx= -vx;
            }
        }
    }




    //Iniciar salto
    public void saltar(){
        if(estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO){
            //Inicia
            estadoSalto = EstadoSalto.SUBIENDO;
            yOriginal = sprite.getY();
            alturaSalto = 0;
        }
    }


    //Mi prueba
    //revidarPiso de la clase Jhack cambiado a revisarPisoSalto
    public int revisarPisoSalto(TiledMap mapa){

        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        int xAbajo = (int) (sprite.getX() / 32);
        int yAbajo = (int) ((sprite.getY() - 32) / 32);

        int xAbajo2 = (int) ((sprite.getX() + 32) /32);
        int yAbajo2 = (int) ((sprite.getY() - 32) / 32);

        if(getEstadoSalto() == Personaje.EstadoSalto.SALTANDO){

            TiledMapTileLayer.Cell celdaAbajo = capa.getCell(xAbajo,yAbajo);
            TiledMapTileLayer.Cell celdaAbajo2 = capa.getCell(xAbajo2,yAbajo2);
            if(getEstadoMovimiento() == EstadoMovimiento.IZQUIERDA){
                if(celdaAbajo != null ){
                    Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                    if("piso".equals(tipoAbajo)){
                        yAbajo = (yAbajo + 1) * 32;
                        return yAbajo;
                    }
                }
            }else{
                if(celdaAbajo != null){
                    Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                    if("piso".equals(tipoAbajo)){
                        yAbajo = (yAbajo + 1) * 32;
                        return yAbajo;
                    }
                }
                if(celdaAbajo2 != null ){
                    Object tipoAbajo2 = (Object) celdaAbajo2.getTile().getProperties().get("tipo");
                    if("piso".equals(tipoAbajo2)){
                        yAbajo2 = (yAbajo2 + 1) * 32;
                        return yAbajo2;
                    }
                }
            }
        }
        return -1;
    }


    public boolean revisarPisoCaminar(TiledMap mapa){
        boolean existePiso = false;
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        int xAbajo = (int) (sprite.getX() /32);
        int yAbajo = (int) ((sprite.getY() - 32) / 32);

        TiledMapTileLayer.Cell celdaAbajo = capa.getCell(xAbajo,yAbajo);

        if(getEstadoMovimiento() == EstadoMovimiento.IZQUIERDA){
            if(celdaAbajo != null){
                Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                int xAbajo2 = (int) ((sprite.getX()-32) /32);
                int yAbajo2 = (int) ((sprite.getY() - 32) / 32);
                TiledMapTileLayer.Cell celdaAbajo2 = capa.getCell(xAbajo2,yAbajo2);
                if(celdaAbajo2 != null){
                    Object tipoAbajo2 = (Object) celdaAbajo2.getTile().getProperties().get("tipo");
                    if("piso".equals(tipoAbajo)){
                        existePiso = true;
                    }
                }
            }

        }
        else{
            int xAbajo2 = (int) ((sprite.getX() + 32) /32);
            int yAbajo2 = (int) ((sprite.getY() - 32) / 32);
            TiledMapTileLayer.Cell celdaAbajo2 = capa.getCell(xAbajo2,yAbajo2);
            /*if(celdaAbajo != null){
                Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                if(tipoAbajo.equals("piso")){
                    existePiso = true;
                }
            }*/
            if(celdaAbajo2 != null){
                Object tipoAbajo2 = (Object) celdaAbajo2.getTile().getProperties().get("tipo");
                if(tipoAbajo2.equals("piso")){
                    existePiso = true;
                }
            }
        }
        return existePiso;
    }


    // Accesor del estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador del estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    // Accesor del estadoSalto
    public EstadoSalto getEstadoSalto(){ return estadoSalto;}

    // Modificador del estadoSalto
    public void setEstadoSalto(EstadoSalto estadoSalto){
        this.estadoSalto = estadoSalto;
    }




    public enum EstadoMovimiento{
        DERECHA,
        IZQUIERDA
    }

    public enum EstadoSalto{
        SUBIENDO,
        BAJANDO,
        EN_PISO,
        SALTANDO
    }

}