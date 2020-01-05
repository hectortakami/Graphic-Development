package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by josemariaaguinigadiaz on 19/10/17.
 */

public class CircuitCircusLevel extends GeneralScreen {

    private Stage scene;

    //Camara Taka

    //Fondo
    private Sprite background;
    private Sprite backgroundTwo;
    private Sprite backgroundThree;

    private GeneralGame generalGame;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Texture texturaJhack, pausa;

    private Texture explotionTexture;
    private Texture texturaMon;

    private jHack jHack;
    private static final float ALTO_MAPA = 8400;
    private static final int TEXTO_Y = 635;
    private Text scoreBoard = new Text(1);

    private String coins = "00";

    private EstadoJuego state;

    //Pruba Ghost
    private Texture electricBallTexture;
    private ElectricBall electricBall;

    private Texture badWiresTexture;
    private BadWires badWires;

    private Texture goodWiresTexture;
    private GoodWires goodWires;

    private Texture textureCheckpoint;
    private Checkpoint checkpoint;

    private Texture textureCoin;
    private Coin coin;


    //HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage sceneHUD;

    private PauseScene pauseScene;



    private float timer;
    private Sprite hole;
    private Sprite emptyHole;
    private Sprite empty;
    private boolean win = false;
    private ArrayList<ElectricBall> arrElectricBall = new ArrayList<ElectricBall>();
    private ArrayList<GoodWires> arrGoodWires = new ArrayList<GoodWires>();
    private ArrayList<BadWires> arrBadWires = new ArrayList<BadWires>();
    private ArrayList<Checkpoint> arrCheckpoint = new ArrayList<Checkpoint>();
    private ArrayList<Coin> arrCoin = new ArrayList<Coin>();
    private AssetManager manager = new AssetManager();

    private Music dieSound;
    private Music jumpSound;

    private float dyingTimer = 0;

    public CircuitCircusLevel(GeneralGame generalGame) {
        this.generalGame = generalGame;

    }

    @Override
    public void show() {
        manager = generalGame.getManager();
        dieSound = generalGame.getDieSound();
        jumpSound = generalGame.getJumpSound();

        loadMap();
        readMatrix();



        state = EstadoJuego.JUGANDO;
        createHUD();
        Gdx.input.setInputProcessor(sceneHUD);
    }

    private void createHUD() {
        //Cámara HUD
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camara.position.set(ANCHO / 2, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        sceneHUD = new Stage(vistaHUD);

        //Salto
        Texture salto = manager.get("BotonesJuego/Stop.png");


        TextureRegionDrawable trBtnSalto = new TextureRegionDrawable(new TextureRegion(salto));
        ImageButton btnSalto = new ImageButton(trBtnSalto);
        btnSalto.setPosition(1, 1);
        btnSalto.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (generalGame.enableMusic && jHack.estadoJuego!= mx.itesm.ht.jHack.EstadoJuego.PERDIO && state!= EstadoJuego.PAUSADO ){
                    jumpSound.play();
                }
                jHack.saltar();
                return true;
            }
        });
        sceneHUD.addActor(btnSalto);


        //Pausa
        pausa = manager.get("ChatCityAssets/pauseButton.png");
        //Texture texturaPausa = manager.get("BotonesJuego/Pausa.png");
        TextureRegionDrawable trBtnPausa = new TextureRegionDrawable(new TextureRegion(pausa));
        ImageButton btnPausa = new ImageButton(trBtnPausa);
        btnPausa.setPosition(ANCHO - btnPausa.getWidth(), ALTO - btnPausa.getHeight());
        btnPausa.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state = state == EstadoJuego.PAUSADO ? EstadoJuego.JUGANDO : EstadoJuego.PAUSADO;

                if (state == EstadoJuego.PAUSADO) {
                    if (pauseScene == null) {
                        pauseScene = new PauseScene(vistaHUD, batch);
                    }
                    Gdx.input.setInputProcessor(pauseScene);
                }
                return true;


            }


        });
        sceneHUD.addActor(btnPausa);

        //Cápsula
        texturaMon = manager.get("BotonesJuego/capsula.png");
        TextureRegionDrawable trdMon = new TextureRegionDrawable(new TextureRegion(texturaMon));
        ImageButton btnMon= new ImageButton(trdMon);
        btnMon.setPosition(-50,ALTO-btnMon.getHeight());

        sceneHUD.addActor(btnMon);
    }

    private void readMatrix() {
        TiledMapTileLayer capa = (TiledMapTileLayer) map.getLayers().get(2);
        for (int x = 0; x < capa.getWidth(); x++) {
            for (int y = 0; y < /*capa.getHeight()*/3470; y++) {  // Hasta que no se ponga màs pequeño el map usaremos ese numero
                TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                if (celda != null) {
                    java.lang.Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("fantasma".equals(tipo)) {
                        electricBallTexture = manager.get("CircuitCircusAssets/ElectricBall64.png");
                        electricBall = new ElectricBall(electricBallTexture, x * 32, y * 32);
                        arrElectricBall.add(electricBall);
                        capa.setCell(x, y, null);
                    }
                    if ("emoji".equals(tipo)) {
                        badWiresTexture = manager.get("CircuitCircusAssets/Wires 64.png");
                        badWires = new BadWires(badWiresTexture, x * 32, y * 32);
                        arrBadWires.add(badWires);

                        //Enemigo GoodEmoji
                        goodWiresTexture = manager.get("CircuitCircusAssets/Wires 64.png");
                        goodWires = new GoodWires(goodWiresTexture, x * 32, y * 32);
                        arrGoodWires.add(goodWires);
                        capa.setCell(x, y, null);
                    }
                    if ("checkpoint2".equals(tipo)) {

                        textureCheckpoint = manager.get("ChatCityAssets/Images/Checkpoint96.png");
                        checkpoint = new Checkpoint(textureCheckpoint, x*32, y*32);
                        arrCheckpoint.add(checkpoint);
                        capa.setCell(x, y, null);
                    }
                    if("checkpointOculto".equals(tipo)){
                        empty = new Sprite(new Texture("ChatCityAssets/EmptySquare64.png"));
                        empty.setPosition(x*32, y*32);
                    }
                }
            }
        }
        TiledMapTileLayer capaCoin = (TiledMapTileLayer) map.getLayers().get(1);
        for(int x = 0; x < capaCoin.getWidth(); x++){
            for(int y = 0; y < capaCoin.getHeight(); y++) {
                TiledMapTileLayer.Cell celda = capaCoin.getCell(x, y);
                if (celda != null) {
                    java.lang.Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("moneda".equals(tipo)) {
                        textureCoin = manager.get("ChatCityAssets/Images/Coin.png");
                        coin = new Coin(textureCoin, x*32, y*32);
                        arrCoin.add(coin);
                        capaCoin.setCell(x, y, null);
                    }
                }
            }
        }
    }

    private void loadMap() {
        background = new Sprite((Texture)manager.get("CircuitCircusAssets/Background2Down.png"));
        backgroundTwo = new Sprite((Texture)manager.get("CircuitCircusAssets/Background2Middle.png"));
        backgroundThree = new Sprite((Texture)manager.get("CircuitCircusAssets/Background2Up.png"));

        background.setPosition(0, 0);
        backgroundTwo.setPosition(0,3841);
        backgroundThree.setPosition(0,7683);

        hole = new Sprite((Texture)manager.get("ChatCityAssets/Images/blackHole.png"));
        hole.setPosition(ANCHO/2-hole.getWidth()/2, ALTO_MAPA-hole.getHeight() - 448);

        emptyHole = new Sprite((Texture)manager.get("ChatCityAssets/EmptySquare64.png"));
        emptyHole.setPosition(ANCHO/2 - emptyHole.getWidth()/2, ALTO_MAPA - (hole.getHeight() - emptyHole.getHeight()) - 448);


        map = manager.get("CircuitCircusAssets/CircuitCityMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        texturaJhack = manager.get("MundoPrueba/Tira.png");
        explotionTexture = manager.get("ChatCityAssets/explosion.png");
        //Posición inicial de jHack
        jHack = new jHack(texturaJhack, explotionTexture,32, 32,generalGame);
    }




    @Override
    public void render(float delta) {
        clearScreen(0f, 0f, 0f);
        //Update
        updateJumpJhack(delta, map);

        updateRotation();

        if(win){
            generalGame.setScreen(new Win(generalGame, 2));
        }

        //jHack.score(map);
        if(state != EstadoJuego.PAUSADO){
            jHack.move(map);
        }

        updateCamera();



        // Draw
        batch.setProjectionMatrix(camara.combined);
        renderer.setView(camara);
        renderer.render();
        batch.begin();
        background.draw(batch);
        backgroundTwo.draw(batch);
        backgroundThree.draw(batch);
        batch.end();

        //HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        sceneHUD.draw();


        // Draw
        batch.setProjectionMatrix(camara.combined);
        renderer.setView(camara);
        renderer.render();
        batch.begin();



        //Verificar enemigo
        jHack.verificarColisiones(map);

        for(int i = 0; i< arrElectricBall.size(); i++){

            arrElectricBall.get(i).render(batch);
            if(state != EstadoJuego.PAUSADO && jHack.getEstadoJuego() != mx.itesm.ht.jHack.EstadoJuego.PERDIO){
                arrElectricBall.get(i).move(map);
                //arrElectricBall.get(i).sprite.rotate(2);
            }
            ghostCollision(arrElectricBall.get(i));
        }

        for(int i = 0; i< arrCheckpoint.size(); i++){
            arrCheckpoint.get(i).render(batch);
            if(jHack.getPoints() >= 20){
                colCheckpoint(arrCheckpoint.get(i));
            }

        }

        for(int i = 0; i< arrCoin.size(); i++){
            arrCoin.get(i).render(batch);
            colCoin(arrCoin.get(i));
        }

        //Dibuja al emoji, lo anima y verifica sus colisiones
        timer+=Gdx.graphics.getDeltaTime();
        if(timer >= 2.5f && timer <= 5.2f){
            for(int i = 0; i< arrBadWires.size(); i++){
                arrBadWires.get(i).render(batch);
                badEmojiCollision(arrBadWires.get(i));
            }
            if(timer >= 5f){
                timer = 0;
            }
        }
        else{
            for(int i = 0; i< arrGoodWires.size(); i++){
                arrGoodWires.get(i).render(batch);
            }
        }



        hole.draw(batch);
        emptyHole.draw(batch);
        jHack.render(batch);
        batch.end();

        //HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        sceneHUD.draw();

        batch.begin();
        //updateMessage();
        scoreBoard.MostrarMensaje(batch,updateMessage(coins), 11*ANCHO/64-45,ALTO-35f);
        batch.end();

        if (state == EstadoJuego.PAUSADO) {
            pauseScene.draw();
            generalGame.pauseCircuitCircus();
        }

        colBlackHole();
        hole.rotate(2);

    }

    private void updateRotation() {
        for(int i = 0; i< arrElectricBall.size(); i++){
            arrElectricBall.get(i).rotate();
        }
    }

    private String updateMessage(String coins){

        if(jHack.getPoints() != Integer.parseInt(coins)){
            if(jHack.getPoints() < 10){
                coins = "0"+ Integer.toString(jHack.getPoints());
            }else {
                coins = Integer.toString(jHack.getPoints());
            }


        }
        return coins;
    }

    private void updateCamera() {
        // Depende de la posición del personaje. Siempre sigue al personaje
        float posY = jHack.getY();
        // Primera mitad de la pantalla
        if (posY < ALTO/2 ) {
            camara.position.set(ANCHO/2, ALTO/2, 0);
        } else if (posY > ALTO_MAPA - ALTO/2) {   // Última mitad de la pantalla
            camara.position.set(camara.position.x, ALTO_MAPA-ALTO/2,0);
        } else {    // En 'medio' del map
            camara.position.set(camara.position.x, posY,0);
        }
        camara.update();
    }

    private void updateJumpJhack(float delta, TiledMap mapa) {
        jHack.actualizar(delta, mapa);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

        manager.unload("CircuitCircusAssets/CircuitCityMap.tmx");
        manager.unload("MundoPrueba/Tira.png");
        manager.unload("ChatCityAssets/explosion.png");
        manager.unload("CircuitCircusAssets/ElectricBall64.png");
        manager.unload("CircuitCircusAssets/Wires 64.png");
        manager.unload("ChatCityAssets/Images/Checkpoint96.png");
        manager.unload("ChatCityAssets/Images/Coin.png");

        manager.unload("BotonesJuego/Stop.png");
        manager.unload("ChatCityAssets/pauseButton.png");
        manager.unload("BotonesJuego/capsula.png");
        manager.unload("MenuAssets/PauseScreen.png");
        manager.unload("ChatCityAssets/homeButton.png");
        manager.unload("ChatCityAssets/continueButton.png");

        manager.unload("CircuitCircusAssets/Background2Down.png");
        manager.unload("CircuitCircusAssets/Background2Middle.png");
        manager.unload("CircuitCircusAssets/Background2Up.png");
        manager.unload("ChatCityAssets/Images/blackHole.png");
        manager.unload("ChatCityAssets/EmptySquare64.png");

    }

    enum EstadoJuego {
        JUGANDO,
        PAUSADO
    }

    public EstadoJuego getState() {
        return state;
    }

    private class PauseScene extends Stage {

        public PauseScene(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture trdPaus = manager.get("MenuAssets/PauseScreen.png");
            Image imgPa = new Image(trdPaus);
            imgPa.setPosition(3*ANCHO/32, ALTO/16);
            Pixmap pixmap = new Pixmap((int) (ALTO * 0.401f), (int) (ALTO * 0.520f), Pixmap.Format.RGBA8888);
            pixmap.setColor(1f, 1f, 1f, 0.65f);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture(pixmap);
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
//            imgRectangulo.setPosition(0.15f * ANCHO, 0.1f * ALTO);
//            imgRectangulo.setPosition(ANCHO / 7, 19 * ALTO / 64);
            imgRectangulo.setPosition(ANCHO/2-pixmap.getWidth()/2, ALTO/2-1.17f*pixmap.getHeight()/2);
            this.addActor(imgRectangulo);
            this.addActor(imgPa);

            // Salir
            Texture texturaBtnSalir = manager.get("ChatCityAssets/homeButton.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);

            //btnSalir.setPosition(ANCHO / 2 - btnSalir.getWidth() / 2, 9*ALTO / 16);
            btnSalir.setPosition((ANCHO / 2)*1.089f , ALTO / 3);
            btnSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    generalGame.controlMusic("menuScreen");
                    generalGame.setScreen(new LoadingScreen(generalGame, Screen.MENU2));
                }
            });

            this.addActor(btnSalir);
            // Continuar
            Texture texturaBtnContinuar = manager.get("ChatCityAssets/continueButton.png");
            TextureRegionDrawable trdContinuar = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnContinuar));
            ImageButton btnContinuar = new ImageButton(trdContinuar);
            //btnContinuar.setPosition(ANCHO / 2 - btnContinuar.getWidth() / 2, ALTO / 3);
            btnContinuar.setPosition(ANCHO / 2 - btnContinuar.getWidth() / 2, 8*ALTO / 16);
            btnContinuar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    generalGame.controlMusic("circuitCircus");
                    state = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(sceneHUD);

                }
            });

            this.addActor(btnContinuar);


            //Configuración
            Texture texturaBtnConfig = new Texture("ChatCityAssets/menuButton.png");
            TextureRegionDrawable trdConfig = new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));
            ImageButton btnConfig = new ImageButton(trdConfig);
            btnConfig.setPosition(ANCHO/2-btnConfig.getWidth()*1.1f,ALTO/3);
            btnConfig.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    generalGame.controlMusic("menuScreen");
                    generalGame.setScreen(new LoadingScreen(generalGame, Screen.LEVEL_SELECTION));

                }
            });
            this.addActor(btnConfig);

        }


    }

    //Prueba de colision de jhack con fantasma
    private void ghostCollision(ElectricBall f) {
        if(f.sprite.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())){
            if(generalGame.enableMusic && jHack.estadoJuego!= mx.itesm.ht.jHack.EstadoJuego.PERDIO){
                dieSound.play();
            }
            jHack.setEstadoJuego(mx.itesm.ht.jHack.EstadoJuego.PERDIO);

            dyingTimer += Gdx.graphics.getDeltaTime();
            Gdx.app.log("Tiempo que interesa", " " + dyingTimer);
            if(dyingTimer >= 1.2f){
                jHack.lose();
                dyingTimer = 0;
            }

            jHack.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        }
        return;
    }

    private void badEmojiCollision(BadWires e) {
        if(e.sprite.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())){
            if(generalGame.enableMusic && jHack.estadoJuego!= mx.itesm.ht.jHack.EstadoJuego.PERDIO){
                dieSound.play();
            }
            jHack.setEstadoJuego(mx.itesm.ht.jHack.EstadoJuego.PERDIO);

            dyingTimer += Gdx.graphics.getDeltaTime();
            Gdx.app.log("Tiempo que interesa", " " + dyingTimer);
            if(dyingTimer >= 1.2f){
                jHack.lose();
                dyingTimer = 0;
            }

            jHack.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        }
        return;
    }


    private void colBlackHole() {
        if(emptyHole.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())){
            win = true;
        }
    }

    public void colCheckpoint(Checkpoint c){
        if(c.sprite.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())){
            jHack.setPosInitialX(c.sprite.getX());
            jHack.setPosInitialY((c.sprite.getY()- 64));

            jHack.setPoints(jHack.getPoints() - 20);
            c.sprite.set(empty);
        }
    }

    public void colCoin(Coin c){
        if(c.sprite.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())){
            c.sprite.set(empty);
            jHack.setPoints(jHack.getPoints()+1);
            if(generalGame.enableMusic){
                Music coinSound = generalGame.getCoinSound();
                coinSound.play();
            }

        }
    }


}



