package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created by josemariaaguinigadiaz on 16/10/17.
 */

public class ChatCityLevel2 extends GeneralScreen {

    private GeneralGame generalGame;
    private Stage scene;
    private Texture pausa,background;
    private float counter, timeCounter;

    private EscenaPausa escenaPausa = new EscenaPausa(vista, batch);
    private EstadoJuego estado;

    //Puntos
    private int puntosJugador = 0;
    private Text text;


    public ChatCityLevel2(GeneralGame generalGame){
        this.generalGame = generalGame;


    }


    @Override
    public void show() {
        loadTextures();
        createButtons();
        crearObjetos();
        crono();

        estado = EstadoJuego.JUGANDO;


        Gdx.input.setInputProcessor(scene);

    }

    private void crearObjetos() {
        text = new Text(1);

    }

   private void createButtons() {
        scene = new Stage(vista);

        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(pausa));
        ImageButton btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(500, 1100);

        scene.addActor(btnPausa);

        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("CLICKED", "TOUCH");
                estado = EstadoJuego.PAUSADO;
                Gdx.input.setInputProcessor(escenaPausa);
            }
        });
    }

    private void loadTextures() {
        //Carga boton de pausa
        pausa= new Texture("BotonesJuego/Pausa.png");
        background = new Texture("ChatCityAssets/background.png");


    }

    @Override
    public void render(float delta) {
        clearScreen(0f,0f,0f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(background,0,0);
        //text.MostrarMensaje(batch, "Puntaje: " +Integer.toString(puntosJugador), ANCHO/2-ANCHO/6, 9*ALTO/10);
        text.MostrarMensaje(batch, "Tiempo: " + crono() , ANCHO/2-ANCHO/6, ALTO);
        batch.end();

        //batch.draw(pausa, 500,1100);

        if(estado == EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }

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
        pausa.dispose();
        escenaPausa.dispose();

    }

    public float crono() {
        timeCounter += Gdx.graphics.getDeltaTime();
        if (timeCounter >= 1.0f) {
            timeCounter = 0;
            counter++;
        }
        return counter;
    }


    enum EstadoJuego{
        JUGANDO,
        PAUSADO
    }

    private class EscenaPausa extends Stage
    {
        //Escena mostrada cuando se pausa el juego
        public EscenaPausa(Viewport vista, SpriteBatch batch){
            super(vista, batch);

            //Crear el rectangulo transparente
            Pixmap pixmap = new Pixmap((int)(ANCHO/2), (int)(ALTO/2), Pixmap.Format.RGBA8888);
            pixmap.setColor(1f, 1f, 1f, 0.65f);
            pixmap.fillRectangle(0,0,pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture( pixmap );
            pixmap.dispose();

            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(ANCHO/2-pixmap.getWidth()/2, ALTO/2-pixmap.getHeight()/2);
            this.addActor(imgRectangulo);


            //Salir
            Texture texturaBtnSalir = new Texture("BotonesJuego/Cuadro de diálogo.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO/2-btnSalir.getWidth()/2, ALTO/2);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    generalGame.setScreen(new MenuScreen(generalGame));



                }
            });

            this.addActor(btnSalir);

            //Continuar

            Texture textuBtnRes = new Texture("LevelSelectionAssets/backButton.png");
            TextureRegionDrawable trdRes = new TextureRegionDrawable(new TextureRegion(textuBtnRes));
            ImageButton btnRes = new ImageButton(trdRes);
            btnRes.setPosition(ANCHO/2-btnRes.getWidth()/2, ALTO/4);
            btnRes.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    estado = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(scene);
                }
            });

            this.addActor(btnRes);

            //



        }

    }

}


