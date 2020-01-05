package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hector on 9/13/2017.
 */

public class LoadingScreen extends GeneralScreen {

    private GeneralGame generalGame;
    private Array<Sprite> gif;
    private Sprite img;
    private float time, screenCounter;
    private int index;
    private Sprite loadingText;
    //Cargador
    private AssetManager manager;
    private Screen nextScreen;

    public LoadingScreen(GeneralGame generalGame, Screen nextScreen){
        this.generalGame = generalGame;
        manager = generalGame.getManager();
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(null);
        gif = new Array<Sprite>();
        time = 0;
        screenCounter=0;
        index = 0;

        loadFrames();

        loadingText = new Sprite(new Texture("LoadingAssets/LoadingText.png"));
        loadingText.scale(-.4f);
        loadingText.setPosition(ANCHO/2-loadingText.getWidth()/2, ALTO*.25f);

        cargarRecursos();


    }


    // Carga los recursos de la siguiente pantalla
    private void cargarRecursos() {
        manager = generalGame.getManager();
        //avance = 0;
        switch (nextScreen) {
            case MENU:
                cargarRecMenuPrincipal();
                break;
            case LEVEL_SELECTION:
                cargarRecLevelSelection();
                break;
            case CHAT_CITY:
                cargarRecChatCity();
                break;
            case CIRCUIT_CIRCUS:
                cargarRecCircuitCircus();
                break;
            case INFORMATION:
                cargarRecInformation();
                break;
            case MENU2:
                cargarRecMenuPrincipal();
                break;
        }
    }


    private void cargarRecInformation() {
        manager.load("InformationAssets/Dache.png", Texture.class);
        manager.load("InformationAssets/Dan.png", Texture.class);
        manager.load("InformationAssets/Chema.png", Texture.class);
        manager.load("InformationAssets/Neto.png", Texture.class);
        manager.load("InformationAssets/Takami.png", Texture.class);
        manager.load("InformationAssets/homeButton.png", Texture.class);
        manager.load("InformationAssets/okButton.png", Texture.class);
        manager.load("InformationAssets/textVideoGames.png", Texture.class);


        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.load(nombre, Texture.class);
        }
    }


    private void cargarRecCircuitCircus() {
        manager.load("CircuitCircusAssets/CircuitCityMap.tmx", TiledMap.class);
        manager.load("MundoPrueba/Tira.png", Texture.class);
        manager.load("ChatCityAssets/explosion.png", Texture.class);
        manager.load("CircuitCircusAssets/ElectricBall64.png", Texture.class);
        manager.load("CircuitCircusAssets/Wires 64.png", Texture.class);
        manager.load("ChatCityAssets/Images/Checkpoint96.png", Texture.class);
        manager.load("ChatCityAssets/Images/Coin.png", Texture.class);

        manager.load("BotonesJuego/Stop.png", Texture.class);
        manager.load("ChatCityAssets/pauseButton.png", Texture.class);
        manager.load("BotonesJuego/capsula.png", Texture.class);
        manager.load("MenuAssets/PauseScreen.png", Texture.class);

        manager.load("CircuitCircusAssets/Background2Down.png", Texture.class);
        manager.load("CircuitCircusAssets/Background2Middle.png", Texture.class);
        manager.load("CircuitCircusAssets/Background2Up.png", Texture.class);
        manager.load("ChatCityAssets/Images/blackHole.png", Texture.class);
        manager.load("ChatCityAssets/EmptySquare64.png", Texture.class);
        manager.load("ChatCityAssets/homeButton.png", Texture.class);
        manager.load("ChatCityAssets/continueButton.png", Texture.class);


    }

    private void cargarRecChatCity() {
        manager.load("MundoPrueba/Tira.png", Texture.class);
        manager.load("ChatCityAssets/explosion.png", Texture.class);
        manager.load("ChatCityAssets/ChatCityMap.tmx", TiledMap.class);
        manager.load("ChatCityAssets/ghost.png", Texture.class);
        manager.load("ChatCityAssets/AngryEmoji64.png", Texture.class);
        manager.load("ChatCityAssets/Images/Checkpoint96.png", Texture.class);
        manager.load("ChatCityAssets/Images/Coin.png", Texture.class);
        manager.load("ChatCityAssets/EmptySquare64.png", Texture.class);
        manager.load("BotonesJuego/Stop.png", Texture.class);
        manager.load("ChatCityAssets/pauseButton.png", Texture.class);
        manager.load("BotonesJuego/capsula.png", Texture.class);
        manager.load("MenuAssets/PauseScreen.png", Texture.class);
        manager.load("ChatCityAssets/background.jpg", Texture.class);
        manager.load("ChatCityAssets/homeButton.png", Texture.class);
        manager.load("ChatCityAssets/continueButton.png", Texture.class);

    }

    private void cargarRecLevelSelection() {
        manager.load("InformationAssets/homeButton.png", Texture.class);
        manager.load("LevelSelectionAssets/ChatCityButton.png", Texture.class);
        manager.load("LevelSelectionAssets/CircuitCircusButton.png", Texture.class);

        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.load(nombre, Texture.class);
        }
    }


    private void cargarRecMenuPrincipal() {
        manager.load("MenuAssets/logo.png",Texture.class);
        manager.load("MenuAssets/playButton.png",Texture.class);
        manager.load("MenuAssets/musicButton.png",Texture.class);
        manager.load("MenuAssets/muteButton.png",Texture.class);
        manager.load("MenuAssets/infoButton.png",Texture.class);

        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.load(nombre, Texture.class);
        }
    }



    @Override
    public void render(float delta) {

        clearScreen(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        //Draw Frame
        gif.get(index).draw(batch);


        //Draw text
        loadingText.draw(batch);
        batch.end();

        //Time
        time+=Gdx.graphics.getDeltaTime();
        screenCounter+=Gdx.graphics.getDeltaTime();
        //Check the time the loading screen is displayed


        //Valor original 7f
        if (screenCounter>=4f){
            //generalGame.setScreen(new MenuScreen(generalGame));
        }

        //Frame index
        if(time>=0.1f){
            index++;
            time=0;
        }
        //Restart frames
        if(index==gif.size){
            index=0;
        }
        actualizarCargador();
        img.rotate(-3);


    }


    private void actualizarCargador() {
        screenCounter+=Gdx.graphics.getDeltaTime();
        if (manager.update()) {
            // Terminó de cargar
            switch (nextScreen){
                case MENU:
                    generalGame.setScreen(new MenuScreen(generalGame));
                    generalGame.controlMusic("menuScreen");
                    break;
                case LEVEL_SELECTION:
                    generalGame.setScreen(new LevelSelectionScreen(generalGame));
                    generalGame.controlMusic("menuScreen");
                    break;
                case CHAT_CITY:
                    generalGame.setScreen(new ChatCityLevel(generalGame));
                    generalGame.controlMusic("chatCity");
                    break;
                case CIRCUIT_CIRCUS:
                    generalGame.setScreen(new CircuitCircusLevel(generalGame));
                    generalGame.controlMusic("circuitCircus");
                    break;
                case INFORMATION:
                    generalGame.setScreen(new InformationScreen(generalGame));
                    break;
                case MENU2:
                    generalGame.setScreen(new MenuScreen(generalGame));
                    generalGame.controlMusic("menuScreen");
                    break;

            }
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

    private void loadFrames() {
        String nombre = String.format("LoadingAssets/loading.png");
        img = new Sprite(new Texture(nombre));
        img.setPosition(ANCHO/2-img.getWidth()/2, ALTO*.47f);
        img.scale(0.7f);
        gif.add(img);
    }

}
