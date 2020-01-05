package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
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
 * Created by netocervantes on 24/10/17.
 */
public class ChatCityLevel extends GeneralScreen{

    //Fondo
    private Sprite background;
    private GeneralGame generalGame;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Texture texturaJhack, pausa;

    private Texture explotionTexture;

    private jHack jHack;
    private static final float ALTO_MAPA = 3600;
    private static final int TEXTO_Y = 635;
    private Text scoreBoard = new Text(1);



    private EstadoJuego estado;

    private Texture ghostTexture;
    private Ghost ghost;

    private Texture badEmojiTexture;
    private BadEmoji badEmoji;

    private Texture goodEmojiTexture;
    private GoodEmoji goodEmoji;

    private Texture textureCheckpoint;
    private Checkpoint checkpoint;

    private Texture coinTexture;
    private Coin coin;
    private String coins = "00";



    //HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage sceneHUD;

    private PauseScene pauseScene;
    //private DeadScene deadScene;

    private Texture texturaMon;

    private float timer;
    private Sprite hole;
    private Sprite emptyHole;
    private Sprite empty;
    private boolean win = false;
    private ArrayList<Ghost> arrFantasma = new ArrayList<Ghost>();
    private ArrayList<GoodEmoji> arrGoodEmoji = new ArrayList<GoodEmoji>();
    private ArrayList<BadEmoji> arrBadEmoji = new ArrayList<BadEmoji>();
    private ArrayList<Checkpoint> arrCheckpoint = new ArrayList<Checkpoint>();
    private ArrayList<Coin> arrCoin = new ArrayList<Coin>();
    private AssetManager manager = new AssetManager();
    private Music dieSound;
    private Music jumpSound;
    private float dyingTimer;


    //Constructor
    public ChatCityLevel(GeneralGame generalGame){
        this.generalGame = generalGame;

    }


    @Override
    public void show() {
        dieSound = generalGame.getDieSound();
        jumpSound = generalGame.getJumpSound();
        loadMap();
        readMatrix();
        estado = EstadoJuego.JUGANDO;
        createHUD();

        Gdx.input.setInputProcessor(sceneHUD);
    }


    private void readMatrix() {
        TiledMapTileLayer capa = (TiledMapTileLayer) map.getLayers().get(2);
        for(int x = 0; x < capa.getWidth(); x++){
            for(int y = 0; y < capa.getHeight(); y++) {
                TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                if (celda != null) {
                    java.lang.Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("fantasma".equals(tipo)) {
                        ghostTexture = manager.get("ChatCityAssets/ghost.png");
                        ghost = new Ghost(ghostTexture, x*32, y*32);
                        arrFantasma.add(ghost);
                        capa.setCell(x, y, null);
                    }

                    if ("emoji".equals(tipo)) {
                        badEmojiTexture = manager.get("ChatCityAssets/AngryEmoji64.png");
                        badEmoji = new BadEmoji(badEmojiTexture, x*32, y*32);
                        arrBadEmoji.add(badEmoji);

                        //Enemigo GoodEmoji
                        goodEmojiTexture = manager.get("ChatCityAssets/AngryEmoji64.png");
                        goodEmoji = new GoodEmoji(goodEmojiTexture, x*32, y*32);
                        arrGoodEmoji.add(goodEmoji);
                        capa.setCell(x, y, null);
                    }

                    if ("checkpoint2".equals(tipo)) {
                        textureCheckpoint = manager.get("ChatCityAssets/Images/Checkpoint96.png");
                        checkpoint = new Checkpoint(textureCheckpoint, x*32, y*32);
                        arrCheckpoint.add(checkpoint);
                        capa.setCell(x, y, null);
                    }

                    if("checkpointOculto".equals(tipo)){
                        //empty = new Sprite(new Texture("ChatCityAssets/EmptySquare64.png"));
                        empty = new Sprite((Texture)(manager.get("ChatCityAssets/EmptySquare64.png")));
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
                        coinTexture = manager.get("ChatCityAssets/Images/Coin.png");
                        coin = new Coin(coinTexture, x*32, y*32);
                        arrCoin.add(coin);
                        capaCoin.setCell(x, y, null);
                    }
                }
            }
        }
    }


    private void createHUD() {
        //Cámara HUD
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO,ALTO,camaraHUD);

        sceneHUD = new Stage(vistaHUD);

        //Salto
        Texture salto = manager.get("BotonesJuego/Stop.png");

        TextureRegionDrawable trBtnSalto = new TextureRegionDrawable( new TextureRegion(salto));
        ImageButton btnSalto = new ImageButton(trBtnSalto);
        btnSalto.setPosition(1,1);
        btnSalto.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (generalGame.enableMusic && jHack.estadoJuego!= mx.itesm.ht.jHack.EstadoJuego.PERDIO && estado!=EstadoJuego.PAUSADO ){
                    jumpSound.play();
                }
                jHack.saltar();
                return true;
            }
        });
        sceneHUD.addActor(btnSalto);


        //Pausa
        pausa= manager.get("ChatCityAssets/pauseButton.png");
        //Texture texturaPausa = manager.get("BotonesJuego/Pausa.png");
        TextureRegionDrawable trBtnPausa =  new TextureRegionDrawable(new TextureRegion(pausa));
        ImageButton btnPausa = new ImageButton( trBtnPausa);
        btnPausa.setPosition(ANCHO-btnPausa.getWidth(), ALTO-btnPausa.getHeight());
        btnPausa.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                estado = estado == EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;

                if(estado == EstadoJuego.PAUSADO){
                    if(pauseScene == null){
                        pauseScene = new PauseScene(vistaHUD, batch);
                    }
                    Gdx.input.setInputProcessor(pauseScene);
                }
                return true;


            }


        });
        sceneHUD.addActor(btnPausa);

        //Marcador
        texturaMon = manager.get("BotonesJuego/capsula.png");
        TextureRegionDrawable trdMon = new TextureRegionDrawable(new TextureRegion(texturaMon));
        ImageButton btnMon= new ImageButton(trdMon);
        btnMon.setPosition(-50,ALTO-btnMon.getHeight());

        sceneHUD.addActor(btnMon);
    }

    private void loadMap() {

        manager = generalGame.getManager();
        manager.load("ChatCityAssets/EmptySquare64.png", Texture.class);

        background = new Sprite((Texture) manager.get("ChatCityAssets/background.jpg"));
        background.setPosition(0,0);

        map = manager.get("ChatCityAssets/ChatCityMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);


        texturaJhack = manager.get("MundoPrueba/Tira.png");
        explotionTexture = manager.get("ChatCityAssets/explosion.png");
        //Posición inicial de jHack
        jHack = new jHack(texturaJhack, explotionTexture ,32, 32,generalGame);

        hole = new Sprite(new Texture("ChatCityAssets/Images/blackHole.png"));
        hole.setPosition(ANCHO/2-hole.getWidth()/2, ALTO_MAPA-hole.getHeight() - 64);

        emptyHole = new Sprite(new Texture("ChatCityAssets/EmptySquare64.png"));
        emptyHole.setPosition(ANCHO/2 - emptyHole.getWidth()/2, ALTO_MAPA - (hole.getHeight() - emptyHole.getHeight()));
    }

    @Override
    public void render(float delta) {
        clearScreen(0,0,0);
        //Actualizar
        updateJumpJhack(delta, map);
        if(win){
            generalGame.setScreen(new Win(generalGame, 1));
            generalGame.controlMusic("menuScreen");
        }
        if(estado != EstadoJuego.PAUSADO){
            jHack.move(map);
        }

        updateCamera();

        // Draw
        batch.setProjectionMatrix(camara.combined);
        renderer.setView(camara);
        renderer.render();
        batch.begin();
        background.draw(batch);
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

        //Dibuja al ghost, lo mueve y verifica sus colisiones
        for(int i = 0; i< arrFantasma.size(); i++){
            arrFantasma.get(i).render(batch);
            if(estado != EstadoJuego.PAUSADO && jHack.getEstadoJuego() != mx.itesm.ht.jHack.EstadoJuego.PERDIO){
                arrFantasma.get(i).move(map);
            }

            ghostCollision(arrFantasma.get(i));
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

        //Dibuja al emoji, lo anima y verifica sus coliciones
        timer+=Gdx.graphics.getDeltaTime();
        if(timer >= 3.5f && timer <= 6.2f ){
            for(int i = 0; i< arrBadEmoji.size(); i++){
                arrBadEmoji.get(i).render(batch);
                badEmojiCollision(arrBadEmoji.get(i));
            }
            if(timer >= 6f){
                timer = 0;
            }
        }
         else{
            for(int i = 0; i< arrGoodEmoji.size(); i++){
                arrGoodEmoji.get(i).render(batch);
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
        scoreBoard.MostrarMensaje(batch,updateMessage(coins), 11*ANCHO/64-45,ALTO-35f);
        batch.end();

        if(estado == EstadoJuego.PAUSADO){
            generalGame.pauseChatCityMusic();
            pauseScene.draw();
//        }else if(estado == EstadoJuego.MUERTO){
//            deadScene = new DeadScene(vista,batch);
//            deadScene.draw();
//        }
        }

        colBlackHole();
        hole.rotate(2);
    }

    private void updateJumpJhack(float delta, TiledMap mapa){
        jHack.actualizar(delta, mapa);
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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        manager.unload("MundoPrueba/Tira.png");
        manager.unload("ChatCityAssets/explosion.png");
        manager.unload("ChatCityAssets/ChatCityMap.tmx");
        manager.unload("ChatCityAssets/ghost.png");
        manager.unload("ChatCityAssets/AngryEmoji64.png");
        manager.unload("ChatCityAssets/Images/Checkpoint96.png");
        manager.unload("ChatCityAssets/Images/Coin.png");
        manager.unload("ChatCityAssets/EmptySquare64.png");
        manager.unload("BotonesJuego/Stop.png");
        manager.unload("ChatCityAssets/pauseButton.png");
        manager.unload("BotonesJuego/capsula.png");
        manager.unload("MenuAssets/PauseScreen.png");
        manager.unload("ChatCityAssets/continueButton.png");
        manager.unload("ChatCityAssets/homeButton.png");

    }

    enum EstadoJuego{
        JUGANDO,
        PAUSADO,
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    private class PauseScene extends Stage {
        // La escena que se muestra cuando está pausado
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
                    generalGame.controlMusic("chatCity");
                    estado = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(sceneHUD);

                }
            });

            this.addActor(btnContinuar);


            //Configuración
            Texture texturaBtnConfig = new Texture("ChatCityAssets/menuButton.png");
            TextureRegionDrawable trdConfig = new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));
            ImageButton btnConfig = new ImageButton(trdConfig);
            btnConfig.setPosition(ANCHO/2-btnConfig.getWidth()*1.1f,ALTO/3);
            btnConfig.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    generalGame.controlMusic("menuScreen");
                    generalGame.setScreen(new LoadingScreen(generalGame, Screen.LEVEL_SELECTION));

                }
            });
            this.addActor(btnConfig);

        }
    }

//    private class DeadScene extends Stage{
//        public DeadScene(Viewport vista, SpriteBatch batch){
//            super(vista,batch);
//            Pixmap pixmap = new Pixmap((int) (ALTO * 0.4f), (int) (ALTO * 0.5f), Pixmap.Format.RGBA8888);
//            pixmap.setColor(1f, 1f, 1f, 0.65f);
//            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
//            Texture texturaRectangulo = new Texture(pixmap);
//            pixmap.dispose();
//            Image imgRectangulo = new Image(texturaRectangulo);
//            imgRectangulo.setPosition(ANCHO/2-pixmap.getWidth()/2, ALTO/2-pixmap.getHeight()/2);
//            this.addActor(imgRectangulo);
//
//
//            //reintentar
//            Texture texturaBtnReintentar = new Texture("BotonesJuego/Cuadro de diálogo.png");
//            TextureRegionDrawable trdReint = new TextureRegionDrawable(new TextureRegion(texturaBtnReintentar));
//            ImageButton btnConfig = new ImageButton(trdReint);
//            btnConfig.setPosition(ANCHO/2-btnConfig.getWidth(),ALTO/2-btnConfig.getHeight()/2);
//            btnConfig.addListener(new ClickListener(){
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    estado = EstadoJuego.JUGANDO;
//                    Gdx.input.setInputProcessor(sceneHUD);
//                }
//            });
//            this.addActor(btnConfig);
//
//            //menu principal
//            Texture texturaBtnmenu = new Texture("BotonesJuego/Cuadro de diálogo.png");
//            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnmenu));
//            ImageButton btnMenu = new ImageButton(trdMenu);
//            btnMenu.setPosition(ANCHO/2+btnMenu.getWidth()/2,ALTO/2-btnMenu.getHeight()/2);
//            btnMenu.addListener(new ClickListener(){
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    generalGame.setScreen(new MenuScreen(generalGame));
//                }
//            });
//            this.addActor(btnMenu);
//
//
//
//
//        }
//    }

    //Prueba de colision de jack con fantasma
    public void ghostCollision(Ghost f){
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


    public void badEmojiCollision(BadEmoji e){
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

    public void colBlackHole(){
        if(emptyHole.getBoundingRectangle().overlaps(jHack.sprite.getBoundingRectangle())) {
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
            AssetManager soundManager = new AssetManager();
            if(generalGame.enableMusic){
                Music coinSound = generalGame.getCoinSound();
                coinSound.play();
            }
        }
    }

    private void musicaPerdio() {
        AssetManager soundManager = new AssetManager();
        soundManager.load("Music/enemySound.mp3", Sound.class);
        soundManager.finishLoading();
        Sound enemySound = soundManager.get("Music/enemySound.mp3");
        enemySound.play();
    }




}
