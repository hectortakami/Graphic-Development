package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hector on 9/28/2017.
 */

public class LevelSelectionScreen extends GeneralScreen{

    private GeneralGame generalGame;

    //Buttons
    private Texture backButton, chatCityButton, circuitCircusButton;
    private Sprite img;
    private Array<Sprite> gifBackground;

    //Scene
    private Stage scene;
    private int index;
    private float time;

    //Cargador
    private AssetManager manager;

    public LevelSelectionScreen(GeneralGame generalGame){
        this.generalGame = generalGame;
        Gdx.input.setCatchBackKey(true);
    }


    @Override
    public void show() {
        gifBackground = new Array<Sprite>();
        loadTextures();
        loadFrames();
        createScene();
        Gdx.input.setInputProcessor(scene);
        time = 0;
        index = 0;

    }

    private void loadTextures() {

        manager = generalGame.getManager();
        backButton = manager.get("InformationAssets/homeButton.png");
        chatCityButton = manager.get("LevelSelectionAssets/ChatCityButton.png");
        circuitCircusButton = manager.get("LevelSelectionAssets/CircuitCircusButton.png");
    }

    private void createScene() {
        scene = new Stage(vista);

        scene.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keyCode){
                if(keyCode== Input.Keys.BACK){
                    generalGame.setScreen(new LoadingScreen(generalGame, Screen.MENU2));
                }
                return true;
            }
        });

        //Create BackButton
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(backButton));
        ImageButton btnBack = new ImageButton(trdBack);
        btnBack.setPosition(10,ALTO-btnBack.getHeight()-10);
        scene.addActor(btnBack);

        //Create ChatCityLevel2 Button
        TextureRegionDrawable trdChat = new TextureRegionDrawable(new TextureRegion(chatCityButton));
        ImageButton btnChat = new ImageButton(trdChat);
        btnChat.setPosition(ANCHO/2-btnChat.getWidth()/2,btnBack.getY()-btnChat.getHeight()-100);
        scene.addActor(btnChat);
        // CREAR NUEVA PANTALLA DE JUEGO --> Agregar listener


        //Create CircuitCircusLevel Button
        TextureRegionDrawable tdrCircuit = new TextureRegionDrawable(new TextureRegion(circuitCircusButton));
        ImageButton btnCircuit = new ImageButton(tdrCircuit);
        btnCircuit.setPosition(btnChat.getX(),btnChat.getY()-btnCircuit.getHeight()-60);
        scene.addActor(btnCircuit);
        // CREAR NUEVA PANTALLA DE JUEGO --> Agregar listener




//*************************************************************************************************************
        //CAMBIAR LOS LISTENERS 
        //ButtonListener
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.MENU2));
                generalGame.controlMusic("menuScreen");
            }
        });

        btnChat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.CHAT_CITY));

            }
        });

        btnCircuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.CIRCUIT_CIRCUS));
            }
        });


    }


    @Override
    public void render(float delta) {
        clearScreen(0f,0f,0f);
        batch.setProjectionMatrix(camara.combined);

        //Batch to draw the background on screen
        batch.begin();
        gifBackground.get(index).draw(batch);
        batch.end();
        time+= Gdx.graphics.getDeltaTime();
        //Frame index
        if(time>=0.03f){
            index++;
            time=0;
        }
        //Restart frames
        if(index== gifBackground.size){
            index=0;
        }

        batch.begin();
        //Draw scene elements
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

        //por mi
        manager.unload("InformationAssets/homeButton.png");
        manager.unload("LevelSelectionAssets/ChatCityButton.png");
        manager.unload("LevelSelectionAssets/CircuitCircusButton.png");

        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.unload(nombre);
        }
    }



    private void loadFrames() {

        //por mi
        for(int i = 0; i< 120; i++){
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif",i);
            img = new Sprite((Texture)manager.get(nombre));
            gifBackground.add(img);
        }

        for (Sprite img:
                gifBackground) {
            img.setSize(ANCHO,ALTO);
            img.setPosition(0,0);
        }



    }
}
