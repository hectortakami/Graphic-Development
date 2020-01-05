import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import mx.itesm.ht.GeneralGame;
import mx.itesm.ht.GeneralScreen;

/**
 * Created by josemariaaguinigadiaz on 09/10/17.
 */

public class ChatCityLevel extends GeneralScreen {


    private GeneralGame generalGame;
    private Stage scene;

    public ChatCityLevel(GeneralGame generalGame) {
        this.generalGame = generalGame;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(scene);

    }

    @Override
    public void render(float delta) {
        clearScreen(1f,1f,1f);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        //Draw scene elements


        borrarPantalla();

        batch.end();

    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
