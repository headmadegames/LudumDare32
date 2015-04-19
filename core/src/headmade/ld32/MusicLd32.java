package headmade.ld32;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MusicLd32 extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
//	Texture img;
	Sound d6;
	Sound g6;
	Sound h6;
	Sound d7;
	Sound base;
	Sound screwdriver;
	AssetManager am = new AssetManager();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		am.load("sounds/d6.wav", Sound.class);
		am.load("sounds/g6.wav", Sound.class);
		am.load("sounds/h6.wav", Sound.class);
		am.load("sounds/d7.wav", Sound.class);
		am.load("sounds/base.wav", Sound.class);
		am.load("sounds/screwdriver.wav", Sound.class);
		am.finishLoading();
		d6 = am.get("sounds/d6.wav", Sound.class);
		g6 = am.get("sounds/g6.wav", Sound.class);
		h6 = am.get("sounds/h6.wav", Sound.class);
		d7 = am.get("sounds/d7.wav", Sound.class);
		base = am.get("sounds/base.wav", Sound.class);
		screwdriver = am.get("sounds/screwdriver.wav", Sound.class);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A) {
			d6.play();
			return true;
		} else if (keycode == Keys.S) {
			g6.play();
			return true;
		} else if (keycode == Keys.D) {
			h6.play();
			return true;
		} else if (keycode == Keys.F) {
//			d6.play(1f, 2f, 0f);
			d7.play();
			return true;
		} else if (keycode == Keys.C) {
			screwdriver.play();
			return true;
		} else if (keycode == Keys.SPACE) {
			base.play();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
