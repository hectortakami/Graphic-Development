package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.lang.*;


/**
 * Created by netocervantes on 24/10/17.
 */

public class jHack extends Personaje {
    private GeneralGame generalGame;
    private float flyingTime;
    private float flyingHeight;
    private float initialY;
    protected float DX = 18f; //3
    private float vx = 200f;
    private float G = 98.1f;
    private float initialVelocity = 200;
    private float flightTime;
    private float ymax;
    private float time;
    private float posInitialX = 32;
    private float posInitialY = 32;
    private int points = 0;

    private Animation walkingAnim;
    private Animation jumpingAnim;
    private Animation dyingAnim;



    protected EstadoJuego estadoJuego;
    private boolean died;

    private float timer;
    private long dyingTimer = 0;

    private int portalCounter = 0;
    private Music coinSound;


    public jHack(Texture texture, Texture explotionTexture, float x, float y,GeneralGame generalGame) {
        this.generalGame=generalGame;
        coinSound = generalGame.getCoinSound();
        TextureRegion region = new TextureRegion(texture);
        TextureRegion explotionRegion = new TextureRegion(explotionTexture);

        TextureRegion[][] frames = region.split(32, 64);
        TextureRegion[][] explotionFrames = explotionRegion.split(100,100);

        //Nuevo jHack altura de 64 y ancho de 32
        walkingAnim = new Animation(0.20f, frames[0][0], frames[0][1], frames[0][2], frames[0][3], frames[0][4], frames[0][5]);
        jumpingAnim = new Animation(0.20f, frames[0][1]);
        dyingAnim = new Animation(0.30f, explotionFrames[0][0],explotionFrames[0][1],explotionFrames[0][2], explotionFrames[0][3], explotionFrames[0][4], explotionFrames[0][5], explotionFrames[0][6], explotionFrames[0][7], explotionFrames[0][8], explotionFrames[0][9], explotionFrames[0][10]);

        sprite = new Sprite(frames[0][0]);
        walkingAnim.setPlayMode(Animation.PlayMode.LOOP);
        dyingAnim.setPlayMode(Animation.PlayMode.LOOP);

        timer = 0;
        dyingTimer = 0;
        sprite.setPosition(x, y);
        estadoMovimiento = EstadoMovimiento.DERECHA;
        estadoSalto = EstadoSalto.EN_PISO;
    }

    //Original
/*
    public void render(SpriteBatch batch) {

        if(estadoSalto == EstadoSalto.SALTANDO){
            timer += Gdx.graphics.getDeltaTime();
            TextureRegion region = (TextureRegion) jumpingAnim.getKeyFrame(timer);
            batch.draw(region, sprite.getX(), sprite.getY());

        } else if (estadoJuego == EstadoJuego.PERDIO){
            timer += Gdx.graphics.getDeltaTime();
            TextureRegion region = (TextureRegion) dyingAnim.getKeyFrame(timer);
            batch.draw(region, sprite.getX(), sprite.getY());

        }
        else {
            switch (estadoMovimiento) {
                case DERECHA:
                case IZQUIERDA:
                    timer += Gdx.graphics.getDeltaTime();
                    TextureRegion region = (TextureRegion) walkingAnim.getKeyFrame(timer);

                    if (estadoMovimiento == EstadoMovimiento.IZQUIERDA) {
                        if (!region.isFlipX()) {
                            region.flip(true, false);
                        }
                    } else {
                        if (region.isFlipX()) {
                            region.flip(true, false);
                        }
                    }
                    batch.draw(region, sprite.getX(), sprite.getY());
                    break;
            }
        }

    }*/

    //Copia
    public void render(SpriteBatch batch) {
        if (estadoJuego == EstadoJuego.PERDIO){
            timer += Gdx.graphics.getDeltaTime();
            TextureRegion region = (TextureRegion) dyingAnim.getKeyFrame(timer);
            batch.draw(region, sprite.getX(), sprite.getY());
        }
        else {
            switch (estadoMovimiento) {
                case DERECHA:
                case IZQUIERDA:
                    timer += Gdx.graphics.getDeltaTime();
                    TextureRegion region = (TextureRegion) walkingAnim.getKeyFrame(timer);
                    TextureRegion region2 = (TextureRegion) jumpingAnim.getKeyFrame(timer);

                    if (estadoMovimiento == EstadoMovimiento.IZQUIERDA) {
                        if (!region.isFlipX()) {
                            region.flip(true, false);
                        }
                        if(!region2.isFlipX()){
                            region2.flip(true, false);                        }
                    } else {
                        if (region.isFlipX()) {
                            region.flip(true, false);
                        }
                        if(region2.isFlipX()){
                            region2.flip(true, false);
                        }
                    }
                    if(estadoSalto == EstadoSalto.SALTANDO){
                        batch.draw(region2, sprite.getX(), sprite.getY());
                        break;
                    }
                    else{
                        batch.draw(region, sprite.getX(), sprite.getY());
                        break;
                    }
            }
        }
    }

    public void actualizar(float delta, TiledMap mapa) {
        // Calcula la nueva posición (por ahora cuando está saltando)
        if (estadoSalto == Personaje.EstadoSalto.SALTANDO) {
            flyingTime += delta * 5;   // El factor DES/ACELERA
            flyingHeight = initialVelocity * flyingTime - 0.5f * G * flyingTime * flyingTime;
            if (flyingTime > 0) { // Sabemos que está bajando
                //Sigue en el aire
                sprite.setY(initialY + flyingHeight);

                if (flyingTime > flightTime / 2) {
                    // Termina el salto
                    int newY = revisarPisoSalto(mapa);
                    if (newY != -1) {
                        sprite.setY(newY);
                        estadoSalto = Personaje.EstadoSalto.EN_PISO;
                    }

                }
            } else {
                Gdx.app.log("Regreso", "normal");
                sprite.setY(initialY);
                estadoSalto = Personaje.EstadoSalto.EN_PISO;
            }
        }
    }



//
    @Override
    public void saltar() {

        if (estadoSalto != Personaje.EstadoSalto.SALTANDO && estadoJuego != EstadoJuego.PERDIO) {
            //Iniciar el salto
            ymax = (initialVelocity * initialVelocity * initialVelocity * initialVelocity) / (2 * G);
            flightTime = (2 * initialVelocity) / G;
            flyingHeight = 0;
            flyingTime = 0;
            initialY = sprite.getY();
            estadoSalto = Personaje.EstadoSalto.SALTANDO;

            Gdx.app.log("saltar", "ymax=" + ymax + ", tiempoV=" + flightTime + ", y0=" + initialY);
        }

    }


    //Intento de verificar enemigo de la clase Personaje
    public void verificarColisiones(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
        if (getEstadoMovimiento() == EstadoMovimiento.DERECHA) {
            int xRight = (int) ((sprite.getX() + 32) / 32);
            int yRight = (int) (sprite.getY() / 32);
            int yAbove = (int) ((sprite.getY() + 64) / 32);
            int xRightRight = (int) ((sprite.getX() + 64) / 32);
            TiledMapTileLayer.Cell rightCell = layer.getCell(xRight, yRight);
            if (rightCell != null) {
                java.lang.Object rightType = rightCell.getTile().getProperties().get("tipo");

                if("portal".equals(rightType)) {
                    teleport(false);
                }
                if("exit".equals(rightType)){
                    teleport(true);
                }
            }
        } else {
            int xLeft = (int) ((sprite.getX()) / 32);
            int yLeft = (int) (sprite.getY() / 32);
            int yAbove = (int) ((sprite.getY() - 64) / 32);
            int xLeftLeft = (int) ((sprite.getX() - 64) / 32);
            TiledMapTileLayer.Cell leftCell = layer.getCell(xLeft, yLeft);
            if (leftCell != null) {
                java.lang.Object leftType = leftCell.getTile().getProperties().get("tipo");

                if("portal".equals(leftType)){
                    teleport(false);
                }
                if("exit".equals(leftType)){
                    teleport(true);
                }
            }
        }
    }

    @Override
    public boolean revisarPisoCaminar(TiledMap mapa) {
        boolean existePiso = false;
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        int xAbajo = (int) (sprite.getX() /32);
        int yAbajo = (int) ((sprite.getY() - 32) / 32);

        TiledMapTileLayer.Cell celdaAbajo = capa.getCell(xAbajo,yAbajo);

        if (getEstadoMovimiento() == EstadoMovimiento.IZQUIERDA) {
            if(celdaAbajo != null) {
                Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                if ("piso".equals(tipoAbajo)) {
                    existePiso = true;
                }
            }
        }
        if (getEstadoMovimiento() == EstadoMovimiento.DERECHA) {
            int xAbajo2 = (int) ((sprite.getX() + 32) /32);
            int yAbajo2 = (int) ((sprite.getY() - 32) / 32);
            TiledMapTileLayer.Cell celdaAbajo2 = capa.getCell(xAbajo2,yAbajo2);
            if(celdaAbajo != null){
                Object tipoAbajo = (Object) celdaAbajo.getTile().getProperties().get("tipo");
                if(tipoAbajo != null && tipoAbajo.equals("piso")){
                     existePiso = true;
                }
            }
            if(celdaAbajo2 != null){
                Object tipoAbajo2 = (Object) celdaAbajo2.getTile().getProperties().get("tipo");
                if(tipoAbajo2!= null && tipoAbajo2.equals("piso")){
                    existePiso = true;
                }
            }
        }
        return existePiso;
    }

    private void teleport(boolean valueExit) {

        int value = portalCounter;
        portalCounter++;

        if(valueExit){
            value = 5;
        }
        if(portalCounter>=3){
            portalCounter = 0;
        }
        switch (value){
            case 0:
                sprite.setPosition(ChatCityLevel.ANCHO - (2 * sprite.getWidth()), 2080);
                break;
            case 1:
                sprite.setPosition(ChatCityLevel.ANCHO - (2 * sprite.getWidth()), 2944);
                break;
            case 2:
                sprite.setPosition(ChatCityLevel.ANCHO - (2 * sprite.getWidth()), 4192);
                break;
            case 5:
                sprite.setPosition(32, 1792);
                break;
        }
    }





    @Override
    public void move(TiledMap map) {
        //Vamos a hacer el movimiento horizontal
        DX = vx* Gdx.graphics.getDeltaTime();
        // DY = vy*Gdx.graphics.getDeltaTime();
        float newX = sprite.getX();
        float newY = sprite.getY();
        //Dibuja derecha a izquierda y revisa si el bloque contiguo es pared
        if (getEstadoMovimiento() == EstadoMovimiento.DERECHA && getEstadoJuego() != EstadoJuego.PERDIO) {
            // Parte de la condicion: && existePiso == true
            if ((revisarPisoCaminar(map) && getEstadoSalto() == EstadoSalto.EN_PISO) || getEstadoSalto() == EstadoSalto.SALTANDO) {
                //Mover el personaje
                newX += DX;
                if (newX <= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    sprite.setX(newX);
                } else if (newX >= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    setEstadoMovimiento(EstadoMovimiento.IZQUIERDA);
                    vx = -vx;
                }
            } else {
                if (time >= 0.05f) {
                    newY -= DY;
                    sprite.setY(newY);
                    time = 0;
                }
                newX += DX;
                if (newX <= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    sprite.setX(newX);
                } else if (newX >= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    setEstadoMovimiento(EstadoMovimiento.IZQUIERDA);
                    vx = -vx;
                }
            }
            // ********************************* MOVIMIENTO A LA IZQUIERDA *******************************************
        } else if (getEstadoMovimiento() == EstadoMovimiento.IZQUIERDA && getEstadoJuego() != EstadoJuego.PERDIO) {
            //Parte de la condicion: && existePiso == true
            if ((revisarPisoCaminar(map) && getEstadoSalto() == EstadoSalto.EN_PISO) || getEstadoSalto() == EstadoSalto.SALTANDO) {
                newX += DX;
                if (newX >= 0 - sprite.getWidth() / 3) {
                    sprite.setX(newX);
                } else if (newX <= 0 - sprite.getWidth() / 3) {
                    setEstadoMovimiento(Personaje.EstadoMovimiento.DERECHA);
                    vx = -vx;
                }
            } else {
                if (time >= 0.05f) {
                    time = 0;
                    newY -= DY;
                    sprite.setY(newY);
                }
                newX += DX;
                if (newX <= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    sprite.setX(newX);
                } else if (newX >= ChatCityLevel.ANCHO - sprite.getWidth()) {
                    setEstadoMovimiento(EstadoMovimiento.IZQUIERDA);
                    vx = -vx;
                }
            }
        }
        time += Gdx.graphics.getDeltaTime();
    }

    public void lose(){
        sprite.setPosition(posInitialX, posInitialY);
        Gdx.app.log("AHHHHHHHHHHH", "LOLOLOLOLOLOLO");
        setEstadoJuego(EstadoJuego.JUGANDO);

    }


    public float getPosInitialX() {
        return posInitialX;
    }

    public float getPosInitialY() {
        return posInitialY;
    }

    public int getPoints() { return points;   }

    public float getY(){
        return sprite.getY();
    }

    public void setPosInitialX(float posInitialX) {
        this.posInitialX = posInitialX;
    }

    public void setPosInitialY(float posInitialY) {
        this.posInitialY = posInitialY;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public void setEstadoJuego(EstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }


    public enum EstadoJuego{
        JUGANDO,
        PERDIO,
        MURIO
    }

}
