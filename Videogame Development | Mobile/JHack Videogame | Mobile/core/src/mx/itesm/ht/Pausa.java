package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by josemariaaguinigadiaz on 17/10/17.
 */

public class Pausa extends GeneralScreen{
    private GeneralGame generalGame;
    private Stage scene;
    private Texture back, resume, menuPrin;

    public Pausa(GeneralGame generalGame){
        this.generalGame = generalGame;


    }





    @Override
    public void show() {
        loadTextures();
        createButtons();


        Gdx.input.setInputProcessor(scene);


    }

    private void createButtons() {
        scene = new Stage(vista);


        //Boton back
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(back));
        ImageButton btnBack = new ImageButton(trdBack);
        btnBack.setPosition(0,0);
        scene.addActor(btnBack);

        //Boton Resume
        TextureRegionDrawable trdRes = new TextureRegionDrawable(new TextureRegion(resume));
        ImageButton btnRes = new ImageButton(trdRes);
        btnRes.setPosition(ANCHO/4, ALTO/3);
        scene.addActor(btnRes);

        //Boton Menu Principal

        TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(menuPrin));
        ImageButton btnMenu = new ImageButton(trdMenu);
        btnMenu.setPosition(ANCHO/4, 2*ALTO/3);
        scene.addActor(btnMenu);

        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("Clicked", "Touch");


                generalGame.setScreen(new MenuScreen(generalGame));
            }
        });



        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("CLICKED","TOUCH");
                generalGame.setScreen(new ChatCityLevel2(generalGame));

            }
        });

        btnRes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("CLICKED", "TOUCH");
                generalGame.setScreen(new ChatCityLevel2(generalGame));
            }
        });








    }

    private void loadTextures() {
        back = new Texture("LevelSelectionAssets/backButton.png");
        resume = new Texture("BotonesJuego/Cuadro de diálogo.png");
        menuPrin = new Texture("BotonesJuego/Cuadro de diálogo.png");

    }

    @Override
    public void render(float delta) {
        clearScreen(0f,0f,0f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        scene.draw();
        batch.end();

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
