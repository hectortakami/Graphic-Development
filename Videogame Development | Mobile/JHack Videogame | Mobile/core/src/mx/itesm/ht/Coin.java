package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Dachely on 15/11/2017.
 */

public class Coin extends Objeto {
    private Animation animacion;
    private float timer;
    //private Texture texture;
    public Coin(Texture textura, float x, float y) {
        TextureRegion region = new TextureRegion(textura);

        TextureRegion[][] frames = region.split(32,32);
        animacion = new Animation(0.10f, frames[0][0], frames[0][1], frames[0][2], frames[0][3], frames[0][4], frames[0][5]);
        sprite = new Sprite(frames[0][0]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timer = 0;
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch) {
        timer += Gdx.graphics.getDeltaTime();
        TextureRegion region = (TextureRegion) animacion.getKeyFrame(timer);
        batch.draw(region, sprite.getX(), sprite.getY());
    }
}
