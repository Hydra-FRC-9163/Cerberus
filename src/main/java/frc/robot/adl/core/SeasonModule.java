package frc.robot.adl.core;

public interface SeasonModule {
    String seasonId();

    void register(SeasonRegistrationContext context);
}
