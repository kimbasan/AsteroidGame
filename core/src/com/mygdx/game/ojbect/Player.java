package com.mygdx.game.ojbect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.AsteroidUtil;

public class Player implements Disposable {
    private static final int HIT_POINTS = 3;
    private static final float THRUST_FORCE_FORWARD = 1.5f;
    private static final float THRUST_FORCE_SIDE = 1.5f;
    private static final String PLAYER_SHIP_IMAGE = "Sprites/playerShip3_blue.png";
    private static final int SPRITE_ROTATION_ANGLE = 90;
    private final Texture playerTexture;
    private final Sprite playerSprite;
    private final Body playerBody;
    private Vector3 forwardDirection;
    private float forwardAngle;
    private int hitPoints;
    private boolean alive;

    public Player(World world, float viewportWidthMeters, float viewportHeightMeters) {

        playerTexture = new Texture(Gdx.files.internal(PLAYER_SHIP_IMAGE));
        playerSprite = new Sprite(playerTexture);
        playerSprite.setOrigin(playerTexture.getWidth() / 2f, playerTexture.getHeight() / 2f);

        float playerHeightPixels = playerTexture.getHeight();
        float playerWidthPixels = playerTexture.getWidth();

        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;

        playerBody = world.createBody(playerDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(AsteroidUtil.pixelToMeter(playerWidthPixels/3));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.2f;
        playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);
        circleShape.dispose();

        playerBody.setUserData(this);
        playerBody.setTransform(viewportWidthMeters /2, viewportHeightMeters/2, 0);

        resetState(viewportWidthMeters, viewportHeightMeters);
    }

    public void updateForward(Vector3 mousePosition) {
        forwardDirection = new Vector3(mousePosition.x - playerBody.getPosition().x,
                mousePosition.y - playerBody.getPosition().y, 0).nor();
        forwardAngle = (float) (MathUtils.radiansToDegrees * Math.atan2(mousePosition.y - playerBody.getPosition().y,
                        mousePosition.x - playerBody.getPosition().x) - SPRITE_ROTATION_ANGLE);
    }

    public void thrustForward() {
        applyThrust(new Vector2(forwardDirection.x, forwardDirection.y), THRUST_FORCE_FORWARD);
    }

    public void thrustLeft() {
        applyThrust(new Vector2(-forwardDirection.y, forwardDirection.x), THRUST_FORCE_SIDE);
    }

    public void thrustRight() {
        applyThrust(new Vector2(forwardDirection.y, -forwardDirection.x), THRUST_FORCE_SIDE);
    }


    private void applyThrust(Vector2 direction, float forcePower) {
        playerBody.applyForceToCenter(new Vector2(playerBody.getMass() * direction.x * forcePower,
                playerBody.getMass() * direction.y * forcePower), true);
    }
    public void render(SpriteBatch spriteBatch, float viewportWidth, float viewportHeight) {

        AsteroidUtil.containBodyOnScreen(playerBody, viewportWidth, viewportHeight, playerSprite.getWidth(), playerSprite.getHeight());

        playerSprite.setPosition(AsteroidUtil.meterToPixel(playerBody.getPosition().x) - playerSprite.getWidth() /2,
                AsteroidUtil.meterToPixel(playerBody.getPosition().y) - playerSprite.getHeight() /2);

        playerSprite.setRotation(forwardAngle);
        playerSprite.draw(spriteBatch);
    }


    @Override
    public void dispose() {
        playerTexture.dispose();
    }

    public void damage() {
        hitPoints--;
        if (hitPoints <= 0) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Body getPlayerBody() {
        return playerBody;
    }

    public void resetState(float viewportWidth, float viewportHeight) {
        alive = true;
        hitPoints = HIT_POINTS;
        playerBody.setTransform(viewportWidth /2, viewportHeight/2, 0);
        playerBody.setActive(true);
        playerBody.setLinearVelocity(Vector2.Zero);
    }

    public Vector3 getForward() {
        return forwardDirection;
    }

    public float getShotAngle() {
        return forwardAngle;
    }
}
