package roscs004.unisa;

import java.util.Random;

public class JumpClass2 extends ApplicationAdapter {

    SpriteBatch batch;				// Used to place textures on screen
    Texture background;				// Create texture to store background image
    Texture repeatBackground;		// Repeats the background when original has scrolled to the end
    Texture platform;				// The platform for the character to run on
    OrthographicCamera camera;		// Camera to allow varying screen sizes to work
    int gameSpeed = 0;				// Controls speed of scrolling background
    float w = 0;					// Stores width of device screen
    float h = 0;					// Stores height of device screen
    int repeatGameSpeed;			// Used for 
    SpriteBatch batch2;
    BitmapFont font;

    @Override
    public void create () {
        // SpriteBatch used to draw Sprite onto screen in render() method
        batch = new SpriteBatch();

        // Set texture to background image
        background = new Texture(Gdx.files.internal("scrollBackground.png"));
        repeatBackground = new Texture(Gdx.files.internal("scrollBackground.png"));
        batch2 = new SpriteBatch();
        repeatGameSpeed = Gdx.graphics.getWidth();

        platform = new Texture(Gdx.files.internal("platform.png"));

        font = new BitmapFont();
        font.getData().setScale(5f);

        // Get height and width of device's screen size
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // Set camera to be size of current device
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/2, h/2);
        // Make sure to update camera every time a change is made
        camera.update();

    }

    @Override
    public void render (){

        // Speed at which the background moves across screen
        gameSpeed -= 20;

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch2.begin();

        // If end of first background is not at right side of screen (i.e. moving inwards past screen edge)
        if (Math.abs(gameSpeed) + Gdx.graphics.getWidth() >= background.getWidth()) {
            repeatGameSpeed -= 20;
            // Draw 'same' background on screen but starting from right side so that background appears to 'continue'
            batch2.draw(repeatBackground, repeatGameSpeed, h/2, repeatBackground.getWidth(), h/2);
            batch2.draw(repeatBackground, repeatGameSpeed, 0, repeatBackground.getWidth(), h/2, 0, 0,
                    repeatBackground.getWidth(), repeatBackground.getHeight(), false, true);
        }

        //if (Math.abs(gameSpeed) >= background.getWidth()) {
        if (repeatGameSpeed <= 0) {
            gameSpeed = 0;
            repeatGameSpeed = Gdx.graphics.getWidth();
        }

        batch2.end();

        batch.begin();

        // Draw assets onto screen
        batch.draw(background, gameSpeed, h/2, background.getWidth(), h/2);			// top half
        batch.draw(background, gameSpeed, 0, background.getWidth(), h/2, 0, 0,
                background.getWidth(), background.getHeight(), false, true);		// bottom half

        batch.draw(platform, 0, h/2 - (platform.getHeight() / 2), platform.getWidth() * 2, platform.getHeight());

        font.draw(batch, "Score: " + renders / 4, 30, h - 30);

        batch.end();
       
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
    }

}
