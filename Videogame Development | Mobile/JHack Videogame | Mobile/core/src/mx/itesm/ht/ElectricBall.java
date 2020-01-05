package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by netocervantes on 14/11/17.
 */

public class ElectricBall extends Personaje {
    private Animation animacion;
    private float timer;
    //private Texture texture;

    public ElectricBall(Texture textura, float x, float y) {
        //this.texture = textura;
        TextureRegion region = new TextureRegion(textura);

        TextureRegion[][] frames = region.split(64,64);
        animacion = new Animation(0.50f, frames[0][0],frames[0][1]);
        sprite = new Sprite(frames[0][0]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timer = 0;
        sprite.setPosition(x,y);
        rotate();
        setEstadoMovimiento(Personaje.EstadoMovimiento.DERECHA);
    }

    public void render(SpriteBatch batch) {
        timer += Gdx.graphics.getDeltaTime();
        TextureRegion region = (TextureRegion) animacion.getKeyFrame(timer);
        //sprite.draw(batch);
        batch.draw(region, sprite.getX(), sprite.getY());

    }

    public void rotate() {
        sprite.rotate(3);
    }
}
