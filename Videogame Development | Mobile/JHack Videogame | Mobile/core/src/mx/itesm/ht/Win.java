package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by josemariaaguinigadiaz on 27/10/17.
 */

public class Win extends GeneralScreen{
    private GeneralGame generalGame;
    private Stage scene;

    private int level;

    private Sprite background;
    private Text text = new Text(2);
    private Text text2 = new Text(2);
    private Text text3 = new Text(2);
    private Text text4 = new Text(2);
    private Text text5 = new Text(2);
    private Text text6 = new Text(2);
    private float screenCounter;


    public Win(GeneralGame generalGame, int level){
        this.generalGame = generalGame;
        this.level = level;


    }



    @Override
    public void show() {

        //loadTextures();
        //createButtons();
        background = new Sprite(new Texture("InformationAssets/background.jpeg"));
        background.setPosition(0,0);
        //crearHUD();


        Gdx.input.setInputProcessor(scene);



    }



    @Override
    public void render(float delta) {

        clearScreen(0f,0f,0f);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        background.draw(batch);
        batch.end();

        switch (level) {

            case 1:
                batch.begin();
                text.MostrarMensaje(batch, "Felicidades", ANCHO / 2, 6 * ALTO / 8);
                text2.MostrarMensaje(batch, "Ingresa ahora ", ANCHO / 2, 5 * ALTO / 8);
                text3.MostrarMensaje(batch, "al 2ndo nivel", ANCHO / 2, 4 * ALTO / 8);
                text4.MostrarMensaje(batch, "si te atreves...", ANCHO / 2, 3 * ALTO / 8);
                batch.end();
                break;

            case 2:
                batch.begin();
                text.MostrarMensaje(batch, "Felicidades ¿De ", ANCHO / 2, 7 * ALTO / 8);
                text2.MostrarMensaje(batch, "verdad llegaste", ANCHO / 2, 6 * ALTO / 8);
                text3.MostrarMensaje(batch, "al ", ANCHO / 2, 5 * ALTO / 8);
                text4.MostrarMensaje(batch, "final? ", ANCHO / 2, 4 * ALTO / 8);
                text5.MostrarMensaje(batch,"Demuestramelo", ANCHO/2, 3*ALTO/8);
                text6.MostrarMensaje(batch,"otra vez..", ANCHO/2,2*ALTO/8);
                batch.end();
                break;
        }
        //Draw scene elements
        //hola





        //Time
        screenCounter += Gdx.graphics.getDeltaTime();

        if(screenCounter>= 2.8f){
            generalGame.setScreen(new LoadingScreen(generalGame, Screen.LEVEL_SELECTION));
        }

    }



    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
