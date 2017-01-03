package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Diogo on 29/11/2016.
 */

public class Player1 extends Sprite {

    public String id;
    public static boolean isGround = false;
    public Body frontWheel, backWheel, carBody;
    public WheelJoint backJoint,frontJoint;
    public boolean isDead;
    public Sprite bodySprite, wheelSprite1,wheelSprite2;

    public  Player1(World _world, FixtureDef _carBodyFDef, FixtureDef _wheelsFDef, float x , float y , float width, float height)
    {
        id = "PLAYER";
        isDead = false;
        bodySprite = new Sprite(new Texture("Body.png"));
        bodySprite.setSize(30,15);
        bodySprite.setOrigin(bodySprite.getWidth()/2, bodySprite.getHeight()/2);
        wheelSprite1 = new Sprite(new Texture("Wheel.png"));
        wheelSprite1.setSize(10,10);
        wheelSprite1.setOrigin(wheelSprite1.getWidth()/2, wheelSprite1.getHeight()/2);
        wheelSprite2 = new Sprite(wheelSprite1);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        //CarBody

        PolygonShape bodyShape = new PolygonShape();
        bodyShape.set(new float[] {-width/2 * 0.8f,height* 0.05f,width/2,-height * 0.1f,width/2 * 0.4f,height/2, -width/2f,height/2});

        _carBodyFDef.shape = bodyShape;

        carBody = _world.createBody(bodyDef);
        carBody.createFixture(_carBodyFDef).setUserData(this);

        //backWheel
        CircleShape wheelShape = new CircleShape();
        wheelShape.setRadius(height/2.5f);

        _wheelsFDef.shape = wheelShape;

        backWheel = _world.createBody(bodyDef);
        backWheel.createFixture(_wheelsFDef).setUserData(this);

        //rightWheel

        frontWheel = _world.createBody(bodyDef);
        frontWheel.createFixture(_wheelsFDef).setUserData(this);

        //backJoint
        WheelJointDef wheelJointDef = new WheelJointDef();
        wheelJointDef.bodyA = carBody;
        wheelJointDef.bodyB = frontWheel;
        wheelJointDef.localAnchorA.set(-width/2 * 0.75f, -height/2 * 1.25f);
        wheelJointDef.frequencyHz = 5f;
        wheelJointDef.localAxisA.set(Vector2.Y);

        backJoint = (WheelJoint)_world.createJoint(wheelJointDef);

        //frontWheel
        wheelJointDef.bodyB = backWheel;
        wheelJointDef.localAnchorA.x *= -1;

        frontJoint = (WheelJoint)_world.createJoint(wheelJointDef);
    }

    public void Hit()
    {
        System.out.println(id + " : I have been hit!!");
        carBody.applyAngularImpulse((float) Math.PI, true);
    }

    public void Update()
    {
        //backWheel.applyLinearImpulse(new Vector2(1500f,0),backWheel.getWorldCenter(),true);
        //frontWheel.applyLinearImpulse(new Vector2(1500f,0),frontWheel.getWorldCenter(),true);
        backWheel.applyAngularImpulse(-4000f,true);//-4000 works
        frontWheel.applyAngularImpulse(-4000f,true);

        bodySprite.setPosition(carBody.getPosition().x - bodySprite.getWidth()/2, carBody.getPosition().y - bodySprite.getHeight()/2);
        bodySprite.setRotation(carBody.getAngle() * MathUtils.radiansToDegrees);
        wheelSprite1.setPosition(frontWheel.getPosition().x, frontWheel.getPosition().y);
        wheelSprite1.setRotation(frontWheel.getAngle()* MathUtils.radiansToDegrees);
        wheelSprite2.setPosition(backWheel.getPosition().x, backWheel.getPosition().y);
        wheelSprite2.setRotation(backWheel.getAngle()* MathUtils.radiansToDegrees);

        if (!isGround)
        {
            carBody.setAngularVelocity(-2f);//-0.5 looks ok
            isGround = false;
        }
    }

    public void HitGround()
    {
        isGround = true;
    }

    public void kill()
    {
        isDead = true;
    }
}
