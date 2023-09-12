package com.mygdx.game.util;

import static com.mygdx.game.util.Constants.PIXELS_TO_METERS;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
public class AsteroidUtil {

    public static Vector2 getRandomPositionOnScreen(float viewportWidth, float viewportHeight) {
        float lowerBound = pixelToMeter(10);
        float x = generateRandomInBorders(lowerBound, viewportWidth / 5,
                viewportWidth - viewportWidth / 5, viewportWidth - lowerBound);
        float y = generateRandomInBorders(lowerBound, viewportHeight / 5,
                viewportHeight - viewportHeight / 5, viewportHeight - lowerBound);
        return new Vector2(x,y);
    }

    private static float generateRandomInBorders(float lowerBound, float lowerSecondBound, float higherBound, float secondHigherBound) {
        return MathUtils.random.nextFloat(0, 1) > 0.5f ?
                MathUtils.random.nextFloat(lowerBound, lowerSecondBound) : MathUtils.random.nextFloat(higherBound, secondHigherBound);
    }

    public static void containBodyOnScreen(Body body, float viewportWidth, float viewportHeight, float bodyWidthPixels, float bodyHeightPixels) {
        float bodyHeightMeters = pixelToMeter(bodyHeightPixels);
        float bodyWidthMeters = pixelToMeter(bodyWidthPixels);
        if (body.getPosition().x > viewportWidth - bodyWidthMeters/2) {
            body.setTransform(new Vector2(0 + bodyWidthMeters/4, body.getPosition().y), body.getAngle());
        } else if (body.getPosition().x < 0 + bodyWidthMeters/4) {
            body.setTransform(new Vector2(viewportWidth - bodyWidthMeters/2, body.getPosition().y), body.getAngle());
        }
        if (body.getPosition().y > viewportHeight - bodyHeightMeters/2) {
            body.setTransform(new Vector2(body.getPosition().x, 0 + bodyHeightMeters/4), body.getAngle());
        } else if (body.getPosition().y < 0 + bodyHeightMeters/4) {
            body.setTransform(new Vector2(body.getPosition().x, viewportHeight - bodyHeightMeters/2), body.getAngle());
        }
    }

    public static float pixelToMeter(float pixelValue) {
        return pixelValue / PIXELS_TO_METERS;
    }

    public static float meterToPixel(float meterValue) {
        return meterValue * PIXELS_TO_METERS;
    }
}
