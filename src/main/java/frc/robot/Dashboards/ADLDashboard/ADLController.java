package frc.robot.Dashboards.ADLDashboard;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;

public class ADLController {
    private final StringPublisher actionPub;
    private final StringPublisher zonePub;
    private final StringPublisher parametersPub;
    private final DoublePublisher priorityPub;
    private final BooleanPublisher preemptPub;
    private final DoublePublisher sequencePub;
    private double sequence = 0.0;

    public ADLController() {
        var nt = NetworkTableInstance.getDefault();
        actionPub = nt.getStringTopic("/ADL/intent/actionId").publish();
        zonePub = nt.getStringTopic("/ADL/intent/zoneId").publish();
        parametersPub = nt.getStringTopic("/ADL/intent/parameters").publish();
        priorityPub = nt.getDoubleTopic("/ADL/intent/priority").publish();
        preemptPub = nt.getBooleanTopic("/ADL/intent/preempt").publish();
        sequencePub = nt.getDoubleTopic("/ADL/intent/sequence").publish();
    }

    public void request(String actionId, String zoneId, String parameters, int priority, boolean preempt) {
        actionPub.set(actionId);
        zonePub.set(zoneId);
        parametersPub.set(parameters);
        priorityPub.set(priority);
        preemptPub.set(preempt);
        sequencePub.set(++sequence);
    }
}
