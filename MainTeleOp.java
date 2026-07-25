package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main TeleOp", group = "Competition")
public class MainTeleOp extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private DcMotor armMotor;
    private Servo clawServo;

    private static final double SLOW_MODE_MULTIPLIER = 0.4;
    private static final double NORMAL_MODE_MULTIPLIER = 1.0;
    private static final double ARM_POWER_SCALE = 0.6;
    private static final double CLAW_OPEN_POSITION = 0.7;
    private static final double CLAW_CLOSED_POSITION = 0.1;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");

        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        armMotor   = hardwareMap.get(DcMotor.class, "arm_motor");
        clawServo  = hardwareMap.get(Servo.class, "claw_servo");
//REVERSE motion
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
//brakes
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Robot initialized. Waiting for start...");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

                        double y  = -gamepad1.left_stick_y;               double x  =  gamepad1.left_stick_x * 1.1;             double rx =  gamepad1.right_stick_x; 
            double speedMultiplier = gamepad1.right_bumper
                    ? SLOW_MODE_MULTIPLIER
                    : NORMAL_MODE_MULTIPLIER;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
            double frontLeftPower  = (y + x + rx) / denominator * speedMultiplier;
            double backLeftPower   = (y - x + rx) / denominator * speedMultiplier;
            double frontRightPower = (y - x - rx) / denominator * speedMultiplier;
            double backRightPower  = (y + x - rx) / denominator * speedMultiplier;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

                        double armInput = -gamepad2.left_stick_y;
            armMotor.setPower(armInput * ARM_POWER_SCALE);

            telemetry.addData("Drive", "y=%.2f x=%.2f rx=%.2f", y, x, rx);
            telemetry.addData("Slow Mode", gamepad1.right_bumper);
            telemetry.addData("Arm Power", "%.2f", armMotor.getPower());
            telemetry.addData("Claw Position", "%.2f", clawServo.getPosition());
            telemetry.update();
        }
    }
}
