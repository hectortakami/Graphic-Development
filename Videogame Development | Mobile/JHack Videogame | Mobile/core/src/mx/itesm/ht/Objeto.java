package mx.itesm.ht;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by netocervantes on 24/10/17.
 */

public class Objeto {

    protected Sprite sprite;    // Imagen

    public Objeto() {

    }

    public Objeto(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }

    public boolean contiene(Vector3 v) {
        float x = v.x;
        float y = v.y;

        return x>=sprite.getX() && x<=sprite.getX()+sprite.getWidth()
                && y>=sprite.getY() && y<=sprite.getY()+sprite.getHeight();
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
