package fr.xebia.nxtchallenge;


public class Robot extends RobotBase {

    @Override protected BaseBehavior[] behaviors() {
        // Behavior list in increasing priority order (last element = most prioritary).
        return new BaseBehavior[] { new Drive(), new ObjectDetected(), new Touch(), new LeaveArena() };
    }

    class Drive extends BaseBehavior {
        boolean turning;

        @Override public boolean takeControl() {
            // Lowest priority, always run if there is nothing more important to do
            return true;
        }

        @Override public void action() {
            hasPriority = true;
            // Put your code here: default behavior: drive forward.
            // You can imagine other strategies to quickly discover your enemy.
        }
    }

    class ObjectDetected extends BaseBehavior {
    	
        @Override public boolean takeControl() {
            return sonar.getDistance() < 35;
        }

        @Override public void action() {
            hasPriority = true;
            System.out.println("Object detected");
            // Your code here... Default: run to join the other robot.
        }
    }

    class Touch extends BaseBehavior {   	
    	
        @Override public boolean takeControl() {
            return touchRight.isPressed() || touchLeft.isPressed();
        }

        @Override public void action() {
            hasPriority = true;
            System.out.println("Touching something");
            // Your code here... Default: back and forward to push the other robot
        }
    }

    class LeaveArena extends BaseBehavior {
    	
    	@Override public boolean takeControl() {
            return light.getLightValue() < 35;
        }

        @Override public void action() {
            hasPriority = true;
            // Stop driving (usually, already done by other behavior at the end of their action).
        }
    }

    // You don't need to change this
    public static void main(String[] args) throws Exception {
        System.out.println("Launching robot");

        Robot robot = new Robot();
        robot.run();
        Thread.yield();
    }
}
