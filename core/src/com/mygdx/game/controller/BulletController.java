package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ojbect.Bullet;
import com.mygdx.game.ojbect.Player;
import com.mygdx.game.ojbect.factory.BulletFactory;

public class BulletController implements Disposable {
    private static final int STARTING_BULLET_SIZE = 2;
    private static final float SHOT_RELOAD_TIME = 1f;
    private final Array<Bullet> activeBullets;
    private final Pool<Bullet> bulletPool;
    private final BulletFactory bulletFactory;
    private float shotReloadTimer = 0f;
    private boolean canShoot = true;
    public BulletController(World world) {
        activeBullets = new Array<>();
        bulletFactory = new BulletFactory(world);
        bulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return bulletFactory.createBullet();
            }
        };
        bulletPool.fill(STARTING_BULLET_SIZE);
    }

    public void render(SpriteBatch batch, float screenWidthMeters, float screenHeightMeters) {
        if (!canShoot) {
            shotReloadTimer += Gdx.graphics.getDeltaTime();
            if (shotReloadTimer >= SHOT_RELOAD_TIME) {
                canShoot = true;
                shotReloadTimer = 0;
            }
        }

        for (int i = activeBullets.size; --i >= 0; ) {
            Bullet bullet = activeBullets.get(i);
            if (bullet.isAlive()) {
                bullet.update(batch, screenWidthMeters, screenHeightMeters);
            } else {
                bulletPool.free(bullet);
                activeBullets.removeIndex(i);
            }
        }
    }

    public void reset() {
        for (int i = activeBullets.size; --i >= 0; ) {
            Bullet bullet = activeBullets.get(i);
            bullet.destroy();
            bulletPool.free(bullet);
            activeBullets.removeIndex(i);
        }
        canShoot = true;
        shotReloadTimer = 0;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public void shoot(Player player) {
        Bullet bullet = bulletPool.obtain();
        bullet.init(player.getPlayerBody().getPosition(), player.getShotAngle(), player.getForward());
        activeBullets.add(bullet);
        canShoot = false;
    }

    @Override
    public void dispose() {
        bulletFactory.dispose();
    }
}
