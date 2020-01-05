package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Dachely on 12/11/2017.
 */

public class Checkpoint extends Objeto{
    private Animation animation;
    private float timer;
    private boolean active = true;
    //private Texture texture;
    public Checkpoint(Texture textura, float x, float y) {
        TextureRegion region = new TextureRegion(textura);

        TextureRegion[][] frames = region.split(96,96);
        animation = new Animation(0.10f, frames[0][1], frames[0][0]);
        sprite = new Sprite(frames[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        timer = 0;
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch) {
        timer += Gdx.graphics.getDeltaTime();
        TextureRegion region = (TextureRegion) animation.getKeyFrame(timer);
        batch.draw(region, sprite.getX(), sprite.getY());

    }

}
