package com.mygdx.game.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class GrabManager {
    private final World world;
    private RevoluteJoint grabJoint;
    private Body grabbedBody;

    public GrabManager(World world) {
        this.world = world;
    }

    public void grab(Body dynamicBody, Body kinematicBody, Vector2 grabPoint, Vector2 alpPoint) {
        if (grabJoint != null) {
            release();
        }

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = dynamicBody;      // Динамическое тело (Alp)
        jointDef.bodyB = kinematicBody;    // Кинематическое тело (SmallStone)
        jointDef.collideConnected = false; // Тела не сталкиваются друг с другом

        // Точка захвата в локальных координатах каждого тела
        jointDef.localAnchorA.set(dynamicBody.getLocalPoint(alpPoint));
        jointDef.localAnchorB.set(kinematicBody.getLocalPoint(grabPoint));

        // Ограничиваем угол вращения для более стабильного захвата
        jointDef.enableLimit = true;
        jointDef.lowerAngle = -0.1f;
        jointDef.upperAngle = 0.1f;

        // Мотор для сопротивления вращению
        jointDef.enableMotor = true;
        jointDef.motorSpeed = 0f;
        jointDef.maxMotorTorque = 1000f;

        grabJoint = (RevoluteJoint) world.createJoint(jointDef);
        grabbedBody = kinematicBody;

        System.out.println("Grabbed small stone!");
    }

    public void release() {
        if (grabJoint != null) {
            world.destroyJoint(grabJoint);
            grabJoint = null;
            grabbedBody = null;
            System.out.println("Released small stone!");
        }
    }

    public boolean isGrabbing() {
        return grabJoint != null;
    }

    public Body getGrabbedBody() {
        return grabbedBody;
    }
}