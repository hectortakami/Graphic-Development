package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Hector on 11/14/2017.
 */

class InformationScreen extends GeneralScreen {

    private GeneralGame generalGame;
    private Texture dache,dany,chema,ernesto,hector,homeButton,okBubbleButton;
    private Sprite bubbleDany, bubbleDache, bubbleChema, bubbleNeto, bubbleHector, img, text;
    private Array<Sprite> gif;
    private Stage scene;
    private int index;
    private float time;
    private boolean hectorBool,danyBool,dacheBool,chemaBool,netoBool;
    private Image imgRectangulo;

    //Cargador
    private AssetManager manager;

    public InformationScreen(GeneralGame generalGame) {
        this.generalGame=generalGame;
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {
        gif = new Array<Sprite>();
        loadTextures();
        loadFrames();
        createButtons();
        Gdx.input.setInputProcessor(scene);
        time = 0;
        index = 0;
        hectorBool = false;

    }

    private void loadTextures() {
        manager = generalGame.getManager();
        img = new Sprite(new Texture("InformationAssets/gameMail.png"));
        img.setPosition(ANCHO/2-img.getWidth()/2,img.getHeight());
        text = new Sprite((Texture)manager.get("InformationAssets/textVideoGames.png"));
        text.setPosition(30,ALTO-text.getHeight()-150);
        dache = manager.get("InformationAssets/Dache.png");
        dany = manager.get("InformationAssets/Dan.png");
        chema = manager.get("InformationAssets/Chema.png");
        ernesto = manager.get("InformationAssets/Neto.png");
        hector = manager.get("InformationAssets/Takami.png");
        homeButton = manager.get("InformationAssets/homeButton.png");
        okBubbleButton = manager.get("InformationAssets/okButton.png");
    }

    private void loadFrames() {
        //por mi
        for(int i = 0; i< 120; i++){
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif",i);
            Sprite sprite = new Sprite((Texture)manager.get(nombre));
            gif.add(sprite);
        }

        for (Sprite img:
             gif) {
            img.setSize(ANCHO,ALTO);
            img.setPosition(0,0);
        }


    }

    private void createButtons() {
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

        Pixmap pixmap = new Pixmap((int) (ANCHO), (int) (ALTO), Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 0.65f);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture texturaRectangulo = new Texture(pixmap);
        pixmap.dispose();
        imgRectangulo = new Image(texturaRectangulo);
        //imgRectangulo.setPosition(0.15f * ANCHO, 0.1f * ALTO);
        imgRectangulo.setPosition(ANCHO/2-pixmap.getWidth()/2, ALTO/2-pixmap.getHeight()/2);


        TextureRegionDrawable trdHome = new TextureRegionDrawable(new TextureRegion(homeButton));
        ImageButton homeButton = new ImageButton(trdHome);
        homeButton.setPosition(0,ALTO-homeButton.getHeight());

        TextureRegionDrawable trdHector = new TextureRegionDrawable(new TextureRegion(hector));
        ImageButton hectorButton = new ImageButton(trdHector);
        hectorButton.setPosition(ANCHO-hectorButton.getWidth()-5,ALTO/2-hectorButton.getHeight()/2);

        TextureRegionDrawable trdDan = new TextureRegionDrawable(new TextureRegion(dany));
        ImageButton danButton = new ImageButton(trdDan);
        danButton.setPosition(hectorButton.getX(),hectorButton.getY()+danButton.getHeight()+15);

        TextureRegionDrawable trdNeti = new TextureRegionDrawable(new TextureRegion(ernesto));
        final ImageButton netiButton = new ImageButton(trdNeti);
        netiButton.setPosition(hectorButton.getX(),hectorButton.getY()-netiButton.getHeight()-15);

        TextureRegionDrawable trdDache = new TextureRegionDrawable(new TextureRegion(dache));
        ImageButton dacheButton = new ImageButton(trdDache);
        dacheButton.setPosition(hectorButton.getX()-dacheButton.getWidth()+90f,hectorButton.getY()+dacheButton.getHeight()/2+5);

        TextureRegionDrawable trdChema = new TextureRegionDrawable(new TextureRegion(chema));
        ImageButton chemaButton = new ImageButton(trdChema);
        chemaButton.setPosition(dacheButton.getX(),dacheButton.getY()-chemaButton.getHeight()-15);

        //ButtonListener
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                generalGame.setScreen(new LoadingScreen(generalGame, Screen.MENU2));
            }
        });

        danButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                bubbleDany = new Sprite(new Texture("InformationAssets/bubbleDan.png"));
                bubbleDany.setPosition(ANCHO/2-bubbleDany.getWidth()/2,ALTO/2-bubbleDany.getHeight()/2);
                TextureRegionDrawable trdClose = new TextureRegionDrawable(new TextureRegion(okBubbleButton));
                final ImageButton okButton = new ImageButton(trdClose);
                okButton.setPosition(bubbleDany.getX() + bubbleDany.getWidth()-okButton.getWidth()/2,bubbleDany.getY() + bubbleDany.getHeight()-okButton.getHeight()/2);
                scene.addActor(imgRectangulo);
                scene.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        danyBool = false;
                        okButton.remove();
                        imgRectangulo.remove();

                    }
                });
                danyBool = true;

            }
        });

        dacheButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                bubbleDache = new Sprite(new Texture("InformationAssets/bubbleDache.png"));
                bubbleDache.setPosition(ANCHO/2-bubbleDache.getWidth()/2,ALTO/2-bubbleDache.getHeight()/2);
                TextureRegionDrawable trdClose = new TextureRegionDrawable(new TextureRegion(okBubbleButton));
                final ImageButton okButton = new ImageButton(trdClose);
                okButton.setPosition(bubbleDache.getX() + bubbleDache.getWidth()-okButton.getWidth()/2,bubbleDache.getY() + bubbleDache.getHeight()-okButton.getHeight()/2);
                scene.addActor(imgRectangulo);
                scene.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        dacheBool = false;
                        okButton.remove();
                        imgRectangulo.remove();

                    }
                });
                dacheBool = true;

            }
        });

        hectorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                bubbleHector = new Sprite(new Texture("InformationAssets/bubbleHector.png"));
                bubbleHector.setPosition(ANCHO/2-bubbleHector.getWidth()/2,ALTO/2-bubbleHector.getHeight()/2);
                TextureRegionDrawable trdClose = new TextureRegionDrawable(new TextureRegion(okBubbleButton));
                final ImageButton okButton = new ImageButton(trdClose);
                okButton.setPosition(bubbleHector.getX() + bubbleHector.getWidth()-okButton.getWidth()/2,bubbleHector.getY() + bubbleHector.getHeight()-okButton.getHeight()/2);
                scene.addActor(imgRectangulo);
                scene.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        hectorBool = false;
                        okButton.remove();
                        imgRectangulo.remove();
                    }
                });
                hectorBool = true;

            }
        });

        chemaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                bubbleChema = new Sprite(new Texture("InformationAssets/bubbleChema.png"));
                bubbleChema.setPosition(ANCHO/2-bubbleChema.getWidth()/2,ALTO/2-bubbleChema.getHeight()/2);
                TextureRegionDrawable trdClose = new TextureRegionDrawable(new TextureRegion(okBubbleButton));
                final ImageButton okButton = new ImageButton(trdClose);
                okButton.setPosition(bubbleChema.getX() + bubbleChema.getWidth()-okButton.getWidth()/2,bubbleChema.getY() + bubbleChema.getHeight()-okButton.getHeight()/2);
                scene.addActor(imgRectangulo);
                scene.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        chemaBool = false;
                        okButton.remove();
                        imgRectangulo.remove();

                    }
                });
                chemaBool = true;

            }
        });



        netiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                bubbleNeto = new Sprite(new Texture("InformationAssets/bubbleNeto.png"));
                bubbleNeto.setPosition(ANCHO/2-bubbleNeto.getWidth()/2,ALTO/2-bubbleNeto.getHeight()/2);
                TextureRegionDrawable trdClose = new TextureRegionDrawable(new TextureRegion(okBubbleButton));
                final ImageButton okButton = new ImageButton(trdClose);
                okButton.setPosition(bubbleNeto.getX() + bubbleNeto.getWidth()-okButton.getWidth()/2,bubbleNeto.getY() + bubbleNeto.getHeight()-okButton.getHeight()/2);
                scene.addActor(imgRectangulo);
                scene.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        netoBool = false;
                        okButton.remove();
                        imgRectangulo.remove();

                    }
                });
                netoBool = true;

            }
        });



        scene.addActor(homeButton);
        scene.addActor(dacheButton);
        scene.addActor(danButton);
        scene.addActor(hectorButton);
        scene.addActor(chemaButton);
        scene.addActor(netiButton);
    }

    @Override
    public void render(float delta) {
        clearScreen(1f,1f,1f);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        gif.get(index).draw(batch);
        batch.end();
        time+= Gdx.graphics.getDeltaTime();
        //Frame index
        if(time>=0.03f){
            index++;
            time=0;
        }
        //Restart frames
        if(index== gif.size){
            index=0;
        }

        batch.begin();
        scene.draw();
        batch.end();

        batch.begin();
        text.draw(batch);
        img.draw(batch);

        if(hectorBool){
            bubbleHector.draw(batch);
        }
        if(danyBool){
            bubbleDany.draw(batch);
        }
        if(dacheBool){
            bubbleDache.draw(batch);
        }
        if(chemaBool){
            bubbleChema.draw(batch);
        }
        if(netoBool){
            bubbleNeto.draw(batch);
        }
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

        manager.unload("InformationAssets/Dache.png");
        manager.unload("InformationAssets/Dan.png");
        manager.unload("InformationAssets/Chema.png");
        manager.unload("InformationAssets/Neto.png");
        manager.unload("InformationAssets/Takami.png");
        manager.unload("InformationAssets/homeButton.png");
        manager.unload("InformationAssets/okButton.png");
        manager.unload("InformationAssets/textVideoGames.png");

        for(int i = 0; i< 120; i++) {
            String nombre = String.format("InformationAssets/Gif/frame_%03d_delay-0.03s.gif", i);
            manager.unload(nombre);
        }
    }
}
