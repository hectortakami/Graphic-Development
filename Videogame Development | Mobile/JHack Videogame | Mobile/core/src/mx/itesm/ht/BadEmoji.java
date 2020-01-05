package mx.itesm.ht;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Dachely on 06/11/2017.
 */

public class BadEmoji extends Personaje{
    private Animation animacion;
    private float timer;
    //private Texture texture;
    public BadEmoji(Texture textura, float x, float y) {
        //this.texture = textura;
        TextureRegion region = new TextureRegion(textura);

        TextureRegion[][] frames = region.split(64,64);
        animacion = new Animation(0.10f, frames[0][1]);
        sprite = new Sprite(frames[0][1]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timer = 0;
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch) {
        //batch.draw(texture, sprite.getX(), sprite.getY());
        TextureRegion region = (TextureRegion) animacion.getKeyFrame(timer);
        batch.draw(region, sprite.getX(), sprite.getY());
    }

}
