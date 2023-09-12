package com.mygdx.game.ojbect.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.ojbect.Bullet;

public class BulletFactory implements Disposable {

    private static final String BULLET_TEXTURE = "Sprites/laserBlue01.png";
    private final World world;
    private final BodyDef bodyDef;

    private final Texture bulletTexture;

    public BulletFactory(World world) {
        this.world = world;
        bulletTexture = new Texture(Gdx.files.internal(BULLET_TEXTURE));

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    public Bullet createBullet() {
        return new Bullet(world, bulletTexture, bodyDef);
    }

    @Override
    public void dispose() {
        bulletTexture.dispose();
    }
}

