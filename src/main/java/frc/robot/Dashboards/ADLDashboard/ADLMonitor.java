package frc.robot.Dashboards.ADLDashboard;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringSubscriber;

public class ADLMonitor {
    private final StringSubscriber stateSub;
    private final StringSubscriber activeActionSub;
    private final StringSubscriber decisionSub;

    public ADLMonitor() {
        var nt = NetworkTableInstance.getDefault();
        stateSub = nt.getStringTopic("/ADL/state").subscribe("DISABLED");
        activeActionSub = nt.getStringTopic("/ADL/activeAction").subscribe("");
        decisionSub = nt.getStringTopic("/ADL/decision").subscribe("");
    }

    public ADLData read() {
        return new ADLData(stateSub.get(), activeActionSub.get(), decisionSub.get());
    }
}
