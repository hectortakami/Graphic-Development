package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Hector on 9/11/2017.
 */

public abstract class GeneralScreen implements com.badlogic.gdx.Screen
{
    //Conventional dimentions of screen
    public static final float ALTO = 1280;
    public static final float ANCHO = 720;

    //Camera and view of the screen
    protected OrthographicCamera camara;
    protected Viewport vista;

    //Object to draw in screen
    protected SpriteBatch batch;

    public GeneralScreen(){
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2, ALTO/2, 0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
        batch = new SpriteBatch();
    }

    //Clear screen
    protected void clearScreen(float r, float g, float b){
        Gdx.gl.glClearColor(r,g,b,1); //Black screen color RGB
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public enum Screen
    {
        MENU,
        CHAT_CITY,
        CIRCUIT_CIRCUS,
        LEVEL_SELECTION,
        INFORMATION,
        MENU2
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void hide() {
        dispose();  // Free the resources of the screen
    }


}
