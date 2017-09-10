package roscs004.unisa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class JumpClass2 extends ApplicationAdapter {

    Sprite jumpButton;
    Sprite flipButton;
    boolean gameOver = false;
    Texture JumpButtonTexture;
    Texture FlipButtonTexture;
    Rectangle r2;
    Sprite badBlockSprite;
    Sprite upsideDownbBad;
    Texture badBlockTexture;
    SpriteBatch batch;				// Used to place textures on screen
    Texture background;				// Create texture to store background image
    Texture repeatBackground;		// Repeats the background when original has scrolled to the end
    Texture platform;				// The platform for the character to run on
    Texture runSheet;				//Texture used to hold run sprite sheet
    Texture flipSheet;				//Texture used to hold flip sprite sheet
    OrthographicCamera camera;		// Camera to allow varying screen sizes to work
    int gameSpeed = 0;				// Controls speed of scrolling background
    float w = 0;					// Stores width of device screen
    float h = 0;					// Stores height of device screen
    int canCreateBlock = 0;			// Ensures that we aren't creating two blocks on top of eachother.
    public static final int MAXSPEED = 100;
    public int speed = 0;
    public int renders;
    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 5;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> jumpAnimation;
    Animation<TextureRegion> flipRunAnimation;
    Animation<TextureRegion> flipJumpAnimation;
    TextureRegion[] runFrames;
    TextureRegion[] jumpFrames;
    TextureRegion[] flipRunFrames;
    TextureRegion[] flipJumpFrames;
    TextureRegion currentFrame;
    TextureRegion jumpCurrentFrame;
    TextureRegion flipRunCurrentFrame;
    TextureRegion flipJumpCurrentFrame;
    float stateTime;
    Integer frameIndex;
    Integer jumpFrameIndex;
    Integer flipRunFrameIndex;
    Integer flipJumpFrameIndex;
    boolean isFliped = false;
    float deltaTime=0;
    int playerState = 0;
    int repeatGameSpeed;
    SpriteBatch batch2;
    BitmapFont font;
    boolean checkTouch;
    int canAnimate;
    int rendersToAdd = 10;
    int added = 0;

    int[][] blocks = new int[][]{
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
    };
    Sprite[] sprites = new Sprite[22];
    boolean[] movement = new boolean[22];

    @Override
    public void create () {
        // SpriteBatch used to draw Sprite onto screen in render() method
        batch = new SpriteBatch();

        // TODO: Replace temp background
        // TODO: Replace temp character

        //two buttons
//        jumpRegion = new TextureRegion();

        //run animation (Needs to be mostly in the render method)
        runSheet = new Texture(Gdx.files.internal("sprite_sheet.png"));
        flipSheet = new Texture(Gdx.files.internal("flip_sprite_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(runSheet, runSheet.getWidth() / FRAME_COLS, runSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] temp = TextureRegion.split(flipSheet, flipSheet.getWidth() / 5, flipSheet.getHeight() / 5);
        runFrames = new TextureRegion[9];
        jumpFrames = new TextureRegion[12];
        flipRunFrames = new TextureRegion[10];
        flipJumpFrames = new TextureRegion[13];
        int index = 0;
        int jumpIndex = 0;
        checkTouch = false;
        canAnimate = 0;
        for(int i=0; i<FRAME_ROWS; i++){
            for(int j=0; j<FRAME_COLS; j++){
                if(index < 9){
                    runFrames[index] = tmp[i][j];
                }

                if(index > 9 && index < 22 ){
                    jumpFrames[jumpIndex++] = tmp[i][j];
                }
                index++;
            }
        }

        int flipIndex = 0;
        int flipJumpIndex = 0;
        for(int i=0; i<FRAME_ROWS; i++){
            for(int j=0; j<FRAME_COLS; j++){
                if(flipIndex < 10){
                    flipRunFrames[flipIndex] = temp[i][j];
                }

                if(flipIndex >= 10 && flipIndex < 23 ){
                    flipJumpFrames[flipJumpIndex++] = temp[i][j];
                }
                flipIndex++;
            }
        }

        FlipButtonTexture = new Texture(Gdx.files.internal("FLIP.png"));
        JumpButtonTexture = new Texture(Gdx.files.internal("JUMP.png"));
        flipButton = new Sprite(FlipButtonTexture);
        jumpButton = new Sprite(JumpButtonTexture);

        runAnimation = new Animation(1.5f, runFrames);
        jumpAnimation = new Animation(1.5f, jumpFrames);
        flipRunAnimation = new Animation(1.5f, flipRunFrames);
        flipJumpAnimation = new Animation(1.5f, flipJumpFrames);
        stateTime = 0f;

        // Set texture to background image
        background = new Texture(Gdx.files.internal("scrollBackground.png"));
        repeatBackground = new Texture(Gdx.files.internal("scrollBackground.png"));
        batch2 = new SpriteBatch();
        repeatGameSpeed = Gdx.graphics.getWidth();


        badBlockTexture = new Texture(Gdx.files.internal("Bad_Block.png"));
        badBlockSprite = new Sprite(badBlockTexture);
        upsideDownbBad = new Sprite(badBlockTexture);
        upsideDownbBad.flip(false, true);

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

        if(gameOver) {
            try{sleep();}catch(InterruptedException ie){}
            gameOver();
        }

        checkTouch = Gdx.input.isTouched();

            if (checkTouch) {
                added = canAnimate + rendersToAdd;
                System.out.println("Added: " + added + " renders: " + renders);
                if (added < renders) {
                    canAnimate = renders;
                } else {
                    checkTouch = false;
                }
        }
        // Speed at which the background moves across screen
        // TODO: Write formula that slowly increases speed as game progresses
        gameSpeed -= 20;

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // TODO: BACKGROUND CODE

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

        // TODO: END BACKGROUND CODE


        stateTime += Gdx.graphics.getDeltaTime() * 30;
        currentFrame = runAnimation.getKeyFrame(stateTime, true);
        jumpCurrentFrame = jumpAnimation.getKeyFrame(stateTime, true);
        flipRunCurrentFrame = flipRunAnimation.getKeyFrame(stateTime,true);
        flipJumpCurrentFrame = flipJumpAnimation.getKeyFrame(stateTime, true);
        frameIndex = runAnimation.getKeyFrameIndex(stateTime);
        jumpFrameIndex = jumpAnimation.getKeyFrameIndex(stateTime);
        flipRunFrameIndex = flipRunAnimation.getKeyFrameIndex(stateTime);
        flipJumpFrameIndex = flipJumpAnimation.getKeyFrameIndex(stateTime);

//        Gdx.app.log("current time",Float.toString(stateTime));
//        Gdx.app.log("current frame index",Integer.toString(frameIndex));

        batch.begin();
        //batch.draw(background, scrollX, h / 2, background.getWidth(), h / 2);
        //(background, x, y, width, height);
        //(background, x, y, width of image, half height of screen (top half));
        // coordinate system works bottom left (0, 0)

        // Draw assets onto screen
        batch.draw(background, gameSpeed, h/2, background.getWidth(), h/2);			// top half
        batch.draw(background, gameSpeed, 0, background.getWidth(), h/2, 0, 0,
                background.getWidth(), background.getHeight(), false, true);		// bottom half

        batch.draw(platform, 0, h/2 - (platform.getHeight() / 2), platform.getWidth() * 2, platform.getHeight());
        batch.draw(flipButton, 0, 0, flipButton.getWidth() * 2, flipButton.getHeight()*2);
        batch.draw(jumpButton, Gdx.graphics.getWidth() - jumpButton.getWidth()*2, 0, (jumpButton.getWidth() * 2), jumpButton.getHeight()*2);


        //Create block randomly
        Random rand = new Random();
        if(rand.nextInt()%50 == 0 && canCreateBlock > 50) {
            //build block
            canCreateBlock = speed;

            for(int i = 0 ; i < 22 ; i++) {
                if(blocks[i][1] == 0) {
                    if(rand.nextInt()%2 == 0) {
                        //Create on top
                        blocks[i][0] = Gdx.graphics.getWidth();
                        blocks[i][1] = rand.nextInt(platform.getHeight()) + (int)(h/2 + platform.getHeight() * 2);
                        sprites[i] = new Sprite(badBlockTexture);
                        movement[i] = true;
                        break;
                    }else {
                        //Create on bottom
                        blocks[i][0] = Gdx.graphics.getWidth();
                        blocks[i][1] = rand.nextInt(platform.getHeight()) + (int)(h/2 - platform.getHeight() * 2);
                        sprites[i] = new Sprite(upsideDownbBad);
                        movement[i] = false;
                        break;
                    }
                }
                else if(blocks[i][0] < 0) {
                    blocks[i][0] = 0;
                    blocks[i][1] = 0;
                }
                else {
                    canCreateBlock = speed;
                }
            }


        }

        if(checkTouch) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            Rectangle touchSpace = new Rectangle(touchPos.x, touchPos.y, 1, 1);
            touchSpace.setCenter(touchPos.x, touchPos.y);



                Rectangle flipButtonRect = new Rectangle(0, 0, flipButton.getWidth(), flipButton.getHeight());
                Rectangle jumpButtonRect = new Rectangle(Gdx.graphics.getWidth() - (jumpButton.getWidth() * 5), 0, (jumpButton.getWidth() * 2) * 2, jumpButton.getHeight());


                if (touchSpace.overlaps(flipButtonRect)) {
                    //Do flip animation, move the hitbox of the character to the new location *instantly*
                    if (isFliped == false) {
                        isFliped = true;
                        playerState = 1;
                    } else if (isFliped == true) {
                        isFliped = false;
                        playerState = 0;
                    }
                } else if (touchSpace.overlaps(jumpButtonRect)) {   //&& canAnimate + 500 >= System.currentTimeMillis()
                    //Do jump animation, move the hitbox of the character up then down
                    playerState = 2;
                    //canAnimate = System.currentTimeMillis();
                }
            }

        r2 = new Rectangle(w/8, h/2 + (platform.getHeight() / 2) - 15, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        //player state, switch between run, flip and jump
        if(playerState == 0){
            //Running on top
            batch.draw(currentFrame, w/8, h/2 + (platform.getHeight() / 2) - 15);
            r2.setPosition(w/8, h/2 + (platform.getHeight() / 2) - 15);
        }else if(playerState == 1){
            //Running on bottom
            batch.draw(flipRunCurrentFrame, w/8, h/2 - platform.getHeight()-15);
            r2.setPosition(w/8, h/2 - (platform.getHeight() / 2) - 15);
        }else if(playerState == 2 && isFliped == false){
            //Jumping on top
            batch.draw(jumpCurrentFrame, w / 8, h / 2 + (platform.getHeight() / 2) + 0);
            //play animation only once
            if(jumpCurrentFrame.equals(jumpAnimation.getKeyFrame(jumpAnimation.getAnimationDuration()))){
                playerState = 0;
            }
        }else if(playerState == 2 && isFliped == true){
            batch.draw(flipJumpCurrentFrame, w/8, h/2 - platform.getHeight());
            if(jumpCurrentFrame.equals(jumpAnimation.getKeyFrame(jumpAnimation.getAnimationDuration()))){
                playerState = 1;
            }
        }


        for(int i = 0 ; i < 22 ; i++) {
            //If the block exists
            if(blocks[i][1] > 0) {
                blocks[i][0] = blocks[i][0] - 20;
                //If block is on top, move down
                if(movement[i] == true && blocks[i][1] >= h/2 + platform.getHeight() * 2) {
                    movement[i] = false;

                    //If block is on bottom, but gets to bottom of platform
                } else if(movement[i] == true && blocks[i][1] <= h/2 && blocks[i][1] >= h/2 - platform.getHeight()/2 - 75) {
                    movement[i] = false;

                    //If block is on top, but gets to top of platform
                } else if(movement[i] == false && blocks[i][1] >= h/2-20 && blocks[i][1] <= h/2 + platform.getHeight()/2 - 10) {
                    movement[i] = true;

                    //If block is on bottom, move up
                } else if(movement[i] == false && blocks[i][1] <= h/2 - platform.getHeight() * 2) {
                    movement[i] = true;
                }

                if(movement[i] == true && blocks[i][1] > h/2) {
                    //If going up and nearing top:
                    if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 50 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 41) {
                        blocks[i][1] += 10;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 40 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 31) {
                        blocks[i][1] += 8;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 30 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 21) {
                        blocks[i][1] += 6;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 20 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 11) {
                        blocks[i][1] += 4;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 10 && blocks[i][1] <= (h/2 + platform.getHeight() * 2)) {
                        blocks[i][1] += 2;
                    } else {
                        blocks[i][1] += 12;
                    }
                }
                else if(movement[i] == true && blocks[i][1] < h/2) {
                    //If going up from bottom
                    if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 10) {
                        blocks[i][1] += 2;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 11 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 20) {
                        blocks[i][1] += 4;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 21 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 30) {
                        blocks[i][1] += 6;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 31 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 40) {
                        blocks[i][1] += 8;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 41 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 50) {
                        blocks[i][1] += 10;
                    } else {
                        blocks[i][1] += 12;
                    }
                }
                else if(movement[i] == false && blocks[i][1] > h/2) {
                    //If going down from the top
                    if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 50 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 41) {
                        blocks[i][1] -= 10;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 40 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 31) {
                        blocks[i][1] -= 8;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 30 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 21) {
                        blocks[i][1] -= 6;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 20 && blocks[i][1] <= (h/2 + platform.getHeight() * 2) - 11) {
                        blocks[i][1] -= 4;
                    } else if (blocks[i][1] >= (h/2 + platform.getHeight() * 2) - 10 && blocks[i][1] <= (h/2 + platform.getHeight() * 2)) {
                        blocks[i][1] -= 2;
                    } else {
                        blocks[i][1] -= 12;
                    }
                }
                else if(movement[i] == false && blocks[i][1] < h/2) {
                    //If going down on the bottom
                    if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 10) {
                        blocks[i][1] -= 2;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 11 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 20) {
                        blocks[i][1] -= 4;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 21 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 30) {
                        blocks[i][1] -= 6;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 31 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 40) {
                        blocks[i][1] -= 8;
                    } else if(blocks[i][1] >= (h/2 - platform.getHeight() * 2) + 41 && blocks[i][1] <= (h/2 - platform.getHeight() * 2) + 50) {
                        blocks[i][1] -= 10;
                    } else {
                        blocks[i][1] -= 12;
                    }
                }
                else {}
                //Redraw the block at its new point
                batch.draw(sprites[i], blocks[i][0], blocks[i][1], 100 , 100);

                Rectangle r1 = new Rectangle(blocks[i][0], blocks[i][1], sprites[i].getWidth(), sprites[i].getHeight());


                if(r1.overlaps(r2)) {
                    //lose game, have a restart button probably
                        gameOver();
                }
            }
            //Else if the block has gone off the screen
            else if(blocks[i][0] < 0) {
                //Remove the block and allow new blocks to be created in this space
                blocks[i][0] = 0;
                blocks[i][1] = 0;
            } else {};
        }

        font.draw(batch, "Score: " + renders / 4, 30, h - 30);

        batch.end();
        renders++;
        if(renders%100 == 0) {
            if(!(speed + 20 > MAXSPEED)) {
                speed+=20;
            } else {
                speed = MAXSPEED;
            }
        }
        canCreateBlock++;

    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
    }

    private void gameOver(){
        gameOver = true;
        font.getData().setScale(10f);
        font.draw(batch, "GAME OVER", w/2 - 400, h/2 + 200);
        Gdx.graphics.setContinuousRendering(false);
        Gdx.app.exit();
    }

    public void sleep() throws InterruptedException{
        Thread.sleep(5000);
    }

}
