package frc.robot.Dashboards.ADLDashboard;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;

public class DashboardPublisherADL {
    private final StringPublisher statePub;
    private final StringPublisher activeActionPub;
    private final StringPublisher decisionPub;
    private final BooleanPublisher enabledPub;

    public DashboardPublisherADL() {
        var nt = NetworkTableInstance.getDefault();
        statePub = nt.getStringTopic("/ADL/dashboard/state").publish();
        activeActionPub = nt.getStringTopic("/ADL/dashboard/activeAction").publish();
        decisionPub = nt.getStringTopic("/ADL/dashboard/decision").publish();
        enabledPub = nt.getBooleanTopic("/ADL/dashboard/enabled").publish();
    }

    public void publish(ADLData data, boolean enabled) {
        statePub.set(data.state());
        activeActionPub.set(data.activeAction());
        decisionPub.set(data.decision());
        enabledPub.set(enabled);
    }
}
