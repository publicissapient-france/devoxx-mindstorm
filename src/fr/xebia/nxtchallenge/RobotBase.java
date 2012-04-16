package fr.xebia.nxtchallenge;

import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/** Externalizes stuff the contestants should not have to worry about. */
public abstract class RobotBase {
    protected static final int MAX_SPEED = 250;
    protected static final int TRAVEL_SPEED = 205;

    protected final DifferentialPilot pilot;
    protected final ColorSensor light;

    protected final UltrasonicSensor sonar;
    protected final TouchSensor touchRight;
    protected final TouchSensor touchLeft;

    protected final Random random = new Random();

    public RobotBase() {
        NXTRegulatedMotor rightMotor = Motor.A;
        NXTRegulatedMotor leftMotor = Motor.B;

        // Decrease to travel further
        float wheelDiameterMm = 33f;
        // Increase to turn more
        float wheelDiameterToTrackWidthRatio = 5.25f;

        pilot = new DifferentialPilot(
                wheelDiameterMm,
                wheelDiameterMm * wheelDiameterToTrackWidthRatio,
                leftMotor, rightMotor, false);
        light = new ColorSensor(SensorPort.S3);
        sonar = new UltrasonicSensor(SensorPort.S2);
        touchRight = new TouchSensor(SensorPort.S1);
        touchLeft = new TouchSensor(SensorPort.S4);
    }

    public void run() throws InterruptedException {
        
    	//calibrateLightSensor();
        Sound.setVolume(10);

        System.out.println("Press any key to start");
        Button.waitForAnyPress();
        System.out.println("GO!!!");

        // Random rotation at the beginning to avoid predictable strategy.
        pilot.rotate(random.nextInt(340));

        Behavior[] customBehaviors = behaviors();

        // Add exit behavior at the end
        int customBehaviorCount = customBehaviors.length;
        Behavior[] behaviors = new Behavior[customBehaviorCount + 1];
        System.arraycopy(customBehaviors, 0, behaviors, 0, customBehaviorCount);
        behaviors[customBehaviorCount] = new Exit();

        Arbitrator arbitrator = new Arbitrator(behaviors);
        arbitrator.start();
    }

    protected abstract Behavior[] behaviors();

    // Not needed
    private void calibrateLightSensor() {
        System.out.println("Calibrate the white");
        Button.ENTER.waitForPressAndRelease();
        light.calibrateHigh();
        System.out.println("Read white value:"
                + light.getNormalizedLightValue() + ", lv="
                + light.getLightValue());
        System.out.println("Calibrate the black");
        Button.ENTER.waitForPressAndRelease();
        light.calibrateLow();
        System.out.println("Read white value:"
                + light.getNormalizedLightValue() + ", lv="
                + light.getLightValue());
        Button.ENTER.waitForPressAndRelease();
    }

}

abstract class BaseBehavior implements Behavior {
    protected boolean hasPriority;

    @Override public void suppress() {
        hasPriority = false;
    }
}

class Exit extends BaseBehavior {

    @Override public boolean takeControl() {
        return Button.ESCAPE.isDown();
    }

    @Override public void action() {
        System.out.println("ESCAPE was pressed, exiting...");
        System.exit(1);
    }

}