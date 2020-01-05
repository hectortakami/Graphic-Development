package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hector on 9/11/2017.
 */

class MenuScreen extends GeneralScreen
{

    private GeneralGame generalGame;

    //Background Elements
    private Array<Sprite> gifBackground;
    private Sprite img;
    private float time;
    private int index;
    //Scene Elements
    private Texture logo, playButton, musicONTexture, musicOffTexture, infoButton;
    private Stage scene;
    //Music
    private ImageButton btnMusicON, btnMusicOFF;
    //Cargador
    private AssetManager manager;




    public MenuScreen(GeneralGame generalGame) {

        this.generalGame = generalGame;
    }

    @Override
    public void show() {
        loadTextures();
        createButtons();
        //Scene
        Gdx.input.setInputProcessor(scene);

        //BackGround Sprite Array
        gifBackground = new Array<Sprite>();
        loadFrames();

        //Time and index
        time=0;
        index=0;

    }


    public void loadTextures(){
        manager = generalGame.getManager();
        logo = manager.get("MenuAssets/logo.png");
        playButton = manager.get("MenuAssets/playButton.png");
        musicONTexture = manager.get("MenuAssets/musicButton.png");
        musicOffTexture = manager.get("MenuAssets/muteButton.png");
        infoButton = manager.get("MenuAssets/infoButton.png");

    }

    public void createButtons(){
        //Initialize Scene
        scene = new Stage(vista);

        //Logo
        TextureRegionDrawable trdLogo = new TextureRegionDrawable(new TextureRegion(logo));
        ImageButton logoImage = new ImageButton(trdLogo);
        logoImage.setPosition(ANCHO/2-logoImage.getWidth()/2,ALTO*.75f);
        scene.addActor(logoImage);

        //Configure Buttons
        //Play Button
        TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(playButton));
        ImageButton btnPlay = new ImageButton(trdPlay);
        btnPlay.setPosition(ANCHO-btnPlay.getWidth()-15, ALTO*.37f);

        //Information Button
        TextureRegionDrawable trdInfo = new TextureRegionDrawable(new TextureRegion(infoButton));
        ImageButton btnInfo = new ImageButton(trdInfo);
        btnInfo.setPosition(btnPlay.getX()-btnInfo.getWidth()/3+25,btnPlay.getY()-btnInfo.getHeight()+15);

        //Configuration Button
        TextureRegionDrawable trdMusicON = new TextureRegionDrawable(new TextureRegion(musicONTexture));
        btnMusicON = new ImageButton(trdMusicON);
        btnMusicON.setPosition(btnPlay.getX()-btnMusicON.getWidth()+30,btnPlay.getY()-60);

        TextureRegionDrawable trdMusicOFF = new TextureRegionDrawable(new TextureRegion(musicOffTexture));
        btnMusicOFF = new ImageButton(trdMusicOFF);
        btnMusicOFF.setPosition(btnPlay.getX()-btnMusicON.getWidth()+30,btnPlay.getY()-60);


        //Add them to scene
        scene.addActor(btnPlay);
        scene.addActor(btnInfo);
        scene.addActor(btnMusicON);


        //ButtonListener
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.LEVEL_SELECTION));

            }
        });

        btnInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.INFORMATION));
            }
        });

        if(generalGame.enableMusic==true){

        }
        btnMusicON.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setEnableMusic(false);

            }
        });

        btnMusicOFF.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setEnableMusic(true);

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

        //MusicControl
        if(generalGame.enableMusic==true){
            scene.addActor(btnMusicON);
            btnMusicOFF.remove();
        }
        if(generalGame.enableMusic==false){
            scene.addActor(btnMusicOFF);
            btnMusicON.remove();
        }
        generalGame.controlMusic("menuScreen");

        //Batch to draw elements on screen
        batch.begin();
        //Draw Scene
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
        manager.unload("MenuAssets/logo.png");
        manager.unload("MenuAssets/playButton.png");
        manager.unload("MenuAssets/musicButton.png");
        manager.unload("MenuAssets/muteButton.png");
        manager.unload("MenuAssets/infoButton.png");

        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.unload(nombre);
        }

    }

    private void loadFrames() {

        for(int i = 0; i< 120; i++){
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif",i);
            Sprite sprite = new Sprite((Texture)manager.get(nombre));
            gifBackground.add(sprite);
        }
        for (Sprite sprite:
             gifBackground) {
            sprite.setSize(ANCHO,ALTO);
            sprite.setPosition(0,0);
        }



    }

}
