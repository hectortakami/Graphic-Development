package mx.itesm.ht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by josemariaaguinigadiaz on 17/10/17.
 */

public class Text {
    private BitmapFont font;
    private int level;

    public Text(int level){
        this.level = level;
        font = new BitmapFont(Gdx.files.internal("Textos/blockstock3.fnt"));
        switch (level){
            case 1:
                font.setColor(Color.BLACK);
                break;
            case 2:
                font.setColor(Color.WHITE);
                break;

        }

        //font.setColor(0,0,1,1);
        //font.setColor(Color.SKY);


    }

    public void MostrarMensaje(SpriteBatch batch, String mensaje, float x, float y){
        GlyphLayout glyph = new GlyphLayout();
        glyph.setText(font, mensaje);

        float anchoTexto = glyph.width;
        font.draw(batch, glyph, x-anchoTexto/2, y);


    }


}
