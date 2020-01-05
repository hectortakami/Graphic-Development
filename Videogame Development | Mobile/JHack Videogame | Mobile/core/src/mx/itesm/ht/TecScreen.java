package mx.itesm.ht;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Hector on 9/28/2017.
 */

public class TecScreen extends GeneralScreen
{

    private GeneralGame generalGame;
    //Tec Logo
    private Sprite logoTec;
    //Time
    private float time;

    public TecScreen(GeneralGame generalGame){
        this.generalGame = generalGame;
    }

    @Override
    public void show() {
        logoTec = new Sprite(new Texture("TecAssets/TecLogo.png"));
        logoTec.setPosition(ANCHO/2-logoTec.getWidth()/2,ALTO/2-logoTec.getHeight()/2);
        time=0;
    }

    @Override
    public void render(float delta) {
        clearScreen(1f,1f,1f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        //Draw Tec logo
        logoTec.draw(batch);
        batch.end();

        time+= Gdx.graphics.getDeltaTime();
        //Valor original 5f
        if(time>=2.5f){
            generalGame.setScreen(new LoadingScreen(generalGame, Screen.MENU));
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
