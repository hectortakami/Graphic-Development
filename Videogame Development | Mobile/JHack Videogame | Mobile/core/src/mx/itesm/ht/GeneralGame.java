package mx.itesm.ht;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GeneralGame extends Game {
	SpriteBatch batch;
	private Music menuMusic,chatCityMusic,circuitCircusMusic,jumpSound,coinSound,dieSound;
	protected boolean enableMusic;

	//AssestManager
	private AssetManager manager;


    @Override
	public void create () {
		enableMusic = true;
		setScreen(new TecScreen(this)); //Create the TecScreen
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MenuMusic.mp3"));
		chatCityMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/ChatCityMusic.mp3"));
		circuitCircusMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/CircuitCircusMusic.mp3"));
		jumpSound = Gdx.audio.newMusic(Gdx.files.internal("Music/jumpSound.mp3"));
		coinSound = Gdx.audio.newMusic(Gdx.files.internal("Music/coinSound.mp3"));
		dieSound = Gdx.audio.newMusic(Gdx.files.internal("Music/enemySound.mp3"));

		manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
	}

	public void setEnableMusic(boolean newStatement){
		enableMusic = newStatement;
	}

	public void controlMusic(String screen){
		if(enableMusic){
			if(screen.equals("menuScreen")){
				circuitCircusMusic.stop();
				chatCityMusic.stop();
				menuMusic.setLooping(true);
				menuMusic.play();
			}
			if(screen.equals("chatCity")){
				circuitCircusMusic.stop();
				menuMusic.pause();
				chatCityMusic.setLooping(true);
				chatCityMusic.play();
			}
			if(screen.equals("circuitCircus")){
				chatCityMusic.stop();
				menuMusic.pause();
				circuitCircusMusic.setLooping(true);
				circuitCircusMusic.play();
			}
		}else{
			menuMusic.pause();
			chatCityMusic.stop();
			circuitCircusMusic.stop();
		}
	}

	public AssetManager getManager(){
		return manager;
	}

	public Music getJumpSound() {
		return jumpSound;
	}

	public Music getCoinSound() {
		return coinSound;
	}

	public Music getDieSound() {
		return dieSound;
	}

	public void pauseChatCityMusic() {
		chatCityMusic.pause();
	}

	public void pauseCircuitCircus() {
		circuitCircusMusic.pause();
	}

}
